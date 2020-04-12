package com.example.abc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView attemptsTextView, registerTextView,adminTextView;
    private int counter = 5;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.email_editText);
        passwordEditText = findViewById(R.id.password_editText);
        loginButton = findViewById(R.id.login_button);
        attemptsTextView = findViewById(R.id.attempts_textView);
        registerTextView = findViewById(R.id.register_textView);
        forgotPasswordTextView = findViewById(R.id.forgot_password_textView);
        adminTextView=findViewById(R.id.admin_textView);

        attemptsTextView.setText("Attempts Remaining: " + String.valueOf(counter));

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        /*if (user != null) {
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }*/
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionListener=new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(MainActivity.this,"Permission not given",Toast.LENGTH_SHORT).show();

                    }
                };
                TedPermission.with(MainActivity.this).setPermissionListener(permissionListener).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE).check();
                validate(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PasswordActivity.class));
            }
        });
        adminTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AdminLogin.class));
            }
        });




    }

     @Override
    public void onStart()
     {
        super.onStart();
      // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
           finish();
           startActivity(new Intent(MainActivity.this, ThirdActivity.class));
        }
    }

    private void validate(String userName, String userPassword) {
        // This "if" statement uses the "validateRegistration" method.  If the EditText fields are NOT blank, it will try to log in.
        if (validateRegistration() == true) {

            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            // This is the method that tries to log a user in.
            firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,ThirdActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        Toast.makeText(MainActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                        counter--;
                        attemptsTextView.setText("Attempts Remaining: " + String.valueOf(counter));
                        progressDialog.dismiss();
                        if (counter == 0) {
                            loginButton.setEnabled(false);
                        }
                    }
                }
            });
        }
    }

    // This evaluates to "true" if neither EditText is empty when trying to log in.
    private boolean validateRegistration() {
        boolean result = false;
        String name = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Enter All Details.", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }

    // This checks if the user has verified his email address by checking his email account.
   /* private void checkEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag  = firebaseUser.isEmailVerified();

        startActivity(new Intent(MainActivity.this, SecondActivity.class));

        // The following would be added instead of the above Intent if I want email verification.
        // A few other changes elsewhere in the code would need to be made as well
        // (some commented out stuff reinstated, and vice versa.)
//        if (emailFlag) {
//            finish();
//            startActivity(new Intent(MainActivity.this, SecondActivity.class));
//        } else {
//            Toast.makeText(this, "Please verify your email.", Toast.LENGTH_SHORT).show();
//            firebaseAuth.signOut();
//        }
    }*/



}
