package com.example.project_appdev;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project_appdev.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {



    private ActivityRegisterBinding Bind;
    private FirebaseAuth firebaseAuth;
    private String email = "", password = "";

    private ActionBar actionBar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Bind = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(Bind.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Sign Up");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Creating Your Account...");
        progressDialog.setCanceledOnTouchOutside(false);

        View root = findViewById(R.id.RegisterMethod);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }




        Bind.registerBtnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        Bind.backBtnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,MainActivity.class));
                finish();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void validateData(){
        email = Bind.emailRegister.getText().toString().trim();
        password = Bind.passwordRegister.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Bind.emailRegister.setError("Email Field Cannot Be Empty");
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Bind.emailRegister.setError("Invalid email Format");
        } else if (TextUtils.isEmpty(password)) {
            Bind.passwordRegister.setError("Password Field Cannot Be Empty");
        } else if (password.length() < 6) {
            Bind.passwordRegister.setError("Password must atleast 6 characters long");
        }else{
            firebaseSignUp();
        }
    }

    private void firebaseSignUp() {
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String email = firebaseUser != null ? firebaseUser.getEmail() : "Unknown";
                        Toast.makeText(Register.this,"Account Created\n"+email,Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(Register.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this,"Failed to Create Account", Toast.LENGTH_SHORT).show();


                    }
                });

    }

}