package com.ali.medicalchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    EditText fullNameEditText, emailEditText, passwordEditText;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPref = getSharedPreferences("my shared pref", MODE_PRIVATE);

        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        fullNameEditText = findViewById(R.id.full_name_edit_text);
        emailEditText = findViewById(R.id.email_register);
        passwordEditText = findViewById(R.id.password_register);
    }

    public void registerAccount(View view) {

        String fullName = fullNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String pass = passwordEditText.getText().toString();

        if (fullName.isEmpty()){
            fullNameEditText.setError("Full name is required");
            fullNameEditText.requestFocus();
            return;
        }

        if (email.isEmpty()){
            emailEditText.setError("E-mail is required");
            emailEditText.requestFocus();
            return;
        }

        if (pass.isEmpty()){
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (pass.length() < 6){
            passwordEditText.setError("Password is week minimum length is 6 characters");
            passwordEditText.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Pleas provide valid email");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("email", email);
                                editor.putString("password", pass);
                                editor.apply();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}