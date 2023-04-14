package com.example.espvfb.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

public class HumidityFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_humidity, container, false);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DHT");

        final TextView mHumidityTextView = root.findViewById(R.id.humidityTextView);
        final ProgressBar mHumidityProgressBar = root.findViewById(R.id.humidityProgressBar);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("humidity").getValue(String.class);
                if (value != null) {
                    mHumidityTextView.setText(value + " %");

                    float floatValue = Float.parseFloat(value.trim());
                    int intValue = Math.round(floatValue);
                    mHumidityProgressBar.setProgress(intValue);
                } else {
                    // Handle null case
                    Log.d("TAG", "Humidity value is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }

        });
        return root;
    }
}