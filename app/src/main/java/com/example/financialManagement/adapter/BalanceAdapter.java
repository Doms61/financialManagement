package com.example.financialManagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.financialManagement.data.Balance;
import com.example.financialManagement.interfaces.OnListItemClickListener;
import com.example.financialManagement.viewHolders.BalanceViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import financialManagement.R;

/**
 * Adapter class for balances
 */
public class BalanceAdapter extends RecyclerView.Adapter<BalanceViewHolder> {

    private final ArrayList<Balance> balanceList;
    final private OnListItemClickListener onListItemClickListener;

    public BalanceAdapter(ArrayList<Balance> balanceList, OnListItemClickListener onListItemClickListener){

        this.balanceList = balanceList;
        this.onListItemClickListener = onListItemClickListener;
    }

    @NonNull
    @Override
    public BalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.balance_list_item, parent, false);
        return new BalanceViewHolder(view, onListItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BalanceViewHolder holder, int position) {
        holder.name.setText(balanceList.get(position).getBalanceName());
        holder.amount.setText(String.valueOf(balanceList.get(position).getBalance()));
    }

    @Override
    public int getItemCount() {
        return balanceList.size();
    }
}
