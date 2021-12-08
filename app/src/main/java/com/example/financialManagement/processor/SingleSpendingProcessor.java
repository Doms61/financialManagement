package com.example.financialManagement.processor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financialManagement.data.Balance;
import com.example.financialManagement.data.Spending;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import financialManagement.R;

import static com.example.financialManagement.enumerations.Enums.BALANCE_LIST;
import static com.example.financialManagement.enumerations.Enums.SPENDING_LIST;
import static com.example.financialManagement.enumerations.Enums.SUBSPENDING_LIST;

public class SingleSpendingProcessor extends AppCompatActivity {

    private FirebaseFirestore db;
    private String email;

    private Spending spending;
    private TextView spendingName;
    private TextView spendingAmount;
    private TextView spendingDesc;
    private TextView Date;

    //FAB Declarations
    private FabProcessor fabProcessor;

    private FloatingActionButton fabSettings;
    private FloatingActionButton fabDelete;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabSave;
    private final List<FloatingActionButton> fabList = new ArrayList<>();

    private TextView tvSave;
    private final List<TextView> tvList = new ArrayList<>();

    private boolean fabExpanded = false;

    private CardView fabSaveCard;
    private final List<CardView> fabCards = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    //end FAB Declarations

    private PopupWindow popupWindow;

    private Balance balance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_spending);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        email = currentUser.getEmail();
        spending = (Spending) getIntent().getSerializableExtra("spending");
        balance = (Balance) getIntent().getSerializableExtra("balance");

        setUp();

        setOnClickListeners();

        fabProcessor.closeSubMenus(fabList, fabSettings, fabCards, tvList);

        TextView spendingName = findViewById(R.id.singleSpendingName_tv);
        TextView spendingAmount = findViewById(R.id.singleSpendingAmount_tv);
        TextView spendingDesc = findViewById(R.id.singleSpendingDesc_tv);
        TextView spendingDate = findViewById(R.id.singleSpendingDate_tv);

        spendingName.setText(spending.getName());
        spendingAmount.setText(String.valueOf(spending.getAmount()));
        spendingDesc.setText(spending.getDescription());
        spendingDate.setText(String.valueOf(spending.getDate()));

    }

    private void setOnClickListeners() {
        fabSettings.setOnClickListener(view -> {
            if (fabExpanded) {
                fabProcessor.closeSubMenus(fabList, fabSettings, fabCards, tvList);
                fabExpanded = false;
            } else {
                fabProcessor.openSubMenus(fabList, fabSettings, fabCards, tvList);

                fabSave.setVisibility(View.INVISIBLE);
                tvSave.setVisibility(View.INVISIBLE);
                fabSaveCard.setVisibility(View.INVISIBLE);

                fabExpanded = true;
            }
        });

//        fabEdit.setOnClickListener(view -> editSpending());

        fabDelete.setOnClickListener(view -> deletePopUp());
    }

    private void deletePopUp() {
        View popupView = createPopUp(R.layout.activity_deletion);

        popupView.findViewById(R.id.confirmDeletion_btn).setOnClickListener(task -> deleteSpending());
        popupView.findViewById(R.id.cancelDeletion_btn).setOnClickListener(task -> popupWindow.dismiss());
    }

    private void deleteSpending(){
        db.collection(email)
                .document(BALANCE_LIST.getDescription())
                .collection(BALANCE_LIST.getDescription())
                .document(balance.getBalanceName())
                .collection(SPENDING_LIST.getDescription())
                .document(spending.getName())
                .collection(SUBSPENDING_LIST.getDescription())
                .document(spending.getDate() + " " + spending.getName())
                .delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(this, spending.getName() + " was deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error: Spending was not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
        popupWindow.dismiss();
        startActivity(new Intent(SingleSpendingProcessor.this, SpendingProcessor.class));
    }

    private View createPopUp(int activity) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(activity, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popupView, width, height, true);

        // show the popup window
        popupWindow.showAtLocation(this.findViewById(R.id.singleSpending), Gravity.CENTER, 0, 0);

        return popupView;
    }

    private void setUp() {
        fabSettings = findViewById(R.id.fab_settings);
        fabProcessor = new FabProcessor();

        fabEdit = findViewById(R.id.fab_edit);
        fabDelete = findViewById(R.id.fab_delete);
        fabSave = findViewById(R.id.fab_save);

        fabList.add(fabEdit);
        fabList.add(fabDelete);
        fabList.add(fabSave);

        TextView tvEdit = findViewById(R.id.fab_edit_tv);
        TextView tvDelete = findViewById(R.id.fab_delete_tv);
        tvSave = findViewById(R.id.fab_save_tv);

        tvList.add(tvEdit);
        tvList.add(tvDelete);
        tvList.add(tvSave);

        CardView fabEditCard = findViewById(R.id.editCard);
        fabSaveCard = findViewById(R.id.saveCard);
        CardView fabDeleteCard = findViewById(R.id.deleteCard);

        fabCards.add(fabDeleteCard);
        fabCards.add(fabSaveCard);
        fabCards.add(fabEditCard);
    }
}
