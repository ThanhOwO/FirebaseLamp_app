package com.example.espvfb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.github.dhaval2404.colorpicker.ColorPickerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ColorPickerView mColorPicker;
    private Button mButton, tempBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColorPicker = findViewById(R.id.colorPicker);
        mButton = findViewById(R.id.button);
        tempBtn = findViewById(R.id.tempBtn);

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

                // Write the RGB values to Firebase
                mDatabase.child("red").setValue(red);
                mDatabase.child("green").setValue(green);
                mDatabase.child("blue").setValue(blue);
            }
        });

        tempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TempHumi.class);
                startActivity(intent);
            }
        });

    }
}