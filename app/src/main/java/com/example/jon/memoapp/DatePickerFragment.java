package com.example.jon.memoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

/**
 * This fragment holder a data picker dialog
 * where the user enters the date they wish to set an alert.
 */
public class DatePickerFragment extends DialogFragment {

    /**
     * Called when dialog fragment is created.
     *
     * @param savedInstanceState The saved state.
     * @return The dialog to present to the user.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default (and minimum) date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (EditMemoActivity) getActivity(), year, month, day);

        // Stop user from selecting a date in the past.
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

        return datePickerDialog;
    }
}

