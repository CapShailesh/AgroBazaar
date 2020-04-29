package com.example.sdlproject;


import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private ArrayList<OrderItem> mOrderList;
//    private  OnItemClickListener mListener;

    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public TextView dateOfOrder, catOfOrder, nameOfProduct, qtyOfOrder, priceSet;
        CardView cardView;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            dateOfOrder = itemView.findViewById(R.id.dateOfOrder);
            nameOfProduct = itemView.findViewById(R.id.title_name);
            priceSet = itemView.findViewById(R.id.priceOfCrop);
            qtyOfOrder = itemView.findViewById(R.id.qtyOfOrder);
            catOfOrder = itemView.findViewById(R.id.categoryOfOrder);
            cardView = itemView.findViewById(R.id.cardview_order);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            {
                menu.add(this.getAdapterPosition(), 1, 0, "Delete");
            }
        }
    }

    public OrderAdapter(ArrayList<OrderItem> orderList) {
        mOrderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_order, parent, false);
        OrderViewHolder ovh = new OrderViewHolder(v);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem currentItem = mOrderList.get(position);
        holder.dateOfOrder.setText(currentItem.getDate());
        holder.nameOfProduct.setText(currentItem.getName());
        holder.catOfOrder.setText(currentItem.getCategory());
        holder.qtyOfOrder.setText(currentItem.getPrice());
        holder.priceSet.setText(currentItem.getQuantity());
        holder.itemView.setLongClickable(true);

    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

//    public interface OnItemClickListener{
//
//        void onItemClick(int position);
//        void onDeleteClick(int position);
//
//    }
//
//    public void setOnClickListener(OnItemClickListener listener){
//
//        mListener = listener;
//
//    }

}

