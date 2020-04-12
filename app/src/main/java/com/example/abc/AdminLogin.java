package com.example.abc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class AdminLogin extends AppCompatActivity {
    private Button login;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog loadingbar;
    private EditText Email, Password, admino;TextView textView,textView1;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        login = findViewById(R.id.login_button1);
        Email = findViewById(R.id.email_editText1);
        Password = findViewById(R.id.password_editText1);

        admino=findViewById(R.id.textView2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = Email.getText().toString();
                final String password = Password.getText().toString();
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseDatabase = FirebaseDatabase.getInstance();
                String ano=admino.getText().toString();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Admin").child(ano);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                            UserAdmin userAdmin = dataSnapshot.getValue(UserAdmin.class);
                            if(email.equals(userAdmin.getUserEmail())&& password.equals(userAdmin.getUserPassword()))
                            {
                                Intent intent=new Intent(AdminLogin.this,FourthActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                finish();

                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Toast.makeText(AdminLogin.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }



    private void Signin(final String email, final String password, String adminno)
    {



    }
}





