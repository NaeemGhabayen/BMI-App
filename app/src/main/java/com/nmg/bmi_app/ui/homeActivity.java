package com.nmg.bmi_app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nmg.bmi_app.R;
import com.nmg.bmi_app.adapter.RecordAdapter;
import com.nmg.bmi_app.model.Record;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class homeActivity extends AppCompatActivity {
    Button btn_addNewRecord, btn_viewFood, btn_addFood;
    TextView tv_logout, tv_status, tv_name;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String userId;
    RecyclerView rv_status;
    RecordAdapter adapter;
    List<Record> records;
    String gender = "test";
    double ageParcent = 0;
    double weight;
    double length;
    double bmi;
    String stutas;
    double weightLastIndex, weightTowLastIndex, lengthLastIndex, bmiChange, bmiLastIndex, bmiTowLastIndex;
    Calendar today;
    int age, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn_addNewRecord = findViewById(R.id.btn_addNewRecord);
        tv_logout = findViewById(R.id.tv_logout);
        rv_status = findViewById(R.id.rv_status);

        btn_viewFood = findViewById(R.id.btn_viewFood);
        btn_addFood = findViewById(R.id.btn_addFood);
        tv_status = findViewById(R.id.tv_status);
        tv_name = findViewById(R.id.tv_name);
        records = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = auth.getUid();
        today = Calendar.getInstance();

        showData();
        btn_viewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), listOfFoodActivity.class));
            }
        });
        btn_addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), addFoodActivity.class));
            }
        });
        btn_addNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), newRecordActivity.class));
            }
        });
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), loginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        btn_addNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), newRecordActivity.class));
            }
        });

        getDataFromFirebase();
    }


    private void showData() {
        fStore.collection("Record").whereEqualTo("userId", userId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Toast.makeText(getApplicationContext(), "rssssssss", Toast.LENGTH_SHORT).show();
                                String date = document.getData().get("date").toString();
                                String lenght = document.getData().get("lenght").toString();
                                String wieght = document.getData().get("wieght").toString();
                                // calc agge percent
                                Log.d("TAG", "onComplete: " + date);

                                //  dob.set(year, month, day);
                                year = Integer.parseInt(date.substring(6));
                                age = today.get(Calendar.YEAR) - year;
                                Log.d("TAG", "onCompletse: " + year);
                                Log.d("TAG", "onCompletse: " + today.get(Calendar.YEAR));
                                Log.d("TAG", "onCompletse: " + age);
                                weight = Double.parseDouble(wieght);
                                length = Double.parseDouble(lenght) / 100 * Double.parseDouble(lenght) / 100;

                                Log.d("age", "onCompletse: " + age);
                                if (age >= 20) {
                                    ageParcent = 1;
                                    bmi = (weight / length) * ageParcent;
                                } else if ((age >= 10 && age <= 19) && gender.equals("Male")) {
                                    ageParcent = 0.90;
                                    bmi = (weight / length) * ageParcent;
                                } else if ((age >= 10 && age <= 19) && gender.equals("Female")) {
                                    ageParcent = 0.80;
                                    bmi = (weight / length) * ageParcent;
                                } else if (age >= 2 && age <= 10) {
                                    ageParcent = 0.7;
                                    bmi = (weight / length) * ageParcent;
                                }
                                //calc bmi

                                Log.e("TAGApoi", "onComplete: ageParcent " + ageParcent);
                                Log.e("TAGApoi", "bmi:weight " + weight);
                                Log.e("TAGApoi", "bmi:length " + length);
                                Log.e("TAGApoi", "bmi:bmi " + bmi);

                                if (bmi > 30) {
                                    stutas = "Obesity";
                                } else if (bmi <= 30 && bmi > 25) {
                                    stutas = "Overweight";
                                } else if (bmi <= 25 && bmi > 18.5) {
                                    stutas = "Healthy Weight";

                                } else if (bmi <= 18.5) {
                                    stutas = "Underweight";
                                }
                                records.add(new Record(date, "time", lenght, wieght, userId, stutas, bmi));
                                Log.e("TAGApoi", "onComplete: " + records.size());
                            }
                            Log.e("TAGApoi", "onComplete: " + records.size());
                            Collections.reverse(records);

                            if (records.size() < 1) {
                                Toast.makeText(getApplicationContext(), "kos ", Toast.LENGTH_SHORT).show();
                            } else {
                                bmiLastIndex = records.get(0).getBmi();
                                bmiTowLastIndex = records.get(1).getBmi();

                                Log.e("bmiLastIndex", "onComplete: " + bmiLastIndex);
                                Log.e("bmiTowLastIndex", "onComplete: " + bmiTowLastIndex);

                                bmiChange = bmiLastIndex -bmiTowLastIndex;
                                Log.e("bmiChange", "onComplete: " + bmiChange);
                                String stuts = records.get(0).getStutes();

                                switch (records.get(0).getStutes()){
                                    case "Obesity":
                                        Toast.makeText(getApplicationContext(), "Obesity", Toast.LENGTH_SHORT).show();
                                        if ((bmiChange<-1)||(bmiChange>=-1 && bmiChange<-0.6)){
                                            tv_status.setText(stuts+" Go Ahead");
                                            Log.e("tv_status", "onComplete: " + "Go Ahead"+ bmiChange);
                                        }else if((bmiChange<-0.3  && bmiChange>=-0.6)||(bmiChange>=-0.3&&bmiChange>0)){
                                            tv_status.setText(stuts+" Little Changes");
                                            Log.e("tv_status", "onComplete: " + "Little Changes"+ bmiChange);
                                        }else if (bmiChange>=0 && bmiChange<0.3){
                                            tv_status.setText(stuts+" Be Careful");
                                            Log.e("tv_status", "onComplete: " + " Be Careful"+ bmiChange);
                                        }else if ((bmiChange>=0.3 && bmiChange<0.6)||(bmiChange>=0.6 && bmiChange<1)||bmiChange>=1){
                                            tv_status.setText(stuts+" So Bad");
                                            Log.e("tv_status", "onComplete: " + "So Bad"+ bmiChange);
                                        }
                                        break;

                                    case "Overweight":
                                        Toast.makeText(getApplicationContext(), "Overweight", Toast.LENGTH_SHORT).show();
                                        if ((bmiChange<-1)||(bmiChange>=0.3 && bmiChange<0.6)){
                                            tv_status.setText(stuts+" Be Careful");
                                            Log.e("tv_status", "onComplete: " + "Be Careful"+ bmiChange);
                                        }else if((bmiChange>=-1 && bmiChange<-0.6)){
                                            tv_status.setText(stuts+" Go Ahead");
                                            Log.e("tv_status", "onComplete: " + "Go Ahead"+ bmiChange);
                                        }else if ((bmiChange<-0.3  && bmiChange>=-0.6)){
                                        tv_status.setText(stuts+" still good");
                                        Log.e("tv_status", "onComplete: " + "Go still"+ bmiChange);
                                        }else if ((bmiChange>=-0.3&&bmiChange<0)||(bmiChange>=0 && bmiChange<0.3)){
                                            tv_status.setText(stuts+" Little Changes");
                                            Log.e("tv_status", "onComplete: " + " Be Little Changes "+ bmiChange);
                                        }else if ((bmiChange>=0.6 && bmiChange<1)||bmiChange>=1){
                                            tv_status.setText(stuts+" So Bad");
                                            Log.e("tv_status", "onComplete: " + "So Bad"+ bmiChange);
                                        }
                                        break;

                                    case "Healthy Weight":
                                        Toast.makeText(getApplicationContext(), "Healthy Weight", Toast.LENGTH_SHORT).show();
                                        if ((bmiChange<-1)){
                                            tv_status.setText(stuts+" So Bad");
                                            Log.e("tv_status", "onComplete: " + "So Bad"+ bmiChange);
                                        }else if((bmiChange>=-1 && bmiChange<-0.6)||(bmiChange<-0.3  && bmiChange>=-0.6)||
                                                (bmiChange>=0.3 && bmiChange<0.6)||(bmiChange>=0.6 && bmiChange<1)||bmiChange>=1){
                                            tv_status.setText(stuts+" Be Careful");
                                            Log.e("tv_status", "onComplete: " + "Go Careful"+ bmiChange);
                                        }else if ((bmiChange>=-0.3&&bmiChange<0)||(bmiChange>=0 && bmiChange<0.3)){
                                            tv_status.setText(stuts+" Little Changes");
                                            Log.e("tv_status", "onComplete: " + " Be Little Changes "+ bmiChange);
                                        }
                                        break;


                                    case "Underweight":
                                        Toast.makeText(getApplicationContext(), "Underweight", Toast.LENGTH_SHORT).show();
                                        if ((bmiChange<-1)||(bmiChange>=-1 && bmiChange<-0.6)||
                                                (bmiChange<-0.3  && bmiChange>=-0.6)) {
                                            tv_status.setText(stuts+" So Bad");
                                            Log.e("tv_status", "onComplete: " + "So Bad");
                                        }else if ((bmiChange>=-0.3&&bmiChange<0)||(bmiChange>=0 && bmiChange<0.3)){
                                            tv_status.setText(stuts+" Little Changes");
                                            Log.e("tv_status", "onComplete: " + " Be Little Changes ");
                                        }else if ((bmiChange>=0.3 && bmiChange<0.6)){
                                            tv_status.setText(stuts+" Still Good");
                                            Log.e("tv_status", "onComplete: " + " Still Good");
                                        }else if((bmiChange>=0.6 && bmiChange<1)||bmiChange>=1){
                                            tv_status.setText(stuts+" Go Ahead");
                                            Log.e("tv_status", "onComplete: " + "Go Ahead");
                                        }
                                        break;
                                }



                            }
                            adapter = new RecordAdapter(getApplicationContext(), records);
                            rv_status.setHasFixedSize(true);
                            rv_status.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            rv_status.setAdapter(adapter);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void getDataFromFirebase() {
        fStore.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String name = task.getResult().get("userName").toString();
                            gender = task.getResult().get("gender").toString();

                            tv_name.setText(name);
                        }
                    }
                });
    }
}