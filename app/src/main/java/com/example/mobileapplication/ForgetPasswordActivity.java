package com.example.mobileapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private Button backToSignIn, btnResetPassword;
    private EditText emailForgetPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        backToSignIn = findViewById(R.id.backToSignIn);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        emailForgetPassword = findViewById(R.id.emailForgetPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailForgetPassword.getText().toString();
                if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
                    emailForgetPassword.setError("email is invalid !");
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgetPasswordActivity.this, "Password reset email sent !", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgetPasswordActivity.this, SigningActivity.class));
                                finish();
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        backToSignIn.setOnClickListener(view -> {
            startActivity(new Intent(ForgetPasswordActivity.this, SigningActivity.class));
        });
    }
}


