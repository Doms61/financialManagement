package com.example.financialManagement.processor;

import android.os.Bundle;
import android.widget.TextView;

import com.example.financialManagement.data.model.LoggedInUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import financialManagement.R;

/**
 * Processor for the user profile
 */
public class UserProfileProcessor extends AppCompatActivity {

    /**
     * Firebase related declarations
     */
    private FirebaseFirestore db;
    private FirebaseUser loggedInUser;
    private FirebaseAuth firebaseAuth;

    /**
     * User related declarations
     */
    private LoggedInUser user;

    /**
     * TextViews and EditTexts holding user information
     */
    private TextView userName;
    private TextView userId;
    private TextView userEmail;

    /**
     * OnCreate method, which should populate all the necessary information with the current users info.
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        loggedInUser = firebaseAuth.getCurrentUser();

        userName = findViewById(R.id.userNameTextView);
        userEmail = findViewById(R.id.emailTextView);
        userId = findViewById(R.id.userIdTextView);

        db.collection(Objects.requireNonNull(loggedInUser.getEmail())).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    System.out.println(document.getId());
                    System.out.println("document " + document);
                    DocumentReference docRef = db.collection(loggedInUser.getEmail()).document(document.getId());
                    docRef.get().addOnSuccessListener(documentSnapshot -> {
                        user = documentSnapshot.toObject(LoggedInUser.class);

                        assert user != null;
                        userName.setText(user.getUserName());
                        userEmail.setText(user.getEmail());
                        userId.setText(user.getUserId());
                    });
                }
            }
        });
    }
}
