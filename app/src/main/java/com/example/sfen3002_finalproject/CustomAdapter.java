package com.example.sfen3002_finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

class CustomAdapter extends ArrayAdapter<Activities> {
    private TextView task_view, date_created, date_due, time_due, subject, description;
    public CustomAdapter(@NonNull Context context, ArrayList<Activities> activity){
        super(context, R.layout.custom_row, activity);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Inflates layout, sets it up to look like the custom row
        LayoutInflater inflater = LayoutInflater.from(getContext());
        //sets up view to access layout within customer adapter class
        View custom_row = inflater.inflate(R.layout.custom_row,parent,false);
        //gets the position of the row in the list view
        Activities act = getItem(position);
        //Sets up text views in custom row layout xml
        task_view = (TextView) custom_row.findViewById(R.id.task_view);
        date_created = custom_row.findViewById(R.id.date_created);
        date_due = custom_row.findViewById(R.id.date_due);
        time_due = custom_row.findViewById(R.id.time_due);
        subject = custom_row.findViewById(R.id.subject_view);
        description = custom_row.findViewById(R.id.desc_view);
        //sets text view to task name in database at particular row
        task_view.setText("Task: "+act.getActivityName());
        //sets text view to date created in database at particular row
        date_created.setText("Date Created: "+"\n"+act.getDateCreated());
        //sets text view to date due in database at particular row
        date_due.setText("Due Date: "+"\n"+act.getDueDate());
        //sets text view to time due in database at particular row
        time_due.setText("Time Due: "+"\n"+act.getTimeDue());
        //sets text view to subject in database at particular row
        subject.setText("Subject: "+"\n"+act.getSubject());
        //sets text view to description in database at particular row
        description.setText("Description of Task: "+"\n"+act.getDescription());
        return custom_row;
    }
}