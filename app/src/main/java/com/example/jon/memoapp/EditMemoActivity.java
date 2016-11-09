package com.example.jon.memoapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class EditMemoActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {


    // Properties of current memo being edited
    private int mMemoPosition;
    private int mMemoId;
    private String mMemoName;
    private int mMemoFlag;

    // User interface references
    private EditText etMemoName;
    private Button btnSetAlert;
    private int alertYear;
    private int alertMonth;
    private int alertDay;
    private int alertHour;
    private int alertMinute;

    /**
     * Called when activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        // Get intent and memo properties from intent extras.
        Intent intent = getIntent();
        mMemoPosition = intent.getIntExtra(MainActivity.INTENT_EXTRA_POSITION, 0);
        mMemoId = intent.getIntExtra(MainActivity.INTENT_EXTRA_ID, 0);
        mMemoName = intent.getStringExtra(MainActivity.INTENT_EXTRA_NAME);
        mMemoFlag = intent.getIntExtra(MainActivity.INTENT_EXTRA_FLAG, 0);

        // Get reference to the memo text box and set text as memo name
        etMemoName = (EditText) findViewById(R.id.etMemoName);
        etMemoName.setText(mMemoName);

        // Get setAlert button reference
        btnSetAlert = (Button) findViewById(R.id.btnSetAlert);
        btnSetAlert.setVisibility(View.INVISIBLE);

        // Move cursor to the end of memo name
        etMemoName.setSelection(etMemoName.getText().length());

        // Set radio button to the corresponding flag of memo
        switch (mMemoFlag) {
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
     *
     * @param view The radio button calling the function
     */
    public void onRadioButtonClicked(View view) {

        btnSetAlert.setVisibility(View.INVISIBLE);

        // Check which button was selected. Set flag to the correct button.
        switch (view.getId()) {
            case R.id.rbNormal:
                mMemoFlag = MainActivity.FLAG_NORMAL;
                break;
            case R.id.rbImportant:
                mMemoFlag = MainActivity.FLAG_IMPORTANT;
                break;
            case R.id.rbUrgent:
                mMemoFlag = MainActivity.FLAG_URGENT;
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
        mMemoName = etMemoName.getText().toString();

        // Override memo with new data
        memos.set(mMemoPosition, new Memo(mMemoName, mMemoFlag));

        // Send back to database
        dbHelper.addMemos(memos);

    }

    /**
     * Called when setAlert button is pressed
     *
     * @param view The setAlert button pressed
     */
    public void setAlert(View view) {

        new DatePickerFragment().show(getFragmentManager(), "datePicker");

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        alertYear = year;
        alertMonth = month;
        alertDay = day;

        // Start timeListener
        new TimePickerFragment().show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

        alertHour = hour;
        alertMinute = minute;

        createAlert();

    }

    private void createAlert() {

        Alert alert = new Alert(alertYear, alertMonth, alertDay, alertHour, alertMinute, mMemoId);

        // Add alert to alerts table
        DbHelper dbHelper = DbHelper.getInstance(this);
        ArrayList<Alert> alerts = dbHelper.getAlerts();
        alerts.add(alert);
        dbHelper.addAlerts(alerts);

        // Create an Intent and the class which will execute when Alarm triggers
        Intent intentAlarm = new Intent(this, AlertReceiver.class);

        // Add intent extras
        intentAlarm.putExtra(MainActivity.INTENT_EXTRA_NAME, mMemoName);
        intentAlarm.putExtra(MainActivity.INTENT_EXTRA_ID, mMemoId);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Set the exact time when the user will be notified
        Calendar cal = Calendar.getInstance();
        cal.set(alertYear, alertMonth, alertDay, alertHour, alertMinute);

        // Set the alarm
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT)
        );

    }
}
