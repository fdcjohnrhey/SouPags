package com.example.soupags.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soupags.R;

import java.util.ArrayList;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {
    private ArrayList<Order> orders;

    public OrderItemsAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View orderList = layoutInflater.inflate(R.layout.rowlistorderitems, parent, false);
        OrderItemsAdapter.ViewHolder viewHolder = new OrderItemsAdapter.ViewHolder(orderList);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemsAdapter.ViewHolder holder, int position) {
        holder.foodName.setText(orders.get(position).getOrderName());
        holder.foodPrice.setText(orders.get(position).getOrderPrice());
        holder.foodImage.setImageResource(orders.get(position).getOrderImage());

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName;
        private TextView foodPrice;
        private ImageView foodImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.foodName = itemView.findViewById(R.id.orderName);
            this.foodPrice = itemView.findViewById(R.id.orderPrice);
            this.foodImage = itemView.findViewById(R.id.orderImage);
        }
    }
}
