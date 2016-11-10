package com.example.jon.memoapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditMemoActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {


    // Properties of current memo being edited
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
    private TextView tvCurrentAlert;
    private DbHelper mDbHelper;

    /**
     * Called when activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        // Get intent and memo properties from intent extras.
        Intent intent = getIntent();
        mMemoId = intent.getIntExtra(MainActivity.INTENT_EXTRA_ID, 0);
        mMemoName = intent.getStringExtra(MainActivity.INTENT_EXTRA_NAME);
        mMemoFlag = intent.getIntExtra(MainActivity.INTENT_EXTRA_FLAG, 0);

        // Get reference to the memo text box and set text as memo name
        etMemoName = (EditText) findViewById(R.id.etMemoName);
        etMemoName.setText(mMemoName);

        // Get setAlert button reference
        btnSetAlert = (Button) findViewById(R.id.btnSetAlert);

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
                break;
        }

        // Get database instance
        mDbHelper = DbHelper.getInstance(this);

        // Get current alert text view and set on click listener for cancelling alert
        tvCurrentAlert = (TextView) findViewById(R.id.tvCurrentAlert);

        // Update UI
        setVisibility();

    }

    /**
     * This is called by the each radio button in the xml.
     *
     * @param view The radio button calling the function
     */
    public void onRadioButtonClicked(View view) {

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
                break;
        }

        // Update UI
        setVisibility();
    }

    /**
     * This method checks if a memo flag is on urgent.
     * If alert has been set, show textview with alert details,
     * else show "set alert" button
     */
    private void setVisibility() {

        Alert mCurrentAlert = mDbHelper.getAlert(mMemoId);

        if (mMemoFlag == MainActivity.FLAG_URGENT) {

            // Show alert controls
            tvCurrentAlert.setVisibility(View.VISIBLE);
            btnSetAlert.setVisibility(View.VISIBLE);

            if (mCurrentAlert != null) {

                // Get the current alert information
                Calendar cal = Calendar.getInstance();
                cal.set(mCurrentAlert.getYear(),
                        mCurrentAlert.getMonth(),
                        mCurrentAlert.getDay(),
                        mCurrentAlert.getHour(),
                        mCurrentAlert.getMinute()
                );
                Date date = cal.getTime();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mma dd/MM/yyyy", Locale.UK);
                String text2display = getString(R.string.tv_current_alert_text) + dateFormatter.format(date);

                // Update text showing alert details
                tvCurrentAlert.setText(text2display);

                // Draw cancel icon onto the right of the text view
                tvCurrentAlert.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cancel, 0);

                // Set on click listener for the cancel icon
                tvCurrentAlert.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= tvCurrentAlert.getRight() - tvCurrentAlert.getTotalPaddingRight()) {

                                // Remove alert
                                mDbHelper.removeAlert(mMemoId);

                                // Update UI
                                setVisibility();

                                return true;
                            }
                        }
                        return true;
                    }
                });

                // Update text
                btnSetAlert.setText(R.string.btn_update_alert);

            } else {

                // Update text
                tvCurrentAlert.setText(R.string.tv_no_alarms_set);
                btnSetAlert.setText(R.string.btn_set_alert);

                // Clear drawables
                tvCurrentAlert.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


            }
        } else {
            // Hide both
            tvCurrentAlert.setVisibility(View.INVISIBLE);
            btnSetAlert.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * When activity is paused, store memos.
     */
    @Override
    protected void onPause() {
        updateMemo();
        super.onPause();
    }

    /**
     * When back button is pressed, store memos and return to MainActivity.
     */
    @Override
    public void onBackPressed() {
        updateMemo();
        super.onBackPressed();
    }

    /**
     * Called when activity is paused/closed.
     * Update the selected memo.
     */
    private void updateMemo() {

        // Get new memo content
        mMemoName = etMemoName.getText().toString();

        // Update memo in database
        mDbHelper.updateMemo(new Memo(mMemoId, mMemoName, mMemoFlag));
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

        checkAlert();

    }

    /**
     * Check if an alert already exists. If so, ask if user wants to replace it.
     */
    private void checkAlert() {

        Alert alert = mDbHelper.getAlert(mMemoId);

        // Check if alert exists
        if (alert != null) {

            // Ask if user wants to replace alert
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to replace the previous alert?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // Remove previous alert
                            mDbHelper.removeAlert(mMemoId);

                            createAlert();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            createAlert();
        }


    }

    private void createAlert() {
        Alert alert = new Alert(alertYear, alertMonth, alertDay, alertHour, alertMinute, mMemoId);

        // Add alert to alerts table
        mDbHelper.addAlert(alert);

        // Create an Intent which will execute when Alert triggers
        Intent intentAlarm = new Intent(this, AlertReceiver.class);

        // Add intent extras
        intentAlarm.putExtra(MainActivity.INTENT_EXTRA_NAME, mMemoName);
        intentAlarm.putExtra(MainActivity.INTENT_EXTRA_ID, mMemoId);

        // Get alarm manager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Set the exact time when the user will be notified
        Calendar cal = Calendar.getInstance();
        cal.set(alertYear, alertMonth, alertDay, alertHour, alertMinute, 0);

        // Set up pending intent to send a broadcast
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, mMemoId, intentAlarm, PendingIntent.FLAG_ONE_SHOT);

        // Set the alarm
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                pendingIntent
        );

        // update UI
        setVisibility();

    }
}
