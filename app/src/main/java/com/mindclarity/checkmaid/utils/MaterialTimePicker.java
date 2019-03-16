package com.mindclarity.checkmaid.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.EditText;

import com.mindclarity.checkmaid.R;

import java.util.Calendar;

public class MaterialTimePicker {

    public static void showTimePicker(final Context context,final EditText editText)
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker timePicker, int selectedHour, int selectedMinute) {
                editText.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle(context.getResources().getString(R.string.select_time));
        mTimePicker.show();
    }
}
