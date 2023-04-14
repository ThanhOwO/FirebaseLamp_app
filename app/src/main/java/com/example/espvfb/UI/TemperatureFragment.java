package com.example.espvfb.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.espvfb.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TemperatureFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_temperature, container, false);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DHT");


        final TextView mTemperatureTextView = root.findViewById(R.id.temperatureTextView);
        final ProgressBar mTemperatureProgressBar = root.findViewById(R.id.temperatureProgressBar);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("temperature").getValue(String.class);
                if (value != null) {
                    mTemperatureTextView.setText(value+" C°");

                    float floatValue = Float.parseFloat(value.trim());
                    int intValue = Math.round(floatValue);
                    mTemperatureProgressBar.setProgress(intValue);
                } else {
                    // Handle null case
                    Log.d("TAG", "Humidity value is null");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        // Inflate the layout for this fragment
        return root;
    }
}