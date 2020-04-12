package com.example.abc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class FourthActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    Button notes,assi,notice,tt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        notes=findViewById(R.id.button2);
        assi=findViewById(R.id.button);
        notice=findViewById(R.id.button4);
        firebaseAuth=FirebaseAuth.getInstance();
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FourthActivity.this,UploadNotes.class));
            }
        });
        assi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FourthActivity.this,UploadAssignment.class));
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FourthActivity.this,UploadNotice.class));
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout_menu: {
                logoutFunction();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private void logoutFunction() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(FourthActivity.this, MainActivity.class));
    }

}
