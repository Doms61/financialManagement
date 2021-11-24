package com.example.financialManagement.processor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.financialManagement.data.Spending;
import com.example.financialManagement.interfaces.OnListItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import financialManagement.R;

public class SpendingProcessor extends AppCompatActivity implements OnListItemClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private Spending spending;
    private TextView spendingName;
    private TextView spendingAmount;
    private TextView spendingDesc;
    private TextView Date;

    private Intent intent;

    private PopupWindow popupWindow;

    // Recycler view - for lists
    private RecyclerView balanceList;
    private RecyclerView.Adapter balanceAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}
