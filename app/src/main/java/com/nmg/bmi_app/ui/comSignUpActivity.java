package com.nmg.bmi_app.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.nmg.bmi_app.R;
import com.nmg.bmi_app.service.DatePickerFragment;
import com.nmg.bmi_app.service.TimePickerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class comSignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Button btn_maxLenght, btn_minLenght, btn_minWeight, btn_maxWeight, btn_saveData;
    EditText  et_lenght, et_weight;
    RadioGroup radioGroup;
    Intent intent;
    String userId;
    TextView et_birthday;
    RadioButton rb_gender;
    FirebaseFirestore firestore;
    int countLenght=100, countWeight=40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_sign_up);
        btn_maxLenght = findViewById(R.id.btn_maxLenght);
        btn_minLenght = findViewById(R.id.btn_minLenght);
        btn_minWeight = findViewById(R.id.btn_minWeight);
        btn_maxWeight = findViewById(R.id.btn_maxWeight);
        btn_saveData = findViewById(R.id.btn_saveData);
        et_birthday = findViewById(R.id.et_birthday);
        et_lenght = findViewById(R.id.et_lenght);
        et_weight = findViewById(R.id.et_weight);
        radioGroup = findViewById(R.id.rb_gender);
        firestore = FirebaseFirestore.getInstance();
        intent = getIntent();
        userId = intent.getStringExtra("userId");

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

        et_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


        btn_minWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(et_weight.getText().toString().equals("40")){
                   Toast.makeText(getApplicationContext(), "enter valide weight", Toast.LENGTH_SHORT).show();
               }else{
                   countWeight--;
                   et_weight.setText(countWeight+"");
               }

            }
        });
        rb_gender =(RadioButton)radioGroup.findViewById(R.id.rb_male);

        btn_minLenght.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_lenght.getText().toString().equals("100")){
                    Toast.makeText(getApplicationContext(), "enter valide Lenght", Toast.LENGTH_SHORT).show();
                }else{
                    countLenght--;
                    et_lenght.setText(countLenght+"");
                }

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_gender =(RadioButton)radioGroup.findViewById(i);
                Toast.makeText(getApplicationContext(), ""+rb_gender.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        DocumentReference reference = firestore.collection("users").document(userId);

        btn_saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_weight.getText().toString())) {
                    et_weight.setError("weight is required");
                    return;
                }
                if (TextUtils.isEmpty(et_birthday.getText().toString())) {
                    et_birthday.setError("birthday is required");
                    return;
                }

                if (TextUtils.isEmpty(et_lenght.getText().toString())) {
                    et_lenght.setError("lenght is required");
                    return;
                }

                if (TextUtils.isEmpty(et_lenght.getText().toString())) {
                    et_lenght.setError("lenght is required");
                    return;
                }
                Map<String, Object> user1 = new HashMap<>();
                user1.put("gender", rb_gender.getText().toString());
                user1.put("lenght", et_lenght.getText().toString());
                user1.put("weight", et_weight.getText().toString());
                user1.put("birthday", et_birthday.getText().toString());

                reference.update(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "nice", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),loginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdf.format(c.getTime());
        et_birthday.setText(currentDate);
    }

}