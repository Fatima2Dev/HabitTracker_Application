package com.example.habittracker;

public class Habit {
    private String name;
    private String description;
    private String replaces;
    private String emoji;
    private int streak;
    private boolean isCompleted;
    private int color;

    public Habit(String name, String description, String replaces, String emoji, int streak, boolean isCompleted, int color) {
        this.name = name;
        this.description = description;
        this.replaces = replaces;
        this.emoji = emoji;
        this.streak = streak;
        this.isCompleted = isCompleted;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getReplaces() {
        return replaces;
    }

    public String getEmoji() {
        return emoji;
    }

    public int getStreak() {
        return streak;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public int getColor() {
        return color;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}