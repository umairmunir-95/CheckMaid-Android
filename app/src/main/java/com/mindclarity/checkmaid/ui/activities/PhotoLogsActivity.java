package com.mindclarity.checkmaid.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.Users;
import com.mindclarity.checkmaid.ui.adapters.ListViewSearchUsersAdapter;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.MaterialDatePicker;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.viewmodel.PhotoLogsViewModel;

import java.util.ArrayList;

public class PhotoLogsActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack;
    private EditText etChooseDate,etProperties;
    private Button btnShow;
    private ProgressBar progressBar;
    private MaterialDatePicker datePicker;
    private PhotoLogsViewModel photoLogsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_logs);
        initializeViews();
    }

    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        btnShow=findViewById(R.id.btn_show);
        progressBar=findViewById(R.id.progress_bar);
        etProperties=findViewById(R.id.et_properties);
        etChooseDate=findViewById(R.id.et_choose_date);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.photologs));
        etChooseDate.setFocusable(false);

        photoLogsViewModel = ViewModelProviders.of(this).get(PhotoLogsViewModel.class);
        datePicker=new MaterialDatePicker();


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PhotoLogsActivity.this, PropertiesActivity.class);
                startActivity(i);
            }
        });

        etChooseDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    datePicker.showDatePickerDialog(PhotoLogsActivity.this,false, etChooseDate, App.CALENDER_DATE_FORMAT, null,null,null);
                    return true;
                }
                return false;
            }
        });

        etProperties.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showPropertiesListPopUp();
                    return true;
                }
                return false;
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkManager.isNetworkAvailable(PhotoLogsActivity.this)) {
                    if(!etChooseDate.getText().toString().equals("") && ! etProperties.getText().toString().equals("")) {
                        String url = App.LIST_ALL_PHOTO_LOGS + "/" + App.BY_DATE + "/" + etChooseDate.getText().toString().trim();
                        photoLogsViewModel.getPhotoLogs(PhotoLogsActivity.this, progressBar, url);
                    }
                }
                else
                {
                    DialogManager.showInfoDialog(PhotoLogsActivity.this,"",getResources().getString(R.string.internet_available_msg),false);
                }
            }
        });
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
                etProperties.setText(""+tvSelectedProperty.getText().toString());
                AppStore.PHOTO_LOGS_PROPERTY_NAME=etProperties.getText().toString().trim();
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
        String[] assignedProperties= Helpers.getPreferenceValue(PhotoLogsActivity.this,getResources().getString(R.string.all_properties)).split(",");
        if(assignedProperties.length==0) {
            DialogManager.showInfoDialog(PhotoLogsActivity.this, "", getResources().getString(R.string.no_property_assignment), false);
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
        ListViewSearchUsersAdapter adapter=new ListViewSearchUsersAdapter(PhotoLogsActivity.this,users);
        lvProperties.setAdapter(adapter);
    }
}
