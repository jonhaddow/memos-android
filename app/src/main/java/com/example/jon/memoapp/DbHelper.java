package com.example.jon.memoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * This is a singleton class that manages all interactions with the database.
 */
public class DbHelper extends SQLiteOpenHelper {

    // Database string constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MemosDatabase";

    /**
     * Inner class that defines the memo table string constants.
     * BaseColumns superclass contains _ID string constant.
     */
    private static class MemoTable implements BaseColumns {
        private static final String COLUMN_NAME_FLAG = "flag";
        private static final String COLUMN_NAME_CONTENT = "content";
        private static final String TABLE_NAME = "memos";
    }

    // SQL Query to create a table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MemoTable.TABLE_NAME + " (" +
                    MemoTable._ID + " INTEGER PRIMARY KEY," +
                    MemoTable.COLUMN_NAME_CONTENT + " TEXT," +
                    MemoTable.COLUMN_NAME_FLAG + " TEXT" + " )";

    // SQL query to delete a table
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MemoTable.TABLE_NAME;

    // The class instance.
    private static DbHelper dbHelperInstance;

    /**
     * Private constructor to stop multiple database instances
     */
    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is the public method to get the access to the database instance.
     * It returns a singleton instance.
     */
    public static synchronized DbHelper getInstance(Context context) {

        // Only instantiate once
        if (dbHelperInstance == null) {
            dbHelperInstance = new DbHelper(context.getApplicationContext());
        }
        return dbHelperInstance;
    }

    /**
     * Runs a query to create a memos table
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * Called when the database version is changed
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    /**
     * Gets all rows from memos tables and return a arraylist of strings containing memos
     */
    public ArrayList<String> getMemos() {

        String[] columns = {MemoTable.COLUMN_NAME_CONTENT};

        Cursor cursor = getReadableDatabase().query(
                DbHelper.MemoTable.TABLE_NAME, // Table name
                columns, // Columns wanted
                null,
                null,
                null,
                null,
                null
        );

        // Convert Cursor of rows to ArrayList of strings
        ArrayList<String> memos = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                memos.add(cursor.getString(0));
            }
            cursor.close();
        }
        return memos;
    }

    /**
     * For every memo in the listView, a row is added to the memos tables
     */
    public void storeMemos(ArrayList<String> memoList) {

        // Gets the database in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Delete all rows in table
        db.delete(DbHelper.MemoTable.TABLE_NAME, null, null);

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        // For each memo in the listview, add a row to the table.
        for (int i = 0; i < memoList.size(); i++) {
            values.put(MemoTable.COLUMN_NAME_CONTENT, memoList.get(i));
            values.put(MemoTable.COLUMN_NAME_FLAG, "N");

            // Insert the new row
            db.insert(DbHelper.MemoTable.TABLE_NAME, null, values);
        }

    }
}
