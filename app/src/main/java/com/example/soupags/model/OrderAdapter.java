package com.example.soupags.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soupags.R;
import com.example.soupags.helper.FireBaseHelper;
import com.example.soupags.helper.FireBaseOrderCallBack;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private ArrayList<Order> orders;
    private String userId;

    public OrderAdapter(ArrayList<Order> orders, String userId) {
        this.orders = orders;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View orderList = layoutInflater.inflate(R.layout.rowlistorders, parent, false);
        OrderAdapter.ViewHolder viewHolder = new OrderAdapter.ViewHolder(orderList);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.orderNumber.setText("Order #: "+orders.get(position).getOrderNumber());

        FireBaseHelper.getOrderItems(new FireBaseOrderCallBack() {
            @Override
            public void onCallback(ArrayList arrayList) {
                OrderItemsAdapter orderItemsAdapter = new OrderItemsAdapter(arrayList);
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
                holder.recyclerView.setHasFixedSize(true);
                holder.recyclerView.setAdapter(orderItemsAdapter);

            }
        }, userId, orders.get(position).getOrderNumber());


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderNumber;
        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.orderNumber = itemView.findViewById(R.id.orderNumber);
            this.recyclerView = itemView.findViewById(R.id.listOrders);
        }

    }
}
