package com.example.financialManagement.processor;

import android.annotation.SuppressLint;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import financialManagement.R;

public class BalanceProcessor extends AppCompatActivity implements BalanceAdapter.OnListItemClickListener {

    private static final String BALANCE_LIST = "balanceList";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private RecyclerView balanceList;
    private RecyclerView.Adapter balanceAdapter;

    private ArrayList<Balance> balances;
    private Balance balance;

    private String docPath;

    private String email;

    //FAB Declarations
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
    //end FAB Declarations

    private PopupWindow popupWindow;



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

        fabProcessor.closeSubMenus(fabList, fabSettings, tvList);

        assert currentUser != null;
        email = currentUser.getEmail();

        assert email != null;
        docPath = db.collection(email).document(BALANCE_LIST).collection(BALANCE_LIST).getPath();

        balances = new ArrayList<>();

        getBalances();
    }

    private void setOnClickListeners() {
        fabSettings.setOnClickListener(view -> {
            if(fabExpanded) {
                fabProcessor.closeSubMenus(fabList, fabSettings, tvList);
                fabExpanded = false;
            }
            else {
                fabDelete.setImageResource(R.drawable.ic_create_black_24dp);
                fabDelete.setVisibility(View.VISIBLE);

                tvDelete.setText("Add");
                tvDelete.setVisibility(View.VISIBLE);

                fabSettings.setImageResource(R.drawable.ic_close_black_24dp);
                fabExpanded = true;
            }
        });

        fabDelete.setOnClickListener(view -> popUp());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void popUp() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_create_balance, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popupView, width, height, true);

        // show the popup window
        popupWindow.showAtLocation(this.findViewById(R.id.balances_layout), Gravity.CENTER, 0, 0);
    }

    //FAB initializations
    private void setUp() {
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
//

    }

    private void getBalances(){
        db.collection(docPath).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())) {
                    db.collection(docPath).document(document.getId()).get().addOnSuccessListener(documentSnapshot -> {
                        balances.add(balance = documentSnapshot.toObject(Balance.class));

                        balanceList.setAdapter(new BalanceAdapter(balances, BalanceProcessor.this));
                    });
                }
            }
        });
    }

    public void saveBtnClick(View view) {
        @SuppressLint("ResourceType") View viewById = findViewById(R.layout.activity_create_balance);

        EditText createBalanceName = viewById.findViewById(R.id.createBalanceName_tv);
        EditText createBalanceAmount = viewById.findViewById(R.id.createBalanceAmount_tv);

//        balance.setBalance(Integer.parseInt(createBalanceAmount.getText().toString()));
//        balance.setBalanceName(createBalanceName.getText().toString());
        Toast.makeText(this, createBalanceAmount.getEditableText(), Toast.LENGTH_SHORT).show();

    }

    public void cancelBtnClick(View view) {
        popupWindow.dismiss();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
//        startActivity(new Intent(BalanceProcessor.this, BalanceViewProcessor.class)
//                .putExtra("balance", balances.get(clickedItemIndex)));
    }
}
