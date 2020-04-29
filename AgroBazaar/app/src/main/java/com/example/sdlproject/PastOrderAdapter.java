package com.example.sdlproject;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PastOrderAdapter extends RecyclerView.Adapter<PastOrderAdapter.PastOrderVieHolder> {
    private ArrayList<PastOrder> mOrderList;
//    private  OnItemClickListener mListener;

    public class PastOrderVieHolder extends RecyclerView.ViewHolder {

        public TextView dateSold, priceSold, qtySold, nameProductSold, refIdSold;
        CardView cardView;


        public PastOrderVieHolder(@NonNull View itemView) {
            super(itemView);

            dateSold  = itemView.findViewById(R.id.date_id_sold);
            priceSold = itemView.findViewById(R.id.price_sold);
            qtySold = itemView.findViewById(R.id.quantity_sold);
            nameProductSold = itemView.findViewById(R.id.product_name_sold);
            refIdSold = itemView.findViewById(R.id.order_ref_id);
            cardView = itemView.findViewById(R.id.cardview_solditem);
        }
    }

    public PastOrderAdapter(ArrayList<PastOrder> orderList) {
        mOrderList = orderList;
    }

    @NonNull
    @Override
    public PastOrderVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_solditem, parent, false);
        PastOrderVieHolder ovh = new PastOrderVieHolder(v);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull PastOrderVieHolder holder, int position) {
        PastOrder currentItem = mOrderList.get(position);
        holder.dateSold.setText(currentItem.getDate());
        holder.priceSold.setText(currentItem.getPrice());
        holder.nameProductSold.setText(currentItem.getName());
        holder.qtySold.setText(currentItem.getQty());
        holder.refIdSold.setText(currentItem.getApproval());
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

}

