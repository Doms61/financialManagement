package com.example.financialManagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.financialManagement.data.Balance;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import financialManagement.R;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.ViewHolder> {

    private ArrayList<Balance> balanceList;
    final private OnListItemClickListener onListItemClickListener;

    public BalanceAdapter(ArrayList<Balance> balanceList, OnListItemClickListener onListItemClickListener){

        this.balanceList = balanceList;
        this.onListItemClickListener = onListItemClickListener;
    }

    @NonNull
    @Override
    public BalanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.balance_list_item, parent, false);
        return new BalanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BalanceAdapter.ViewHolder holder, int position) {
        holder.name.setText(balanceList.get(position).getBalanceName());
    }

    @Override
    public int getItemCount() {
        return balanceList.size();
    }

    //TODO: separate ViewHolder class to its own java class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.balanceListName_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }

    //TODO: separate OnListItemClickListener interface to its own java interface class
    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
