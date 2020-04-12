package com.example.abc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ThirdActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private Button assi,notes,notice,tt;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    ImageView imageView;
    TextView name,profileEmailTextView;
    View h;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        firebaseAuth=FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar1);
        notes=findViewById(R.id.button2);
        assi=findViewById(R.id.button);
        notice=findViewById(R.id.button4);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.draw_layout);
        NavigationView navigationView=findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        h=navigationView.getHeaderView(0);
        imageView=h.findViewById(R.id.drawerpic);
        name=h.findViewById(R.id.drawername);
        profileEmailTextView=h.findViewById(R.id.email);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());

        StorageReference storageReference = firebaseStorage.getReference();
        // Get the image stored on Firebase via "User id/Images/Profile Pic.jpg".
        storageReference.child("Users").child(firebaseAuth.getUid()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Using "Picasso" (http://square.github.io/picasso/) after adding the dependency in the Gradle.
                // ".fit().centerInside()" fits the entire image into the specified area.
                // Finally, add "READ" and "WRITE" external storage permissions in the Manifest.
                Picasso.get().load(uri).fit().into(imageView);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                String na=userProfile.getUserName();
                name.setText(na);
                profileEmailTextView.setText( userProfile.getUserEmail());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ThirdActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThirdActivity.this,ViewNotes.class));

            }
        });
        assi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThirdActivity.this,ViewAssignment.class));
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThirdActivity.this,ViewNotice.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.profile:
                Intent intent = new Intent(ThirdActivity.this,ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.faculty:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FacultyFragment()).commit();
                break;
            case R.id.help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HelpFragment()).commit();
                break;
            case R.id.aboutus:
                Intent intent5=new Intent(ThirdActivity.this,AboutUsActivity.class);
                startActivity(intent5);
                break;
            case R.id.notes:
                    Intent intent1 = new Intent(ThirdActivity.this,ViewNotes.class);
                    startActivity(intent1);
                    break;
            case R.id.notice:
                Intent intent2 = new Intent(ThirdActivity.this,ViewNotice.class);
                startActivity(intent2);
                break;
            case R.id.assignment:
                Intent intent3 = new Intent(ThirdActivity.this,ViewAssignment.class);
                startActivity(intent3);
                break;
            case R.id.timetable:
                Intent intent4 = new Intent(ThirdActivity.this,ViewTimeTable.class);
                startActivity(intent4);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

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
        startActivity(new Intent(ThirdActivity.this, MainActivity.class));
    }
}
