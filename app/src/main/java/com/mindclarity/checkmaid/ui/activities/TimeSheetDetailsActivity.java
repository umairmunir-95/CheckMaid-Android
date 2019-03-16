package com.mindclarity.checkmaid.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrinterId;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.BuildConfig;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.TimeSheetDetails;
import com.mindclarity.checkmaid.data.common_models.Users;
import com.mindclarity.checkmaid.ui.adapters.ListViewSearchUsersAdapter;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.ImageUtils;
import com.mindclarity.checkmaid.utils.MaterialDatePicker;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.viewmodel.ClockInClockOutViewModel;
import com.mindclarity.checkmaid.viewmodel.UsersViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class TimeSheetDetailsActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack;
    private LinearLayout llDate, llUser, llProperty;
    private EditText etDate, etUser, etProperty;
    private TextView tvTotalLogs;
    private ProgressBar progressBar;
    private FloatingActionButton fabShare,fabDownload;
    private Button btnShow;
    private TableLayout tblTimeLogs;
    private MaterialDatePicker datePicker;
    private ClockInClockOutViewModel clockInClockOutViewModel;
    private UsersViewModel usersViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet_details);
        initializeViews();
    }

    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        tblTimeLogs = findViewById(R.id.tbl_time_logs);
        btnShow = findViewById(R.id.btn_show);
        llUser = findViewById(R.id.ll_user);
        tvTotalLogs=findViewById(R.id.tv_total_logs);
        llProperty = findViewById(R.id.ll_property);
        llDate = findViewById(R.id.ll_date);
        etDate = findViewById(R.id.et_choose_date);
        etUser = findViewById(R.id.et_select_user);
        etProperty = findViewById(R.id.et_select_property);
        progressBar=findViewById(R.id.progress_bar);
        fabShare=findViewById(R.id.fab_share);
        fabDownload=findViewById(R.id.fab_download);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getIntent().getStringExtra(getResources().getString(R.string.from)));
        datePicker=new MaterialDatePicker();
        
        clockInClockOutViewModel=ViewModelProviders.of(this).get(ClockInClockOutViewModel.class);
        usersViewModel=ViewModelProviders.of(this).get(UsersViewModel.class);
        showHideFormContent();

        fabDownload.setEnabled(false);
        fabDownload.setAlpha(0.3f);
        fabShare.setEnabled(false);
        fabShare.setAlpha(0.3f);

        etDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    datePicker.showDatePickerDialog(TimeSheetDetailsActivity.this,false, etDate, App.CALENDER_DATE_FORMAT, null,null,null);
                    return true;
                }
                return false;
            }
        });

        etUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showUsersListPopUp();
                    return true;
                }
                return false;
            }
        });

        etProperty.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showPropertiesListPopUp();
                    return true;
                }
                return false;
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TimeSheetDetailsActivity.this, TimeSheetsActivity.class);
                startActivity(i);
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScrollView scrollView = findViewById(R.id.layout);
                Bitmap bitmap = ImageUtils.getBitmapFromScrollView(scrollView, scrollView.getChildAt(0).getHeight(), scrollView.getChildAt(0).getWidth());
                try
                {
                    Helpers.createPdf(TimeSheetDetailsActivity.this,bitmap);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (DocumentException e) {
                    e.printStackTrace();
                }


            }
        });

        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockInClockOutViewModel.createTimeSheetCSV(TimeSheetDetailsActivity.this);
            }
        });


        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTimeLogs();
            }
        });
    }

    private void showHideFormContent()
    {
        if(tvHeader.getText().toString().equals(getResources().getString(R.string.by_date)))
        {
            llDate.setVisibility(View.VISIBLE);
            llProperty.setVisibility(View.GONE);
            llUser.setVisibility(View.GONE);
        }
        else if (tvHeader.getText().toString().equals(getResources().getString(R.string.by_property)))
        {
            llProperty.setVisibility(View.VISIBLE);
            llUser.setVisibility(View.GONE);
            llDate.setVisibility(View.GONE);
        }
        else{
            llUser.setVisibility(View.VISIBLE);
            llProperty.setVisibility(View.GONE);
            llDate.setVisibility(View.GONE);
        }
    }

    private void loadTimeLogs() {
        if (NetworkManager.isNetworkAvailable(TimeSheetDetailsActivity.this)) {
            String url="";
            if(tvHeader.getText().toString().equals(getResources().getString(R.string.by_date))) {
                url = App.LIST_ALL_TIME_LOGS + "/" + App.BY_DATE + "/"+etDate.getText().toString().trim();
            }
            else if(tvHeader.getText().toString().equals(getResources().getString(R.string.by_property)))
            {
                url = App.LIST_ALL_TIME_LOGS + "/" + App.BY_PROPERTY + "/"+etProperty.getText().toString().trim();
            }
            else
            {
                url = App.LIST_ALL_TIME_LOGS + "/" + App.BY_USER + "/" + AppStore.USER_ID;
            }
            clockInClockOutViewModel.getLogsByUser(TimeSheetDetailsActivity.this, progressBar,tvTotalLogs, tblTimeLogs, url,fabDownload,fabShare);
        }
        else
        {
            DialogManager.showInfoDialog(TimeSheetDetailsActivity.this,"", getResources().getString(R.string.internet_available_msg),false);
        }
    }

    private void showUsersListPopUp()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_user_list, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        EditText etSearch=promptsView.findViewById(R.id.et_search);
        ListView lvUsers=promptsView.findViewById(R.id.lv_users);
        ImageView ivCross=promptsView.findViewById(R.id.iv_close);
        ProgressBar listProgressBar=promptsView.findViewById(R.id.list_progress_bar);
        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView tvSelectedUser = view.findViewById(R.id.tv_name);
                TextView tvID = view.findViewById(R.id.tv_id);
                etUser.setText(""+tvSelectedUser.getText().toString());
                AppStore.USER_ID=tvID.getText().toString();
                alertDialog.cancel();
            }
        });
        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        if(NetworkManager.isNetworkAvailable(TimeSheetDetailsActivity.this)) {
            usersViewModel.listAllUsers(TimeSheetDetailsActivity.this, listProgressBar, lvUsers, App.LIST_ALL_USERS);
        }
        else
        {
            DialogManager.showInfoDialog(TimeSheetDetailsActivity.this,"",getResources().getString(R.string.internet_available_msg),false);
        }
    }

    private void showPropertiesListPopUp()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_user_list, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        ListView lvProperties=promptsView.findViewById(R.id.lv_users);
        TextView tvProperties=promptsView.findViewById(R.id.tv_option);
        ImageView ivCross=promptsView.findViewById(R.id.iv_close);
        tvProperties.setText(getResources().getString(R.string.list_of_properties));
        lvProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView tvSelectedProperty = view.findViewById(R.id.tv_name);
                TextView tvID = view.findViewById(R.id.tv_id);
                etProperty.setText(""+tvSelectedProperty.getText().toString());
                alertDialog.cancel();
            }
        });
        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        ArrayList<Users> users=new ArrayList<>();
        String[] assignedProperties= Helpers.getPreferenceValue(TimeSheetDetailsActivity.this,getResources().getString(R.string.all_properties)).split(",");
        if(assignedProperties.length==0) {
            DialogManager.showInfoDialog(TimeSheetDetailsActivity.this, "", getResources().getString(R.string.no_property_assignment), false);
        }
        else{
            for(int i=0;i<assignedProperties.length;i++)
            {
                if(!(assignedProperties[i].trim().equals(""))) {
                    Users users1 = new Users();
                    users1.setUserName(assignedProperties[i]);
                    users1.setUserId("");
                    users.add(users1);
                }
            }
        }
        ListViewSearchUsersAdapter adapter=new ListViewSearchUsersAdapter(TimeSheetDetailsActivity.this,users);
        lvProperties.setAdapter(adapter);
    }
}