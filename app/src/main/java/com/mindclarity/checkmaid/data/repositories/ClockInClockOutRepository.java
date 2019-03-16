package com.mindclarity.checkmaid.data.repositories;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.TimeSheetDetails;
import com.mindclarity.checkmaid.data.remote.ApiClient;
import com.mindclarity.checkmaid.data.remote.interfaces.TimeLogsInterface;
import com.mindclarity.checkmaid.data.remote.models.clockin_clockout.create.ClockInClockOutRequest;
import com.mindclarity.checkmaid.data.remote.models.clockin_clockout.create.ClockInClockOutResponse;
import com.mindclarity.checkmaid.data.remote.models.clockin_clockout.get_by_user.GetTimeLogdByUserModel;
import com.mindclarity.checkmaid.utils.CSVUtils;
import com.mindclarity.checkmaid.utils.DialogManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClockInClockOutRepository {

    private ArrayList<TimeSheetDetails> timeSheetDetails=new ArrayList<>();
    private long totalHours=0;
    private long totalMinutes=0;

    public ClockInClockOutRepository(Application application) {
    }

    public void createTimeLog(final Context context, final ProgressBar progressBar, final String logType, ClockInClockOutRequest clockInClockOutRequest) {

        TimeLogsInterface timeLogsInterfacee = ApiClient.getClient().create(TimeLogsInterface.class);
        Call<ClockInClockOutResponse> call = timeLogsInterfacee.createTimeLog(clockInClockOutRequest);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ClockInClockOutResponse>() {
            @Override
            public void onResponse(Call<ClockInClockOutResponse> call, Response<ClockInClockOutResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("TAG", "Response Code : " + response.body());
                    if(logType.equals("clockIn")) {
                        DialogManager.showNetworkInfo(context, "", context.getResources().getString(R.string.clock_in_success), "clockIn");
                    }
                    else if(logType.equals("clockOut"))
                    {
                        DialogManager.showNetworkInfo(context, "",context.getResources().getString(R.string.clock_out_success), "clockOut");
                    }
                }
                else
                {
                    if(logType.equals("clockIn")) {
                        DialogManager.showNetworkInfo(context, "", response.message(), "clockIn");
                    }
                    else if(logType.equals("clockOut"))
                    {
                        DialogManager.showNetworkInfo(context, "", response.message(), "clockOut");
                    }
                }
            }

            @Override
            public void onFailure(Call<ClockInClockOutResponse> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                if(logType.equals("clockIn")) {
                    DialogManager.showNetworkInfo(context, "", t.toString(), "clockIn");
                }
                else if(logType.equals("clockOut"))
                {
                    DialogManager.showNetworkInfo(context, "", t.toString(), "clockOut");
                }
            }
        });
    }


    public void getLogsByUser(final Context context, final ProgressBar progressBar, final TextView tvTotalTime, final TableLayout tableLayout, String url, final FloatingActionButton fabDownload, final FloatingActionButton fabShare) {

        TimeLogsInterface timeLogsInterface = ApiClient.getClient().create(TimeLogsInterface.class);
        Call<List<GetTimeLogdByUserModel>> call = timeLogsInterface.getLogsByUser(url);
        progressBar.setVisibility(View.VISIBLE);
        totalHours=0;
        totalMinutes=0;
        call.enqueue(new Callback<List<GetTimeLogdByUserModel>>() {
            @Override
            public void onResponse(Call<List<GetTimeLogdByUserModel>> call, Response<List<GetTimeLogdByUserModel>> response) {
                progressBar.setVisibility(View.GONE);
                tableLayout.removeAllViews();
                if (response.isSuccessful()) {

                    if (response.body().size() == 0) {
                        DialogManager.showNetworkInfo(context, "", context.getResources().getString(R.string.no_data_found), "");
                    } else {
                        TableRow rowHeading = new TableRow(context);
                        TextView propertyNameHeading = new TextView(context);
                        propertyNameHeading.setText("Property Name");
                        propertyNameHeading.setTypeface(null, Typeface.BOLD);
                        propertyNameHeading.setGravity(Gravity.CENTER_HORIZONTAL);
                        propertyNameHeading.setPadding(10, 10, 10, 10);
                        TextView dateHeading = new TextView(context);
                        dateHeading.setText("Date");
                        dateHeading.setTypeface(null, Typeface.BOLD);
                        dateHeading.setGravity(Gravity.CENTER_HORIZONTAL);
                        dateHeading.setPadding(10, 10, 10, 10);
                        TextView clockInHeading = new TextView(context);
                        clockInHeading.setText("ClockIn");
                        clockInHeading.setTypeface(null, Typeface.BOLD);
                        clockInHeading.setGravity(Gravity.CENTER_HORIZONTAL);
                        clockInHeading.setPadding(10, 10, 10, 10);
                        TextView clockOutHeading = new TextView(context);
                        clockOutHeading.setTypeface(null, Typeface.BOLD);
                        clockOutHeading.setText("ClockOut");
                        clockOutHeading.setGravity(Gravity.CENTER_HORIZONTAL);
                        clockOutHeading.setPadding(10, 10, 10, 10);

                        rowHeading.addView(propertyNameHeading, new TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.25f));
                        rowHeading.addView(dateHeading, new TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.25f));
                        rowHeading.addView(clockInHeading, new TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.25f));
                        rowHeading.addView(clockOutHeading, new TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.25f));

                        tableLayout.addView(rowHeading);

                        View vlineHeading = new View(context);
                        vlineHeading.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
                        tableLayout.addView(vlineHeading);
                        tvTotalTime.setVisibility(View.VISIBLE);
                        fabDownload.setEnabled(true);
                        fabDownload.setAlpha(1f);
                        fabShare.setEnabled(true);
                        fabShare.setAlpha(1f);
                        for (int i = 0; i < response.body().size(); i++) {
                            if (!response.body().get(i).getCheckInTime().equals("") && !response.body().get(i).getCheckOutTime().equals("")) {
                                TableRow row = new TableRow(context);
                                if (i % 2 == 0) {
                                    row.setBackgroundColor(context.getResources().getColor(R.color.white));
                                } else {
                                    row.setBackgroundColor(context.getResources().getColor(R.color.gray));
                                }
                                TextView propertyName = new TextView(context);
                                propertyName.setText(response.body().get(i).getPropertyName());
                                propertyName.setGravity(Gravity.CENTER_HORIZONTAL);
                                propertyName.setPadding(10, 10, 10, 10);
                                TextView date = new TextView(context);
                                date.setText(response.body().get(i).getDate());
                                date.setGravity(Gravity.CENTER_HORIZONTAL);
                                date.setPadding(10, 10, 10, 10);
                                TextView clockIn = new TextView(context);
                                clockIn.setText(response.body().get(i).getCheckInTime());
                                clockIn.setGravity(Gravity.CENTER_HORIZONTAL);
                                clockIn.setPadding(10, 10, 10, 10);
                                TextView clockOut = new TextView(context);
                                clockOut.setText(response.body().get(i).getCheckOutTime());
                                clockOut.setGravity(Gravity.CENTER_HORIZONTAL);
                                clockOut.setPadding(10, 10, 10, 10);

                                row.addView(propertyName, new TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.25f));
                                row.addView(date, new TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.25f));
                                row.addView(clockIn, new TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.25f));
                                row.addView(clockOut, new TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.25f));

                                tableLayout.addView(row);

                                View vline = new View(context);
                                vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
                                tableLayout.addView(vline);

                                //Data For CSV Format
                                timeSheetDetails.add(new TimeSheetDetails(
                                        response.body().get(i).getPropertyName(),
                                        response.body().get(i).getDate(),
                                        response.body().get(i).getCheckInTime(),
                                        response.body().get(i).getCheckOutTime()));

                                // Logic for calculating total time
                                String clockInTime = response.body().get(i).getCheckInTime();
                                String clockOutTime = response.body().get(i).getCheckOutTime();
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                Date d1 = null;
                                Date d2 = null;
                                try {
                                    d1 = format.parse(clockInTime);
                                    d2 = format.parse(clockOutTime);
                                    long diff = d2.getTime() - d1.getTime();
                                    long diffMinutes = diff / (60 * 1000) % 60;
                                    long diffHours = diff / (60 * 60 * 1000) % 24;

                                    totalHours = totalHours + diffHours;
                                    totalMinutes = totalMinutes + diffMinutes;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    if (response.message().trim().equals("Not Found")) {
                        DialogManager.showNetworkInfo(context, "", context.getResources().getString(R.string.no_data_found), "");
                    } else {
                        DialogManager.showNetworkInfo(context, "", response.message(), "");
                    }
                }
                long hours = totalMinutes / 60;
                long minutes = totalMinutes % 60;
                totalHours = totalHours + hours;
                totalMinutes = minutes;
                Log.d("Total TIme : ", totalHours + " : " + totalMinutes);
                Log.d("Converted Total TIme : ", totalHours + " : " + totalMinutes);
                tvTotalTime.setText("Total : " + totalHours + "hrs " + totalMinutes + "minutes");
            }

            @Override
            public void onFailure(Call<List<GetTimeLogdByUserModel>> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"");
            }
        });
    }

    public void createTimeSheetCSV(Context context)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/CheckMaids/CSV");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        File file = new File(myDir, timeStamp+".csv");
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            CSVUtils.writeLine(writer, Arrays.asList("PropertyName", "Date", "CheckIn","CheckOut"));
            for (TimeSheetDetails t : timeSheetDetails) {

                List<String> list = new ArrayList<>();
                list.add(t.getPropertyName());
                list.add(t.getDate());
                list.add(t.getCheckIn());
                list.add(t.getCheckOut());
                CSVUtils.writeLine(writer, list);
            }
            writer.flush();
            writer.close();
            Toast.makeText(context,"CSV file save successfully.",Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
