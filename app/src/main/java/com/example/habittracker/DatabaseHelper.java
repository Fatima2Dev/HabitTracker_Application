package com.example.habittracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
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



    public static final String T2_COL_7 = "Habit_reflection";

    public static final String T2_COL_8 = "Current_Feeling";
    public static final String T2_COL_9 = "Streak";
    public static final String T2_COL_10 = "Color";

    public static final String T2_COL_11= "Completed";

    public static final String T2_COL_12 = "User_ID";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context=context;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        String createHabitsTable = "CREATE TABLE "+TABLE_HABITS+" (" +
                "Habit_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Habit_name TEXT NOT NULL,"+
                "Cue TEXT, " +
                "Habit_action TEXT, " +
                "Reward TEXT, " +
                "Habit_replaced TEXT," +
                "Habit_reflection TEXT,"+
                "Current_Feeling INTEGER,"+
                "Streak INTEGER,"+
                "Color INTEGER,"+
                "Completed INTEGER NOT NULL," +
                "User_ID INTEGER NOT NULL" +
                ")";

        String createUsersTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT, PASSWORD TEXT)";

        try {
            Log.d("DatabaseHelper", "Attempting to create users table.");
            db.execSQL(createUsersTable);
            Log.d("DatabaseHelper", "Users table created successfully.");

            Log.d("DatabaseHelper", "Attempting to create habits table.");
            db.execSQL(createHabitsTable);
            Log.d("DatabaseHelper", "Habits table created successfully.");
        } catch (SQLException e) {

            Log.e("DatabaseHelper", "Error creating tables: " + e.getMessage());
        }



        //db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT, PASSWORD TEXT)");
       // db.execSQL(createHabitsTable);
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
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_1));
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
    public boolean habitExists2(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HABITS,
                new String[]{T2_COL_1},
                T2_COL_2 + " = ?",
                new String[]{name},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean addHabit(String name,String cue, String action, String reward,
                            String replacedHabit,
                            String reflection, int feeling, int streak, int color, int userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (habitExists2(name)){
            Toast.makeText(context, "There already exists a habit with the entered name",Toast.LENGTH_LONG).show();
            return false;
        }
        contentValues.put(T2_COL_2,name);
        contentValues.put(T2_COL_3, cue);
        contentValues.put(T2_COL_4, action);
        contentValues.put(T2_COL_5, reward);
        contentValues.put(T2_COL_6,replacedHabit);

        contentValues.put(T2_COL_7,reflection);
        contentValues.put(T2_COL_8,feeling);
        contentValues.put(T2_COL_9,streak);
        contentValues.put(T2_COL_10,color);
        contentValues.put(T2_COL_11,0);
        contentValues.put(T2_COL_12,userid);



        long result = db.insert(TABLE_HABITS, null, contentValues);
        return result != -1; // returns true if insert is successful
    }

    public boolean updateHabit(int id,String name, String cue, String action, String reward,
                               String replacedHabit,

                               String reflection, int feeling,int streak, int color,int completed,int userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (this.habitExists(name, id)) {
            Toast.makeText(context, "Another habit already has this name.", Toast.LENGTH_LONG).show();
            return false;
        }

        values.put(T2_COL_2, name);
        values.put(T2_COL_3, cue);
        values.put(T2_COL_4, action);
        values.put(T2_COL_5, reward);
        values.put(T2_COL_6,replacedHabit);

        values.put(T2_COL_7,reflection);
        values.put(T2_COL_8,feeling);
        values.put(T2_COL_9,streak);
        values.put(T2_COL_10,color);
        values.put(T2_COL_11, completed);
        values.put(T2_COL_12,userid);


        int rows = db.update(TABLE_HABITS,values, T2_COL_1+" = ?",new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean habitExists(String name, int currentHabitId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HABITS,
                new String[]{T2_COL_1}, // Use the correct column constant
                T2_COL_2+" = ? AND " + T2_COL_1 + " != ?",
                new String[]{name, String.valueOf(currentHabitId)},                null, null, null
        );


        boolean exists = cursor.getCount()>0;
        cursor.close();
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

    public ArrayList<Habit> getAllHabits(int personId) {
        ArrayList<Habit> habitList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(
                TABLE_HABITS,              // Use the correct table constant
                null,
                T2_COL_12 + " = ?",        // Use the correct user ID column constant
                new String[]{String.valueOf(personId)},
                null, null, null
        );


        if (cursor.moveToFirst()) {
            do {
                Habit habit = new Habit(
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_1)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_2)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_3)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_4)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_5)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_6)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_7)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_8)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_9)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_10)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_11)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_12))
                );
                habitList.add(habit);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return habitList;
    }

    // In C:/Users/fatim/Desktop/GitHub/HabitTracker_Application/app/src/main/java/com/example/habittracker/DatabaseHelper.java

// ... (after the getAllHabits method)

    public Habit getHabitById(int habitId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Habit habit = null;

        try (Cursor cursor = db.query(
                TABLE_HABITS,
                null,
                T2_COL_1 + " = ?",
                new String[]{String.valueOf(habitId)},
                null, null, null
        )) {
            if (cursor.moveToFirst()) {
                habit = new Habit(
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_1)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_2)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_3)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_4)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_5)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_6)),
                        cursor.getString(cursor.getColumnIndexOrThrow(T2_COL_7)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_8)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_9)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_10)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_11)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(T2_COL_12))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return habit; // Return the found habit, or null if not found
    }


}







