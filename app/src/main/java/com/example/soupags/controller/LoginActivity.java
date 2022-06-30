package com.example.soupags.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soupags.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail,inputPassword;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private TextView txtloginerror,registerLink;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.btnlogin);
        txtloginerror = (TextView) findViewById(R.id.txtloginerror);
        registerLink = (TextView) findViewById(R.id.linkregister);

        mAuth = FirebaseAuth.getInstance();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String pass = inputPassword.getText().toString();

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    // Check if user is signed in (non-null) and update UI accordingly.
                                    FirebaseUser currentUser = mAuth.getCurrentUser();

                                    sharedPreferences =   view.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.remove("USER_ID");
                                    editor.putString("USER_ID", currentUser.getUid().toString());
                                    editor.commit();



                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }else{
                                    Toast.makeText(LoginActivity.this,"Error Login!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                );
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }
}