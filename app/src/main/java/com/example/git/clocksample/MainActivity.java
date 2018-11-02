package com.example.git.clocksample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClockTextView tv1=findViewById(R.id.tv1);
        ClockTextView tv2=findViewById(R.id.tv2);
        ClockTextView tv3=findViewById(R.id.tv3);
        ClockTextView tv4=findViewById(R.id.tv4);
        tv1.setText("10:00");
        tv2.setText("3","分钟");
        tv3.setText("30","分钟");
        tv4.setText("300","分钟");
    }
}
