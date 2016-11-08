package com.example.jon.memoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String MEMO_POSITION = "position";
    public static final String MEMO_FLAG = "flag";
    public static final String MEMO_NAME = "name";
    public static final int FLAG_NORMAL = 0;
    public static final int FLAG_IMPORTANT = 1;
    public static final int FLAG_URGENT = 2;


    // Database helper instance
    private DbHelper mDbHelper;

    // Memo list adapter
    private MemoListAdapter mMemoAdapter;
    private ArrayList<Memo> mMemos;
    private ListView mMemoListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = DbHelper.getInstance(this);

        // Get the memo listview
        mMemoListview = (ListView) findViewById(R.id.lvMemoList);

        // populate memo list from database
        mMemos = mDbHelper.getMemos();

        // Construct memo list adapter to display list
        mMemoAdapter = new MemoListAdapter(this, 0, mMemos);
        mMemoListview.setAdapter(mMemoAdapter);

        EditText etAddMemo = (EditText) findViewById(R.id.etAddMemo);
        etAddMemo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    addMemo();
                    handled = true;
                }
                return handled;
            }
        });

        FloatingActionButton fabAddMemo = (FloatingActionButton) findViewById(R.id.fabAddMemo);
        fabAddMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMemo();
            }
        });

    }

    private void addMemo() {

        // Get memo from textbox
        EditText memoTextbox = ((EditText) findViewById(R.id.etAddMemo));
        String memoName = memoTextbox.getText().toString();
        mMemos.add(new Memo(memoName, MainActivity.FLAG_NORMAL));

        // Clear textbox
        memoTextbox.setText("");

        // Update listview
        mMemoAdapter.notifyDataSetChanged();

        // Send updated list to database
        mDbHelper.storeMemos(mMemos);

    }

    @Override
    protected void onRestart() {

        // Populate memo list with updated memos from database
        mMemos = mDbHelper.getMemos();
        mMemoAdapter = new MemoListAdapter(this, 0, mMemos);
        mMemoListview.setAdapter(mMemoAdapter);

        super.onRestart();
    }

    public void deleteMemo(int position) {
        mMemos.remove(position);
        mMemoAdapter.notifyDataSetChanged();
        mDbHelper.storeMemos(mMemos);
    }

    public void editMemo(final int position) {

        String memoName = mMemos.get(position).getName();
        int memoFlag = mMemos.get(position).getFlag();

        Intent intent = new Intent(this, EditMemoActivity.class);
        intent.putExtra(MEMO_POSITION, position);
        intent.putExtra(MEMO_NAME, memoName);
        intent.putExtra(MEMO_FLAG, memoFlag);

        startActivity(intent);
    }
}
