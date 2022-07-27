package com.ali.medicalchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private String[] drNames = {"Dr Ali", "Dr Ahmed", "Dr Osama", "Dr Hussein", "Dr Nabeel",
    "Dr Bassam", "Dr Mohammed"};

    private String[] drEmail = {"drali", "drahmed", "drosama", "drhussein", "drnabeel",
            "drbassam", "drmohammed"};

    private SharedPreferences sharedPref;
    private ListView listOfMessages;

    private ArrayAdapter<String> usersAdapter;
    private ArrayList<String> usersList;

    private String drName = "";
    private String userName = "";
    private int c = 0;
    private boolean isDoctor = false;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences("my shared pref", MODE_PRIVATE);

        usersList = new ArrayList<>();
        listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (type.equals("doctor"))
            isDoctor = true;

        if (isDoctor){
            getUsersList();
            drName = drNames[c];
        }
        else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.support_simple_spinner_dropdown_item, drNames);
            listOfMessages.setAdapter(adapter);
            userName = getEmail();
        }

        listOfMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                if (drName.isEmpty()) {
                    drName = ((TextView) view).getText().toString();
                    intent.putExtra("dr name", drName);
                    intent.putExtra("user name", userName);
                    drName = "";
                }
                if (userName.isEmpty()) {
                    userName = ((TextView) view).getText().toString();
                    intent.putExtra("dr name", drName);
                    intent.putExtra("user name", userName);
                    userName = "";
                }
                startActivity(intent);
            }
        });

        }

    private void getUsersList() {
        for (int i=0; i< drEmail.length; i++){
            if (getEmail().equals(drEmail[i]))
                break;
            c++;
        }
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("chat")
                .child(drNames[c]);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot n : snapshot.getChildren()){
                    usersList.add(n.getKey());
                }
                usersAdapter = new ArrayAdapter<>(MainActivity.this,
                        R.layout.support_simple_spinner_dropdown_item, usersList);
                listOfMessages.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            // Close activity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            Toast.makeText(MainActivity.this,
                    "You have been signed out.",
                    Toast.LENGTH_LONG)
                    .show();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();
        }
        return true;
    }

    public static String getEmail(){
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        int i = email.indexOf('@');
        return email.substring(0, i);
    }

}