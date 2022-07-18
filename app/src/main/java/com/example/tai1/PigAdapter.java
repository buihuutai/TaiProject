package com.example.tai1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PigAdapter extends RecyclerView.Adapter<PigAdapter.PigViewHolder>{

    private List<Pig> pigList;
    private Context context;

    public PigAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Pig> list) {
        this.pigList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pig_profile, parent,false);
        PigViewHolder holder = new PigViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PigViewHolder holder, int position) {
        Pig pig = pigList.get(position);
        if (pig == null) { return; }

        holder.pigImage.setImageResource(R.drawable.pig);
        holder.pigName.setText(pig.getName());
        holder.pigAge.setText("Age: " + pig.getAge());
        holder.pigOrigin.setText("Origin " + pig.getOrigin());
        holder.pigWeight.setText("Weight" + pig.getWeight());
    }

    @Override
    public int getItemCount() {
        if (pigList != null) {
            return pigList.size();
        }
        return 0;
    }

    public class PigViewHolder extends RecyclerView.ViewHolder {
        private ImageView pigImage;
        private TextView pigName, pigAge, pigOrigin, pigWeight;

        public PigViewHolder(@NonNull View itemView) {
            super(itemView);

            pigImage = itemView.findViewById(R.id.pig_image);
            pigName = itemView.findViewById(R.id.pig_name);
            pigAge = itemView.findViewById(R.id.pig_age);
            pigOrigin = itemView.findViewById(R.id.pig_origin);
            pigWeight = itemView.findViewById(R.id.pig_weight);
        }
    }
}
