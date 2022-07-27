package com.ali.medicalchat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference mRef;
    private ArrayList<ChatMessage> list;
    private ChatAdapter adapter;
    private RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        list = new ArrayList<>();

        mRef = FirebaseDatabase.getInstance().getReference("chat")
                .child(intent.getStringExtra("dr name"))
                .child(intent.getStringExtra("user name"));

        EditText text = findViewById(R.id.input);

        recyclerView = findViewById(R.id.chat_listview);
        Log.i("Dr name ::::::", intent.getStringExtra("dr name"));
        Log.i("User name ::::::", intent.getStringExtra("user name"));
        adapter = new ChatAdapter(list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(list.size()-1);
        readMessages();

        ((FloatingActionButton)findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatMessage message = new ChatMessage(text.getText().toString(), MainActivity.getEmail());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime datetime = LocalDateTime.now();
                String output = formatter.format(datetime);
                list.clear();
                mRef.child(output).setValue(message);
                text.setText("");
            }
        });
    }

    private void readMessages(){
        mRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot n : snapshot.getChildren()){
                    list.add(n.getValue(ChatMessage.class));
                }//end for()
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(list.size()-1);
            }//end onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}