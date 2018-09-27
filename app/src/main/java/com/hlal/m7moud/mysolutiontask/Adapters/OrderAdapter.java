package com.hlal.m7moud.mysolutiontask.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hlal.m7moud.mysolutiontask.Data.DataItem;
import com.hlal.m7moud.mysolutiontask.MainActivity;
import com.hlal.m7moud.mysolutiontask.R;
import com.hlal.m7moud.mysolutiontask.ShowOredersActivity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> implements View.OnClickListener  {

    List<DataItem> dataItems ;
    Context context ;
    DataItem DataEncap = new DataItem();
    Fragment fragment ;
    MainActivity mainActivity ;

    public OrderAdapter(List<DataItem> dataItems, Context context ,MainActivity mainActivity ) {
        this.dataItems = dataItems;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view , parent , false);

        ViewHolder viewHolder =  new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DataEncap = dataItems.get(position);
        holder.cardView.setTag(position);
        holder.OrderType.setText(DataEncap.getOrderType());
        holder.OrderState.setText(DataEncap.getOrderStatuse());
        holder.OrderDate.setText(DataEncap.getOrderDate());
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView ;

        TextView OrderType , OrderState , OrderDate ;


        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.Card);
            OrderType = view.findViewById(R.id.OrderType);
            OrderState = view.findViewById(R.id.OrderState);
            OrderDate = view.findViewById(R.id.Orderdate);
            cardView.setOnClickListener((View.OnClickListener) this);

        }

        @Override
        public void onClick(View v) {

            int position = (int) v.getTag();


            DataEncap = dataItems.get(position);

            Intent intent = new Intent(mainActivity , ShowOredersActivity.class);
            intent.putExtra("Type" , DataEncap.getOrderType());
            intent.putExtra("Date" , DataEncap.getOrderDate());
            intent.putExtra("State" , DataEncap.getOrderStatuse());
            intent.putExtra("Desc" , DataEncap.getOrderDesc());
         //   intent.putExtra("Uri" , DataEncap.getImageUrl());


        }
    }
}
