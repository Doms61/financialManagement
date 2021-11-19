package com.example.financialManagement.processor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.financialManagement.data.model.LoggedInUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
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

    private FabProcessor fabProcessor;

    private FloatingActionButton fabSettings;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabDelete;
    private FloatingActionButton fabSave;
    private List<FloatingActionButton> fabList = new ArrayList<>();

    private TextView tvEdit;
    private TextView tvDelete;
    private TextView tvSave;
    private List<TextView> tvList = new ArrayList<>();

    private boolean fabExpanded = false;

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

        setUp();

        setOnClickListeners();

        fabProcessor.closeSubMenus(fabList, fabSettings, tvList);

        getUserData();
    }

    public void onBtnClick(View view) {
        popUp();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void popUp() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_create_balance, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; //lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(this.findViewById(R.id.userProfil_layout), Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }

    private void setOnClickListeners() {
        fabSettings.setOnClickListener(view -> {
            if(fabExpanded) {
                fabProcessor.closeSubMenus(fabList, fabSettings, tvList);
                popUp();
                fabExpanded = false;
            }
            else {
                fabProcessor.openSubMenus(fabList, fabSettings, tvList);
                popUp();
                fabExpanded = true;
            }
        });
    }

    private void setUp() {
        userName = findViewById(R.id.userNameTextView);
        userEmail = findViewById(R.id.emailTextView);
        userId = findViewById(R.id.userIdTextView);

        fabSettings = findViewById(R.id.fab_settings);
        fabProcessor = new FabProcessor();

        fabEdit = findViewById(R.id.fab_edit);
        fabDelete = findViewById(R.id.fab_delete);
        fabSave = findViewById(R.id.fab_save);

        fabList.add(fabEdit);
        fabList.add(fabDelete);
        fabList.add(fabSave);

        tvEdit = findViewById(R.id.fab_edit_tv);
        tvDelete = findViewById(R.id.fab_delete_tv);
        tvSave = findViewById(R.id.fab_save_tv);

        tvList.add(tvEdit);
        tvList.add(tvDelete);
        tvList.add(tvSave);
    }

    private void getUserData(){
        db.collection(Objects.requireNonNull(loggedInUser.getEmail())).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
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
