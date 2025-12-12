package com.example.habittracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateHabitActivity extends AppCompatActivity {

    public static final String EXTRA_HABIT_NAME = "com.example.habittracker.EXTRA_HABIT_NAME";
    public static final String EXTRA_HABIT_DESCRIPTION = "com.example.habittracker.EXTRA_HABIT_DESCRIPTION";
    public static final String EXTRA_REPLACES_HABIT = "com.example.habittracker.EXTRA_REPLACES_HABIT";
    public static final String EXTRA_EMOJI = "com.example.habittracker.EXTRA_EMOJI";
    public static final String EXTRA_HABIT_COLOR = "com.example.habittracker.EXTRA_HABIT_COLOR";

    DatabaseHelper db = new DatabaseHelper(this);





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);

        ImageButton backButton = findViewById(R.id.btn_back);
        EditText emojiEditText = findViewById(R.id.et_emoji);
        EditText habitNameEditText = findViewById(R.id.et_habit_name);
        EditText habitActionEditText = findViewById(R.id.et_habit_action);
        EditText habitCueEditText = findViewById(R.id.et_habit_cue);
        EditText habitRewardEditText = findViewById(R.id.et_habit_reward);
        EditText replacesHabitEditText = findViewById(R.id.et_habit_replaces);
        RadioGroup colorRadioGroup = findViewById(R.id.rg_habit_color);
        Button createHabitButton = findViewById(R.id.btn_create_habit);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Simply close the activity
            }
        });

        createHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String habitName = habitNameEditText.getText().toString();
                String habitCue = habitCueEditText.getText().toString();
                String habitAction = habitActionEditText.getText().toString();
                String habitReward = habitRewardEditText.getText().toString();
                String replacesHabit = replacesHabitEditText.getText().toString();
                String emoji = emojiEditText.getText().toString();
                int selectedColor = getSelectedColor(colorRadioGroup.getCheckedRadioButtonId());

                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                int userId = prefs.getInt("userId", -1);

                boolean result = db.addHabit(habitName,habitCue,habitAction,habitReward,replacesHabit,"",
                        0,0,selectedColor,userId);
                
                if (result==true){

                    Intent dashboard = new Intent(getApplicationContext(),HabitDashboardActivity.class);
                    startActivity(dashboard);
                    finish();}
                    else {
                    Toast.makeText(CreateHabitActivity.this,"Failed to create habit",Toast.LENGTH_SHORT).show();
                    }

                }
                



        });
    }

    private int getSelectedColor(int checkedId) {
        if (checkedId == R.id.rb_color_1) {
            return Color.parseColor("#FF7043"); // A shade of red
        } else if (checkedId == R.id.rb_color_2) {
            return Color.parseColor("#FFCA28"); // A shade of orange
        } else if (checkedId == R.id.rb_color_3) {
            return Color.parseColor("#66BB6A"); // A shade of green
        } else if (checkedId == R.id.rb_color_4) {
            return Color.parseColor("#42A5F5"); // A shade of blue
        }
        return Color.parseColor("#FF7043"); // Default color
    }
}