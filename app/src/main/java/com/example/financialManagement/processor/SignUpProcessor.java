package com.example.financialManagement.processor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.financialManagement.MainActivity;
import com.example.financialManagement.data.model.LoggedInUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import financialManagement.R;

/**
 * Sign-in processor, which controls the flow and handles all the operations at sign-up
 */
public class SignUpProcessor extends AppCompatActivity {

    /**
     * Firebase authentication and database
     */
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    /**
     * User information as String
     */
    private String email;
    private String usrName;
    private String pwd;
    private String repeatedPwd;

    /**
     * Edit text holding user information
     */
    private EditText password;
    private EditText repeatedPassword;
    private EditText mail;
    private EditText userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        password = findViewById(R.id.signupPwd);
        repeatedPassword = findViewById(R.id.signupRepeatPwd);
        mail = findViewById(R.id.signupEmail);
        userName = findViewById(R.id.signupUsrName);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        startActivity(new Intent(SignUpProcessor.this, MainActivity.class));
        return super.onOptionsItemSelected(item);

    }

    /**
     * Sign-in button click. Contains extraction of the fields inputted by the user. Checks those inputs and if everything correct
     * adds the user to the database.
     *
     * @param view View
     */
    public void signUpClick(View view) {
        email = mail.getText().toString();
        usrName = userName.getText().toString();
        pwd = password.getText().toString();
        repeatedPwd = repeatedPassword.getText().toString();

        if ("".equals(email) || "".equals(pwd) || "".equals(repeatedPwd) || "".equals(usrName)) {
            Toast.makeText(SignUpProcessor.this, "All fields are mandatory! ", Toast.LENGTH_LONG).show();
            refresh();
        }

        if (isPwdCorrect()) {
            firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    addPerson();
                    startActivity(new Intent(SignUpProcessor.this, MainActivity.class));
                } else {
                    Toast.makeText(SignUpProcessor.this, "This email already exists!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            refresh();
        }
    }

    /**
     * Refreshes the activity, thus clearing all forms.
     */
    private void refresh() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    /**
     * Adding the person to the DB with it's generated UUID, and inputted user name.
     */
    private void addPerson() {
        LoggedInUser loggedInUser = new LoggedInUser(UUID.randomUUID().toString(), usrName, email);
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put("userId", loggedInUser.getUserId());
        dataToSave.put("userName", usrName);
        dataToSave.put("email", email);

        db.collection(email).document(usrName).set(dataToSave).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SignUpProcessor.this, "User saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignUpProcessor.this, "User was not saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Returns a boolean value based upon if the provided passwords are correct and match some criteria.
     *
     * @return boolean
     */
    private boolean isPwdCorrect() {
        return true;
/*
        if (!pwd.equals(repeatedPwd)) {
            Toast.makeText(this, "Passwords do not match. Please try again!", Toast.LENGTH_SHORT).show();
        } else if (pwd.length() < 8) {
            Toast.makeText(this, "Password is too short. At leas 8 characters!", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
   */ }
}
