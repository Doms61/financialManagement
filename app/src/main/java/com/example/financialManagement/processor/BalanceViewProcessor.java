package com.example.financialManagement.processor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financialManagement.adapter.SpendingAdapter;
import com.example.financialManagement.data.Balance;
import com.example.financialManagement.data.Budget;
import com.example.financialManagement.data.Spending;
import com.example.financialManagement.interfaces.OnListItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import financialManagement.R;

import static com.example.financialManagement.enumerations.Enums.BALANCE_LIST;
import static com.example.financialManagement.enumerations.Enums.SPENDING_LIST;

/**
 * Class for handling a specific balance and all his properties including it's spending.
 */
public class BalanceViewProcessor extends AppCompatActivity implements OnListItemClickListener {

    private FirebaseFirestore db;

    private Balance balance;

    private RecyclerView spendingList;
    private RecyclerView.Adapter spendingAdapter;

    private final ArrayList<Spending> spendings = new ArrayList<>();
    private Spending spending;

    // Path to the document containing all the balances on firebase
    private String docPath;

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

    private final List<CardView> fabCards = new ArrayList<>();
    //end FAB Declarations

    // popup window (new spending creation)
    private PopupWindow popupWindow;
    private TextView spendingName;
    private TextView spendingAmount;
    private TextView spendingDescription;
    private final List<Budget> budgets = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private final ArrayList<String> budgetNames = new ArrayList<>();

    private String email;

    private EditText createBalanceAmount;
    private EditText createBalanceName;
    private Button editBalanceBtn;
    private Button cancelBalanceBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        spendingList = findViewById(R.id.balanceSpendingReports_rv);
        spendingList.hasFixedSize();
        spendingList.setLayoutManager(new LinearLayoutManager(this));

        setUp();

        setOnClickListeners();

        fabProcessor.closeSubMenus(fabList, fabSettings, fabCards, tvList);

        balance = (Balance) getIntent().getSerializableExtra("balance");

        TextView balanceName = findViewById(R.id.balanceNameTV);
        TextView balanceAmount = findViewById(R.id.balanceAmountTV);

        balanceName.setText(balance.getBalanceName());
        balanceAmount.setText(String.valueOf(balance.getBalance()));

        assert currentUser != null;
        email = currentUser.getEmail();

        assert email != null;
        docPath = db.collection(email)
                .document(BALANCE_LIST.getDescription())
                .collection(BALANCE_LIST.getDescription())
                .document(balance.getBalanceName())
                .collection(SPENDING_LIST.getDescription()).getPath();

