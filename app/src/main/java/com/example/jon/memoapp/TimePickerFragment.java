package com.example.jon.memoapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * This fragment holder a time picker dialog
 * where the user enters the time they wish to set an alert.
 */
public class TimePickerFragment extends DialogFragment {

    /**
     * Called when dialog fragment is created.
     *
     * @param savedInstanceState The saved state.
     * @return The dialog to return to the user.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker.
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it.
        return new TimePickerDialog(getActivity(), (EditMemoActivity) getActivity(), hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
