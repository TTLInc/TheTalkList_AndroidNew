package com.ttl.project.thetalklist.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ttl.project.thetalklist.R;
import com.ttl.project.thetalklist.model.SubjectModel;

import java.util.List;


public class SubjectViewAdepter extends RecyclerView.Adapter<SubjectViewAdepter.ViewHolder> {
    List<SubjectModel.SubjectsBean> countries;
    Context applicationContext;

    public SubjectViewAdepter(List<SubjectModel.SubjectsBean> countries, Context applicationContext) {
        this.countries = countries;
        this.applicationContext = applicationContext;
    }

    @Override
    public SubjectViewAdepter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_searchview_rowlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubjectViewAdepter.ViewHolder holder, int position) {
        holder.txtCountryName.setText(countries.get(position).getSubject() + "");
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCountryName;

        public ViewHolder(View itemView) {
            super(itemView);
            txtCountryName = (TextView) itemView.findViewById(R.id.txtSubject);
        }
    }
}
