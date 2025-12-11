package com.example.habittracker;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    public interface OnHabitInteractionListener {
        void onHabitStateChanged();
        void onHabitLongPressed(int position);
        void onHabitClicked(int position);
    }

    private final List<Habit> habits;
    private final OnHabitInteractionListener listener;

    public HabitAdapter(List<Habit> habits, OnHabitInteractionListener listener) {
        this.habits = habits;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        holder.bind(habits.get(position));
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    class HabitViewHolder extends RecyclerView.ViewHolder {
        private final View habitColorView;
        private final TextView emojiTextView;
        private final TextView nameTextView;
        private final TextView descriptionTextView;
        private final TextView replacesTextView;
        private final LinearLayout streakContainer;
        private final TextView streakTextView;
        private final CheckBox completedCheckBox;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            habitColorView = itemView.findViewById(R.id.view_habit_color);
            emojiTextView = itemView.findViewById(R.id.tv_emoji);
            nameTextView = itemView.findViewById(R.id.tv_habit_name);
            descriptionTextView = itemView.findViewById(R.id.tv_habit_description);
            replacesTextView = itemView.findViewById(R.id.tv_replaces_habit);
            streakContainer = itemView.findViewById(R.id.streak_container);
            streakTextView = itemView.findViewById(R.id.tv_streak);
            completedCheckBox = itemView.findViewById(R.id.cb_habit_completed);

            completedCheckBox.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    Habit habit = habits.get(position);
                    if(completedCheckBox.isChecked()){
                    habit.setCompleted(1);}
                    updateStrikeThrough(habit.isCompleted()==1);
                    listener.onHabitStateChanged();
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onHabitLongPressed(position);
                    return true;
                }
                return false;
            });

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onHabitClicked(position);
                }
            });
        }

        void bind(final Habit habit) {
            habitColorView.setBackgroundColor(habit.getColor());
            nameTextView.setText(habit.getName());
            completedCheckBox.setChecked(habit.isCompleted()==1);
            updateStrikeThrough(habit.isCompleted()==1);

            emojiTextView.setVisibility(habit.getEmoji() != null && !habit.getEmoji().isEmpty() ? View.VISIBLE : View.GONE);
            emojiTextView.setText(habit.getEmoji());

            //descriptionTextView.setVisibility(habit.getDescription() != null && !habit.getDescription().isEmpty() ? View.VISIBLE : View.GONE);
            //descriptionTextView.setText(habit.getDescription());

            boolean isReplaced = habit.getReplaces() != null && !habit.getReplaces().isEmpty();
            replacesTextView.setVisibility(isReplaced ? View.VISIBLE : View.GONE);
            if (isReplaced) replacesTextView.setText("Replaces: " + habit.getReplaces());

            boolean showStreak = !isReplaced && habit.getStreak() > 0;
            streakContainer.setVisibility(showStreak ? View.VISIBLE : View.GONE);
            if (showStreak) streakTextView.setText(habit.getStreak() + "-day streak");

            ((CardView) itemView).setCardBackgroundColor(isReplaced ? Color.parseColor("#1A66BB6A") : Color.TRANSPARENT);
        }

        private void updateStrikeThrough(boolean isCompleted) {
            if (isCompleted) {
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    }
}