package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Habit_Details extends AppCompatActivity {

    TextView Habitname_tv;


    private DatabaseHelper db;

    public static final String EXTRA_HABIT_ID = "extra_habit_id";


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

        Intent intent = getIntent();
        int habitId = intent.getIntExtra(EXTRA_HABIT_ID, -1);
        if (habitId != -1) {
            Habit habit = db.getHabitById(habitId);

            if (habit != null) {

            }

        ImageView backArrow = findViewById(R.id.backArrow);
        Habitname_tv =(TextView)findViewById(R.id.habit_name_tv);
        Habitname_tv.setText(habit_name);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // closes this activity
            }
        });
}
}}