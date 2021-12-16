package com.nmg.bmi_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.nmg.bmi_app.R;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(5*1000);

                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),loginActivity.class);
                    startActivity(i);

                    //Remove activity
                    finish();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        };
        // start thread
        background.start();
    }
}