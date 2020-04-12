package com.example.abc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadNotes extends AppCompatActivity {

     EditText titl,desc,posted;
     Button upload;
     StorageReference storageReference;
     DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes);
        titl=findViewById(R.id.u_title);
        desc=findViewById(R.id.description);
        posted=findViewById(R.id.posted_by);
        upload=findViewById(R.id.button5);
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference=FirebaseDatabase.getInstance().getReference("Notes");
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPdfFile();
            }
        });


    }

    private void selectPdfFile()
    {
        Intent intent=new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            Uri uri=data.getData();
            String file= MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            uploadPDFFile(uri,file);

        }

    }

    private void uploadPDFFile(Uri data,String ex)
    {
        final ProgressDialog progressDialog=new ProgressDialog(UploadNotes.this);
        progressDialog.setTitle("Uploading ...");
        progressDialog.show();
        StorageReference reference=storageReference.child("uploads/notes/"+System.currentTimeMillis()+"."+ex);
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete());
                Uri url=uri.getResult();
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
                String date=simpleDateFormat.format(calendar.getTime());
                SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("hh:mm:ss");
                String time=simpleDateFormat1.format(calendar.getTime());
                UploadPdf uploadPdf=new UploadPdf(titl.getText().toString(),desc.getText().toString(),posted.getText().toString(),url.toString(),date,time);
                databaseReference.child(databaseReference.push().getKey()).setValue(uploadPdf);
                Toast.makeText(UploadNotes.this,"File Uploaded",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded  "+(int)(progress)+" % ");
            }
        });
    }

}
