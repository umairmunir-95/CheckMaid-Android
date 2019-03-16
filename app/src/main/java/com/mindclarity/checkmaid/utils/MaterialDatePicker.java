package com.mindclarity.checkmaid.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;

import com.mindclarity.checkmaid.App;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MaterialDatePicker {

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Calendar myCalendar;
    private DatePickerDialog datePickerDialog;

    public void showDatePickerDialog(Context context,boolean disableBackDate, final EditText editText, final String dateFormat,final  String selectedDate,String minDate,String maxDate) {
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editText.setText(selectedDate(dateFormat));
            }
        };
        if (selectedDate!=null) {
            if(!(selectedDate.equals(""))) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(App.CALENDER_DATE_FORMAT,Locale.ENGLISH);
                    Date date = sdf.parse(selectedDate);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    datePickerDialog=new DatePickerDialog(context, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                    if(disableBackDate) {
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    }
                    datePickerDialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                datePickerDialog=new DatePickerDialog(context, dateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                if(disableBackDate) {
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                }
                datePickerDialog.show();
            }
        }
        else {
            datePickerDialog=new DatePickerDialog(context, dateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            if(disableBackDate) {
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
            datePickerDialog.show();
        }
    }

    private String selectedDate(String dateFormat)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        return sdf.format(myCalendar.getTime());
    }
}
