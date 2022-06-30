package com.example.soupags.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soupags.R;
import com.example.soupags.helper.FireBaseDeleteItemCallback;
import com.example.soupags.helper.FireBaseHelper;
import com.example.soupags.helper.FireBaseMenuItemsCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<Cart> cartList;
    private SharedPreferences sharedPreferences;
    private String userId;

    public CartAdapter(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View orderList = layoutInflater.inflate(R.layout.rowlistcart, parent, false);
        ViewHolder viewHolder = new ViewHolder(orderList);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.orderName.setText(cartList.get(position).getOrderName());
        holder.orderPrice.setText("₱"+ cartList.get(position).getOrderPrice());
        holder.orderImage.setImageResource(cartList.get(position).getOrderImage());

        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences =   view.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                userId = sharedPreferences.getString("USER_ID", "");
                int itemPos = holder.getAdapterPosition();

//                FireBaseHelper.deleteCartItem(new FireBaseDeleteItemCallback() {
//                    @Override
//                    public void onCallback(boolean success) {
//                        cartList.remove(itemPos);
//                        notifyItemRemoved(itemPos);
//                        notifyDataSetChanged();
//
//
//                        FireBaseHelper.getCartItemTotal(new FireBaseMenuItemsCallback() {
//                            @Override
//                            public void onCallback(int count) {
//                                holder.total.setText(""+count);
//                            }
//                        }, userId);
//
//
//                    }
//                }, userId, Integer.toString(itemPos));


            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView orderName;
        public TextView orderPrice;
        public TextView removeItem;
        public ImageView orderImage;
        public TextView total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.orderImage = itemView.findViewById(R.id.orderImage);
            this.orderName = itemView.findViewById(R.id.orderName);
            this.orderPrice = itemView.findViewById(R.id.orderPrice);
            this.removeItem = itemView.findViewById(R.id.removeItem);
            this.total = itemView.findViewById(R.id.checkoutTotal);
        }
    }
}
