package com.example.jon.memoapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Database helper instance
    private DbHelper mDbHelper;

    // Memo list adapter
    private MemoListAdapter mMemoAdapter;
    private ArrayList<String> mMemoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the memo listview
        ListView mMemoListview = (ListView) findViewById(R.id.lvMemoList);

        // Set up an ArrayList of memos
        mMemoList = new ArrayList<>();

        // Get dbhelper class
        mDbHelper = DbHelper.getInstance(this);

        // Add all memos from database to listview
        mMemoList.addAll(mDbHelper.getMemos());

        // Construct memo list adapter to display list
        mMemoAdapter = new MemoListAdapter(this, 0, mMemoList);
        mMemoListview.setAdapter(mMemoAdapter);


        FloatingActionButton fabAddMemo = (FloatingActionButton) findViewById(R.id.fabAddMemo);
        fabAddMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get memo from textbox
                EditText memoTextbox = ((EditText) findViewById(R.id.etMemoContent));
                mMemoList.add(memoTextbox.getText().toString());

                // Clear textbox
                memoTextbox.setText("");

                // Update listview
                mMemoAdapter.notifyDataSetChanged();

                // Send updated list to database
                mDbHelper.storeMemos(mMemoList);
            }
        });

    }
}
