package com.example.habittracker;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HabitDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_HABIT_NAME = "extra_habit_name";
    public static final String EXTRA_HABIT_DESCRIPTION = "extra_habit_description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_details);

        String habitName = getIntent().getStringExtra(EXTRA_HABIT_NAME);
        String habitDescription = getIntent().getStringExtra(EXTRA_HABIT_DESCRIPTION);

        TextView nameTextView = findViewById(R.id.tv_habit_name_details);
        TextView descriptionTextView = findViewById(R.id.tv_habit_description_details);

        nameTextView.setText(habitName);
        descriptionTextView.setText(habitDescription);
    }
}