package com.example.mobileapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity<result> extends AppCompatActivity {
    private TextView haveAccount;
    private EditText nameUser, emailUser, phoneUser, cinUser, passwordUser;
    private Button register;
    private String name, email, cin, phone, password;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        haveAccount = findViewById(R.id.haveAccount);
        nameUser = findViewById(R.id.nameUser);
        emailUser = findViewById(R.id.emailUser);
        phoneUser = findViewById(R.id.phoneUser);
        cinUser = findViewById(R.id.cinUser);
        passwordUser = findViewById(R.id.passwordUser);
        register = findViewById(R.id.register);
        firebaseAuth =FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    progressDialog.setMessage("please wait...!");
                    progressDialog.show();
                  String email_user =emailUser.getText().toString().trim();
                  String password_user =passwordUser.getText().toString().trim();
                  firebaseAuth.createUserWithEmailAndPassword(email_user,password_user).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful()){
                              sendEmailVerification();

                          }else{
                              Toast.makeText(RegisterActivity.this, "Register failed !", Toast.LENGTH_SHORT).show();
                              progressDialog.dismiss();
                          }
                      }
                  });
                } else {
                    Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

        });
        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, SigningActivity.class));
            }
        });
    }

    private void sendEmailVerification() {

        FirebaseUser user =firebaseAuth.getCurrentUser();
    if (user != null){
      user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()) {
                  sendUserData();
                  Toast.makeText(RegisterActivity.this, "Registration done ! Please check your Email ! ", Toast.LENGTH_SHORT).show();
                  firebaseAuth.signOut();
                  finish();
                  startActivity(new Intent(RegisterActivity.this, SigningActivity.class));
                  progressDialog.dismiss();
              } else {
                  Toast.makeText(RegisterActivity.this, "Registration failed ", Toast.LENGTH_SHORT).show();
                  progressDialog.dismiss();
              }
          }
      });
    }
    }


    private void sendUserData() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("Users");
        UserProfile userProfil =new UserProfile(name,email,phone,cin);
        reference.child(""+firebaseAuth.getUid()).setValue(userProfil);

    }

    private boolean validate() {
        boolean result = false;
        name = nameUser.getText().toString();
        email = emailUser.getText().toString();
        phone = phoneUser.getText().toString();
        cin = cinUser.getText().toString();
        password = passwordUser.getText().toString();
        if (name.isEmpty() || name.length() < 8) {
            nameUser.setError("name is invalid !");
        } else if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            emailUser.setError("email is invalid !");
        } else if (phone.isEmpty() || phone.length() < 8) {
            phoneUser.setError("Phone is invalid !");
        } else if (cin.isEmpty() || cin.length() < 8) {
            cinUser.setError("cin is invalid !");
        } else if (password.isEmpty() || password.length() < 8) {
            passwordUser.setError("password is invalid !");
        } else {
            result = true;
        }
        return result;
    }
}