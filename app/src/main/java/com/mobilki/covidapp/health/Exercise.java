package com.mobilki.covidapp.health;

public class Exercise {
    public Exercise(TypeExercise type, boolean goodShoulders, boolean goodBack, boolean goodWrists,
                    boolean goodKnees, boolean goodElbows, boolean goodHip, String name_pl,
                    String name_en, int minReps, int maxReps, String description_pl,
                    String description_en, String yt) {
        this.typeExercise = type;
        this.goodShoulders = goodShoulders;
        this.goodBack = goodBack;
        this.goodWrists = goodWrists;
        this.goodKnees = goodKnees;
        this.goodElbows = goodElbows;
        this.goodHip = goodHip;
        this.name_pl = name_pl;
        this.name_en = name_en;
        this.minReps = minReps;
        this.maxReps = maxReps;
        this.description_pl = description_pl;
        this.description_en = description_en;
        this.yt = yt;
    }

    public enum TypeExercise {
        UPPER,
        LOWER,
        CONDITION;

    }

    public TypeExercise getType() {
        return typeExercise;
    }

    public boolean isGoodShoulders() {
        return goodShoulders;
    }

    public boolean isGoodBack() {
        return goodBack;
    }

    public boolean isGoodWrists() {
        return goodWrists;
    }

    public boolean isGoodKnees() {
        return goodKnees;
    }

    public boolean isGoodElbows() {
        return goodElbows;
    }

    public boolean isGoodHip() {
        return goodHip;
    }

    public String getName_pl() {
        return name_pl;
    }

    public String getName_en() {
        return name_en;
    }

    public int getMinReps() {
        return minReps;
    }

    public int getMaxReps() {
        return maxReps;
    }

    public String getDescription_pl() {
        return description_pl;
    }

    public String getDescription_en() {
        return description_en;
    }

    public String getYt() {
        return yt;
    }

    TypeExercise typeExercise;
    boolean goodShoulders;
    boolean goodBack;
    boolean goodWrists;
    boolean goodKnees;
    boolean goodElbows;
    boolean goodHip;
    String name_pl;
    String name_en;
    int minReps;
    int maxReps;
    String description_pl;
    String description_en;
    String yt;
}
