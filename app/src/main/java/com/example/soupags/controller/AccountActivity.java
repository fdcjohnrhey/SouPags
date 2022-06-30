package com.example.soupags.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.soupags.R;
import com.example.soupags.helper.FireBaseHelper;
import com.example.soupags.helper.FireBaseOrderCallBack;
import com.example.soupags.model.Cart;
import com.example.soupags.model.Order;
import com.example.soupags.model.OrderAdapter;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private OrderAdapter orderAdapter;
    private RecyclerView orderRecycler;
    private String userId;
    ArrayList<Order> orders = new ArrayList<Order>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setTitle("Orders");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", "");

        orderRecycler = findViewById(R.id.orderRecycler);


        FireBaseHelper.getOrderNumber(new FireBaseOrderCallBack() {
            @Override
            public void onCallback(ArrayList arrayList) {
                orders = arrayList;
                Log.d("snapshotOrder", ""+orders);
                orderAdapter = new OrderAdapter(orders, userId);
                orderRecycler.setHasFixedSize(true);
                orderRecycler.setLayoutManager(new LinearLayoutManager(AccountActivity.this));
                orderRecycler.setAdapter(orderAdapter);
            }
        }, userId);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}