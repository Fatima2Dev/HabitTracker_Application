package com.example.habittracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HabitDashboardActivity extends AppCompatActivity implements HabitAdapter.OnHabitInteractionListener {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String HABITS_KEY = "habits";

    private HabitAdapter habitAdapter;
    private List<Habit> habits;

    private RecyclerView habitsRecyclerView;
    private TextView emptyStateTextView;
    private TextView progressIndicatorTextView;

    private final ActivityResultLauncher<Intent> createHabitLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String habitName = result.getData().getStringExtra(CreateHabitActivity.EXTRA_HABIT_NAME);
                    String habitDescription = result.getData().getStringExtra(CreateHabitActivity.EXTRA_HABIT_DESCRIPTION);
                    String replacesHabit = result.getData().getStringExtra(CreateHabitActivity.EXTRA_REPLACES_HABIT);
                    String emoji = result.getData().getStringExtra(CreateHabitActivity.EXTRA_EMOJI);
                    int habitColor = result.getData().getIntExtra(CreateHabitActivity.EXTRA_HABIT_COLOR, Color.parseColor("#FF7043"));

                    if (habitName != null && !habitName.isEmpty()) {
                        Habit newHabit = new Habit(habitName, habitDescription, replacesHabit, emoji, 0, false, habitColor);
                        habits.add(0, newHabit);
                        habitAdapter.notifyItemInserted(0);
                        saveAndRefresh();
                    }
                }
            });

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
            createHabitLauncher.launch(intent);
        });

        loadHabits();

        habitAdapter = new HabitAdapter(habits, this);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        habitsRecyclerView.setAdapter(habitAdapter);

        updateDashboard();
    }

    private void saveAndRefresh() {
        saveHabits();
        updateDashboard();
    }

    private void updateDashboard() {
        updateProgressIndicator();
        updateEmptyState();
    }

    private void updateProgressIndicator() {
        long completedCount = 0;
        long activeHabitCount = 0;
        for (Habit habit : habits) {
            if (habit.getReplaces() == null || habit.getReplaces().isEmpty()) {
                activeHabitCount++;
                if (habit.isCompleted()) {
                    completedCount++;
                }
            }
        }
        progressIndicatorTextView.setText(activeHabitCount + "/" + completedCount);
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
        saveAndRefresh();
    }

    @Override
    public void onHabitLongPressed(int position) {
        Habit habitToDelete = habits.get(position);
        new AlertDialog.Builder(this)
                .setTitle("Delete Habit")
                .setMessage("Are you sure you want to delete '" + habitToDelete.getName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    habits.remove(position);
                    habitAdapter.notifyItemRemoved(position);
                    saveAndRefresh();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onHabitClicked(int position) {
        Habit habit = habits.get(position);
        Intent intent = new Intent(this, HabitDetailsActivity.class);
        intent.putExtra(HabitDetailsActivity.EXTRA_HABIT_NAME, habit.getName());
        intent.putExtra(HabitDetailsActivity.EXTRA_HABIT_DESCRIPTION, habit.getDescription());
        startActivity(intent);
    }

    private void loadHabits() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String json = sharedPreferences.getString(HABITS_KEY, null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Habit>>() {}.getType();
        habits = gson.fromJson(json, type);

        if (habits == null) {
            habits = new ArrayList<>();
        }
    }

    private void saveHabits() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(habits);
        editor.putString(HABITS_KEY, json);
        editor.apply();
    }
}