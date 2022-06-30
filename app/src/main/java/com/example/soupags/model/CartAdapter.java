package com.example.soupags.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soupags.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<Cart> cartList;
    private SharedPreferences sharedPreferences;

    public CartAdapter(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View orderList = layoutInflater.inflate(R.layout.rowlistorder, parent, false);
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
                String userId = sharedPreferences.getString("USER_ID", "");
                int itemPos = holder.getAdapterPosition();
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(Cart.class.getSimpleName());
                dbRef.child(userId).child(cartList.get(itemPos).getCartId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            cartList.remove(itemPos);
//                           notifyItemRemoved(itemPos);
//                            //notifyItemRangeChanged(itemPos,cartList.size()-1);
//                            notifyDataSetChanged();
//                            Log.e("cartKeyList", " "+cartList);
//                            Toast.makeText(view.getContext(), "Item removed!", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(view.getContext(), "Item not removed!", Toast.LENGTH_SHORT).show();
//                        }
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder { public TextView orderName;
        public TextView orderPrice;
        public TextView removeItem;
        public ImageView orderImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.orderImage = itemView.findViewById(R.id.orderImage);
            this.orderName = itemView.findViewById(R.id.orderName);
            this.orderPrice = itemView.findViewById(R.id.orderPrice);
            this.removeItem = itemView.findViewById(R.id.removeItem);
        }
    }
}
