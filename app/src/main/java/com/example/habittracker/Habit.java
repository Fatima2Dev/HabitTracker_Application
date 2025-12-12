package com.example.habittracker;

public class Habit {


    private int Habit_ID;
    private String name;
    private String Cue;
    private String Action;
    private String Reward;
    private String replaces;



    private String Habit_reflection;

    private int Current_Feeling;
    private String emoji;
    private int streak;

    private int color;

    private int userid;

    private int isCompleted;

    public Habit(String name, String description, String replaces, String emoji, int streak, int isCompleted, int color) {
        this.name = name;
        this.replaces = replaces;
        this.emoji = emoji;
        this.streak = streak;
        this.isCompleted = isCompleted;
        this.color = color;
    }
    public Habit(int Habit_ID, String name, String Cue, String Action, String Reward,
                 String replaces, String Habit_reflection,
                 int Current_Feeling, int streak, int color, int isCompleted,int userid) {

        this.Habit_ID = Habit_ID;
        this.name = name;
        this.Cue = Cue;
        this.Action = Action;
        this.Reward = Reward;
        this.replaces = replaces;
        this.Habit_reflection = Habit_reflection;
        this.Current_Feeling = Current_Feeling;
        this.streak = streak;
        this.color = color;
        this.isCompleted = isCompleted;
        this.userid = userid;
    }




    public int getHabit_ID() {
        return Habit_ID;
    }

    public String getName() {
        return name;
    }
    public String getCue() {
        return Cue;
    }

    public String getAction() {
        return Action;
    }

    public String getReward() {
        return Reward;
    }


    public String getHabit_reflection() {
        return Habit_reflection;
    }

    public int getCurrent_Feeling() {
        return Current_Feeling;
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

    public int isCompleted() {
        return isCompleted;
    }

    public int getColor() {
        return color;
    }

    public void setCompleted(int completed) {
        isCompleted = completed;
    }
}