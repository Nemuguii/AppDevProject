package com.example.project_appdev;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.project_appdev.databinding.ActivityLandingPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandingPage extends AppCompatActivity {

    ImageView Home,Ticket,Track,Profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Home = findViewById(R.id.Home);
        Ticket = findViewById(R.id.Ticket);
        Track = findViewById(R.id.Track);
        Profile = findViewById(R.id.Profile);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,new fraghome()).commit();

        Home.setOnClickListener(v ->{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fraghome()).commit();
        });
        Ticket.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragticket()).commit();
        });
        Track.setOnClickListener(v ->{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragtrack()).commit();
        });
        Profile.setOnClickListener(v->{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragprofile()).commit();
        });
    }




}