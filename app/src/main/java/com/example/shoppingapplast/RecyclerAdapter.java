package com.example.shoppingapplast;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    ArrayList<Product> products;
    OnItemClicked mListener ;
    String TAG = "RecyclerAdapter" ;

    public interface OnItemClicked {
        void onItemClick(int position) ;
    }

    public RecyclerAdapter (ArrayList<Product> products, OnItemClicked listener){
        this.products = products;
        this.mListener =listener ;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mName;
        TextView mDescription;
        ImageView mImageView;
        OnItemClicked onItemClicked ;

        public MyViewHolder (View view, OnItemClicked onItemClicked){
            super(view);

            mName = view.findViewById(R.id.product_name);
            mImageView = view.findViewById(R.id.product_image);
            mDescription = view.findViewById(R.id.description);
            this.onItemClicked = onItemClicked ;
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            if(onItemClicked ==null) {
                return;
            }
            onItemClicked.onItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list_item_layout, parent,false);
        MyViewHolder mvh = new MyViewHolder(view, mListener);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = products.get(position);

        holder.mName.setText(product.getProduct_name());
        holder.mDescription.setText(product.getDescription());

        //Picasso.get().load(Uri.parse(((product)currentItem).getImage_url())).into(holder.ImageView);
        Picasso.get().load(product.getImage_URL()).into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }


}