package com.example.financialManagement.processor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financialManagement.adapter.BalanceAdapter;
import com.example.financialManagement.data.Balance;
import com.example.financialManagement.interfaces.OnListItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
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

/**
 * Class that handles viewing all the balances and adding new ones.
 */
public class BalanceProcessor extends AppCompatActivity implements OnListItemClickListener {

    // Firebase relevant
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    // Recycler view - for lists
    private RecyclerView balanceList;
    private RecyclerView.Adapter balanceAdapter;

    // Balance relevant
    private ArrayList<Balance> balances;
    private Balance balance;

    // Path to the document containing all the balances on firebase
    private String docPath;

    //FAB Declarations
    private FabProcessor fabProcessor;

    private FloatingActionButton fabSettings;
    private FloatingActionButton fabDelete;
    private final List<FloatingActionButton> fabList = new ArrayList<>();

    private TextView tvDelete;
    private final List<TextView> tvList = new ArrayList<>();

    private CardView fabDeleteCard;
    private final List<CardView> fabCards = new ArrayList<>();

    private boolean fabExpanded = false;
    //end FAB Declarations

    // Popup window (new balance creation) relevant
    private PopupWindow popupWindow;
    private EditText createBalanceAmount;
    private EditText createBalanceName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balances);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        balanceList = findViewById(R.id.balances_rv);
        balanceList.hasFixedSize();
        balanceList.setLayoutManager(new LinearLayoutManager(this));

        setUp();

        setOnClickListeners();

        fabProcessor.closeSubMenus(fabList, fabSettings, fabCards, tvList);

        assert currentUser != null;
        String email = currentUser.getEmail();

        assert email != null;
        docPath = db.collection(email)
                .document(BALANCE_LIST.getDescription())
                .collection(BALANCE_LIST.getDescription()).getPath();

        balances = new ArrayList<>();

        getBalances();
        balanceAdapter = new BalanceAdapter(balances, BalanceProcessor.this);
    }

    /**
     * Save button in the add new balance popup.
     *
     * @param view View
     */
    public void saveBtnClick(View view) {
        Map<String, Object> dataToSave = new HashMap<>();
        balance.setBalance(Double.parseDouble(createBalanceAmount.getText().toString()));
        balance.setBalanceName(createBalanceName.getText().toString());

        dataToSave.put("balance", balance.getBalance());
        dataToSave.put("balanceName", balance.getBalanceName());
        db.collection(docPath).document(balance.getBalanceName()).set(dataToSave).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "New balance added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Balance failed to be created", Toast.LENGTH_SHORT).show();
            }
        });
        refresh();
    }

    /**
     * Cancel button in the add new balance popup.
     *
     * @param view View
     */
    public void cancelBtnClick(View view) {
        popupWindow.dismiss();
    }

    /**
     * On list item click, it should open the balance in a new intent.
     *
     * @param clickedItemIndex Index of the clicked item
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        startActivity(new Intent(BalanceProcessor.this, BalanceViewProcessor.class)
                .putExtra("balance", balances.get(clickedItemIndex)));
    }

    /**
     * On click listeners for FAB mainly
     */
    private void setOnClickListeners() {
        fabSettings.setOnClickListener(view -> {
            if (fabExpanded) {
                fabProcessor.closeSubMenus(fabList, fabSettings, fabCards, tvList);
                fabExpanded = false;
            } else {
                fabDelete.setImageResource(R.drawable.ic_create_black_24dp);
                fabDelete.setVisibility(View.VISIBLE);

                tvDelete.setText("Add");
                tvDelete.setVisibility(View.VISIBLE);

                fabDeleteCard.setVisibility(View.VISIBLE);

                fabSettings.setImageResource(R.drawable.ic_close_black_24dp);
                fabExpanded = true;
            }
        });
        fabDelete.setOnClickListener(view -> popUp());
    }

    /**
     * Popup creation, initialization, declaration
     */
    private void popUp() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.activity_create_balance, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popupView, width, height, true);

        // show the popup window
        popupWindow.showAtLocation(this.findViewById(R.id.balances_layout), Gravity.CENTER, 0, 0);

        createBalanceName = popupView.findViewById(R.id.createBalanceName_tv);
        createBalanceAmount = popupView.findViewById(R.id.createBalanceAmount_tv);
    }

    /**
     * FAB initialization
     */
    private void setUp() {
        fabSettings = findViewById(R.id.fab_settings);
        fabProcessor = new FabProcessor();

        FloatingActionButton fabEdit = findViewById(R.id.fab_edit);
        fabDelete = findViewById(R.id.fab_delete);
        FloatingActionButton fabSave = findViewById(R.id.fab_save);

        fabList.add(fabEdit);
        fabList.add(fabDelete);
        fabList.add(fabSave);

        TextView tvEdit = findViewById(R.id.fab_edit_tv);
        tvDelete = findViewById(R.id.fab_delete_tv);
        TextView tvSave = findViewById(R.id.fab_save_tv);

        tvList.add(tvEdit);
        tvList.add(tvDelete);
        tvList.add(tvSave);

        CardView fabEditCard = findViewById(R.id.editCard);
        CardView fabSaveCard = findViewById(R.id.saveCard);
        fabDeleteCard = findViewById(R.id.deleteCard);

        fabCards.add(fabDeleteCard);
        fabCards.add(fabSaveCard);
        fabCards.add(fabEditCard);
    }

    /**
     * Get all the balances from firebase and show them in the view
     */
    private void getBalances() {
        db.collection(docPath).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    db.collection(docPath).document(document.getId()).get().addOnSuccessListener(documentSnapshot -> {
                        balances.add(balance = documentSnapshot.toObject(Balance.class));

                        balanceList.setAdapter(new BalanceAdapter(balances, BalanceProcessor.this));
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
}
