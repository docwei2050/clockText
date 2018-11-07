package com.example.git.clocksample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ClockTextView tv1 = findViewById(R.id.tv1);
        final ClockTextView tv2 = findViewById(R.id.tv2);
        ClockTextView tv3 = findViewById(R.id.tv3);
        ClockTextView tv4 = findViewById(R.id.tv4);
        tv1.setText("10:00");
        tv2.setText("3", "分钟");
        tv3.setText("30", "分钟");
        tv4.setText("300", "分钟");

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText(String.format(Locale.CHINA, "%1$02d:00", count++));
                if (count % 2 == 0) {
                    tv1.setText("100:00");
                }
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count % 3 == 0) {
                    tv2.setText("300", "分钟");
                } else if (count % 3 == 1) {
                    tv2.setText("30", "分钟");
                } else {
                    tv2.setText("3", "分钟");
                }
            }
        });
    }
}
