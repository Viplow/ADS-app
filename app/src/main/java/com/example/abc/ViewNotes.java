package com.example.abc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewNotes extends AppCompatActivity {
    ListView listView;
    DatabaseReference databaseReference;
    List<UploadPdf> uploadPdfs;
    DownloadManager downloadManager;
     @Override
    protected void onCreate(Bundle savedInstanceState)
       {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        listView=findViewById(R.id.mylistview);
        uploadPdfs=new ArrayList<>();
        viewAllFiles();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url=uploadPdfs.get(position).getUrl();
                downloadManager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri=Uri.parse(url);
                DownloadManager.Request request=new DownloadManager.Request(uri);
                request.setTitle("Download "+uploadPdfs.get(position).getHead());
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                String file= MimeTypeMap.getFileExtensionFromUrl(uri.toString());
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis()+"."+file);
                downloadManager.enqueue(request);



            }
        });


    }

    private void viewAllFiles()
    {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Notes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    UploadPdf uploadPdf=ds.getValue(UploadPdf.class);
                    uploadPdfs.add(uploadPdf);
                }
                String uploads[]=new String[uploadPdfs.size()];
                String des[]=new String[uploadPdfs.size()];
                String posted[]=new String[uploadPdfs.size()];
                String url[]=new String[uploadPdfs.size()];
                String date[]= new String[uploadPdfs.size()];
                String time[]=new String[uploadPdfs.size()];
                for(int i=0;i<uploads.length;i++)
                {
                    uploads[i]= uploadPdfs.get(i).getHead();
                    des[i]= uploadPdfs.get(i).getDescription();
                    posted[i]= uploadPdfs.get(i).getPostedby();
                    url[i]=uploadPdfs.get(i).getUrl();
                    date[i]=uploadPdfs.get(i).getDate();
                    time[i]=uploadPdfs.get(i).getTime();
                }

              /*  ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads){
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view=super.getView(position, convertView, parent);
                        TextView myText=(TextView)view.findViewById(android.R.id.text1);
                        myText.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                listView.setAdapter(adapter);*/
                int images[]={R.mipmap.jpg,R.mipmap.excel,R.mipmap.word,R.mipmap.pdf};
                ViewNotes.MyAdapter myAdapter=new ViewNotes.MyAdapter(ViewNotes.this,uploads,posted,des,images,url,date,time);
                listView.setAdapter(myAdapter);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
   class MyAdapter extends ArrayAdapter<String>
    {
        Context context;
        String rTitle[];
        String rPosted[];
        String rDescription[];
        int rimages[];
        String url1[];
        String rdate[];
        String rtime[];

        MyAdapter(Context context1, String[] Title, String[] Posted, String[] Description,int imgs[],String[] url,String[] date,String[] time) {
            super(context1,R.layout.row,R.id.mt,Title);
            this.context = context1;
            this.rTitle = Title;
            this.rPosted = Posted;
            this.rDescription = Description;
            this.rimages=imgs;
            this.url1=url;
            this.rdate=date;
            this.rtime=time;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView myTitle=row.findViewById(R.id.mt);
            TextView myPostedby=row.findViewById(R.id.pb);
            TextView myDescription=row.findViewById(R.id.desc);
            ImageView imageView=row.findViewById(R.id.imag);
            myTitle.setText(rTitle[position]);
            myDescription.setText("  Posted By :  " + rPosted[position]+"\n Date:"+rdate[position]+"\n Time:"+rtime[position]);
            myPostedby.setText( "Description : "+rDescription[position]);
            String file= MimeTypeMap.getFileExtensionFromUrl(url1[position]);
            if (file.equals("pdf"))
                imageView.setImageResource(rimages[3]);
            if (file.equals("docx")|| file.equals("doc"))
                imageView.setImageResource(rimages[2]);
            if (file.equals("xls"))
                imageView.setImageResource(rimages[1]);
            if (file.equals("jpeg")|| file.equals("jpg")||file.equals("png"))
                imageView.setImageResource(rimages[0]);


            return row;


        }
    }
   /*class MyAdapter extends BaseAdapter
   {
       Context context;
       String rTitle[];
       String rPosted[];
       String rDescription[];

       public MyAdapter(Context context, String[] rTitle, String[] rPosted, String[] rDescription) {
           this.context = context;
           this.rTitle = rTitle;
           this.rPosted = rPosted;
           this.rDescription = rDescription;
       }

       @Override
       public int getCount() {
           return 0;
       }

       @Override
       public Object getItem(int position) {
           return null;
       }

       @Override
       public long getItemId(int position) {
           return 0;
       }
       private class ViewHolder{
           TextView myTitle;
           TextView myPostedby;
           TextView myDescription;


       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder holder;
           if(convertView==null){
               holder=new ViewHolder();
               LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               convertView=inflater.inflate(R.layout.row,parent,false);
               holder.myTitle=convertView.findViewById(R.id.mt);
               holder.myPostedby=convertView.findViewById(R.id.pb);
               holder.myDescription=convertView.findViewById(R.id.desc);
               convertView.setTag(holder);

           }
           else
           {
               holder= (ViewHolder) convertView.getTag();

           }
           holder.myTitle.setText(rTitle[position]);
           holder.myDescription.setText(rDescription[position]);
           holder.myPostedby.setText(rPosted[position]);

           return convertView;
       }
   }*/


}
