package com.saceone.picker;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.saceone.pickerlibrary.HorizontalRuler;
import com.saceone.pickerlibrary.HorizontalRulerDivisor;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private HorizontalRuler mPicker;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Elements to be added to the ruler
        ArrayList<Object> rvlist = new ArrayList<>();
        for (float i = 50; i <= 100; i += 0.5) {
            rvlist.add(i);
        }

        // Ruler settings
        mPicker = (HorizontalRuler) findViewById(R.id.horizontalRuler);
        mPicker.setElements(rvlist);
        mPicker.setLabelBackgroundColor(R.color.colorPrimaryDark);
        mPicker.setLabelTextColor(getResources().getColor(R.color.colorAccent));
        mPicker.setInitValue(60f); //must be called after setElements
        mPicker.setFadingEdge(100);
        mPicker.setDivisorColor(R.color.colorAccent);
        mPicker.setValueColor(getResources().getColor(R.color.colorAccent));
        mPicker.appendLabel(" kg");
        mPicker.addNewDivisor(new HorizontalRulerDivisor(100, 4));

        // Button that retrieves the centered value and toasts it
        mButton = (Button) findViewById(R.id.btnReveal);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, String.valueOf(mPicker.getCenteredValue()), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
