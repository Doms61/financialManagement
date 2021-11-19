package com.example.financialManagement.viewHolders;

import android.view.View;
import android.widget.TextView;

import com.example.financialManagement.interfaces.OnListItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import financialManagement.R;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView name;
    final private OnListItemClickListener onListItemClickListener;

    public ViewHolder(@NonNull View itemView, OnListItemClickListener onListItemClickListener) {
        super(itemView);
        name = itemView.findViewById(R.id.balanceListName_tv);
        this.onListItemClickListener = onListItemClickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onListItemClickListener.onListItemClick(getAdapterPosition());
    }
}
