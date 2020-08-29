package com.example.sfen3002_finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class DBAdapter {
    //primary key column
    static final String KEY_ROWID = "_id";
    //date created column
    static final String KEY_DATE_CREATED = "date_created";
    //due date column
    static final String KEY_DATE_DUE = "date_due";
    //activity name column
    static final String KEY_ACTIVITY = "activity_name";
    //time due column
    static final String KEY_TIME_DUE = "time_due";
    //subject column
    static final String KEY_SUBJECT = "subject";
    //description column
    static final String KEY_DESCRIPTION = "description";
    static final String TAG = "DBAdapter";
    //Database name
    private static final String DATABASE_NAME = "MyDB2";
    //name of table
    static final String DATABASE_TABLE = "activities";
    static final int DATABASE_VERSION = 1;
    //create statement
    static final String DATABASE_CREATE =
            "create table activities (_id integer primary key autoincrement,"
                    +"date_created text not null, "+
                    "date_due text not null,"+
                    "activity_name text not null,"+
                    "time_due text not null,"+
                    "subject text not null,"+
                    "description text not null);";
    final Context context;
    DataBaseHelper DBHelper;
    SQLiteDatabase db;
    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DataBaseHelper(context);
    }
    //---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //---insert a activity into the database---
    public long insertActivity(String date_created, String date_due, String activity_name, String time_due,
                               String subject, String description) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE_CREATED,date_created);
        initialValues.put(KEY_DATE_DUE,date_due);
        initialValues.put(KEY_ACTIVITY,activity_name);
        initialValues.put(KEY_TIME_DUE,time_due);
        initialValues.put(KEY_SUBJECT,subject);
        initialValues.put(KEY_DESCRIPTION,description);
        return db.insert(DATABASE_TABLE,null, initialValues);
    }
    //---closes the database---
    public void close() {
        DBHelper.close();
    }
    //---retrieves all the activities---
    public Cursor getAllActivities() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_DATE_CREATED,
                        KEY_DATE_DUE, KEY_ACTIVITY,KEY_TIME_DUE,KEY_SUBJECT,KEY_DESCRIPTION}, null, null, null, null,
                KEY_ROWID+" ASC");
    }
    //retrieves all activities and orders it by due date
    public Cursor sortByDueDate() {
     /*  return db.rawQuery("SELECT " + KEY_TIME_DUE+","+KEY_DATE_DUE+","+KEY_SUBJECT
                +","+KEY_ACTIVITY+","+KEY_DESCRIPTION+","+KEY_DATE_CREATED+ " FROM " + DATABASE_TABLE + " ORDER BY " +
                KEY_DATE_DUE+" ASC", null);*/
       return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_DATE_CREATED,
                        KEY_DATE_DUE, KEY_ACTIVITY,KEY_TIME_DUE,KEY_SUBJECT,KEY_DESCRIPTION}, null, null, null, null,
                KEY_DATE_DUE+"");
    }
    //used for testing only this not necessa nope leave it for now
    public Cursor sort() {
        return db.rawQuery("SELECT " + KEY_TIME_DUE+","+KEY_DATE_DUE+","+KEY_SUBJECT
                +","+KEY_ACTIVITY+","+KEY_DESCRIPTION+","+KEY_DATE_CREATED+ " FROM " + DATABASE_TABLE + " order By " +
                KEY_SUBJECT, null);
    }
    //retrieves all activities and orders it by subject
    public Cursor sortBySubject() {
        /*return db.rawQuery("SELECT " + KEY_TIME_DUE+","+KEY_DATE_DUE+","+KEY_SUBJECT
                +","+KEY_ACTIVITY+","+KEY_DESCRIPTION+","+KEY_DATE_CREATED+ " FROM " + DATABASE_TABLE + " order By " +
                KEY_SUBJECT+"ASC", null);*/
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_DATE_CREATED,
                        KEY_DATE_DUE, KEY_ACTIVITY,KEY_TIME_DUE,KEY_SUBJECT,KEY_DESCRIPTION}, null, null, null, null,
                KEY_SUBJECT);
    }
    //retrieves all activities and groups it by subject
    public Cursor groupBySubject() {
        /*return db.rawQuery("SELECT " + KEY_TIME_DUE+","+KEY_DATE_DUE+","+KEY_SUBJECT
                +","+KEY_ACTIVITY+","+KEY_DESCRIPTION+","+KEY_DATE_CREATED+ " FROM " + DATABASE_TABLE + " group By " +
                KEY_SUBJECT+"ASC", null);*/
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_DATE_CREATED,
                        KEY_DATE_DUE, KEY_ACTIVITY,KEY_TIME_DUE,KEY_SUBJECT,KEY_DESCRIPTION}, null, null, KEY_SUBJECT, null,
                null);
    }
    //retrieves all activities and groups it by due date
    public Cursor groupByDueDate() {
        /*return db.rawQuery("SELECT " + KEY_TIME_DUE+","+KEY_DATE_DUE+","+KEY_SUBJECT
                +","+KEY_ACTIVITY+","+KEY_DESCRIPTION+","+KEY_DATE_CREATED+ " FROM " + DATABASE_TABLE + " group By " +
                KEY_DATE_DUE+"ASC", null);*/
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_DATE_CREATED,
                        KEY_DATE_DUE, KEY_ACTIVITY,KEY_TIME_DUE,KEY_SUBJECT,KEY_DESCRIPTION}, null, null, KEY_DATE_DUE, null,
                null);
    }
    //---retrieves a particular activity---
    public Cursor getActivity(long rowId) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_DATE_CREATED,
                        KEY_DATE_DUE, KEY_ACTIVITY,KEY_TIME_DUE,KEY_SUBJECT,KEY_DESCRIPTION},
                KEY_ROWID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //---updates an activity---
    public boolean updateActivity(long rowId, String date_due, String activity_name, String time_due, String
            subject, String description) {
        ContentValues args = new ContentValues();
        args.put(KEY_DATE_DUE, date_due);
        args.put(KEY_ACTIVITY, activity_name);
        args.put(KEY_TIME_DUE,time_due);
        args.put(KEY_SUBJECT,subject);
        args.put(KEY_DESCRIPTION,description);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    //---deletes a particular activity---
    public boolean deleteActivity(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    private class DataBaseHelper extends SQLiteOpenHelper {
        DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.w(TAG, "Upgrading database from version " + i + " to "
                    + i1 + ", which will destroy all old data");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS activities");
            onCreate(sqLiteDatabase);
        }
    }
}