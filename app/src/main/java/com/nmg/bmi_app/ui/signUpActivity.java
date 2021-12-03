package com.nmg.bmi_app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nmg.bmi_app.R;

import java.util.HashMap;
import java.util.Map;

public class signUpActivity extends AppCompatActivity {
    TextView tv_login;
    Button btn_create;
    EditText et_userName, et_password, et_rePassword, et_email;
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    FirebaseFirestore db;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sig_up);
        tv_login = findViewById(R.id.tv_login);
        et_password = findViewById(R.id.et_password);
        et_userName = findViewById(R.id.et_userName);
        et_rePassword = findViewById(R.id.et_rePassword);
        et_email = findViewById(R.id.et_email);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        btn_create = findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_userName.getText().toString())) {
                    et_userName.setError("UserName is required");
                    return;
                }
                if (TextUtils.isEmpty(et_email.getText().toString())) {
                    et_email.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText().toString())) {
                    et_password.setError("Password is required");
                    return;
                }

                if (et_password.getText().length() < 6) {
                    et_password.setError("Password Must be >= 6");
                    return;
                }
                if (!et_password.getText().toString().equals(et_rePassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "please check the same password", Toast.LENGTH_SHORT).show();
                    return;
                }

                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull final Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        userid = auth.getUid();
                                        DocumentReference reference = fStore.collection("users")
                                                .document(userid);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("userName", et_userName.getText().toString());
                                        user.put("Email", et_email.getText().toString());
                                        user.put("password", et_password.getText().toString());
                                        reference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "creatUser", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplication(), comSignUpActivity.class).putExtra("userId",userid).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                            }
                                        });


                                    } else {
                                        Toast.makeText(signUpActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), loginActivity.class));
            }
        });
    }
}