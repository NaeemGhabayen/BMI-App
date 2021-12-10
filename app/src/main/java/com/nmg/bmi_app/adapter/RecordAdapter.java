package com.nmg.bmi_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.nmg.bmi_app.R;
import com.nmg.bmi_app.model.Record;

import java.util.List;


public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolderTrind> {
    private Context context;
    private List<Record> records;
    private static ClickListener clickListener;

    public RecordAdapter(Context context, List<Record> results) {
        this.context = context;
        this.records = results;

    }


    @NonNull
    @Override
    public ViewHolderTrind onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTrind(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_old_status,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTrind holder, int position) {
        holder.tv_lenght.setText(records.get(position).getLenght());
//        holder.tv_status.setText(records.get(position).get());
        holder.tv_wieght.setText(records.get(position).getWieght());
        holder.tv_date.setText(records.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }


    public void setlist(List<Record> Results) {
        this.records = Results;
        notifyDataSetChanged();
    }


    public Record getAllResults(int pos) {
        return records.get(pos);
    }


    public class ViewHolderTrind extends RecyclerView.ViewHolder {
        TextView tv_lenght, tv_status, tv_wieght,tv_date;


        public ViewHolderTrind(@NonNull View itemView) {
            super(itemView);
            tv_lenght = itemView.findViewById(R.id.tv_lenght);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_wieght = itemView.findViewById(R.id.tv_wieght);
            tv_date = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(records.get(getAdapterPosition()));
                }
            });
        }

    }

    public void OnItemCliclkLisener(ClickListener clickListener1) {
        clickListener = clickListener1;
    }

    public interface ClickListener {
        void onClick(Record result);
    }

}







