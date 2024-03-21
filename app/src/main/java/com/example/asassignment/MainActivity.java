package com.example.asassignment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//imports all the libraries that are needed for this page of the app
public class MainActivity extends AppCompatActivity {
    //Initializes all variables
    TextInputEditText editTextEmail, editTextPassword;

    Button signIn;

    TextView signUp;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // connects variables to the xml through IDs
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
        //Allows the user to change screens between the login screen and the register screen
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
        //Checks if the details entered by the user match a user in the database, if there is a match the user logs in and is redirected the the homepage
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this,"Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, HomePage.class);
                                startActivity(intent);
                                finish();

                            }
                            else {
                                Toast.makeText(MainActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
    }
}