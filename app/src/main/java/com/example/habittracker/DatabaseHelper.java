package com.example.habittracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;

    public static final String DATABASE_NAME = "HabitTracker.db";
    public static final String TABLE_NAME = "users";

    public static final String TABLE_HABITS = "Habits";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "EMAIL";
    public static final String COL_3 = "PASSWORD";

    public static final String T2_COL_1 = "Habit_ID";
    public static final String T2_COL_2 = "Habit_name";
    public static final String T2_COL_3 = "Cue";
    public static final String T2_COL_4 = "Habit_action";
    public static final String T2_COL_5 = "Reward";
    public static final String T2_COL_6 = "Habit_replaced";

    public static final String T2_COL_7 = "Habit_hour";
    public static final String T2_COL_8 = "Habit_minutes";

    public static final String T2_COL_9 = "Habit_reflection";

    public static final String T2_COL_10 = "Current_Feeling";
    public static final String T2_COL_11 = "Streak";
    public static final String T2_COL_12 = "Color";

    public static final String T2_COL_13 = "Completed";

    public static final String T2_COL_14 = "User_ID";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        String createHabitsTable = "CREATE TABLE "+TABLE_HABITS+" (" +
                "Habit_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Habit_name NOT NULL,"+
                "Cue TEXT, " +
                "Habit_action TEXT, " +
                "Reward TEXT, " +
                "Habit_replaced TEXT," +
                "Habit_hour INTEGER," +
                "Habit_minutes INTEGER,"+
                "Habit_reflection TEXT,"+
                "Current_feeling INT,"+
                "Streak INT,"+
                "Color INT,"+
                "User_ID INT NOT NULL,"+
                "Completed INT NOT NULL"+
                ");";

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT, PASSWORD TEXT)");
        db.execSQL(createHabitsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABITS);
        onCreate(db);
    }


    public boolean addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, email);
        contentValues.put(COL_3, password);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // returns true if insert is successful
    }

    public int getUserID(String email, String password){
        int userId =-1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COL_1}, COL_2+" = ?" +" AND "+COL_3+ "= ?",
                new String[]{email,password},
                null, null, null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            }
            cursor.close();
        }

        return userId;


    }


    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COL_1 };
        String selection = COL_2 + "=? AND " + COL_3 + "=?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public boolean addHabit(String name,String cue, String action, String reward,
                            String replacedHabit,
                            int hour , int minute, String reflection, int feeling, int streak, int color, int userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (habitExists(name)){
            Toast.makeText(context, "There already exists a habit with the entered name",Toast.LENGTH_LONG).show();
            return false;
        }
        contentValues.put(T2_COL_2,name);
        contentValues.put(T2_COL_3, cue);
        contentValues.put(T2_COL_4, action);
        contentValues.put(T2_COL_5, reward);
        contentValues.put(T2_COL_6,replacedHabit);
        contentValues.put(T2_COL_7,hour);
        contentValues.put(T2_COL_8,minute);
        contentValues.put(T2_COL_9,reflection);
        contentValues.put(T2_COL_10,feeling);
        contentValues.put(T2_COL_11,streak);
        contentValues.put(T2_COL_12,color);
        contentValues.put(T2_COL_13,0);
        contentValues.put(T2_COL_14,userid);



        long result = db.insert(TABLE_HABITS, null, contentValues);
        return result != -1; // returns true if insert is successful
    }

    public boolean updateHabit(int id,String name, String cue, String action, String reward,
                               String replacedHabit,
                               int hour, int minute,
                               String reflection, int feeling,int streak, int color,int userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (habitExists(name)){
            Toast.makeText(context, "There already exists a habit with the entered name",Toast.LENGTH_LONG).show();
            return false;
        }

        String ID = Integer.toString(id);
        values.put(T2_COL_2, name);
        values.put(T2_COL_3, cue);
        values.put(T2_COL_4, action);
        values.put(T2_COL_5, reward);
        values.put(T2_COL_6,replacedHabit);
        values.put(T2_COL_7,hour);
        values.put(T2_COL_8,minute);
        values.put(T2_COL_9,reflection);
        values.put(T2_COL_10,feeling);
        values.put(T2_COL_11,streak);
        values.put(T2_COL_12,color);
        values.put(T2_COL_14,userid);


        int rows = db.update(TABLE_HABITS,values, T2_COL_1+" = ?",new String[]{ID});
        return rows > 0;
    }

    public boolean habitExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HABITS,
                new String[]{"id"},
                T2_COL_2+" = ?",
                new String[]{name},
                null, null, null
        );

        boolean exists = false;
        if (cursor != null) {
            if( cursor.getCount() > 0)
            {exists=true;}
            cursor.close();
        }

        return exists;
    }

    public boolean deleteHabit(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_HABITS,
                T2_COL_1+" = ?",
                new String[]{String.valueOf(id)}
        );

        return rows > 0;
    }

    public ArrayList<Habit> getAllHabitsForPerson(int personId) {
        ArrayList<Habit> habitList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the habits table for rows matching the person_id
        Cursor cursor = db.query(
                "habits",                  // Table name
                null,                       // Columns (null = all)
                "person_id = ?",            // WHERE clause
                new String[]{String.valueOf(personId)}, // WHERE args
                null, null, null            // groupBy, having, orderBy
        );

        if (cursor.moveToFirst()) {
            do {
                Habit habit = new Habit(
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_1)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_2)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_3)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_4)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_5)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_6)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_7)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_8)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_9)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_10)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_11)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_12)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_13)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_14))
                );
                habitList.add(habit);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return habitList;
    }








}