package com.example.financialManagement.processor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.financialManagement.MainActivity;
import com.example.financialManagement.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import financialManagement.R;

public class SignInProcessor extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private String email;
    private  String usrName;
    private  String pwd;
    private  String repeatedPwd;

    private EditText password;
    private EditText repeatedPassword;
    private EditText mail;
    private EditText userName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        password = findViewById(R.id.signinPwd);
        repeatedPassword = findViewById(R.id.signinRepeatPwd);
        mail = findViewById(R.id.signinEmail);
        userName = findViewById(R.id.signinUsrName);

    }

    public void signInClick (View view) {
        //TODO: add Objects.requireNonNull()

        email = mail.getText().toString();
        usrName = userName.getText().toString();
        pwd = password.getText().toString();
        repeatedPwd = repeatedPassword.getText().toString();

        if("".equals(email)|| "".equals(pwd) ||"".equals(repeatedPwd)) {
            Toast.makeText(SignInProcessor.this, "Email and password cannot be empty.", Toast.LENGTH_LONG).show();
            refresh();
        }

        if (isPwdCorrect()) {
            //TODO: add user to DB and redirect to login page
            firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    addPerson();
                    startActivity(new Intent(SignInProcessor.this, MainActivity.class));
                }
                else{
                    Toast.makeText(SignInProcessor.this, "Incorrect email. Try again!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //TODO: reset password fields
        }
    }

    private void refresh() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
    private void addPerson() {
        //TODO: add userName to person
        LoggedInUser loggedInUser = new LoggedInUser(UUID.randomUUID().toString(), usrName, email);
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put("User ID", loggedInUser.getUserId());
        dataToSave.put("User name", usrName);
        dataToSave.put("Email", email);

        db.collection(email).document(usrName).set(dataToSave).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SignInProcessor.this, "User saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignInProcessor.this, "User was not saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Returns a boolean value based upon if the provided passwords are correct and match some criteria.
     *
     * @return boolean
     */
    private boolean isPwdCorrect() {

        if (!pwd.equals(repeatedPwd)) {
            Toast.makeText(this, "Passwords do not match. Please try again!", Toast.LENGTH_SHORT).show();
        } else if (pwd.length() < 8 ){
            Toast.makeText(this, "Password is too short. At leas 8 characters!", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }
}
