package com.example.soupags.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soupags.R;
import com.example.soupags.helper.FireBaseAddFoodCallback;
import com.example.soupags.helper.FireBaseFoodInfoCallback;
import com.example.soupags.helper.FireBaseHelper;
import com.example.soupags.helper.FireBaseMenuItemsCallback;
import com.example.soupags.model.Cart;
import com.example.soupags.model.Food;
import com.example.soupags.model.TopPicks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class AddOrderActivity extends AppCompatActivity {
    private Button addOrder;
    private SharedPreferences sharedPreferences;
    private int foodId, imgId;
    private String userId, fromTopPick;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ImageView addOrderImg;
    private TextView foodName, foodDesc, foodPrice;
    private TextView textCartItemCount;
    private int mCartItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", "");

        addOrderImg = (ImageView) findViewById(R.id.addOrderImg);
        foodDesc = (TextView) findViewById(R.id.addOrderDesc);
        foodPrice = (TextView) findViewById(R.id.addOrderPrice);
        foodName = (TextView) findViewById(R.id.addOrderFoodName);

        Intent intent = getIntent();
        fromTopPick = intent.getStringExtra("from_top_pick");
        foodId = intent.getIntExtra("food_id", 0);

        if(fromTopPick == null) {
            FireBaseHelper.getFoodItemInfo(new FireBaseFoodInfoCallback() {
                @Override
                public void onCallback(JSONObject jsonObject) {
                    if(jsonObject.length() != 0){
                        try {
                            jsonObject.getString("imgId");

                            imgId = Integer.parseInt(jsonObject.getString("imgId"));
                            addOrderImg.setImageResource(imgId);
                            foodDesc.setText(jsonObject.getString("foodDesc"));
                            foodPrice.setText(jsonObject.getString("foodPrice"));
                            foodName.setText(jsonObject.getString("foodName"));


                            setTitle(jsonObject.getString("foodName"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(AddOrderActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }, foodId, Food.class.getSimpleName());
        }else{

            FireBaseHelper.getFoodItemInfo(new FireBaseFoodInfoCallback() {
                @Override
                public void onCallback(JSONObject jsonObject) {
                    if(jsonObject.length() != 0){
                        try {
                            jsonObject.getString("imgId");

                            imgId = Integer.parseInt(jsonObject.getString("imgId"));
                            addOrderImg.setImageResource(imgId);
                            foodDesc.setText(jsonObject.getString("foodDesc"));
                            foodPrice.setText(jsonObject.getString("foodPrice"));
                            foodName.setText(jsonObject.getString("foodName"));
                            setTitle(jsonObject.getString("foodName"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(AddOrderActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }, foodId, TopPicks.class.getSimpleName());

        }

        addOrder = findViewById(R.id.addOrderButton);
        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FireBaseHelper.addItem(new FireBaseAddFoodCallback() {
                    @Override
                    public void onCallback(String message) {
                        if(message == ""){
                            Intent intent = new Intent(AddOrderActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(AddOrderActivity.this, "Failed to add order. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, userId, foodName.getText().toString(), foodPrice.getText().toString(), imgId);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                Intent intent = new Intent(AddOrderActivity.this, CheckoutActivity.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
        }
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
        super.onBackPressed();
    }
}