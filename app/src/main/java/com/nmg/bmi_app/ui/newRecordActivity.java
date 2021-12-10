package com.nmg.bmi_app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nmg.bmi_app.R;
import com.nmg.bmi_app.model.Record;

import java.util.HashMap;
import java.util.Map;

public class newRecordActivity extends AppCompatActivity {
    Button btn_maxLenght, btn_minLenght, btn_minWeight, btn_maxWeight, btn_saveData;
    EditText et_date, et_lenght, et_weight , et_time;
    int countLenght;
    int countWeight;
    Record record;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        btn_maxLenght = findViewById(R.id.btn_maxLenght);
        btn_minLenght = findViewById(R.id.btn_minLenght);
        btn_minWeight = findViewById(R.id.btn_minWeight);
        btn_maxWeight = findViewById(R.id.btn_maxWeight);
        btn_saveData = findViewById(R.id.btn_saveData);
        et_date = findViewById(R.id.et_date);
        et_lenght = findViewById(R.id.et_lenght);
        et_weight = findViewById(R.id.et_weight);
        et_time = findViewById(R.id.et_time);
        record = new Record();
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId  = auth.getUid();
        btn_maxLenght.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countLenght++;
                et_lenght.setText(countLenght+"");
            }
        });

        btn_maxWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countWeight++;
                et_weight.setText(countWeight+"");
            }
        });

        btn_minWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_weight.getText().toString().equals("0")){
                    Toast.makeText(getApplicationContext(), "enter valide weight", Toast.LENGTH_SHORT).show();
                }else{
                    countWeight--;
                    et_weight.setText(countWeight+"");
                }
            }
        });

        btn_minLenght.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_lenght.getText().toString().equals("0")){
                    Toast.makeText(getApplicationContext(), "enter valide Lenght", Toast.LENGTH_SHORT).show();
                }else{
                    countLenght--;
                    et_lenght.setText(countLenght+"");
                }
            }
        });

        btn_saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_weight.getText().toString())) {
                    et_weight.setError("weight is required");
                    return;
                }
                if (TextUtils.isEmpty(et_date.getText().toString())) {
                    et_date.setError("date is required");
                    return;
                }

                if (TextUtils.isEmpty(et_time.getText().toString())) {
                    et_time.setError("time is required");
                    return;
                }

                if (TextUtils.isEmpty(et_lenght.getText().toString())) {
                    et_lenght.setError("lenght is required");
                    return;
                }
                record.setUserId(userId);
                record.setDate(et_date.getText().toString());
                record.setLenght(et_lenght.getText().toString());
                record.setWieght(et_weight.getText().toString());
                record.setTime(et_time.getText().toString());
                fStore.collection("Record").add(record).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                            et_date.setText("");
                            et_time.setText("");
                            et_lenght.setText("");
                            et_weight.setText("");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}