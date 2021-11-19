package com.example.financialManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.financialManagement.processor.BalanceProcessor;
import com.example.financialManagement.processor.SignUpProcessor;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import financialManagement.R;

/**
 * Main activity class, or also knows as first page or login page
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private EditText pwd;
    private EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.emailAddress);
        pwd = findViewById(R.id.loginPwd);
    }

    /**
     * Sign in button, just redirects the user to the sign in page
     *
     * @param view View
     */
    public void signUpClick(View view) {
        startActivity(new Intent(MainActivity.this, SignUpProcessor.class));
    }

    /**
     * Login click, takes the imputed values and tries to log in the user with his email and password
     *
     * @param view View
     */
    public void loginClick(View view) {
        String mail = "qwee@qwe.com";//email.getText().toString();
        String password = "qweqweqwe";//pwd.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(MainActivity.this, BalanceProcessor.class));//UserProfileProcessor.class));//
            } else {
                Toast.makeText(MainActivity.this, "Incorrect email or password. Try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
