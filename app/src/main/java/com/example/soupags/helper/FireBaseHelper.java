package com.example.soupags.helper;

import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.soupags.model.Cart;
import com.example.soupags.model.Food;
import com.example.soupags.model.Order;
import com.example.soupags.model.TopPicks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FireBaseHelper {
    private static int totalOrder;

    public static void getFoodList(FireBaseFoodItemsCallback firebaseFoodItemsCallback){
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
                        firebaseFoodItemsCallback.onCallback(foods);
                    }
                    Log.d("soup: 2 foods",""+foods);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getTopPicks(FireBaseFoodItemsCallback firebaseFoodItemsCallback){
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

                        firebaseFoodItemsCallback.onCallback(topPicks);
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

    public static void getCartItems(FireBaseFoodItemsCallback fireBaseFoodItemsCallback, String userId){
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
                    fireBaseFoodItemsCallback.onCallback(carts);
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

    public static void addItem(FireBaseAddFoodCallback fireBaseAddFoodCallback, String userId, String foodName, String foodPrice, int imgId){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference(Cart.class.getSimpleName());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cartId = dbRef.child(userId).push().getKey();
                dbRef.child(userId).child(cartId).setValue(new Cart(
                        foodName,
                        foodPrice,
                        imgId,
                        cartId
                ));

                fireBaseAddFoodCallback.onCallback("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                fireBaseAddFoodCallback.onCallback(""+ error);
            }
        });
    }

    public static void getFoodItemInfo(FireBaseFoodInfoCallback fireBaseFoodInfoCallback, int foodId, String name){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference(name);

        JSONObject foodInfo = new JSONObject();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        if (Integer.parseInt(d.getKey().toString()) == foodId) {
                            String imgId = d.child("foodImgId").getValue().toString();
                            String foodDesc = d.child("foodDesc").getValue().toString();
                            String foodPrice = d.child("foodPrice").getValue().toString();
                            String foodName = d.child("foodName").getValue().toString();

                            try {
                                foodInfo.put("imgId", imgId);
                                foodInfo.put("foodDesc", foodDesc);
                                foodInfo.put("foodPrice", foodPrice);
                                foodInfo.put("foodName", foodName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            fireBaseFoodInfoCallback.onCallback(foodInfo);

                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getOrderNumber(FireBaseOrderCallBack fireBaseOrderCallBack, String userId){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference(Order.class.getSimpleName());

        ArrayList<Order> orders = new ArrayList<Order>();

        dbRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotOrder) {
                if(snapshotOrder.exists()){
                    for(DataSnapshot orderSnap : snapshotOrder.getChildren()){
                        orders.add(new Order(
                                orderSnap.getKey().toString()
                        ));
                    }
                    fireBaseOrderCallBack.onCallback(orders);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getOrderItems(FireBaseOrderCallBack fireBaseOrderCallBack, String userId, String orderNumber){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference(Order.class.getSimpleName());

        ArrayList<Order> orders = new ArrayList<Order>();

        dbRef.child(userId).child(orderNumber).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotOrder) {
                if(snapshotOrder.exists()){
                    for(DataSnapshot orderSnap : snapshotOrder.getChildren()){
                        orders.add(new Order(
                            orderSnap.child("orderName").getValue().toString(),
                            orderSnap.child("orderPrice").getValue().toString(),
                            Integer.parseInt(orderSnap.child("orderImage").getValue().toString())
                        ));
                    }
                    fireBaseOrderCallBack.onCallback(orders);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void deleteCartItem(FireBaseDeleteItemCallback fireBaseDeleteItemCallback, String userId, String itemPosition){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference(Cart.class.getSimpleName());
        dbRef.child(userId).child(itemPosition).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    fireBaseDeleteItemCallback.onCallback(true);
                }else{
                    fireBaseDeleteItemCallback.onCallback(false);
                }
            }
        });
    }
}
