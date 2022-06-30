package com.example.soupags.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.soupags.R;
import com.example.soupags.helper.FireBaseHelper;
import com.example.soupags.helper.FireBaseFoodCallback;
import com.example.soupags.helper.FireBaseMenuItemsCallback;
import com.example.soupags.model.Cart;
import com.example.soupags.model.Food;
import com.example.soupags.model.FoodAdapter;
import com.example.soupags.model.TopPicks;
import com.example.soupags.model.TopPicksAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Food> foods;
    ArrayList<TopPicks> topPicks;
    private RecyclerView foodRecyclerView, topPicksRecyclerView;
    private TextView textCartItemCount;
    private int mCartItemCount;
    private SharedPreferences sharedPreferences;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", "");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference(Food.class.getSimpleName());

        FireBaseHelper.getFoodList(new FireBaseFoodCallback() {
            @Override
            public void onCallback(ArrayList arrayList) {
                foods = arrayList;
                foodRecyclerView = (RecyclerView) findViewById(R.id.menuRecycler);
                FoodAdapter foodAdapter= new FoodAdapter(foods);
                foodRecyclerView.setHasFixedSize(true);
                foodRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                foodRecyclerView.setAdapter(foodAdapter);
            }
        });


        FireBaseHelper.getTopPicks(new FireBaseFoodCallback() {
            @Override
            public void onCallback(ArrayList arrayList) {
                topPicks = arrayList;
                topPicksRecyclerView = (RecyclerView) findViewById(R.id.recyclerTopPicks);
                TopPicksAdapter topPicksAdapter = new TopPicksAdapter(topPicks);
                topPicksRecyclerView.setHasFixedSize(true);
                topPicksRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                topPicksRecyclerView.setAdapter(topPicksAdapter);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(MainActivity.this, CheckoutActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {
        FireBaseHelper.getMenuItems(new FireBaseMenuItemsCallback() {
            @Override
            public void onCallback(int count) {
                mCartItemCount = count;

                if (textCartItemCount != null) {
                    if (mCartItemCount == 0) {
                        if (textCartItemCount.getVisibility() != View.GONE) {
                            textCartItemCount.setVisibility(View.GONE);
                        }
                    } else {
                        textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                        if (textCartItemCount.getVisibility() != View.VISIBLE) {
                            textCartItemCount.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }
        }, userId);
    }

    @Override
    public void onBackPressed() {
        setupBadge();
    }
}