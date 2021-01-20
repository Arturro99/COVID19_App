package com.mobilki.covidapp.health;

public class Exercise {
    @Override
    public String toString() {
        return "Exercise{" +
                "type=" + typeExercise +
                ", goodShoulders=" + goodShoulders +
                ", goodBack=" + goodBack +
                ", goodWrists=" + goodWrists +
                ", goodKnees=" + goodKnees +
                ", goodElbows=" + goodElbows +
                ", goodHip=" + goodHip +
                ", name_pl='" + name_pl + '\'' +
                ", name_en='" + name_en + '\'' +
                ", minReps=" + minReps +
                ", maxReps=" + maxReps +
                ", description_pl='" + description_pl + '\'' +
                ", description_en='" + description_en + '\'' +
                ", yt='" + yt + '\'' +
                '}';
    }


    public Exercise(TypeExercise type, boolean goodShoulders, boolean goodBack, boolean goodWrists, boolean goodKnees, boolean goodElbows, boolean goodHip, String name_pl, String name_en, int minReps, int maxReps, String description_pl, String description_en, String yt) {
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

    public void setType(TypeExercise type) {
        this.typeExercise = type;
    }

    public void setGoodShoulders(boolean goodShoulders) {
        this.goodShoulders = goodShoulders;
    }

    public void setGoodBack(boolean goodBack) {
        this.goodBack = goodBack;
    }

    public void setGoodWrists(boolean goodWrists) {
        this.goodWrists = goodWrists;
    }

    public void setGoodKnees(boolean goodKnees) {
        this.goodKnees = goodKnees;
    }

    public void setGoodElbows(boolean goodElbows) {
        this.goodElbows = goodElbows;
    }

    public void setGoodHip(boolean goodHip) {
        this.goodHip = goodHip;
    }

    public void setName_pl(String name_pl) {
        this.name_pl = name_pl;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public void setMinReps(int minReps) {
        this.minReps = minReps;
    }

    public void setMaxReps(int maxReps) {
        this.maxReps = maxReps;
    }

    public void setDescription_pl(String description_pl) {
        this.description_pl = description_pl;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public void setYt(String yt) {
        this.yt = yt;
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
