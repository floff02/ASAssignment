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
//imports all the libraries that are needed for this page of the app
public class HomePage extends AppCompatActivity {
    //Initializes all variables
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
        // connects variables to the xml through IDs
        profile = findViewById(R.id.profile);
        addBook = findViewById(R.id.addBook);
        msg = findViewById(R.id.msg);
        bookList = (ListView) findViewById(R.id.BookList);
        BookList = new ArrayList<>();
        BookData = new ArrayList<>();
        // connects this page of the application to the firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://asassignment-8ab96-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference dbRef = database.getReference("books");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, BookList);

        bookList.setAdapter(adapter);


        //This accesses the Realtime database to get the amount of records stored in the database, this is stored in the database as "count".
        // ID is also incremented until it is the same value as count this to ensure that the correct amount of Books are displayed the the user
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
        //allows the user to swap screens between home page and user form
        addBook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, UserForm.class);
                startActivity(intent);

            }
        });
        //Allows the user to swap screens between homepage and profile
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, Profile.class);
                startActivity(intent);
            }
        });





    }
    //This function calls the data stored in the realtime database and populates the list view on the homepages displaying the books that the user has entered in to database
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