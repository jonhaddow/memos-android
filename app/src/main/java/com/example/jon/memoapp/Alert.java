package com.example.jon.memoapp;

/**
 * This class holds the data relating to a Memo's Alert.
 */
public class Alert {

    // This is the memoId of the memo associated with the alert.
    private int memoId;

    // These fields hold the exact time the user will be alerted.
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public Alert(int year, int month, int day, int hour, int minute, int memoId) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.memoId = memoId;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getMemoId() {
        return memoId;
    }

}
