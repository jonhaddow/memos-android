package com.example.jon.memoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Define constants
    public static final String INTENT_EXTRA_POSITION = "position";
    public static final String INTENT_EXTRA_NAME = "flag";
    public static final String INTENT_EXTRA_FLAG = "name";
    public static final int FLAG_NORMAL = 0;
    public static final int FLAG_IMPORTANT = 1;
    public static final int FLAG_URGENT = 2;

    // Database helper
    private DbHelper mDbHelper;

    // Adapter for the memoList
    private MemoListAdapter mMemoAdapter;

    // Array list of Memos
    private ArrayList<Memo> mMemos;

    // User Interface references
    private ListView mMemoListview;
    private EditText etAddMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get singleton database instance
        mDbHelper = DbHelper.getInstance(this);

        // Get the memo list view
        mMemoListview = (ListView) findViewById(R.id.lvMemoList);

        // Populate memo list from database
        mMemos = mDbHelper.getMemos();

        // Construct memo list adapter to display list
        mMemoAdapter = new MemoListAdapter(this, 0, mMemos);
        mMemoListview.setAdapter(mMemoAdapter);

        // Get add memo text box
        etAddMemo = (EditText) findViewById(R.id.etAddMemo);
        etAddMemo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                // If enter/send key is selected...
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // add memo
                    addMemo();
                    handled = true;
                }
                return handled;
            }
        });

        // If floating action button is clicked, add memo to list
        FloatingActionButton fabAddMemo = (FloatingActionButton) findViewById(R.id.fabAddMemo);
        fabAddMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMemo();
            }
        });

    }

    /**
     * This method adds a memo to the memo list
     */
    private void addMemo() {

        // Get memo from text box
        String memoName = etAddMemo.getText().toString();
        mMemos.add(new Memo(memoName, MainActivity.FLAG_NORMAL));

        // Clear text box
        etAddMemo.setText("");

        // Update list view
        mMemoAdapter.notifyDataSetChanged();

        // Send updated list to database
        mDbHelper.storeMemos(mMemos);

    }

    /**
     * Called when activity is restarted. It updates the list view with memos
     */
    @Override
    protected void onRestart() {

        // Populate memo list with updated memos from database
        mMemos = mDbHelper.getMemos();
        mMemoAdapter = new MemoListAdapter(this, 0, mMemos);
        mMemoListview.setAdapter(mMemoAdapter);

        super.onRestart();
    }

    /**
     * Deletes the memo at the given position
     *
     * @param position Position of memo
     */
    public void deleteMemo(final int position) {

        // Confirm removal of memo
        new AlertDialog.Builder(this)
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Remove memo from list and send new list to database
                        mMemos.remove(position);
                        mMemoAdapter.notifyDataSetChanged();
                        mDbHelper.storeMemos(mMemos);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();


    }

    /**
     * Passes the selected memo properties to the EditMemo activity
     *
     * @param position Position of memo to be edited
     */
    public void editMemo(final int position) {

        // Get the properties of the selected memo
        String memoName = mMemos.get(position).getName();
        int memoFlag = mMemos.get(position).getFlag();

        // Send properties as intent extras
        Intent intent = new Intent(this, EditMemoActivity.class);
        intent.putExtra(INTENT_EXTRA_POSITION, position);
        intent.putExtra(INTENT_EXTRA_FLAG, memoName);
        intent.putExtra(INTENT_EXTRA_NAME, memoFlag);

        // Start EditMemo Activity
        startActivity(intent);
    }
}
