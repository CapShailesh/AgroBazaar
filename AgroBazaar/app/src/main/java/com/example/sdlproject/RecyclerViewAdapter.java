package com.example.sdlproject;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {


    private Context mContext;
    private List<Upload> mData;
    private List<Upload> mDataFull;

    public RecyclerViewAdapter(FragmentActivity mContext, List<Upload> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mDataFull = new ArrayList<>(mData);
    }

//    public RecyclerViewAdapter(MainActivity mainActivity, List<Upload> lstBook) {
//
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tv_Upload_title.setText(mData.get(position).getName());
        Picasso.with(mContext)
                .load(mData.get(position).getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.cropImageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CropActivity.class);
                intent.putExtra("UploadTitle", mData.get(position).getName());
                intent.putExtra("Category", mData.get(position).getmCategory());
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Upload> filteredList = new ArrayList<>();

            if(constraint==null || constraint.length() == 0){
                filteredList.addAll(mDataFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Upload item : mDataFull){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData.clear();
            mData.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_Upload_title;
        ImageView cropImageView;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_Upload_title = (TextView) itemView.findViewById(R.id.crop_name_id);
            cropImageView = (ImageView) itemView.findViewById(R.id.crop_img_id);
//            category = (TextView) itemView.findViewById(R.id.)
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }

}

