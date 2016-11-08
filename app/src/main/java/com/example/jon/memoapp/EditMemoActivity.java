package com.example.jon.memoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.ArrayList;

public class EditMemoActivity extends AppCompatActivity {

    private int memoFlag;
    private int memoPosition;
    private String memoName;
    private EditText etMemoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        Intent intent = getIntent();

        memoPosition = intent.getIntExtra(MainActivity.MEMO_POSITION, 0);
        memoName = intent.getStringExtra(MainActivity.MEMO_NAME);
        memoFlag = intent.getIntExtra(MainActivity.MEMO_FLAG, 0);

        etMemoName = (EditText) findViewById(R.id.etMemoName);
        etMemoName.setText(memoName);

        switch (memoFlag) {
            case MainActivity.FLAG_NORMAL:
                ((RadioButton) findViewById(R.id.rbNormal)).toggle();
                break;
            case MainActivity.FLAG_IMPORTANT:
                ((RadioButton) findViewById(R.id.rbImportant)).toggle();
                break;
            case MainActivity.FLAG_URGENT:
                ((RadioButton) findViewById(R.id.rbUrgent)).toggle();
                break;
        }

    }

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.rbNormal:
                memoFlag = MainActivity.FLAG_NORMAL;
                break;
            case R.id.rbImportant:
                memoFlag = MainActivity.FLAG_IMPORTANT;
                break;
            case R.id.rbUrgent:
                memoFlag = MainActivity.FLAG_URGENT;
                break;
        }
    }

    @Override
    protected void onPause() {
        storeMemos();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        storeMemos();
        super.onBackPressed();
    }

    private void storeMemos() {

        DbHelper dbHelper = DbHelper.getInstance(this);

        // Get current list of memos
        ArrayList<Memo> memos = dbHelper.getMemos();

        // Get new memo name
        memoName = etMemoName.getText().toString();

        // Override memo with new data
        memos.set(memoPosition, new Memo(memoName,memoFlag));

        // Send back to database
        dbHelper.storeMemos(memos);

    }
}
