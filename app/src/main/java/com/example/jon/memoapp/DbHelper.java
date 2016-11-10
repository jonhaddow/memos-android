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
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "MemosDatabase";

    /**
     * Inner class that defines the memo table string constants.
     * BaseColumns superclass contains _ID string constant.
     */
    private static class MemoTable implements BaseColumns {
        private static final String TABLE_NAME = "memos";
        private static final String COLUMN_NAME_FLAG = "flag";
        private static final String COLUMN_NAME_TITLE = "title";
    }

    /**
     * Inner class that defines the alert table string constants.
     * BaseColumns superclass contains _ID string constant.
     */
    private static class AlertTable implements BaseColumns {
        private static final String TABLE_NAME = "alerts";
        private static final String COLUMN_NAME_YEAR = "year";
        private static final String COLUMN_NAME_MONTH = "month";
        private static final String COLUMN_NAME_DAY = "day";
        private static final String COLUMN_NAME_HOUR = "hour";
        private static final String COLUMN_NAME_MINUTE = "minute";
        private static final String COLUMN_NAME_MEMO_ID = "memoId";
    }

    // SQL Query to create a memo table
    private static final String SQL_CREATE_MEMO_TABLE =
            "CREATE TABLE " + MemoTable.TABLE_NAME + " (" +
                    MemoTable._ID + " INTEGER PRIMARY KEY," +
                    MemoTable.COLUMN_NAME_TITLE + " TEXT NOT NULL," +
                    MemoTable.COLUMN_NAME_FLAG + " INTEGER NOT NULL )";

    // SQL Query to create a alert table
    private static final String SQL_CREATE_ALERT_TABLE =
            "CREATE TABLE " + AlertTable.TABLE_NAME + " (" +
                    AlertTable._ID + " INTEGER PRIMARY KEY," +
                    AlertTable.COLUMN_NAME_YEAR + " INTEGER NOT NULL," +
                    AlertTable.COLUMN_NAME_MONTH + " INTEGER NOT NULL," +
                    AlertTable.COLUMN_NAME_DAY + " INTEGER NOT NULL," +
                    AlertTable.COLUMN_NAME_HOUR + " INTEGER NOT NULL," +
                    AlertTable.COLUMN_NAME_MINUTE + " INTEGER NOT NULL," +
                    AlertTable.COLUMN_NAME_MEMO_ID + " INTEGER NOT NULL," +
                    "FOREIGN KEY(" + AlertTable.COLUMN_NAME_MEMO_ID + ") REFERENCES " + MemoTable.TABLE_NAME + " (" + MemoTable._ID + "))";

    // SQL query to delete memo table
    private static final String SQL_DELETE_MEMO_TABLE =
            "DROP TABLE IF EXISTS " + MemoTable.TABLE_NAME;

    // SQL query to delete alert table
    private static final String SQL_DELETE_ALERT_TABLE =
            "DROP TABLE IF EXISTS " + AlertTable.TABLE_NAME;

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
     * Runs a query to create tables
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_MEMO_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ALERT_TABLE);
    }

    /**
     * Called when the database version is changed
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_MEMO_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_ALERT_TABLE);
        onCreate(sqLiteDatabase);
    }

    /**
     * Gets all rows from memo tables and return an array list containing memos
     */
    public ArrayList<Memo> getMemos() {

        Cursor cursor = getReadableDatabase().query(
                MemoTable.TABLE_NAME, // Table name
                null, // all columns
                null, // no criteria
                null, null, null, null
        );

        // Convert Cursor of rows to ArrayList of Memos
        ArrayList<Memo> memos = new ArrayList<>();
        while (cursor.moveToNext()) {
            memos.add(new Memo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
        }
        cursor.close();

        return memos;
    }

    /**
     * Add a memo to the memos table
     */
    public void addMemo(Memo memo) {

        // Gets the database in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        // Pair columns and values
        values.put(MemoTable.COLUMN_NAME_TITLE, memo.getName());
        values.put(MemoTable.COLUMN_NAME_FLAG, memo.getFlag());

        // Insert into table
        db.insert(MemoTable.TABLE_NAME, null, values);
    }

    /**
     * Updates a memo in the memos table
     */
    public void updateMemo(Memo memo) {

        // Gets the database in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        // Pair columns and values
        values.put(MemoTable.COLUMN_NAME_TITLE, memo.getName());
        values.put(MemoTable.COLUMN_NAME_FLAG, memo.getFlag());

        // Insert into table
        db.update(MemoTable.TABLE_NAME, values, MemoTable._ID + "=" + memo.getId(), null);
    }

    /**
     * Remove a memo from the memos table
     */
    public void removeMemo(int memoId) {

        // Gets the database in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Delete row containing memoId
        db.delete(MemoTable.TABLE_NAME, MemoTable._ID + "=" + memoId, null);
    }

    /**
     * Add alert to alerts tables
     *
     * @param alert Alert to be added
     */
    public void addAlert(Alert alert) {

        // Gets the database in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(AlertTable.COLUMN_NAME_YEAR, alert.getYear());
        values.put(AlertTable.COLUMN_NAME_MONTH, alert.getMonth());
        values.put(AlertTable.COLUMN_NAME_DAY, alert.getDay());
        values.put(AlertTable.COLUMN_NAME_HOUR, alert.getHour());
        values.put(AlertTable.COLUMN_NAME_MINUTE, alert.getMinute());
        values.put(AlertTable.COLUMN_NAME_MEMO_ID, alert.getMemoId());

        // Insert the new row
        db.insert(DbHelper.AlertTable.TABLE_NAME, null, values);
    }

    /**
     * Remove alert from alerts table
     *
     * @param memoId Memo Id of the alert to remove
     */
    public void removeAlert(int memoId) {

        // Gets the database in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Delete row containing memoId
        int rowsAffected = db.delete(AlertTable.TABLE_NAME, AlertTable.COLUMN_NAME_MEMO_ID + " = " + memoId, null);

        System.out.println("alert removed, rows affected: " + rowsAffected);
    }

    /**
     * Get alerts matching the memo id
     *
     * @param memoId Memo id to be compared to each row in alerts table
     * @return Alert object matching memo id.
     */
    public Alert getAlert(int memoId) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                AlertTable.TABLE_NAME,
                null, // All columns
                AlertTable.COLUMN_NAME_MEMO_ID + " = " + memoId,
                null, null, null, null
        );

        Alert alert;
        cursor.moveToNext();
        if (cursor.getCount() == 0) {
            alert = null;
        } else {
            alert = new Alert(
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    memoId
            );
        }
        cursor.close();
        return alert;
    }
}
