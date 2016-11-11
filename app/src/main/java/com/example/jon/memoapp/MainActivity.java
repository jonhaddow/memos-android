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

/**
 * This is the main activity that is started when app first launches.
 */
public class MainActivity extends AppCompatActivity {

    // Define constants.
    public static final String INTENT_EXTRA_ID = "id";
    public static final String INTENT_EXTRA_FLAG = "flag";
    public static final String INTENT_EXTRA_NAME = "name";
    public static final int FLAG_NORMAL = 0;
    public static final int FLAG_IMPORTANT = 1;
    public static final int FLAG_URGENT = 2;

    // Singleton database helper.
    private DbHelper mDbHelper;

    // Array list of Memos.
    private ArrayList<Memo> mMemos;

    // UI references.
    private EditText etAddMemo;
    private MemoListAdapter mMemoAdapter;

    /**
     * Called when activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get singleton database instance.
        mDbHelper = DbHelper.getInstance(this);

        // Get the UI elements.
        ListView memoListView = (ListView) findViewById(R.id.lvMemoList);
        etAddMemo = (EditText) findViewById(R.id.etAddMemo);
        FloatingActionButton fabAddMemo = (FloatingActionButton) findViewById(R.id.fabAddMemo);

        mMemos = mDbHelper.getMemos();

        // Create new adapter to store each list item.
        mMemoAdapter = new MemoListAdapter(this, 0, mMemos);

        // Set adapter to list view.
        memoListView.setAdapter(mMemoAdapter);

        // Fill list view with memos.
        populateListView();


        // If list view is empty, display text instead.
        memoListView.setEmptyView(findViewById(android.R.id.empty));

        // Set listener for add memo edit box so send key is handled correctly.
        etAddMemo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                // If enter/send key is selected...
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // Add memo to list.
                    addMemo();
                    handled = true;
                }
                return handled;
            }
        });

        // If floating action button is clicked, add memo to list.
        fabAddMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMemo();
            }
        });
    }

    /**
     * This method adds a memo to the memo list.
     */
    private void addMemo() {

        // Get memo name from text box.
        String memoName = etAddMemo.getText().toString();

        // Check that string is not empty.
        if (!memoName.equals("")) {

            // Create new Memo object.
            Memo memo = new Memo(memoName, MainActivity.FLAG_NORMAL);

            // Add memo to memos table in database.
            mDbHelper.addMemo(memo);

            // Clear text box.
            etAddMemo.setText("");

            // Update list.
            populateListView();
        }
    }

    /**
     * Called when activity is resumed. It updates the list view with memos.
     */
    @Override
    protected void onRestart() {
        populateListView();
        super.onRestart();
    }

    /**
     * Re-populate listView with memos from database.
     */
    private void populateListView() {

        // Get array list of memos from the memos table.
        mMemos.clear();
        mMemos.addAll(mDbHelper.getMemos());

        mMemoAdapter.notifyDataSetChanged();
    }

    /**
     * Delete the memo at the given position.
     *
     * @param position Position of memo
     */
    public void deleteMemo(final int position) {

        // Confirm removal of memo with user.
        new AlertDialog.Builder(this)
                .setMessage(R.string.delete_memo_message)
                .setPositiveButton(R.string.positive_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Get MemoId.
                        int memoId = mMemos.get(position).getId();

                        // Remove memo and associated alerts from database.
                        mDbHelper.removeMemo(memoId);
                        mDbHelper.removeAlert(memoId);

                        // Update list view.
                        populateListView();
                    }
                })
                .setNegativeButton(R.string.negative_cancel, null)
                .show();
    }

    /**
     * Passes the selected memo properties to the EditMemo activity to be edited.
     *
     * @param position Position of memo to be edited.
     */
    public void editMemo(final int position) {

        // Get the properties of the selected memo.
        int memoId = mMemos.get(position).getId();
        String memoName = mMemos.get(position).getName();
        int memoFlag = mMemos.get(position).getFlag();

        // Send properties as intent extras.
        Intent intent = new Intent(this, EditMemoActivity.class);
        intent.putExtra(INTENT_EXTRA_ID, memoId);
        intent.putExtra(INTENT_EXTRA_NAME, memoName);
        intent.putExtra(INTENT_EXTRA_FLAG, memoFlag);

        // Start EditMemo Activity.
        startActivity(intent);
    }
}
