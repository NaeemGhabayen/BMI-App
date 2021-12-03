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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nmg.bmi_app.R;

public class loginActivity extends AppCompatActivity {
    TextView tv_signUp;
    EditText et_email, et_password;
    Button btn_login;
    ProgressBar bar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_signUp = findViewById(R.id.tv_signUp);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        bar = findViewById(R.id.progressBar2);
        auth = FirebaseAuth.getInstance();
        bar.setVisibility(View.INVISIBLE);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                bar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(loginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplication(), homeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    bar.setVisibility(View.INVISIBLE);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    bar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), signUpActivity.class));
            }
        });
    }
}