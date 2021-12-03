package com.nmg.bmi_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nmg.bmi_app.R;

public class homeActivity extends AppCompatActivity {
Button btn_addNewRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn_addNewRecord = findViewById(R.id.btn_addNewRecord);

        btn_addNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),newRecordActivity.class));
            }
        });
    }
}