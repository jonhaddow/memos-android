package com.example.jon.memoapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the memo listview
        ListView memoList = (ListView) findViewById(R.id.lvMemoList);

        // Set up an ArrayList of memos
        final ArrayList<String> data = new ArrayList<>();

        // Construct memo list adapter to display list
        final MemoListAdapter memoAdapter = new MemoListAdapter(this, 0, data);
        memoList.setAdapter(memoAdapter);

        FloatingActionButton fabAddMemo = (FloatingActionButton) findViewById(R.id.fabAddMemo);
        fabAddMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText memoTextbox = ((EditText)findViewById(R.id.etMemoContent));
                data.add(memoTextbox.getText().toString());
                memoAdapter.notifyDataSetChanged();
            }
        });

    }
}
