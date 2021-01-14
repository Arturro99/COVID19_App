package com.mobilki.covidapp.health;

public class Exercise {
    public Exercise(Type type, boolean goodShoulders, boolean goodBack, boolean goodWrists, boolean goodKnees, boolean goodElbows, boolean goodHip, String name_pl, String name_en, int minReps, int maxReps, String description_pl, String description_en, String yt) {
        this.type = type;
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

    public enum Type {
        UPPER,
        LOWER,
        EFFICIENCY
    }
    Type type;
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
