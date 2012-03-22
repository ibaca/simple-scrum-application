
package org.inftel.ssa.mobile.ui.fragments;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

public class DateDialogFragment extends DialogFragment {

    public static String TAG = "DateDialogFragment";

    static Context sContext;

    private int mYear;
    private int mMonth;
    private int mDay;
    private static EditText textView;

    public static DateDialogFragment newInstance(Context context, EditText titleResource) {
        DateDialogFragment dialog = new DateDialogFragment();
        sContext = context;
        textView = titleResource;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(sContext, dateSetListener, mYear, mMonth, mDay);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                        int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    dateDialogFragmentDateSet();
                }
            };

    public void dateDialogFragmentDateSet() {
        textView.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" "));
    }

}
