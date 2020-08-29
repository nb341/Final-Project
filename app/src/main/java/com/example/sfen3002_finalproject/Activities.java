package com.example.sfen3002_finalproject;

//Class used to provide an easy way to access data from arraylist
public class Activities {
    private long id;
    private String activity_name;
    private String due_date;
    private String date_created;
    private String description;
    private String subject;
    private String time_due;
    //Constructor takes in id, activity_name, date_created, description, subject, time_due
    public Activities(long id, String date_created, String due_date, String activity_name, String time_due,
                      String subject, String description){
        this.id = id;
        this.activity_name=activity_name;
        this.date_created=date_created;
        this.due_date=due_date;
        this.time_due=time_due;
        this.description=description;
        this.subject=subject;
    }
    //set id of activity
    public void setId(long id){
        this.id = id;
    }
    //get id of activity
    public long getId(){
        return id;
    }
    //set activity name
    public void setActivityName(String activity_name){
        this.activity_name=activity_name;
    }
    //gets activity name
    public String getActivityName(){
        return activity_name;
    }
    //sets date created
    public void setDateCreated(String date_created){
        this.date_created=date_created;
    }
    //gets date created
    public String getDateCreated(){
        return date_created;
    }
    //sets due date
    public void setDueDate(String date_created){
        this.date_created=date_created;
    }
    //gets due date
    public String getDueDate(){
        return due_date;
    }
    //sets description
    public void setDescription(String description){
        this.description=description;
    }
    //gets description
    public String getDescription(){
        return description;
    }
    //sets subject
    public void setSubject(String subject){
        this.subject=subject;
    }
    //gets subject
    public String getSubject(){
        return subject;
    }
    //sets time due
    public void setTimeDue(String time_due){
        this.time_due = time_due;
    }
    //gets time due
    public String getTimeDue(){
        return time_due;
    }
}
