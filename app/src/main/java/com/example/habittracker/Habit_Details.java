package com.example.habittracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Habit_Details extends AppCompatActivity {

    TextView Habitname_tv;
    EditText cue_et, action_et, reward_et, notes_et;

    ImageView happy_face, neutral_face, sad_face;
    String habit_name;

    Button edit_btn, save_btn;


    private DatabaseHelper db;

    public static final String EXTRA_HABIT_ID = "extra_habit_id";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habit_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Habitname_tv = (TextView) findViewById(R.id.habit_name_tv);
        cue_et = (EditText) findViewById(R.id.cue_et);
        action_et = (EditText) findViewById(R.id.routine_et);
        reward_et = (EditText) findViewById(R.id.reward_et);
        notes_et = (EditText) findViewById(R.id.reflection_et);
        happy_face = (ImageView) findViewById(R.id.happy_face);
        neutral_face = (ImageView) findViewById(R.id.neutral_face);
        sad_face = (ImageView) findViewById(R.id.sad_face);
        Habitname_tv = (TextView) findViewById(R.id.habit_name_tv);

        edit_btn = (Button) findViewById(R.id.edit_habit_btn);
        save_btn = (Button) findViewById(R.id.save_btn);


        db = new DatabaseHelper(this);


        Intent intent = getIntent();
        int habitId = intent.getIntExtra(EXTRA_HABIT_ID, -1);
        if (habitId != -1) {
            Habit habit = db.getHabitById(habitId);

            if (habit != null) {

                Habitname_tv.setText(habit.getName());

                cue_et.setText(habit.getCue());
                action_et.setText(habit.getAction());
                reward_et.setText(habit.getReward());
                notes_et.setText(habit.getHabit_reflection());
            }

        }
        ImageView backArrow = findViewById(R.id.backArrow);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // closes this activity
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });



    }

    public boolean editHabits(){

    }

    public void enableEditing(){

        cue_et.setFocusableInTouchMode(true);
        action_et.setFocusableInTouchMode(true);
        reward_et.setFocusableInTouchMode(true);
        notes_et.setFocusableInTouchMode(true);

    }

    public void disableEditing(){
        cue_et.setFocusableInTouchMode(false);
        action_et.setFocusableInTouchMode(false);
        reward_et.setFocusableInTouchMode(false);
        notes_et.setFocusableInTouchMode(false);

    }

    public void EditButtonHide(){

    }

    public void EditButtonShow(){

    }

    public void SaveButtonHide(){

    }

    public void SaveButtonShow(){

    }
}