        getSpendings();
        spendingAdapter = new SpendingAdapter(spendings, this);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        startActivity(new Intent(BalanceViewProcessor.this, SpendingProcessor.class)
                .putExtra("spending", spendings.get(clickedItemIndex)));
    }

    /**
     * Saving new spending for this balance
     *
     * @param view View
     */
    public void saveSpendingBtn(View view) {
        Map<String, Object> dataToSave = new HashMap<>();
        spending = new Spending();
        spending.setAmount(BigDecimal.valueOf(Double.parseDouble(spendingAmount.getText().toString())).setScale(2, RoundingMode.HALF_UP).doubleValue());
        spending.setName(spendingName.getText().toString());
        spending.setDate(new Date());
        spending.setDescription(spendingDescription.getText().toString());

        for (Spending spend : spendings) {
            if (spend.getName().equals(spending.getName()))
                spending.setAmount(BigDecimal.valueOf(spend.getAmount() + spending.getAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }

        dataToSave.put("name", spending.getName());
        dataToSave.put("amount", spending.getAmount());
        dataToSave.put("date", spending.getDate());
        dataToSave.put("description", spending.getDescription());

        db.collection(docPath).document(spending.getName()).set(dataToSave).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "New spending added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Spending failed to be created", Toast.LENGTH_SHORT).show();
            }
        });
        updateBalance();
        popupWindow.dismiss();
        refresh();
    }

    private void getBudgetNames() {
        //TODO: get budgets from db -> budgets
        for (Budget budget : budgets) {
            budgetNames.add(budget.getName());
        }
    }

    /**
     * On click listeners for FAB mainly
     */
    @SuppressLint("SetTextI18n")
    private void setOnClickListeners() {
        fabSettings.setOnClickListener(view -> {
            if (fabExpanded) {
                fabProcessor.closeSubMenus(fabList, fabSettings, fabCards, tvList);
                fabExpanded = false;
            } else {

                fabSave.setImageResource(android.R.drawable.ic_input_add);
                fabSave.setVisibility(View.VISIBLE);

                tvSave.setText("Add spending");
                tvSave.setVisibility(View.VISIBLE);

                fabProcessor.openSubMenus(fabList, fabSettings, fabCards, tvList);
                fabExpanded = true;
            }
        });
        fabSave.setOnClickListener(task -> popUp());

        fabDelete.setOnClickListener(task -> deletePopUp());

        fabEdit.setOnClickListener(task -> editPopUp());
    }

    /**
     * Canceling new spending for this balance
     *
     * @param view View
     */
    public void cancelSpendingBtn(View view) {
        popupWindow.dismiss();
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
        popupWindow.showAtLocation(this.findViewById(R.id.balanceSpendingReports_rv), Gravity.CENTER, 0, 0);

        return popupView;
    }
    private void deletePopUp() {
        // Create popUp
        View popupView = createPopUp(R.layout.activity_deletion);

        popupView.findViewById(R.id.confirmDeletion_btn).setOnClickListener(task -> deleteBalance());
        popupView.findViewById(R.id.cancelDeletion_btn).setOnClickListener(task -> popupWindow.dismiss());
    }

    private void deleteBalance(){
        db.collection(email)
                .document(BALANCE_LIST.getDescription())
                .collection(BALANCE_LIST.getDescription())
                .document(balance.getBalanceName()).delete()
                .addOnCompleteListener(task -> {
           if(task.isSuccessful()) {
               Toast.makeText(this, balance.getBalance() + " was deleted", Toast.LENGTH_SHORT).show();
           } else {
               Toast.makeText(this, "Error: Balance was not deleted", Toast.LENGTH_SHORT).show();
           }
        });
        popupWindow.dismiss();
        startActivity(new Intent(BalanceViewProcessor.this, BalanceProcessor.class));
    }

    private void editPopUp() {
        View popupView = createPopUp(R.layout.activity_create_balance);

        createBalanceName = popupView.findViewById(R.id.createBalanceName_tv);
        createBalanceAmount = popupView.findViewById(R.id.createBalanceAmount_tv);
        editBalanceBtn = popupView.findViewById(R.id.createBalanceSave_btn);
        cancelBalanceBtn = popupView.findViewById(R.id.createBalanceCancel_btn);

        createBalanceName.setText(balance.getBalanceName());
        createBalanceName.setEnabled(false);
        createBalanceAmount.setText(String.valueOf(balance.getBalance()));

        cancelBalanceBtn.setOnClickListener(task -> popupWindow.dismiss());

        editBalanceBtn.setOnClickListener(task -> editBalance());
    }

    private void editBalance() {
        Map<String, Object> dataToSave = new HashMap<>();
        balance.setBalance(Integer.parseInt(createBalanceAmount.getText().toString()));
        balance.setBalanceName(createBalanceName.getText().toString());

        dataToSave.put("balance", balance.getBalance());
        dataToSave.put("balanceName", balance.getBalanceName());
        db.collection(email)
                .document(BALANCE_LIST.getDescription())
                .collection(BALANCE_LIST.getDescription())
                .document(balance.getBalanceName())
                .set(dataToSave)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Balance was edited", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Balance failed to be saved", Toast.LENGTH_SHORT).show();
                    }
                });
        popupWindow.dismiss();
        refresh();
    }

    /**
     * Get all the spendings from firebase and show them in the view
     */
    private void getSpendings() {
        db.collection(docPath).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    db.collection(docPath).document(document.getId()).get().addOnSuccessListener(documentSnapshot -> {
                        spendings.add(spending = documentSnapshot.toObject(Spending.class));

                        spendingList.setAdapter(new SpendingAdapter(spendings, BalanceViewProcessor.this));
                    });
                }
            }
        });
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
     * Popup creation, initialization, declaration
     */
    private void popUp() {
        View popupView = createPopUp(R.layout.activity_create_spending);

        //TODO: pull out dropdown and items to be global, inflate it with real data from firebase (doable only after budget is available)
        //get the spinner from the xml.
        Spinner dropdown = popupView.findViewById(R.id.typeSpinner);

        //create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "three"};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);//TODO: replace items -> budgetNames
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        spendingName = popupView.findViewById(R.id.createSpendingName_tv);
        spendingAmount = popupView.findViewById(R.id.createSpendingAmount_tv);
        spendingDescription = popupView.findViewById(R.id.createSpendingDesc_tv);
    }

    /**
     * FAB initialization
     */
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
        CardView fabSaveCard = findViewById(R.id.saveCard);
        CardView fabDeleteCard = findViewById(R.id.deleteCard);

        fabCards.add(fabDeleteCard);
        fabCards.add(fabSaveCard);
        fabCards.add(fabEditCard);
    }

    private void updateBalance() {
        balance.setBalance(balance.getBalance() - spending.getAmount());
        Map<String, Object> update = new HashMap<>();
        update.put("balance", balance.getBalance());
        update.put("balanceName", balance.getBalanceName());

        db.collection(email)
                .document(BALANCE_LIST.getDescription())
                .collection(BALANCE_LIST.getDescription())
                .document(balance.getBalanceName())
                .set(update);
    }
}
