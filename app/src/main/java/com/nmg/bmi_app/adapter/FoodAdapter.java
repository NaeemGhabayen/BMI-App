package com.nmg.bmi_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nmg.bmi_app.R;
import com.nmg.bmi_app.model.Food;
import com.nmg.bmi_app.model.Record;
import com.nmg.bmi_app.ui.homeActivity;
import com.nmg.bmi_app.ui.listOfFoodActivity;

import java.util.List;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolderTrind> {
    private Context context;
    private List<Food> foods;
    FirebaseFirestore fStore;

    private static ClickListener clickListener;

    public FoodAdapter(Context context, List<Food> results) {
        this.context = context;
        this.foods = results;

    }


    @NonNull
    @Override
    public ViewHolderTrind onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTrind(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_of_list_food,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTrind holder, int position) {
        fStore = FirebaseFirestore.getInstance();
        holder.tv_category.setText(foods.get(position).getCategory());
        holder.tv_calory.setText(foods.get(position).getCalary());
        holder.tv_nameFood.setText(foods.get(position).getName());
        Glide.with(context).load(foods.get(position).getFireUri()).into(holder.img_food);
        int p =  position;
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fStore.collection("Food").document(foods.get(p).getDocumentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "deltet item is succsefuly ", Toast.LENGTH_SHORT).show();
                            foods.remove(p);
                         context.startActivity(new Intent(context , listOfFoodActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


    public void setlist(List<Food> Results) {
        this.foods = Results;
        notifyDataSetChanged();
    }


    public Food getAllResults(int pos) {
        return foods.get(pos);
    }


    public class ViewHolderTrind extends RecyclerView.ViewHolder {
        TextView tv_category, tv_calory, tv_nameFood;
        ImageView img_food,btn_delete;
        Button  btn_edit;

        public ViewHolderTrind(@NonNull View itemView) {
            super(itemView);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_calory = itemView.findViewById(R.id.tv_calory);
            tv_nameFood = itemView.findViewById(R.id.tv_nameFood);
            img_food = itemView.findViewById(R.id.img_food);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_edit = itemView.findViewById(R.id.btn_edit);

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(foods.get(getAdapterPosition()));
                }
            });

        }

    }



    public void OnItemCliclkLisener(ClickListener clickListener1) {
        clickListener = clickListener1;
    }

    public interface ClickListener {
        void onClick(Food result);
    }
}







