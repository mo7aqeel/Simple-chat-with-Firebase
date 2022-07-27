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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passEditText;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private SharedPreferences sharedPref;
    private String email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.email_edit_text_doc);
        passEditText = findViewById(R.id.password_edit_text_doc);
        progressBar = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
        sharedPref = getSharedPreferences("my shared pref", MODE_PRIVATE);

//        email = sharedPref.getString("email", "");
//        pass = sharedPref.getString("password", "");
//
//        if (!email.isEmpty() && !pass.isEmpty())
//            login();

        TextView registerText = findViewById(R.id.register_txtview);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        Button loginBtn = findViewById(R.id.login_btn_doc);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEmail();
                login();
            }
        });

        ((TextView) findViewById(R.id.login_doc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LoginDoctorActivity.class));
                finish();
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
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("type", "user");
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}