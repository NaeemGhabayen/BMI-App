package com.nmg.bmi_app.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nmg.bmi_app.R;
import com.nmg.bmi_app.model.Food;

public class addFoodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText et_nameFood, et_calary;
    Spinner sp_category;
    ImageView imgFood;
    Button btn_uploadPhoto, btn_save1;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    StorageReference firebaseStorage;
    Uri img, fireUri;
    Food food;
    String userId, cat;
    ProgressDialog progressdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        et_nameFood = findViewById(R.id.et_nameFood);
        et_calary = findViewById(R.id.et_calary);
        sp_category = findViewById(R.id.sp_category);
        imgFood = findViewById(R.id.imgFood);
        btn_save1 = findViewById(R.id.btn_save1);
        btn_uploadPhoto = findViewById(R.id.btn_uploadPhoto);
        fStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(adapter);
        sp_category.setOnItemSelectedListener(this);
        cat = sp_category.getSelectedItem().toString();
        firebaseStorage = FirebaseStorage.getInstance().getReference();

        btn_uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opengalary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(opengalary, 1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                img = data.getData();
                Glide.with(getApplicationContext()).load(img).into(imgFood);
                //profile_image.setImageURI(img);
                Uploadetofirebase(img);
            }
        }
    }

    private void Uploadetofirebase(Uri image) {
        progressdialog = ProgressDialog.show(this, "","Please Wait...", true);
        final StorageReference reference = firebaseStorage.child("users/" + auth.getUid() + "/profile.jpg");
        reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("TAG", "onSuccess: " + uri);
                        fireUri = uri;
                        Toast.makeText(getApplicationContext(), "Image Uploded", Toast.LENGTH_SHORT).show();
                        progressdialog.dismiss();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error Uploded", Toast.LENGTH_SHORT).show();
            }
        });
        btn_save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_nameFood.getText().toString())) {
                    et_nameFood.setError("name is required");
                    return;
                }
                if (TextUtils.isEmpty(et_calary.getText().toString())) {
                    et_calary.setError("calary is required");
                    return;
                }
                if (fireUri == null) {
                    Toast.makeText(getApplicationContext(), "please upload photo", Toast.LENGTH_LONG).show();
                    return;
                }
                food = new Food();

                food.setUserId(userId);
                food.setCategory(cat);
                food.setFireUri(fireUri + "");
                food.setCalary(et_calary.getText().toString());
                food.setName(et_nameFood.getText().toString());
                fStore.collection("Food").add(food).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                            et_calary.setText("");
                            et_nameFood.setText("");
                            Glide.with(getApplicationContext()).load(R.drawable.ic_launcher_background).into(imgFood);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        cat = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}