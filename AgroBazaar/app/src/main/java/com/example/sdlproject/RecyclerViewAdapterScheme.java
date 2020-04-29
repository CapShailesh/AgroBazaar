package com.example.sdlproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterScheme extends RecyclerView.Adapter<RecyclerViewAdapterScheme.MyViewHolder> {

    private Context context;
    private List<Scheme> data;

    public RecyclerViewAdapterScheme(Context context, List<Scheme> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.cardview_scheme, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.scheme_name.setText(data.get(position).getSchemeName());

        holder.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(mContext, BookActivity.class);
//                intent.putExtra("BookTitle", mData.get(position).getSchemeName());
//                intent.putExtra("Description", mData.get(position).getLink());
//                mContext.startActivity(intent);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(position).getLink()));
                context.startActivity(browserIntent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView scheme_name;
        CardView cardView1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            scheme_name = (TextView) itemView.findViewById(R.id.scheme_name);
            cardView1 = (CardView) itemView.findViewById(R.id.parent_layout);

        }
    }

}
