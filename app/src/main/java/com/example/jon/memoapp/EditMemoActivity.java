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

/**
 * This activity is for the user to edit a memo's name or flag. They can also add an alert.
 */
public class EditMemoActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // Tags for the date picker and time picker fragments
    public static final String TIME_PICKER = "timePicker";
    public static final String DATE_PICKER = "datePicker";

    // Properties of current memo being edited.
    private int mMemoId;
    private String mMemoName;
    private int mMemoFlag;

    // User interface references.
    private EditText etMemoName;
    private TextView tvCurrentAlert;
    private Button btnSetAlert;

    // Alert time values.
    private int alertYear;
    private int alertMonth;
    private int alertDay;
    private int alertHour;
    private int alertMinute;

    private Alert mCurrentAlert;

    // Database singleton instance.
    private DbHelper mDbHelper;

    /**
     * Called when activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        // Get memo properties from intent.
        Intent intent = getIntent();
        mMemoId = intent.getIntExtra(MainActivity.INTENT_EXTRA_ID, 0);
        mMemoName = intent.getStringExtra(MainActivity.INTENT_EXTRA_NAME);
        mMemoFlag = intent.getIntExtra(MainActivity.INTENT_EXTRA_FLAG, 0);

        // Get reference to the user interface elements.
        etMemoName = (EditText) findViewById(R.id.etMemoName);
        tvCurrentAlert = (TextView) findViewById(R.id.tvCurrentAlert);
        btnSetAlert = (Button) findViewById(R.id.btnSetAlert);

        // Get database instance.
        mDbHelper = DbHelper.getInstance(this);

        // Set text in text box as current memo name.
        etMemoName.setText(mMemoName);

        // Move cursor to the end of memo name in text box.
        etMemoName.setSelection(etMemoName.getText().length());

        // Set radio button to the corresponding flag of current memo.
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

        // Update alert controls.
        setAlertControls();
    }

    /**
     * This is called by the each radio button in the xml.
     *
     * @param view The specific radio button calling the function.
     */
    public void onRadioButtonClicked(View view) {

        // Check which button was selected and set as new flag.
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

        // Update alert controls.
        setAlertControls();
    }

    /**
     * This method checks if the memo flag is on urgent. If so,
     * alert controls are displayed.
     */
    private void setAlertControls() {

        // If flag is set to urgent...
        if (mMemoFlag == MainActivity.FLAG_URGENT) {

            // Show alert controls.
            tvCurrentAlert.setVisibility(View.VISIBLE);
            btnSetAlert.setVisibility(View.VISIBLE);


            // Check if an alert has been set.
            mCurrentAlert = mDbHelper.getAlert(mMemoId);

            // If alert has been set...
            if (mCurrentAlert != null) {

                // Get the current alert information.
                Calendar cal = Calendar.getInstance();
                cal.set(mCurrentAlert.getYear(),
                        mCurrentAlert.getMonth(),
                        mCurrentAlert.getDay(),
                        mCurrentAlert.getHour(),
                        mCurrentAlert.getMinute()
                );
                Date date = cal.getTime();

                // Present it in a proper format.
                SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mma dd/MM/yyyy", Locale.UK);
                String text2display = getString(R.string.tv_current_alert_text) + dateFormatter.format(date);

                // Update text view showing alert details.
                tvCurrentAlert.setText(text2display);

                // Draw cancel icon onto the right of the text view.
                tvCurrentAlert.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cancel, 0);

                // Set on click listener for the cancel icon.
                tvCurrentAlert.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= tvCurrentAlert.getRight() - tvCurrentAlert.getTotalPaddingRight()) {

                                // Remove current alert.
                                mDbHelper.removeAlert(mMemoId);

                                // Update alert controls.
                                setAlertControls();
                            }
                        }
                        return true;
                    }
                });

                // Update text of button.
                btnSetAlert.setText(R.string.btn_update_alert);

            } else {

                // Update text of UI elements.
                tvCurrentAlert.setText(R.string.tv_no_alarms_set);
                btnSetAlert.setText(R.string.btn_set_alert);

                // Clear cancel icon.
                tvCurrentAlert.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        } else {

            // Hide alert controls.
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
     * When back button is pressed, store memos before returning to MainActivity.
     */
    @Override
    public void onBackPressed() {
        updateMemo();
        super.onBackPressed();
    }

    /**
     * Update the selected memo in the memo table.
     */
    private void updateMemo() {

        // Get new memo title from text box.
        mMemoName = etMemoName.getText().toString();

        // Update memo in memos table.
        mDbHelper.updateMemo(new Memo(mMemoId, mMemoName, mMemoFlag));
    }

    /**
     * Called when setAlert button is pressed.
     *
     * @param view The button.
     */
    public void setAlert(View view) {

        // Ask user to select a date to set an alert.
        new DatePickerFragment().show(getFragmentManager(), DATE_PICKER);
    }

    /**
     * The callback method from the date picker.
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        // Store values of the date selected.
        alertYear = year;
        alertMonth = month;
        alertDay = day;

        // Ask user to select a time to set an alert.
        new TimePickerFragment().show(getFragmentManager(), TIME_PICKER);
    }

    /**
     * The callback method from the time picker.
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

        // Store values of the time selected.
        alertHour = hour;
        alertMinute = minute;

        checkAlert();
    }

    /**
     * Check if an alert already exists. If so, ask if user wants to replace it.
     */
    private void checkAlert() {

        // Check if alert exists.
        if (mCurrentAlert != null) {

            // Ask if user wants to replace alert.
            new AlertDialog.Builder(this)
                    .setMessage(R.string.alert_replace_message)
                    .setPositiveButton(R.string.positive_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // Remove previous alert.
                            mDbHelper.removeAlert(mMemoId);

                            createAlert();
                        }
                    })
                    .setNegativeButton(R.string.negative_no, null)
                    .show();
        } else {
            createAlert();
        }
    }

    /**
     * Creates the alert to be broadcast at the date and time set by the user.
     */
    private void createAlert() {

        // Set up alert object.
        Alert alert = new Alert(alertYear, alertMonth, alertDay, alertHour, alertMinute, mMemoId);

        // Add alert to alerts table.
        mDbHelper.addAlert(alert);

        // Create an Intent which will execute when Alert triggers.
        Intent intentAlarm = new Intent(this, AlertReceiver.class);

        // Add memoId as intent extra.
        intentAlarm.putExtra(MainActivity.INTENT_EXTRA_ID, mMemoId);

        // Get alarm manager.
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Set the exact time when the user will be notified.
        Calendar cal = Calendar.getInstance();
        cal.set(alertYear, alertMonth, alertDay, alertHour, alertMinute, 0);

        /*
        Set up pending intent to send a broadcast.
        The alarm is set with the memoId as the request code.
        This ensures that future alerts from the same memo will override this one.
         */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, mMemoId, intentAlarm, PendingIntent.FLAG_ONE_SHOT);

        // Set the alarm.
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                pendingIntent
        );

        // Update alert controls.
        setAlertControls();
    }
}
