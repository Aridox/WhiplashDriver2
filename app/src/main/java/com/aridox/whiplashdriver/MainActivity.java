package com.aridox.whiplashdriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Button bolbtn, rossprbtn, portprbtn, maintenancebtn, trainingbtn, feedbackbtn, optionsbtn;
    ImageView imageView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bolbtn = findViewById(R.id.bolbtn);
        rossprbtn = findViewById(R.id.rossprbtn);

        bolbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BolActivity.class);
                startActivity(i);
            }
        });
        rossprbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RossPayroll.class);
                startActivity(i);
            }
        });
    }
}