package com.example.soupags.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soupags.R;
import com.example.soupags.controller.AddOrderActivity;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>  {
    private ArrayList<Food> foodList;
    private SharedPreferences sharedPreferences;

    public FoodAdapter(ArrayList<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View foodList =layoutInflater.inflate(R.layout.rowlistmenu, parent, false);
        ViewHolder viewHolder = new ViewHolder(foodList);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
        holder.foodName.setText(foodList.get(position).getFoodName());
        holder.foodDesc.setText(foodList.get(position).getFoodDesc());
        holder.foodPrice.setText("₱"+ foodList.get(position).getFoodPrice());
        holder.foodImgId.setImageResource(foodList.get(position).getFoodImgId());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences =   view.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("FOOD_ID");
                editor.putInt("FOOD_ID", holder.getAdapterPosition());
                editor.commit();


                Intent intent = new Intent(view.getContext(), AddOrderActivity.class);
                intent.putExtra("food_id", holder.getAdapterPosition());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView foodName;
        public TextView foodDesc;
        public TextView foodPrice;
        public ImageView foodImgId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.foodName = itemView.findViewById(R.id.foodName);
            this.foodDesc = itemView.findViewById(R.id.foodDesc);
            this.foodPrice = itemView.findViewById(R.id.foodPrice);
            this.foodImgId = itemView.findViewById(R.id.foodImage);
        }
    }
}
