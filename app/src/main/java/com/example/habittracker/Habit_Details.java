package com.example.habittracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Habit_Details extends AppCompatActivity {

    TextView Habitname_tv;
    EditText cue_et, action_et, reward_et, notes_et;

    TextView feeling_inquiry_tv;
    LinearLayout feeling_selection_layout;

    ImageView happy_face, neutral_face, sad_face;
    String habit_name;

    Button edit_btn, save_btn;


    private DatabaseHelper db;
    Habit habit;
    int habitId;

    private int selectedFeeling = 0
;

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

        feeling_inquiry_tv = (TextView) findViewById(R.id.feeling_inquiry_tv);
        feeling_selection_layout = (LinearLayout) findViewById(R.id.feeling_selection_layout);

        edit_btn = (Button) findViewById(R.id.edit_habit_btn);
        save_btn = (Button) findViewById(R.id.save_btn);


        db = new DatabaseHelper(this);


        Intent intent = getIntent();
        habitId = intent.getIntExtra(EXTRA_HABIT_ID, -1);

        if (habitId != -1) {
            habit = db.getHabitById(habitId);

            if (habit != null) {

                Habitname_tv.setText(habit.getName());

                cue_et.setText(habit.getCue());
                action_et.setText(habit.getAction());
                reward_et.setText(habit.getReward());
                determineReflection();

                int initialFeeling = habit.getCurrent_Feeling();

                updateFeelingSelection(initialFeeling);
            }

        }
        ImageView backArrow = findViewById(R.id.backArrow);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // closes this activity
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = editClick();
                if (success) {
                    Toast.makeText(Habit_Details.this, "Please change your desired fields", Toast.LENGTH_SHORT).show();
                }


            }

        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveClick();

            }

        });

        happy_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if editing is enabled before allowing a change
                if (cue_et.isFocusable()) { // A simple way to check if we are in edit mode
                    updateFeelingSelection(1);
                }
            }
        });

        neutral_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cue_et.isFocusable()) {
                    updateFeelingSelection(2);
                }
            }
        });

        sad_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cue_et.isFocusable()) {
                    updateFeelingSelection(3);
                }
            }
        });



    }

    public boolean editClick() {
        EditButtonHide();
        enableEditing();
        showReflectionInquiry();
        SaveButtonShow();
        return true;


    }

    public void showReflectionInquiry(){
        feeling_inquiry_tv.setVisibility(View.VISIBLE);
        feeling_selection_layout.setVisibility(View.VISIBLE);
    }

    public void hideReflectionInquiry(){
        feeling_inquiry_tv.setVisibility(View.GONE);
        feeling_selection_layout.setVisibility(View.GONE);
    }


    public void saveClick() {
        boolean success = updateHabit();
        if (success) { return;}
        Toast.makeText(this, "Habit not updated", Toast.LENGTH_SHORT).show();
        EditButtonShow();
        disableEditing();

        SaveButtonHide();

    }

    public void determineReflection() {
        if (habit.getHabit_reflection() == null || habit.getHabit_reflection().equals("")){
            notes_et.setHint("No current reflection yet");
        }
        else {
            notes_et.setText(habit.getHabit_reflection());
        }

    }
    public boolean updateHabit() {

        if (habit == null) {
            return false;
        }

        String updatedCue = cue_et.getText().toString();
        String updatedAction = action_et.getText().toString();
        String updatedReward = reward_et.getText().toString();
        String updatedReflection = notes_et.getText().toString();
        boolean success = db.updateHabit(habit.getHabit_ID(), habit.getName(),
                updatedCue, updatedAction, updatedReward,
                habit.getHabit_replaced(),
                updatedReflection,
                selectedFeeling,
                habit.getStreak(),
                habit.getColor(),
                habit.isCompleted(),
                habit.getUser_ID());

        if (success) {

            UpdateHabitDetails();
            Toast.makeText(this, "Habit updated successfully", Toast.LENGTH_SHORT).show();
            disableEditing();
            EditButtonShow();
            SaveButtonHide();
            hideReflectionInquiry();
            return true;
        }

        return false;

    }

    public void enableEditing() {

        cue_et.setFocusableInTouchMode(true);
        action_et.setFocusableInTouchMode(true);
        reward_et.setFocusableInTouchMode(true);
        notes_et.setFocusableInTouchMode(true);
        cue_et.setClickable(true);
        action_et.setClickable(true);
        reward_et.setClickable(true);
        notes_et.setClickable(true);


    }

    public void disableEditing() {
        findViewById(R.id.main).requestFocus();
        cue_et.setFocusableInTouchMode(false);
        action_et.setFocusableInTouchMode(false);
        reward_et.setFocusableInTouchMode(false);
        notes_et.setFocusableInTouchMode(false);
        cue_et.setClickable(false);
        action_et.setClickable(false);
        reward_et.setClickable(false);
        notes_et.setClickable(false);

        cue_et.clearFocus();
        action_et.clearFocus();
        reward_et.clearFocus();
        notes_et.clearFocus();



    }

    public void EditButtonHide() {
        edit_btn.setVisibility(View.GONE);

    }

    public void EditButtonShow() {
        edit_btn.setVisibility(View.VISIBLE);

    }

    public void SaveButtonHide() {
        save_btn.setVisibility(View.GONE);

    }

    public void SaveButtonShow() {
        save_btn.setVisibility(View.VISIBLE);

    }

    public void UpdateHabitDetails() {
        if (habitId != -1) {
            habit = db.getHabitById(habitId);

            if (habit != null) {

                Habitname_tv.setText(habit.getName());

                cue_et.setText(habit.getCue());
                action_et.setText(habit.getAction());
                reward_et.setText(habit.getReward());
                determineReflection();

                return;
            }

        }
         Toast.makeText(this, "Habit not found", Toast.LENGTH_SHORT).show();
    }



    private void updateFeelingSelection(int feeling) {
        // Update the class variable
        this.selectedFeeling = feeling;


        happy_face.setAlpha(feeling == 1 ? 1.0f : 0.3f);


        neutral_face.setAlpha(feeling == 2 ? 1.0f : 0.3f);


        sad_face.setAlpha(feeling == 3 ? 1.0f : 0.3f);
    }


}

