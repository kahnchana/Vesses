package com.suemsolutions.www.plantdoctortest;

/**
 * Created by shehan on 11/5/17.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DiseasesAdapter extends RecyclerView.Adapter<DiseasesAdapter.MyViewHolder> {

    private List<Disease> diseaseList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, probability;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.card_disease_name);
            probability = (TextView) view.findViewById(R.id.card_disease_probability);
        }
    }


    public DiseasesAdapter(List<Disease> diseaseList) {
        this.diseaseList = diseaseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_disease, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Disease disease = diseaseList.get(position);
        holder.name.setText(disease.getName());
        holder.probability.setText( (Integer.toString(disease.getProbability())));
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

}
