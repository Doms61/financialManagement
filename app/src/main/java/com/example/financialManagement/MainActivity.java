package com.example.financialManagement;

//import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.financialManagement.processor.SignInProcessor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import financialManagement.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private EditText pwd;
    private EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = findViewById(R.id.emailAddress);
        pwd = findViewById(R.id.loginPwd);
    }

    public void signInClick(View view) {
//        setContentView(R.layout.activity_signin);

        startActivity(new Intent(MainActivity.this, SignInProcessor.class));
    }

    public void loginClick(View view) {
        String mail = email.getText().toString();
        String password = pwd.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
//                String id = db.collection(mail).d;
//                Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SignInProcessor.class));
            } else {
                Toast.makeText(MainActivity.this, "Incorrect email or password. Try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
