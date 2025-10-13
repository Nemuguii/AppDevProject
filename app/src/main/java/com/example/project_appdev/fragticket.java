package com.example.project_appdev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class fragticket extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ticket, container, false);

        // 🔹 Route distances in km
        Map<String, Integer> routeDistances = new HashMap<>();
        routeDistances.put("Daet - Labo", 35);
        routeDistances.put("Daet - Talisay", 13);
        routeDistances.put("Daet - Vinzons", 18);
        routeDistances.put("Daet - Mercedes", 12);
        routeDistances.put("Daet - Paracale", 60);
        routeDistances.put("Daet - Jose Panganiban", 70);
        routeDistances.put("Daet - Basud", 25);
        routeDistances.put("Daet - Capalonga", 90);
        routeDistances.put("Daet - Sta. Elena", 100);

        // 🔹 Duration multipliers
        Map<String, Double> durationMultipliers = new HashMap<>();
        durationMultipliers.put("1 Week", 3.0);
        durationMultipliers.put("2 Weeks", 4.8);
        durationMultipliers.put("1 Month", 5.5);
        durationMultipliers.put("3 Months", 6.5);
        durationMultipliers.put("6 Months", 12.0);
        durationMultipliers.put("9 Months", 17.0);
        durationMultipliers.put("12 Months", 22.0);

        // 🔹 Duration in days
        Map<String, Integer> durationDays = new HashMap<>();
        durationDays.put("1 Week", 7);
        durationDays.put("2 Weeks", 14);
        durationDays.put("1 Month", 30);
        durationDays.put("3 Months", 90);
        durationDays.put("6 Months", 180);
        durationDays.put("9 Months", 270);
        durationDays.put("12 Months", 365);

        // 🔹 View Pass logic
        LinearLayout viewbutton = view.findViewById(R.id.viewpass);

        viewbutton.setOnClickListener(v -> {
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("passes");

            dbRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot passSnap : snapshot.getChildren()) {
                        TransportPass pass = passSnap.getValue(TransportPass.class);

                        if (pass != null) {
                            View passCard = getLayoutInflater().inflate(R.layout.pas_card, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setView(passCard);
                            AlertDialog dialog = builder.create();
                            dialog.show();

                            ((TextView) passCard.findViewById(R.id.fromDestination)).setText(pass.from);
                            ((TextView) passCard.findViewById(R.id.toDestination)).setText(pass.to);
                            ((TextView) passCard.findViewById(R.id.destinationvalue)).setText(pass.name);
                            ((TextView) passCard.findViewById(R.id.payment)).setText("₱" + String.format("%.2f", pass.payment));
                            ((TextView) passCard.findViewById(R.id.payment_method)).setText(pass.paymentMode);
                            ((TextView) passCard.findViewById(R.id.startdate)).setText(pass.startDate);
                            ((TextView) passCard.findViewById(R.id.expiredate)).setText(pass.expiryDate);

                            //Button closeBtn = passCard.findViewById(R.id.closeDialog);
                          //  closeBtn.setOnClickListener(v -> dialog.dismiss());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireContext(), "Error loading pass", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // 🔹 New Pass logic
        LinearLayout newpasss = view.findViewById(R.id.newpass);
        newpasss.setOnClickListener(v -> {
            View dialogview = getLayoutInflater().inflate(R.layout.dialog_book_ticket, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(dialogview);
            AlertDialog dialog = builder.create();
            dialog.show();

            Spinner routeSpinner = dialogview.findViewById(R.id.routeSpinner);
            Spinner durationSpinner = dialogview.findViewById(R.id.durationSpinner);
            Spinner paymentSpinner = dialogview.findViewById(R.id.paymentSpinner);

            Button proceedbtn = dialogview.findViewById(R.id.proceedPayment);
            proceedbtn.setOnClickListener(proceedView -> {
                if (routeSpinner.getSelectedItem() == null ||
                        durationSpinner.getSelectedItem() == null ||
                        paymentSpinner.getSelectedItem() == null) {
                    Toast.makeText(requireContext(), "Please complete all selections", Toast.LENGTH_SHORT).show();
                    return;
                }

                String selectedRoute = routeSpinner.getSelectedItem().toString();
                String selectedDuration = durationSpinner.getSelectedItem().toString();
                String selectedPayment = paymentSpinner.getSelectedItem().toString();

                int baseDistance = routeDistances.getOrDefault(selectedRoute, 0);
                double multiplier = durationMultipliers.getOrDefault(selectedDuration, 1.0);
                double farePerKm = 2.5;
                double totalFare = baseDistance * farePerKm * multiplier;

                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
                confirmBuilder.setTitle("Confirm Purchase");
                confirmBuilder.setMessage("Route: " + selectedRoute +
                        "\nDuration: " + selectedDuration +
                        "\nPayment: " + selectedPayment +
                        "\nTotal: ₱" + String.format("%.2f", totalFare));
                confirmBuilder.setPositiveButton("Confirm", (confirmDialog, which) -> {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("passes");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String startDate = sdf.format(Calendar.getInstance().getTime());

                    Calendar expirycal = Calendar.getInstance();
                    int durationInDays = durationDays.getOrDefault(selectedDuration, 30);
                    expirycal.add(Calendar.DAY_OF_MONTH, durationInDays);
                    String expiryDate = sdf.format(expirycal.getTime());

                    String[] routeParts = selectedRoute.split(" - ");
                    String from = routeParts.length > 0 ? routeParts[0].trim() : "Unknown";
                    String to = routeParts.length > 1 ? routeParts[1].trim() : "Unknown";

                    TransportPass pass = new TransportPass(
                            from,
                            to,
                            "TEST", // Replace with actual user name if needed
                            totalFare,
                            selectedPayment,
                            startDate,
                            expiryDate
                    );

                    databaseReference.push().setValue(pass)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(requireContext(), "Pass Saved Successfully", Toast.LENGTH_LONG).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(requireContext(), "Failed to Save Pass", Toast.LENGTH_LONG).show();
                            });
                });
                confirmBuilder.setNegativeButton("Cancel", null);
                confirmBuilder.show();
            });
        });

        return view;
    }

    // 🔹 TransportPass model
    public static class TransportPass {
        public String from;
        public String to;
        public String name;
        public double payment;
        public String paymentMode;
        public String startDate;
        public String expiryDate;

        public TransportPass() {} // Required for Firebase

        public TransportPass(String from, String to, String name, double payment,
                             String paymentMode, String startDate, String expiryDate) {
            this.from = from;
            this.to = to;
            this.name = name;
            this.payment = payment;
            this.paymentMode = paymentMode;
            this.startDate = startDate;
            this.expiryDate = expiryDate;
        }
    }
}