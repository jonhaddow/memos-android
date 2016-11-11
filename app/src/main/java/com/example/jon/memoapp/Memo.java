package com.example.jon.memoapp;

/**
 * This class holds the data relating to a Memo.
 */
public class Memo {

    // The memo id.
    private int id;

    // String content of memo.
    private final String name;

    // Int representing flag of memo (0-Normal, 1-Important, 2-Urgent).
    private final int flag;

    public Memo(String name, int flag) {
        this.name = name;
        this.flag = flag;
    }

    public Memo(int id, String name, int flag) {
        this.id = id;
        this.name = name;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFlag() {
        return flag;
    }
}
