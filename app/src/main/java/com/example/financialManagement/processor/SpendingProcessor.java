package com.example.financialManagement.processor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financialManagement.adapter.SpendingAdapter;
import com.example.financialManagement.data.Balance;
import com.example.financialManagement.data.Spending;
import com.example.financialManagement.interfaces.OnListItemClickListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

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
import static com.example.financialManagement.enumerations.Enums.SUBSPENDING_LIST;

public class SpendingProcessor extends AppCompatActivity implements OnListItemClickListener {

    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private String subSpendingdocPath;

    private Spending spending;
    private final ArrayList<Spending> spendings = new ArrayList<>();
    private TextView spendingName;
    private TextView spendingAmount;
    private TextView spendingDescription;
    private TextView Date;

    private Balance balance;
    private Intent intent;

    private PopupWindow popupWindow;

    // Recycler view - for lists
    private RecyclerView spendingList;
    private RecyclerView.Adapter spendingAdapter;

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
    private ArrayAdapter<String> adapter;
    //end FAB Declarations

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        spendingList = findViewById(R.id.spendingHistory_rv);
        spendingList.hasFixedSize();
        spendingList.setLayoutManager(new LinearLayoutManager(this));

        spending = (Spending) getIntent().getSerializableExtra("spending");
        balance = (Balance) getIntent().getSerializableExtra("balance");

        setUp();

        setOnClickListeners();

        fabProcessor.closeSubMenus(fabList, fabSettings, fabCards, tvList);

        TextView spendingName = findViewById(R.id.spendingName_tv);
        TextView spendingAmount = findViewById(R.id.spendingAmount_tv);
        TextView spendingDesc = findViewById(R.id.spendingDesc_tv);
        TextView spendingDate = findViewById(R.id.spendingDate_tv);

        spendingName.setText(spending.getName());
        spendingAmount.setText(String.valueOf(spending.getAmount()));
        spendingDesc.setText(spending.getDescription());
        spendingDate.setText(String.valueOf(spending.getDate()));

        assert currentUser != null;
        subSpendingdocPath = db.collection(Objects.requireNonNull(currentUser.getEmail()))
                .document(BALANCE_LIST.getDescription())
                .collection(BALANCE_LIST.getDescription())
                .document(balance.getBalanceName())
                .collection(SPENDING_LIST.getDescription())
                .document(spending.getName())
                .collection(SUBSPENDING_LIST.getDescription())
                .getPath();
        System.out.println(subSpendingdocPath);

