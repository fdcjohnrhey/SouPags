package com.example.soupags.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soupags.R;
import com.example.soupags.helper.FireBaseFoodCallback;
import com.example.soupags.helper.FireBaseHelper;
import com.example.soupags.helper.FireBaseMenuItemsCallback;
import com.example.soupags.model.Cart;
import com.example.soupags.model.CartAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String userId;
    private TextView foddName, foodPrice, totalPrice, removeItem;
    private ImageView foodImg;
    private RecyclerView orderRecycler;
    ArrayList<Cart> carts = new ArrayList<Cart>();
    private int totalOrder;
    private Button payButton;

    private String[] foodIdArr;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", "");


        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(Cart.class.getSimpleName());

        totalPrice = (TextView) findViewById(R.id.checkoutTotal);
        removeItem = (TextView) findViewById(R.id.removeItem);
        payButton = (Button) findViewById(R.id.checkoutPayButton);
        orderRecycler = (RecyclerView) findViewById(R.id.checkoutRecycler);

        totalOrder = 0;
        FireBaseHelper.getCartItems(new FireBaseFoodCallback() {
            @Override
            public void onCallback(ArrayList arrayList) {
                cartAdapter = new CartAdapter(carts);
                orderRecycler.setHasFixedSize(true);
                orderRecycler.setLayoutManager(new LinearLayoutManager(CheckoutActivity.this));
                orderRecycler.setAdapter(cartAdapter);
            }
        }, userId);

        FireBaseHelper.getCartItemTotal(new FireBaseMenuItemsCallback() {
            @Override
            public void onCallback(int count) {
                totalOrder = count;
                totalPrice.setText("₱"+ totalOrder);
            }
        }, userId);



        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbRefOrder = FirebaseDatabase.getInstance().getReference("Order");
                DatabaseReference dbRefCart = FirebaseDatabase.getInstance().getReference(Cart.class.getSimpleName());

                copyRecord(dbRefCart, dbRefOrder);

                Toast.makeText(CheckoutActivity.this,"Ordered Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    private void copyRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toPath.child(userId).push().setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            dataSnapshot.getRef().removeValue();
                        } else {
                            System.out.println("Failed");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        fromPath.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}