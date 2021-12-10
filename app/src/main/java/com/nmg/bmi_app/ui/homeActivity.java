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
        showData();
        btn_viewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),listOfFoodActivity.class));
            }
        });
        btn_addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),addFoodActivity.class));
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
                                records.add(new Record(date, "time", lenght, wieght, userId));
                                Log.e("TAGApoi", "onComplete: " + records.size());
                            }
                            Log.e("TAGApoi", "onComplete: " + records.size());
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
                            tv_name.setText(name);
                        }
                    }
                });
    }
}