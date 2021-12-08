package com.example.financialManagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.financialManagement.data.Spending;
import com.example.financialManagement.interfaces.OnListItemClickListener;
import com.example.financialManagement.viewHolders.SpendingViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import financialManagement.R;

public class SpendingAdapter extends RecyclerView.Adapter<SpendingViewHolder> {

    private final ArrayList<Spending> spendingList;
    final private OnListItemClickListener onListItemClickListener;

    public SpendingAdapter(ArrayList<Spending> spendingList, OnListItemClickListener onListItemClickListener){

        this.spendingList = spendingList;
        this.onListItemClickListener = onListItemClickListener;
    }

    @NonNull
    @Override
    public SpendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.spending_list_item, parent, false);
        return new SpendingViewHolder(view, onListItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull SpendingViewHolder holder, int position) {
        holder.name.setText(spendingList.get(position).getName());
        holder.amount.setText(String.valueOf(spendingList.get(position).getAmount()));
        holder.date.setText(spendingList.get(position).getDateShort());
    }

    @Override
    public int getItemCount() {
        return spendingList.size();
    }
}
