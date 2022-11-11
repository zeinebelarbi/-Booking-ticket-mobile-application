package com.example.mobileapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.Principal;

public class SigningActivity extends AppCompatActivity {

    private TextView newUser, forgetPassword;
    private EditText emailSign, passwordSignIn;
    private Button signIn;
    private CheckBox remember;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);
        newUser = findViewById(R.id.newUser);
        emailSign = findViewById(R.id.emailSignIn);
        passwordSignIn = findViewById(R.id.passwordSignIn);
        signIn = findViewById(R.id.btnSignIn);
        remember = findViewById(R.id.remember);

        forgetPassword = findViewById(R.id.forgetPassword);

        forgetPassword.setOnClickListener(view -> {
            startActivity(new Intent(SigningActivity.this, ForgetPasswordActivity.class));
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigningActivity.this, RegisterActivity.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
        String checkBox = preferences.getString("remember", "");

        if (checkBox.equals("true")) {
            startActivity(new Intent(SigningActivity.this, PrincipalActivity.class));
        } else if (checkBox.equals("false")) {
            Toast.makeText(this, "Please sign in !", Toast.LENGTH_SHORT).show();
        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                } else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailSign.getText().toString().isEmpty() || !emailSign.getText().toString().contains("@")) {
                    emailSign.setError("Email is invalid !");
                } else if (passwordSignIn.getText().toString().isEmpty() || passwordSignIn.getText().toString().length() < 8) {
                    passwordSignIn.setError("password is invalid !");
                } else {
                    validate(emailSign.getText().toString(), passwordSignIn.getText().toString());
                }
            }
        });


    }

    private void validate(String email, String password) {

        progressDialog.setMessage("please wait...!");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    checkEmailVerification();
                } else {
                    Toast.makeText(SigningActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

    }

    private void checkEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        boolean emailFlag = user.isEmailVerified();

        if (emailFlag) {
            startActivity(new Intent(SigningActivity.this, PrincipalActivity.class));
        } else if (!emailFlag) {
            Toast.makeText(this, "Please check your email !", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }
}