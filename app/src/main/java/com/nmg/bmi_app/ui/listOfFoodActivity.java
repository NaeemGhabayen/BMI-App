package com.nmg.bmi_app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nmg.bmi_app.R;
import com.nmg.bmi_app.adapter.FoodAdapter;
import com.nmg.bmi_app.model.Food;

import java.util.ArrayList;
import java.util.List;

public class listOfFoodActivity extends AppCompatActivity {
    RecyclerView rv_foods;
    FoodAdapter adapter;
    List<Food> foods;
    TextView tv_result;

    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_food);
        rv_foods = findViewById(R.id.rv_foods);
        tv_result = findViewById(R.id.tv_result);
        foods = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = auth.getUid();
        showData();
    }

    private void showData() {
        fStore.collection("Food").whereEqualTo("userId", userId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()){
                                tv_result.setVisibility(View.VISIBLE);
                            }else{
                                rv_foods.setVisibility(View.VISIBLE);
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nameFood = document.getData().get("name").toString();
                                String calary = document.getData().get("calary").toString();
                                String fireUri = document.getData().get("fireUri").toString();
                                String category = document.getData().get("category").toString();
                                String documentId = document.getId();
                                foods.add(new Food(userId, calary,nameFood,fireUri,category,documentId));
                                Log.e("TAGApoi", "onComplete: " + foods.size());
                            }
                            Log.e("TAGApoi", "onComplete: " + foods.size());
                            adapter = new FoodAdapter(getApplicationContext(), foods);
                            rv_foods.setHasFixedSize(true);
                            rv_foods.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            rv_foods.setAdapter(adapter);
                            adapter.OnItemCliclkLisener(new FoodAdapter.ClickListener() {
                                @Override
                                public void onClick(Food result) {
                                    Intent i = new Intent(getApplicationContext(), editFoodActivty.class);
                                    i.putExtra("nameFood",result.getName());
                                    i.putExtra("calary",result.getCalary());
                                    i.putExtra("photo",result.getFireUri());
                                    i.putExtra("documentId",result.getDocumentId());
                                    i.putExtra("category",result.getCategory());
                                    startActivity(i);
                                }
                            });

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


    }
}