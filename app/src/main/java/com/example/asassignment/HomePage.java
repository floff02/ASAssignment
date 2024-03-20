package com.example.asassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.Long;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    Button profile, msg;
    ImageButton addBook;
    ListView bookList;
    ArrayList<String> BookList;
    int count;
    ArrayList<Object> BookData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        profile = findViewById(R.id.profile);
        addBook = findViewById(R.id.addBook);
        msg = findViewById(R.id.msg);
        bookList = (ListView) findViewById(R.id.BookList);
        BookList = new ArrayList<>();
        BookData = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://asassignment-8ab96-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference dbRef = database.getReference("books");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, BookList);

        bookList.setAdapter(adapter);



        dbRef.child("count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    count = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
                for(int id = 1; id <= count; id++){
                    getBook(dbRef, id, adapter);
                }
            }});

        addBook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, UserForm.class);
                startActivity(intent);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, Profile.class);
                startActivity(intent);
            }
        });





    }

    protected void getBook(DatabaseReference dbRef, int id, ArrayAdapter<String> adapter) {
        dbRef.child(String.valueOf(id)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (task.getResult().getValue() != null) {
                        BookData.add(task.getResult().getValue());
                        BookList.add(task.getResult().getValue()
                                .toString()
                                .replace("{", "\n ")
                                .replace(",", "\n")
                                .replace("}", "\n")
                                .replace("pub=","publisher=")
                                .replace("=", ": ")
                        );
                        adapter.notifyDataSetChanged();
                        Log.d("firebase", BookData.toString());
                    }
                }
            }
        });
    }
}