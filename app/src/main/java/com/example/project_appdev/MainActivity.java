package com.example.project_appdev;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project_appdev.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button registerButton;
    private ActivityMainBinding binding;
    private ActionBar actionBar;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private String email = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Login");
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Logging In....");
        progressDialog.setCanceledOnTouchOutside(false);
        
        firebaseAuth = FirebaseAuth.getInstance();
        CheckUser();


        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.MainMethod), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });


        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });



    }

    private void CheckUser() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            startActivity(new Intent(this, LandingPage.class));
            finish();
        }
    }

    private void validateData(){
        email = binding.emailLogin.getText().toString().trim();
        password = binding.passwordLogin.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            binding.emailLogin.setError("Email Field Cannot Be Empty");
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailLogin.setError("Invalid email Format");
        }  else if (TextUtils.isEmpty(password)) {
            binding.passwordLogin.setError("Password Field Cannot Be Empty");
        }else{
            firebaseLogin();
        }
    }

    private void firebaseLogin(){
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String email = firebaseUser.getEmail();
                Toast.makeText(MainActivity.this,"Welcome User\n"+email,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LandingPage.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Invalid Account Name or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }


}