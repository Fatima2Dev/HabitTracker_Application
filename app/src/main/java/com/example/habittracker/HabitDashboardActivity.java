package com.example.habittracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HabitDashboardActivity extends AppCompatActivity implements HabitAdapter.OnHabitInteractionListener {

    private HabitAdapter habitAdapter;
    private List<Habit> habits = new ArrayList<>();
    private RecyclerView habitsRecyclerView;
    private TextView emptyStateTextView;
    private TextView progressIndicatorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_dashboard);

        habitsRecyclerView = findViewById(R.id.rv_habits);
        emptyStateTextView = findViewById(R.id.tv_empty_state);
        progressIndicatorTextView = findViewById(R.id.tv_progress_indicator);

        FloatingActionButton fab = findViewById(R.id.fab_add_habit);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(HabitDashboardActivity.this, CreateHabitActivity.class);
            startActivity(intent);
        });

        // Initial load
        loadHabits();
        habitAdapter = new HabitAdapter(habits, this);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        habitsRecyclerView.setAdapter(habitAdapter);

        updateDashboard();
    }








    @Override
    protected void onResume() {
        super.onResume();
        loadHabits();
        habitAdapter.notifyDataSetChanged();
        updateDashboard();
    }



    // In HabitDashboardActivity.java

    private void loadHabits() {
        DatabaseHelper db = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("currentUserId", -1);

        // ** THIS IS THE FIX **
        // 1. Clear the existing list that the adapter is watching.
        habits.clear();
        // 2. Add all the new items into that same list.
        habits.addAll(db.getAllHabits(userId));
    }


    private void updateDashboard() {
        updateProgressIndicator();
        updateEmptyState();
    }

    private void updateProgressIndicator() {
        long completedCount = 0;
        long totalHabitCount = habits.size();
        for (Habit habit : habits) {
            if (habit.isCompleted() == 1) {
                completedCount++;
            }
        }
        progressIndicatorTextView.setText(totalHabitCount + "/" + completedCount);
    }

    private void updateEmptyState() {
        if (habits.isEmpty()) {
            emptyStateTextView.setVisibility(View.VISIBLE);
            habitsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateTextView.setVisibility(View.GONE);
            habitsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHabitStateChanged() {
        loadHabits();
        habitAdapter.notifyDataSetChanged();
        updateDashboard();
    }

    @Override
    public void onHabitLongPressed(int position) {
        Habit habitToDelete = habits.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Delete Habit")
                .setMessage("Are you sure you want to delete '" + habitToDelete.getName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    DatabaseHelper db = new DatabaseHelper(this);
                    boolean deleted = db.deleteHabit(habitToDelete.getHabit_ID()); // pass ID

                    if (deleted) {
                        habits.remove(position);
                        habitAdapter.notifyItemRemoved(position);
                        updateDashboard();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onHabitClicked(int position) {
        Habit habit = habits.get(position);
        Intent intent = new Intent(this, Habit_Details.class);

        intent.putExtra(Habit_Details.EXTRA_HABIT_ID, habit.getHabit_ID());
        startActivity(intent);
    }
}