        getSpendings();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        startActivity(new Intent(SpendingProcessor.this, SingleSpendingProcessor.class)
                .putExtra("spending", spendings.get(clickedItemIndex))
                .putExtra("balance", balance));
    }


    /**
     * Get all the spendings from firebase and show them in the view
     */
    private void getSpendings() {
        db.collection(subSpendingdocPath).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    db.collection(subSpendingdocPath).document(document.getId()).get().addOnSuccessListener(documentSnapshot -> {
                        spendings.add(spending = documentSnapshot.toObject(Spending.class));

                        spendingList.setAdapter(new SpendingAdapter(spendings, SpendingProcessor.this));
                    });
                }
            }
        });
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
        popupWindow.showAtLocation(this.findViewById(R.id.spendingHistory_rv), Gravity.CENTER, 0, 0);

        return popupView;
    }

    private void deletePopUp() {
        // Create popUp
        View popupView = createPopUp(R.layout.activity_deletion);

        popupView.findViewById(R.id.confirmDeletion_btn).setOnClickListener(task -> deleteSpending());
        popupView.findViewById(R.id.cancelDeletion_btn).setOnClickListener(task -> popupWindow.dismiss());
    }

    //TODO: Future improvement -> somehow incorporate this so it will delete all the subCollections
    private Task<Void> deleteAll(String path) {
        Task<QuerySnapshot> querySnapshotTask = FirebaseFirestore.getInstance().collection(path).get();
        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        for (QueryDocumentSnapshot qs : Objects.requireNonNull(querySnapshotTask.getResult())) {
            batch.delete(qs.getReference());
        }
        return batch.commit();
    }


    /**
     * Popup creation, initialization, declaration
     */
    private void addSpendingPopUp() {
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
        spendingName.setText(spending.getName());
        spendingName.setFocusable(View.NOT_FOCUSABLE);
        spendingAmount = popupView.findViewById(R.id.createSpendingAmount_tv);
        spendingDescription = popupView.findViewById(R.id.createSpendingDesc_tv);

        popupView.findViewById(R.id.createSpendingSave_btn).setOnClickListener(task -> addNewSpending());
    }

    private void addNewSpending() {
        Map<String, Object> dataToSave = new HashMap<>();
        double original = -1;
        double userInput = BigDecimal.valueOf(Double.parseDouble(spendingAmount.getText().toString())).setScale(2, RoundingMode.HALF_UP).doubleValue();
        spending = new Spending();
        spending.setAmount(userInput);
        spending.setName(spendingName.getText().toString());
        spending.setDate(new Date());
        spending.setDateShort(String.valueOf(spending.getDate()));
        spending.setDescription(spendingDescription.getText().toString());

        for (Spending spend : spendings) {
            if (spend.getName().equals(spending.getName())) {
                original = userInput;
                spending.setAmount(BigDecimal.valueOf(spend.getAmount() + spending.getAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
        }

        dataToSave.put("name", spending.getName());
        dataToSave.put("amount", spending.getAmount());
        dataToSave.put("date", spending.getDate());
        dataToSave.put("description", spending.getDescription());
        dataToSave.put("dateShort", spending.getDateShort().substring(spending.getDateShort().lastIndexOf("20")).concat(" ").concat(spending.getDateShort().substring(3, 10).trim()));

        String docPath = db.collection(Objects.requireNonNull(currentUser.getEmail()))
                .document(BALANCE_LIST.getDescription())
                .collection(BALANCE_LIST.getDescription())
                .document(balance.getBalanceName())
                .collection(SPENDING_LIST.getDescription()).getPath();

        db.collection(docPath).document(spending.getName()).set(dataToSave).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "New spending added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Spending failed to be created", Toast.LENGTH_SHORT).show();
            }
        });

        if (original > 0) {
            spending.setAmount(original);
            dataToSave.put("amount", spending.getAmount());
        }
        db.collection(docPath)
                .document(spending.getName())
                .collection(SUBSPENDING_LIST.getDescription())
                .document(spending.getDate() + " " + spending.getName()).set(dataToSave);

        updateBalance();
        popupWindow.dismiss();
        refresh();
    }

    private void updateBalance() {
        balance.setBalance(balance.getBalance() - spending.getAmount());
        Map<String, Object> update = new HashMap<>();
        update.put("balance", balance.getBalance());
        update.put("balanceName", balance.getBalanceName());

        db.collection(Objects.requireNonNull(currentUser.getEmail()))
                .document(BALANCE_LIST.getDescription())
                .collection(BALANCE_LIST.getDescription())
                .document(balance.getBalanceName())
                .set(update);
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

    private void deleteSpending() {
        db.collection(Objects.requireNonNull(currentUser.getEmail()))
                .document(BALANCE_LIST.getDescription())
                .collection(BALANCE_LIST.getDescription())
                .document(balance.getBalanceName())
                .collection(SPENDING_LIST.getDescription())
                .document(spending.getName()).delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Spending Deletion was successful.", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        startActivity(new Intent(SpendingProcessor.this, BalanceViewProcessor.class).putExtra("balance", balance));
                    } else {
                        Toast.makeText(this, "Error: Spending could not be deleted. Try again!", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                }
        );
    }

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

        fabDelete.setOnClickListener(view -> deletePopUp());

        fabSave.setOnClickListener(view -> addSpendingPopUp());
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
        CardView fabSaveCard = findViewById(R.id.saveCard);
        CardView fabDeleteCard = findViewById(R.id.deleteCard);

        fabCards.add(fabDeleteCard);
        fabCards.add(fabSaveCard);
        fabCards.add(fabEditCard);
    }
}
