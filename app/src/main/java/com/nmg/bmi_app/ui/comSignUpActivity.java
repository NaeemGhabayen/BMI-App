package com.nmg.bmi_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nmg.bmi_app.R;

public class comSignUpActivity extends AppCompatActivity {
Button btn_addFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_sign_up);
        btn_addFood = findViewById(R.id.btn_addFood);

        btn_addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),addFoodActivity.class));
            }
        });
    }
}