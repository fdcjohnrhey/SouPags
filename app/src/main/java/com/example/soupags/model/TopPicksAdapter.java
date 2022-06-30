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

public class TopPicksAdapter extends RecyclerView.Adapter<TopPicksAdapter.ViewHolder> {
    private ArrayList<TopPicks> topPicksList;
    private SharedPreferences sharedPreferences;

    public TopPicksAdapter(ArrayList<TopPicks> topPicksList) {
        this.topPicksList = topPicksList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View foodList =layoutInflater.inflate(R.layout.rowtoppicks, parent, false);
        ViewHolder viewHolder = new ViewHolder(foodList);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TopPicksAdapter.ViewHolder holder, int position) {
        holder.topPickName.setText(""+topPicksList.get(position).getFoodName());
        holder.topPickImg.setImageResource(topPicksList.get(position).getFoodImgId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences =   view.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("FOOD_ID");
                editor.putInt("FOOD_ID", holder.getAdapterPosition());
                editor.commit();

                Intent intent = new Intent(view.getContext(), AddOrderActivity.class);
                intent.putExtra("from_top_pick", "true");
                intent.putExtra("food_id", holder.getAdapterPosition());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topPicksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView topPickName;
        public ImageView topPickImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.topPickImg = itemView.findViewById(R.id.topPickImg);
            this.topPickName = itemView.findViewById(R.id.topPickFoodName);
        }
    }
}
