package com.example.financialManagement.viewHolders;

import android.view.View;
import android.widget.TextView;

import com.example.financialManagement.interfaces.OnListItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import financialManagement.R;

/**
 * ViewHolder class for all the recycler views
 */
public class SpendingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView name;
    public TextView amount;
    public TextView date;
    final private OnListItemClickListener onListItemClickListener;

    /**
     * Constructor.
     *
     * @param itemView View
     * @param onListItemClickListener OnListItemClickListener
     */
    public SpendingViewHolder(@NonNull View itemView, OnListItemClickListener onListItemClickListener) {
        super(itemView);
        name = itemView.findViewById(R.id.spendingListName_tv);
        amount = itemView.findViewById(R.id.spendingListAmount_tv);
        date = itemView.findViewById(R.id.spendingListDate_tv);
        this.onListItemClickListener = onListItemClickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onListItemClickListener.onListItemClick(getAdapterPosition());
    }
}
