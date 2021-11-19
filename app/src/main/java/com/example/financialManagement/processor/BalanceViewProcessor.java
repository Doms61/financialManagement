package com.example.financialManagement.processor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.financialManagement.data.Balance;
import com.example.financialManagement.data.Budget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import financialManagement.R;

/**
 * Class for handling a specific balance and all his properties including it's spending.
 */
public class BalanceViewProcessor extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    //FAB Declarations
    private FabProcessor fabProcessor;

    private FloatingActionButton fabSettings;
    private FloatingActionButton fabDelete;
    private FloatingActionButton fabSave;
    private final List<FloatingActionButton> fabList = new ArrayList<>();

    private TextView tvDelete;
    private TextView tvSave;
    private final List<TextView> tvList = new ArrayList<>();

    private boolean fabExpanded = false;
    //end FAB Declarations

    private TextView balanceName;
    private TextView balanceAmount;
    private Balance balance;

    private PopupWindow popupWindow;
    private TextView spendingName;
    private TextView spendingAmount;
    private List<Budget> budgets;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        setUp();

        setOnClickListeners();

        fabProcessor.closeSubMenus(fabList, fabSettings, tvList);

        balance = (Balance) getIntent().getSerializableExtra("balance");

        balanceName = findViewById(R.id.balanceNameTV);
        balanceAmount = findViewById(R.id.balanceAmountTV);

        balanceName.setText(balance.getBalanceName());
        balanceAmount.setText(String.valueOf(balance.getBalance()));
    }


    /**
     * On click listeners for FAB mainly
     */
    @SuppressLint("SetTextI18n")
    private void setOnClickListeners() {
        fabSettings.setOnClickListener(view -> {
            if (fabExpanded) {
                fabProcessor.closeSubMenus(fabList, fabSettings, tvList);
                fabExpanded = false;
            } else {

                fabSave.setImageResource(android.R.drawable.ic_input_add);
                fabSave.setVisibility(View.VISIBLE);

                tvSave.setText("Add spending");
                tvSave.setVisibility(View.VISIBLE);


                fabProcessor.openSubMenus(fabList, fabSettings, tvList);
                fabExpanded = true;
            }
        });
        fabSave.setOnClickListener(task -> popUp());
    }

    /**
     * Saving new spending for this balance
     *
     * @param view View
     */
    public void saveSpendingBtn(View view) {

    }

    /**
     * Canceling new spending for this balance
     *
     * @param view View
     */
    public void cancelSpendingBtn(View view) {
        popupWindow.dismiss();
    }


    /**
     * Popup creation, initialization, declaration
     */
    private void popUp() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.activity_create_spending, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popupView, width, height, true);

        // show the popup window
        popupWindow.showAtLocation(this.findViewById(R.id.balanceSpendingReports_rv), Gravity.CENTER, 0, 0);

        //TODO: pull out dropdown and items to be global, inflate it with real data from firebase (doable only after budget is available)
        //get the spinner from the xml.
        Spinner dropdown = popupView.findViewById(R.id.typeSpinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "three"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

//        createBalanceName = popupView.findViewById(R.id.createBalanceName_tv);
//        createBalanceAmount = popupView.findViewById(R.id.createBalanceAmount_tv);
    }

    /**
     * FAB initialization
     */
    private void setUp() {
        fabSettings = findViewById(R.id.fab_settings);
        fabProcessor = new FabProcessor();

        FloatingActionButton fabEdit = findViewById(R.id.fab_edit);
        fabDelete = findViewById(R.id.fab_delete);
        fabSave = findViewById(R.id.fab_save);

        fabList.add(fabEdit);
        fabList.add(fabDelete);
        fabList.add(fabSave);

        TextView tvEdit = findViewById(R.id.fab_edit_tv);
        tvDelete = findViewById(R.id.fab_delete_tv);
        tvSave = findViewById(R.id.fab_save_tv);

        tvList.add(tvEdit);
        tvList.add(tvDelete);
        tvList.add(tvSave);
    }
}
