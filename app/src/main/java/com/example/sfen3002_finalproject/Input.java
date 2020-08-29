package com.example.sfen3002_finalproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class Input extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    //declares Edit texts for edit texts in the input_layout
    protected EditText task, desc, subject, due_date,time_due;
    //to use database
    private DBAdapter db;
    //stores the time set by the user
    private String time_due_s;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Gets builder to set up custom dialog for Input
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Gets Layout for custom dialog for input
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //set up view for text box in input layout xml
        View view = inflater.inflate(R.layout.input_layout, null);
        //set up view for text box in input layout xml
        task = view.findViewById(R.id.task_edit);
        desc = view.findViewById(R.id.desc_edit);
        subject = view.findViewById(R.id.subject_edit);
        time_due = view.findViewById(R.id.time_due);
        //opens time picker when choose time due is clicked
        time_due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        due_date = view.findViewById(R.id.date_due_edit);
        //opens date picker when choose due date is picked
        due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        //building custom dialog for input
        builder.setView(view);
        builder.setTitle("New Task");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //returns feedback message when input is cancelled
                Toast.makeText(getContext(), "Adding New Task Cancelled ", Toast.LENGTH_LONG).show();
            }
        });
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //initializes DBAdapter
                db = new DBAdapter(getActivity());
                //opens database to begin insert
                db.open();
                //Gets values entered in the text boxes in the input layout
                String time_s = time_due.getText().toString();
                //gets the current date for the date created
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String des = desc.getText().toString();
                String sub = subject.getText().toString();
                String task_in = task.getText().toString();
                String date_due_s = due_date.getText().toString();
                String time_due_now = time_due.getText().toString();
                //inserts task into database
                if (db.insertActivity(date, date_due_s, task_in, time_due_now, sub, des) >= 0) {
                    //returns feedback message if insert successful
                    Toast.makeText(getContext(), "Insert Successful ", Toast.LENGTH_LONG).show();
                } else {
                    //returns feedback message if insert fail
                    Toast.makeText(getContext(), "Insert fail ", Toast.LENGTH_LONG).show();
                }
                //closes database
                db.close();
                //displays updated list with new task
                ((MainActivity) getActivity()).RegularDisplay();          }
        });
        return builder.create();
    }
    //opens date picker when choose due date is clicked in the input layout
    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        time_due.setText(time_due_s);
    }
    //sets date due to the one picked by the user
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final String due_date_ed = dayOfMonth+"/"+String.valueOf(month+1)+"/"+year;
        due_date.setText(due_date_ed);
    }
    //opens time picker when choose time due is clicked in the input layout
    public void showTimePicker(){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timepicker = new TimePickerDialog(getActivity(),this, hour, minute, true);
        timepicker.show();
    }
    //sets time due to the one picked by the user
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time_due_s_1=hourOfDay+":"+minute;
        String min = "0";
        if(minute<10){
           min =min+minute;
           time_due_s_1 = String.valueOf(hourOfDay)+":"+min;
        }
        else {
            time_due_s_1 = String.valueOf(hourOfDay)+":"+String.valueOf(min);
        }
        time_due.setText(time_due_s_1);
    }
}