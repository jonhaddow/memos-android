package com.example.jon.memoapp;

public class Memo {

    // String content of memo
    private String name;

    // int representing flag of memo
    private int flag;

    /**
     * Constructor
     *
     * @param name content of memo
     * @param flag flag of memo
     */
    public Memo(String name, int flag) {
        this.name = name;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public int getFlag() {
        return flag;
    }


}
