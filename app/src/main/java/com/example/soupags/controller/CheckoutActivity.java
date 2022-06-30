package com.example.soupags.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soupags.R;
import com.example.soupags.helper.FireBaseFoodItemsCallback;
import com.example.soupags.helper.FireBaseHelper;
import com.example.soupags.helper.FireBaseMenuItemsCallback;
import com.example.soupags.model.Cart;
import com.example.soupags.model.CartAdapter;
import com.example.soupags.network.NetworkHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.Stripe;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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


    private Stripe stripe;
    PaymentSheet paymentSheet;
    private String customerId, ephemeralKey,clientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        setTitle("Checkout");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PaymentConfiguration.init(this, NetworkHelper.PUBLIC_API_KEY);
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);

        });


        sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", "");


        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(Cart.class.getSimpleName());

        totalPrice = (TextView) findViewById(R.id.checkoutTotal);
        removeItem = (TextView) findViewById(R.id.removeItem);
        payButton = (Button) findViewById(R.id.checkoutPayButton);
        orderRecycler = (RecyclerView) findViewById(R.id.checkoutRecycler);

        totalOrder = 0;
        FireBaseHelper.getCartItems(new FireBaseFoodItemsCallback() {
            @Override
            public void onCallback(ArrayList arrayList) {
                carts = arrayList;
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
                getCustomerID();
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

                            Toast.makeText(CheckoutActivity.this,"Ordered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                            startActivity(intent);
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



    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){


            DatabaseReference dbRefOrder = FirebaseDatabase.getInstance().getReference("Order");
            DatabaseReference dbRefCart = FirebaseDatabase.getInstance().getReference(Cart.class.getSimpleName());

            copyRecord(dbRefCart, dbRefOrder);
        }
    }

    private void getCustomerID(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            customerId = jsonObject.getString("id");
                            getEphemeralKey(customerId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer "+ NetworkHelper.SECRET_API_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CheckoutActivity.this);
        requestQueue.add(stringRequest);

    }

    private void getEphemeralKey(String customerId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ephemeralKey = jsonObject.getString("id");

                            getSecretClient(customerId, ephemeralKey);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer "+ NetworkHelper.SECRET_API_KEY);
                header.put("Stripe-Version", "2020-08-27");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CheckoutActivity.this.customerId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CheckoutActivity.this);
        requestQueue.add(stringRequest);

    }

    private void getSecretClient(String customerId, String ephemeralKey){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            clientSecret = jsonObject.getString("client_secret");

                            paymentFlow();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer "+ NetworkHelper.SECRET_API_KEY);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CheckoutActivity.this.customerId);
                params.put("amount", ""+totalOrder+"00");
                params.put("currency", "USD");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CheckoutActivity.this);
        requestQueue.add(stringRequest);

    }

    private void paymentFlow(){
        paymentSheet.presentWithPaymentIntent(
                clientSecret,
                new PaymentSheet.Configuration("SouPags",
                        new PaymentSheet.CustomerConfiguration(customerId, ephemeralKey))
        );
    }

}