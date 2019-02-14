package com.functional;

import java.util.NavigableMap;
import java.util.TreeMap;

public class Student {

    private static final NavigableMap<Integer, String> gradeLetters = new TreeMap<>();

    static{
        gradeLetters.put(90, "A");
        gradeLetters.put(80, "B");
        gradeLetters.put(70, "C");
        gradeLetters.put(60, "D");
        gradeLetters.put(50, "E");
        gradeLetters.put(0, "F");
    }

    private String name;
    private int score;

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getLetterGrade() {
        return gradeLetters.floorEntry(score).getValue();
    }

    public Student (String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return name + ", " +score +"%, grade is " +getLetterGrade();
    }
}
