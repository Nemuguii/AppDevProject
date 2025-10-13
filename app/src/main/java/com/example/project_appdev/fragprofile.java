package com.example.project_appdev;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class fragprofile extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imagePicker;
    private FirebaseAuth firebaseAuth;
    private TextView emailText;

    private TextView nameInput;
    private TextView logOuts;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imagePicker = view.findViewById(R.id.imagePicker);
        TextView nameText = view.findViewById(R.id.nameInput);
        emailText = view.findViewById(R.id.EmailId);
        nameInput = view.findViewById(R.id.nameInput);
        logOuts = view.findViewById(R.id.logOUT);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Logging Out...");
        progressDialog.setCanceledOnTouchOutside(false);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String gmail = firebaseUser.getEmail();
        String username = gmail.substring(0,gmail.indexOf("@"));

        if (firebaseUser != null) {
            emailText.setText(firebaseUser.getEmail());
            nameInput.setText(username);
        }

        logOuts.setOnClickListener(v -> {
            progressDialog.show();
            firebaseAuth.signOut();
            progressDialog.dismiss();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });




        imagePicker.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == requireActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imagePicker.setImageURI(imageUri);
        }
    }
}