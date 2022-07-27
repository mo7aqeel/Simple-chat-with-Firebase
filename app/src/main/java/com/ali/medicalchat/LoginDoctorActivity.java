package com.ali.medicalchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginDoctorActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText emailEditText, passEditText;
    private String email, pass;
    private SharedPreferences sharedPref;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);

        mAuth = FirebaseAuth.getInstance();
        sharedPref = getSharedPreferences("my shared pref", MODE_PRIVATE);

        progressBar = findViewById(R.id.progressBar4);
        emailEditText = findViewById(R.id.email_edit_text_doc);
        passEditText = findViewById(R.id.password_edit_text_doc);

        Button loginBtn = findViewById(R.id.login_btn_doc);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEmail();
                login();
            }
        });

    }

    private void getEmail(){
        email = emailEditText.getText().toString();
        pass = passEditText.getText().toString();

        if (email.isEmpty()){
            emailEditText.setError("E-mail is required");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Pleas provide valid email");
            emailEditText.requestFocus();
            return;
        }

        if (pass.isEmpty()){
            passEditText.setError("Password is required");
            passEditText.requestFocus();
        }
    }

    private void login() {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
//                    SharedPreferences.Editor editor = sharedPref.edit();
//                    editor.clear();
//                    editor.putString("email", email);
//                    editor.putString("password", pass);
//                    editor.apply();
                    Intent intent = new Intent(LoginDoctorActivity.this, MainActivity.class);
                    intent.putExtra("type", "doctor");
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginDoctorActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}