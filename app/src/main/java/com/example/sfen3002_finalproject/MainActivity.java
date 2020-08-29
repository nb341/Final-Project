package com.example.sfen3002_finalproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.nio.file.attribute.GroupPrincipal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    //button to add new task
    private FloatingActionButton fb;
    //List view to display list of tasks
    private ListView listView;
    //for custom row to display elements of a task
    private CustomAdapter adapter;
    private Activities activity;
    //stores list of objects of type activities
    private ArrayList<Activities> activities;
    //Used to access SQLite Database
    private DBAdapter db;
    //Text field to update task name
    private EditText task_update;
    //Text field to update due date
    private EditText date_due;
    //Text field to update time due
    private EditText time_due;
    //Text field to update subject
    private EditText subject;
    //Text field to update description
    private EditText description;
    //Spinner for drop down menu
    private Spinner spinner;
    //Array to store the different displays
    private String [] Group_Sort;
    //Displays tasks by subject in ascending order when true
    /*private boolean sort_by_subject = false;
    //Displays tasks by due in ascending order when true
    private boolean sort_by_due_date = false;
    //Displays tasks and group them by subject in ascending order when true
    private boolean group_by_subject = false;
    //Displays tasks and group them by due date ascending order when true
    private boolean group_by_due_date = false;
    //holds the value of array group_sort[] depending what the user selects*/
    int key=0;
    private String display="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpView();
        registerForContextMenu (listView);
        RegularDisplay();


    }
    void setUpView(){
        //sets up spinner view
        spinner = (Spinner) findViewById(R.id.spinner);
        //stores the values in sort_group in group_sort
        Group_Sort = getResources().getStringArray(R.array.Sort_Group);
        //sets up and displays drop down menu for spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Sort_Group,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //toast message appears saying which menu option was clicked in the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                display= Group_Sort[i];
                Toast.makeText(getApplicationContext(), Group_Sort[i]+" Display Selected",
                        Toast.LENGTH_LONG).show();
                  if(i==0){
                      //regualr displat
                      RegularDisplay();
                  }
                  if(i==1){
                      //sortby duedate
                      SortByDueDate();
                  }
                if(i==2){
                    //sortbysubject
                    Subject_Sort();
                }
                if(i==3){
                    //group by duedate
                    GroupByDueDate();
                }
                if(i==4){
                    //group by subject
                    GroupBySubject();
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //initializes DBAdapter in MainActivity
        db = new DBAdapter(this);
        //sets up view for list view
        listView = (ListView) findViewById(R.id.List_View);
        //makes list view clickable
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        //sets up view for add task button
        fb = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        //opens input dialog when button clicked
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenInputDialog();
            }
        });
    }
    //Opens dialog fragment to add new task/activities
    void OpenInputDialog(){
        Input ip = new Input();
        ip.show(getSupportFragmentManager(),null);
    }
    //Opens up calendar when choose date is clicked in update dialog
    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    //Create single choice option menu item when a row on the custom list view is clicked
    @Override
    public void onCreateContextMenu (ContextMenu menu, View v,
                                     ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu (menu, v, menuInfo);
        //Adds Edit and Delete as menu options
        menu.add ("Edit");
        menu.add ("Delete");
    }
    @Override public boolean onContextItemSelected (final MenuItem item)
    {
        //Gets items from custom adapter stored in the arraylist
        AdapterView.AdapterContextMenuInfo acts = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo ();
        //gets the position of the item clicked



    final Activities act1 = activities.get (acts.position);
    //id stores the value of the primary key of the item that was clicked
    final long id = act1.getId ();
    //If Delete option is chosen
    if (item.getTitle() == "Delete")
    {
        //opens database to begin delete
        db.open ();
        //if database delete query returns true displays toast confirming delete
        if (db.deleteActivity (id))
            Toast.makeText (this, "Delete Successful",
                    Toast.LENGTH_LONG).show ();
        else
            //if delete fails, displays delete failed
            Toast.makeText (this, "Delete Failed", Toast.LENGTH_LONG).show ();
        //closes database
        db.close ();
        //Displays update list view
        RegularDisplay();
    }
    //if Edit option is chosen
 else if (item.getTitle () == "Edit")
    {
        //Alert dialog used to open custom dialog for to update task
        final AlertDialog.Builder builder =
                new AlertDialog.Builder (MainActivity.this);
        //For the custom dialog layout
        LayoutInflater inflater = getLayoutInflater ();
        //Sets the view of the custom dialog
        final View view = inflater.inflate (R.layout.update_layout, null);
        //Makes edit texts in the update_layout.xml accessible in the main activity
        task_update= (EditText) view.findViewById (R.id.task_update);
        date_due = (EditText) view.findViewById(R.id.date_due_update);
        time_due = (EditText) view.findViewById(R.id.time_due_update);
        subject = (EditText) view.findViewById(R.id.subject_update);
        description = (EditText) view.findViewById(R.id.desc_update);
        //sets the edit texts in the update_layout.xml to the activity values that was clicked in the list
        task_update.setText(act1.getActivityName());
        time_due.setText(act1.getTimeDue());
        date_due.setText(act1.getDueDate());
        subject.setText(act1.getSubject());
        description.setText(act1.getDescription());
        //opens up date picker for update when clicked
        date_due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        //opens time picker when that option is clicked
        time_due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        //sets up custom dialog for update task
        builder.setView (view).setTitle ("Update Task").
                setNegativeButton ("Cancel", new DialogInterface.OnClickListener ()
                {
                    @Override
                    public void onClick (DialogInterface dialog,
                                         int which)
                    {
                        Toast.makeText(getApplicationContext(), "Updating Task Cancelled ",
                                Toast.LENGTH_LONG).show();
                    }
                    //sets positive button to Update and performs update when clicked
                }).setPositiveButton ("Update",
                new DialogInterface.OnClickListener ()
                {
                    @Override
                    public void
                    onClick (DialogInterface dialog, int which)
                    {
                        //opens database to begin update
                        db.open ();
                        //testing for null values
                        System.out.println("THIS IS THE TASK +>>>>>>>>>>>>>>> " + act1);;
                        //Gets updated task from textbox
                        String task = task_update.getText().toString();
                        String date_d = date_due.getText().toString();
                        String sub_time = time_due.getText().toString();
                        String sub = subject.getText().toString();
                        String desc = description.getText().toString();
                        //performs update and return confirmation messages
                        if (db.updateActivity (id, date_d, task, sub_time, sub,desc))
                            Toast.makeText (MainActivity.this, "Update Successful", Toast.LENGTH_LONG).show
                                    ();
                        else
                            //returns Update failed to screen if update fail
                            Toast.makeText (MainActivity.this, "Update Failed", Toast.LENGTH_LONG).show();
                        //closes database
                        db.close ();
//displays updated list of tasks
                        RegularDisplay();
                    }
                }
        );
        //to display the Update task dialog ie the layout in the update_layout.xml file
        AlertDialog alertDialog = builder.create ();
        alertDialog.show ();
    }
 return true;
}
    //Opens time picker when choose time is clicked in the update_layout
    public void showTimePicker(){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timepicker = new TimePickerDialog(MainActivity.this,this, hour, minute, true);
        timepicker.show();
    }
    //Chooses which display to use set all others to false

    void GroupBySubject(){
        //opens database to being display
        db.open();
        //Declares cursor variable
        Cursor c = db.groupBySubject();
        //moves cursor to first position
        //  c=db.getAllActivities();
        if (c.moveToFirst())
        {
            //creates and initialize empty arraylist of type activities
            activities = new ArrayList < Activities> ();
            //clears old values in the arraylist to prevent previous garbage values
            activities.clear ();
            //Stores values from database in Activities object and then adds it to arraylist
            do
            {
                activity = new Activities (c.getLong (0), c.getString (1), c.getString (2),
                        c.getString (3),c.getString(4),c.getString(5),c.getString(6));
                activities.add (activity);
            }
            while (c.moveToNext ());
            //sets up custom adapter to display the values in the arraylist
            adapter = new CustomAdapter (this, activities);
            //gets list view from activity_main.xml
            listView = (ListView) findViewById (R.id.List_View);
            //formats listview to custom list view from custom adapter
            listView.setAdapter (adapter);
        }
        //closes database
        db.close ();
    }


    void GroupByDueDate(){
        //opens database to being display
        db.open();
        //Declares cursor variable
        Cursor c = db.groupByDueDate();
        //moves cursor to first position
        //  c=db.getAllActivities();
        if (c.moveToFirst())
        {
            //creates and initialize empty arraylist of type activities
            activities = new ArrayList < Activities> ();
            //clears old values in the arraylist to prevent previous garbage values
            activities.clear ();
            //Stores values from database in Activities object and then adds it to arraylist
            do
            {
                activity = new Activities (c.getLong (0), c.getString (1), c.getString (2),
                        c.getString (3),c.getString(4),c.getString(5),c.getString(6));
                activities.add (activity);
            }
            while (c.moveToNext ());
            //sets up custom adapter to display the values in the arraylist
            adapter = new CustomAdapter (this, activities);
            //gets list view from activity_main.xml
            listView = (ListView) findViewById (R.id.List_View);
            //formats listview to custom list view from custom adapter
            listView.setAdapter (adapter);
        }
        //closes database
        db.close ();
    }
    public void SortByDueDate(){
        //Display task by first one entered
        //opens database to being display
        db.open();
        //Declares cursor variable
        Cursor c = db.sortByDueDate();
        //moves cursor to first position
        //  c=db.getAllActivities();
        if (c.moveToFirst())
        {
            //creates and initialize empty arraylist of type activities
            activities = new ArrayList < Activities> ();
            //clears old values in the arraylist to prevent previous garbage values
            activities.clear ();
            //Stores values from database in Activities object and then adds it to arraylist
            do
            {
                activity = new Activities (c.getLong (0), c.getString (1), c.getString (2),
                        c.getString (3),c.getString(4),c.getString(5),c.getString(6));
                activities.add (activity);
            }
            while (c.moveToNext ());
            //sets up custom adapter to display the values in the arraylist
            adapter = new CustomAdapter (this, activities);
            //gets list view from activity_main.xml
            listView = (ListView) findViewById (R.id.List_View);
            //formats listview to custom list view from custom adapter
            listView.setAdapter (adapter);
        }
        //closes database
        db.close ();
    }

    public void Subject_Sort()
    {
        //opens database to being display
        db.open();
        //Declares cursor variable
        Cursor c = db.sortBySubject();
        //moves cursor to first position
        //  c=db.getAllActivities();
        if (c.moveToFirst())
        {
            //creates and initialize empty arraylist of type activities
            activities = new ArrayList < Activities> ();
            //clears old values in the arraylist to prevent previous garbage values
            activities.clear ();
            //Stores values from database in Activities object and then adds it to arraylist
            do
            {
                activity = new Activities (c.getLong (0), c.getString (1), c.getString (2),
                        c.getString (3),c.getString(4),c.getString(5),c.getString(6));
                activities.add (activity);
            }
            while (c.moveToNext ());
            //sets up custom adapter to display the values in the arraylist
            adapter = new CustomAdapter (this, activities);
            //gets list view from activity_main.xml
            listView = (ListView) findViewById (R.id.List_View);
            //formats listview to custom list view from custom adapter
            listView.setAdapter (adapter);
        }
        //closes database
        db.close ();
    }
    public void RegularDisplay()
    {
        //opens database to being display
        db.open();
        //Declares cursor variable
        Cursor c = db.getAllActivities();
        //moves cursor to first position
     //  c=db.getAllActivities();
        if (c.moveToFirst())
        {
            //creates and initialize empty arraylist of type activities
            activities = new ArrayList < Activities> ();
            //clears old values in the arraylist to prevent previous garbage values
            activities.clear ();
            //Stores values from database in Activities object and then adds it to arraylist
            do
            {
                activity = new Activities (c.getLong (0), c.getString (1), c.getString (2),
                        c.getString (3),c.getString(4),c.getString(5),c.getString(6));
                activities.add (activity);
            }
            while (c.moveToNext ());
            //sets up custom adapter to display the values in the arraylist
            adapter = new CustomAdapter (this, activities);
            //gets list view from activity_main.xml
            listView = (ListView) findViewById (R.id.List_View);
            //formats listview to custom list view from custom adapter
            listView.setAdapter (adapter);
        }
        //closes database
        db.close ();
    }
    //sets date to the one chosen from the date picker in update
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final String due_date_ed = dayOfMonth+"/"+String.valueOf(month+1)+"/"+year;
        date_due.setText(due_date_ed);
    }
    //sets time due to the one chosen from the time picker in update
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time_due_s;
        String min = "0";
        if(minute<10){
            min +=minute;
            time_due_s = String.valueOf(hourOfDay)+":"+min;
        }
        else {
            time_due_s = String.valueOf(hourOfDay)+":"+String.valueOf(min);
        }
        time_due.setText(time_due_s);
    }
}
