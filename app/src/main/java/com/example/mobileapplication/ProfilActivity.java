package com.example.mobileapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
private EditText nameProfil,emailProfil,phoneProfil,cinProfil;
    private Button btnEdit,btnLogOut;

private FirebaseAuth firebaseAuth;
private FirebaseDatabase firebaseDatabase;
private FirebaseUser user;
private DatabaseReference reference;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        nameProfil=findViewById(R.id.nameProfil);
        emailProfil=findViewById(R.id.emailProfil);
        phoneProfil=findViewById(R.id.phoneProfil);
        cinProfil=findViewById(R.id.cinProfil);
        btnEdit=findViewById(R.id.btnEdit);
        btnLogOut=findViewById(R.id.btnLogOut);

        drawerLayout=findViewById(R.id.drawer_layout_profil);
        navigationView=findViewById(R.id.navigation_view_profil);
        menuIcon=findViewById(R.id.menu_profil);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=firebaseDatabase.getInstance();

        user=firebaseAuth.getCurrentUser();
        reference=firebaseDatabase.getReference().child("Users").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullName =snapshot.child("name").getValue().toString();
                String email =snapshot.child("email").getValue().toString();
                String phone =snapshot.child("phone").getValue().toString();
                String cin =snapshot.child("cin").getValue().toString();

                nameProfil.setText(fullName);
                emailProfil.setText(email);
                phoneProfil.setText(phone);
                cinProfil.setText(cin);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        SharedPreferences preferences =getSharedPreferences("checkbox",MODE_PRIVATE);
        SharedPreferences.Editor editor =preferences.edit();
        editor.putString("remember","false");
        editor.apply();
        firebaseAuth.signOut();
        startActivity(new Intent(ProfilActivity.this,SigningActivity.class));
        Toast.makeText(ProfilActivity.this, "Log out", Toast.LENGTH_SHORT).show();
        finish();;
    }
});
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameProfil.setFocusableInTouchMode(true);
                phoneProfil.setFocusableInTouchMode(true);
                cinProfil.setFocusableInTouchMode(true);
                btnEdit.setText("save");

                btnEdit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String editName=nameProfil.getText().toString();
                   String editPhone=phoneProfil.getText().toString();
                   String editCin=cinProfil.getText().toString();
                   reference.child("name").setValue(editName);
                   reference.child("phone").setValue(editPhone);
                   reference.child("cin").setValue(editCin);
                   nameProfil.setFocusableInTouchMode(false);
                   phoneProfil.setFocusableInTouchMode(false);
                   cinProfil.setFocusableInTouchMode(false);
                   nameProfil.clearFocus();
                   phoneProfil.clearFocus();
                   cinProfil.clearFocus();
                   btnEdit.setText("edit");
                   startActivity(new Intent(ProfilActivity.this,PrincipalActivity.class));
               }
           });
            }
        });


        navigationDrawer();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.add:
                        startActivity(new Intent(ProfilActivity.this,PrincipalActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.profilUserActivity:

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.ticketElectrique:
                        startActivity(new Intent(ProfilActivity.this,TicketElectrique.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });


    }
    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.profilUserActivity);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else drawerLayout.openDrawer((GravityCompat.START));

            }
        });

        drawerLayout.setScrimColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}