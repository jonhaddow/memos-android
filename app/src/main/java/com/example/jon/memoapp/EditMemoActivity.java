package com.example.jon.memoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.ArrayList;

public class EditMemoActivity extends AppCompatActivity {

    // Properties of current memo being edited
    private String memoName;
    private int memoFlag;
    private int memoPosition;

    // User interface references
    private EditText etMemoName;
    private Button btnSetAlert;

    /**
     * Called when activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        // Get intent and memo properties from intent extras.
        Intent intent = getIntent();
        memoPosition = intent.getIntExtra(MainActivity.INTENT_EXTRA_POSITION, 0);
        memoName = intent.getStringExtra(MainActivity.INTENT_EXTRA_FLAG);
        memoFlag = intent.getIntExtra(MainActivity.INTENT_EXTRA_NAME, 0);

        // Get reference to the memo text box and set text as memo name
        etMemoName = (EditText) findViewById(R.id.etMemoName);
        etMemoName.setText(memoName);

        // Get setAlert button reference
        btnSetAlert = (Button) findViewById(R.id.btnSetAlert);
        btnSetAlert.setVisibility(View.INVISIBLE);

        // Move cursor to the end of memo name
        etMemoName.setSelection(etMemoName.getText().length());

        // Set radio button to the corresponding flag of memo
        switch (memoFlag) {
            case MainActivity.FLAG_NORMAL:
                ((RadioButton) findViewById(R.id.rbNormal)).toggle();
                break;
            case MainActivity.FLAG_IMPORTANT:
                ((RadioButton) findViewById(R.id.rbImportant)).toggle();
                break;
            case MainActivity.FLAG_URGENT:
                ((RadioButton) findViewById(R.id.rbUrgent)).toggle();
                btnSetAlert.setVisibility(View.VISIBLE);
                break;
        }

    }

    /**
     * This is called by the each radio button in the xml.
     * @param view The radio button calling the function
     */
    public void onRadioButtonClicked(View view) {

        btnSetAlert.setVisibility(View.INVISIBLE);

        // Check which button was selected. Set flag to the correct button.
        switch (view.getId()) {
            case R.id.rbNormal:
                memoFlag = MainActivity.FLAG_NORMAL;
                break;
            case R.id.rbImportant:
                memoFlag = MainActivity.FLAG_IMPORTANT;
                break;
            case R.id.rbUrgent:
                memoFlag = MainActivity.FLAG_URGENT;
                btnSetAlert.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * When activity is paused, store memos.
     */
    @Override
    protected void onPause() {
        storeMemos();
        super.onPause();
    }

    /**
     * When back button is pressed, store memos and return to MainActivity.
     */
    @Override
    public void onBackPressed() {
        storeMemos();
        super.onBackPressed();
    }

    /**
     * Called when activity is paused/closed.
     * It get the current list of memos, updates the selected memo, and returns the list.
     */
    private void storeMemos() {

        // Get singleton instance.
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

    /**
     * Called when setAlert button is pressed
     * @param view The setAlert button pressed
     */
    public void setAlert(View view) {

    }
}
