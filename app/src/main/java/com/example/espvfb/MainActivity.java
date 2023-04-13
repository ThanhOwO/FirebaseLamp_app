package com.example.espvfb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.github.dhaval2404.colorpicker.ColorPickerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ColorPickerView mColorPicker;
    private Button mButton, tempBtn;
    private Switch mSwitch;
    private TextView autonoti;
    private ImageView powerOff, powerOn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColorPicker = findViewById(R.id.colorPicker);
        mButton = findViewById(R.id.button);
        tempBtn = findViewById(R.id.tempBtn);
        mSwitch = findViewById(R.id.modeSwitch);
        autonoti = findViewById(R.id.AutoNoti);
        powerOff = findViewById(R.id.poweroff);
        powerOn = findViewById(R.id.poweron);

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected color from the ColorPicker
                int color = mColorPicker.getColor();

                // Convert the color to RGB values
                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                // Write the RGB values to Firebase under a child named "rgb"
                Map<String, Object> rgbValues = new HashMap<>();
                rgbValues.put("red", red);
                rgbValues.put("green", green);
                rgbValues.put("blue", blue);
                mDatabase.child("rgb").setValue(rgbValues);
            }
        });


        tempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TempHumi.class);
                startActivity(intent);
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Mode 1: Change color based on temperature
                    mDatabase.child("mode").setValue("auto");
                    mColorPicker.setVisibility(View.GONE);
                    mButton.setVisibility(View.GONE);
                    autonoti.setVisibility(View.VISIBLE);
                } else {
                    // Mode 2: Manual color change
                    mDatabase.child("mode").setValue("manual");
                    mColorPicker.setVisibility(View.VISIBLE);
                    mButton.setVisibility(View.VISIBLE);
                    autonoti.setVisibility(View.GONE);
                }
            }
        });

        // Add a listener for the temperature child
        mDatabase.child("temperature").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get the temperature value from Firebase
                float temperature = Float.parseFloat(snapshot.getValue(String.class));

                // If the mode is "auto", update the color of the ColorPicker
                if (mSwitch.isChecked()) {
                    getColorForTemperature(temperature);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        //Power management
        mDatabase.child("power").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean powerValue = snapshot.getValue(Boolean.class);
                if (powerValue) {
                    // Power is ON, show all layout and hide powerOn image view
                    mColorPicker.setVisibility(View.VISIBLE);
                    mButton.setVisibility(View.VISIBLE);
                    mSwitch.setVisibility(View.VISIBLE);
                    autonoti.setVisibility(View.GONE);
                    powerOn.setVisibility(View.GONE);
                    powerOff.setVisibility(View.VISIBLE);
                } else {
                    // Power is OFF, hide all layout and show powerOn image view
                    mColorPicker.setVisibility(View.GONE);
                    mButton.setVisibility(View.GONE);
                    mSwitch.setVisibility(View.GONE);
                    autonoti.setVisibility(View.GONE);
                    powerOn.setVisibility(View.VISIBLE);
                    powerOff.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        powerOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set power child value to true
                mDatabase.child("power").setValue(true);
            }
        });

        powerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set power child value to false
                mDatabase.child("power").setValue(false);
            }
        });

    }

    private int getColorForTemperature(float temperature) {
        int red, green, blue;
        if(temperature < 10.00){
            red = 0;
            green = 0;
            blue = 255;
        }
        else if(temperature < 20.00){
            red = 0;
            green = 255;
            blue = 255;
        }
        else if(temperature < 25.00){
            red = 2;
            green = 247;
            blue = 170;
        }
        else if(temperature < 30.00){
            red = 247;
            green = 235;
            blue = 2;
        }
        else if(temperature < 35.00){
            red = 247;
            green = 129;
            blue = 2;
        }else{
            red = 255;
            green = 0;
            blue = 0;
        }

        // Write the RGB values to Firebase if mode is "auto"
        if (mSwitch.isChecked()) {
            // Write the RGB values to Firebase under a child named "rgb"
            Map<String, Object> rgbValues = new HashMap<>();
            rgbValues.put("red", red);
            rgbValues.put("green", green);
            rgbValues.put("blue", blue);
            mDatabase.child("rgb").setValue(rgbValues);
        }

        // Return the color as an integer
        return Color.rgb(red, green, blue);
    }
}