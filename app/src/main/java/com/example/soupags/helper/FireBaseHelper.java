package com.example.soupags.helper;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.soupags.controller.CheckoutActivity;
import com.example.soupags.model.Cart;
import com.example.soupags.model.CartAdapter;
import com.example.soupags.model.Food;
import com.example.soupags.model.TopPicks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseHelper {
    private static int totalOrder;

    public static void getFoodList(FireBaseFoodCallback firebaseFoodCallback){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference(Food.class.getSimpleName());
        ArrayList<Food> foods = new ArrayList<Food>();


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot d: dataSnapshot.getChildren()){
                        Food food = d.getValue(Food.class);
                        foods.add(food);
                        firebaseFoodCallback.onCallback(foods);
                    }
                    Log.d("soup: 2 foods",""+foods);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getTopPicks(FireBaseFoodCallback firebaseFoodCallback){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference(TopPicks.class.getSimpleName());
        ArrayList<TopPicks> topPicks = new ArrayList<TopPicks>();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot d: dataSnapshot.getChildren()){
                        topPicks.add(new TopPicks(
                                d.child("foodName").getValue().toString(),
                                Integer.parseInt(d.child("foodImgId").getValue().toString())
                        ));

                        firebaseFoodCallback.onCallback(topPicks);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getMenuItems(FireBaseMenuItemsCallback fireBaseMenuItemsCallback, String userId){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference(Cart.class.getSimpleName());

        dbRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fireBaseMenuItemsCallback.onCallback((int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getCartItems(FireBaseFoodCallback fireBaseFoodCallback, String userId){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference(Cart.class.getSimpleName());
        ArrayList<Cart> carts = new ArrayList<Cart>();

        dbRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotOrder) {
                if(snapshotOrder.exists()){
                    for(DataSnapshot orderSnap : snapshotOrder.getChildren()){
                        carts.add(new Cart(
                                orderSnap.child("orderName").getValue().toString(),
                                orderSnap.child("orderPrice").getValue().toString(),
                                Integer.parseInt(orderSnap.child("orderImage").getValue().toString()),
                                (String) orderSnap.child("cartId").getValue()
                        ));
                    }
                    fireBaseFoodCallback.onCallback(carts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getCartItemTotal(FireBaseMenuItemsCallback fireBaseMenuItemsCallback, String userId){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference(Cart.class.getSimpleName());
        totalOrder = 0;

        dbRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotOrder) {
                if(snapshotOrder.exists()){
                    for(DataSnapshot orderSnap : snapshotOrder.getChildren()){
                        totalOrder = totalOrder +  Integer.parseInt(orderSnap.child("orderPrice").getValue().toString());
                    }
                    fireBaseMenuItemsCallback.onCallback(totalOrder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
