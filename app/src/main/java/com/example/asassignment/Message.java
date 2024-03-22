package com.example.asassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Message extends AppCompatActivity {
    Button profile, back, send;
    ListView msgList;
    TextInputEditText editTextmessage;
    ArrayList<String> MessageList;
    int count;
    int id;
    ArrayList<Object> MessageData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        profile = findViewById(R.id.profile);
        back = findViewById(R.id.back);
        editTextmessage = findViewById(R.id.message);
        send = findViewById(R.id.send);
        msgList = (ListView) findViewById(R.id.MsgList);
        MessageList = new ArrayList<>();
        MessageData = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://asassignment-8ab96-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference dbRef = database.getReference("messages");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MessageList);

        msgList.setAdapter(adapter);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Message.this, Profile.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Message.this, HomePage.class);
                startActivity(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                message = String.valueOf(editTextmessage.getText());
                id++;

                Messagej msg = new Messagej(message, id);
                DatabaseReference msgRef = dbRef.child(String.valueOf(id));
                dbRef.child("count").setValue(id);
                msgRef.child("user").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                msgRef.child("message").setValue(msg.message);
                Toast.makeText(Message.this, "Message Sent", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Message.this, Message.class);
                startActivity(intent);
            }
        });

        dbRef.child("count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    id = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
                for (int i = 1; i <= id; i++) {
                    Log.d("firebase", "Log " + i);
                    getMsg(dbRef, i, adapter);
                }
            }
        });
    }

    protected void getMsg(DatabaseReference dbRef, int id, ArrayAdapter<String> adapter) {
        dbRef.child(String.valueOf(id)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (task.getResult().getValue() != null) {
                        MessageData.add(task.getResult().getValue());
                        String[] msgs = task.getResult().getValue()
                                .toString()
                                .replace("{", "")
                                .replace("}", "")
                                .replace("message=", "\n")
                                .split(", user=");
                        MessageList.add(String.join("", msgs[1], msgs[0]));
                        adapter.notifyDataSetChanged();
                        Log.d("firebase", MessageData.toString());
                    }
                }
            }
        });
    }


}
