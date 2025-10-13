package com.example.project_appdev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class fraghome extends Fragment {

    private FirebaseAuth firebaseAuth;
    private TextView name, email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout first
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        // Bind TextViews from the layout
        name = view.findViewById(R.id.profileName);
        email = view.findViewById(R.id.profileEmail);

        // Set user info if available
        if (firebaseUser != null) {
            String gmail = firebaseUser.getEmail();
            String username = gmail.substring(0, gmail.indexOf("@"));

            name.setText(username); // Display username
            email.setText(gmail);   // Display full email
        }

        return view;
    }
}