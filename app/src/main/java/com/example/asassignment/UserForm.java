package com.example.asassignment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.lang.Long;
import android.os.Bundle;
//imports all the libraries that are needed for this page of the app
public class UserForm extends AppCompatActivity {
    //Initializes all variables
    TextInputEditText editTextBookName, editTextAuthorName, editTextPublisherName, editTextISBN10, editTextISBN13, editTextReview;
    Button submit, back;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        // connects variables to the xml through IDs
        editTextBookName = findViewById(R.id.BookName);
        editTextAuthorName = findViewById(R.id.AuthorName);
        editTextPublisherName = findViewById(R.id.PublisherName);
        editTextISBN10 = findViewById(R.id.ISBN10_Num);
        editTextISBN13 = findViewById(R.id.ISBN13_Num);
        editTextReview = findViewById(R.id.review);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);


        //Connects this page of the app to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://asassignment-8ab96-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference dbRef = database.getReference("books");
        //Access the realtime database to access count, this is done to ensure when a new book is created it has a unique ID
        dbRef.child("count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    id = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }});
        //Allows the user to create a new book and add the book to the database ensuring that each book added has a unique ID
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String BookName, Author, Publisher, ISBN10, ISBN13, Review ;
                BookName = String.valueOf(editTextBookName.getText());
                Author = String.valueOf(editTextAuthorName.getText());
                Publisher = String.valueOf(editTextPublisherName.getText());
                ISBN10 = String.valueOf(editTextISBN10.getText());
                ISBN13 = String.valueOf(editTextISBN13.getText());
                Review = String.valueOf(editTextReview.getText());

                id++;
                Book book = new Book(BookName, id, Author, Publisher, ISBN10, ISBN13, Review);
                DatabaseReference bookRef = dbRef.child(String.valueOf(book.cat_num));
                dbRef.child("count").setValue(id);
                bookRef.child("name").setValue(book.name);
                bookRef.child("author").setValue(book.author);
                bookRef.child("pub").setValue(book.pub);
                bookRef.child("ISBN_10").setValue(book.ISBN_10);
                bookRef.child("ISBN_13").setValue(book.ISBN_13);
                bookRef.child("review").setValue(book.Review);
                Toast.makeText(UserForm.this, "Saved Book", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserForm.this, HomePage.class);
                startActivity(intent);
            }
        });



        //Allows the user to back back to the HomePage
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserForm.this, HomePage.class);
                startActivity(intent);
            }
        });
    }


}