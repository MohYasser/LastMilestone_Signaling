package com.example.shoppingapplast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DetailedAdapter extends RecyclerView.Adapter<DetailedAdapter.MyViewHolder> {

    ArrayList<Shop_enhanced> arraylist;

    OnShopItemClicked onShopItemClicked ;

    public interface OnShopItemClicked{
        void OnShopItemClick( int position) ;
    }

    public DetailedAdapter (ArrayList<Shop_enhanced> arraylist , OnShopItemClicked clickListener ){
        this.arraylist = arraylist;
        this.onShopItemClicked = clickListener ;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mName;
        TextView mSp_offers;
        TextView mPrice;
        TextView mDistance;
        OnShopItemClicked onShopItemClicked ;
        public MyViewHolder (View view, OnShopItemClicked onShopItemClicked){
            super(view);

            mName = view.findViewById(R.id.shop_name);
            mPrice = view.findViewById(R.id.price);
            mDistance = view.findViewById(R.id.distance);
            mSp_offers = view.findViewById(R.id.sp_offers);
            this.onShopItemClicked = onShopItemClicked ;
            itemView.setOnClickListener(this) ;
        }
        @Override
        public void onClick(View v) {

            onShopItemClicked.OnShopItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shops_list_item_layout, parent,false);
        MyViewHolder mvh = new MyViewHolder(view , onShopItemClicked) ;
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Shop_enhanced shopitem = arraylist.get(position);

        holder.mName.setText(shopitem.getShop_name());
        holder.mPrice.setText(String.valueOf(shopitem.getShop_price()));
        holder.mDistance.setText(String.valueOf(shopitem.getShop_distance()));
        holder.mSp_offers.setText(shopitem.getShop_sp_offers());

    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }
}