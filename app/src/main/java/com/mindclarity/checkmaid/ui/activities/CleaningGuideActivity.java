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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.Users;
import com.mindclarity.checkmaid.ui.adapters.ListViewSearchUsersAdapter;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.viewmodel.GuidesViewModel;

import java.util.ArrayList;

public class CleaningGuideActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack, ivPicture;
    private LinearLayout viewFlipperLayout;
    private EditText etFindProperty;
    private Button btnGetInfo;
    private ProgressBar progressBar;
    private LinearLayout llPicture, llNotes;
    private GuidesViewModel guidesViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_guide);
        initializeViews();
        if (Helpers.getPreferenceValue(CleaningGuideActivity.this, getResources().getString(R.string.assigned_properties)).equals("")) {
            DialogManager.showInfoDialog(CleaningGuideActivity.this, "", getResources().getString(R.string.no_property_assignment), false);
        }

    }

    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        llNotes = findViewById(R.id.ll_notes);
        llPicture = findViewById(R.id.ll_picture);
        viewFlipperLayout=findViewById(R.id.view_flipper_root);
        ivPicture = findViewById(R.id.iv_picture);
        btnGetInfo = findViewById(R.id.btn_get_info);
        etFindProperty = findViewById(R.id.et_find_property);
        progressBar = findViewById(R.id.progress_bar);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.cleaning_guide));

        guidesViewModel = ViewModelProviders.of(this).get(GuidesViewModel.class);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helpers.getPreferenceValue(CleaningGuideActivity.this,getResources().getString(R.string.user_type)).equals(getResources().getString(R.string.admin)))
                {
                    Intent i = new Intent(CleaningGuideActivity.this, PropertiesActivity.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(CleaningGuideActivity.this, FindPropertyActivity.class);
                    startActivity(i);
                }
            }
        });

        etFindProperty.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showUsersListPopUp();
                    return true;
                }
                return false;
            }
        });

        btnGetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isNetworkAvailable(CleaningGuideActivity.this)) {
                    if (!etFindProperty.getText().toString().trim().equals("")) {
                        guidesViewModel.findProperty(CleaningGuideActivity.this, progressBar, App.LIST_ALL_GUIDES + "/" + etFindProperty.getText().toString().trim(), false,llPicture, llNotes, ivPicture, viewFlipperLayout);
                    }
                } else {
                    DialogManager.showInfoDialog(CleaningGuideActivity.this, "", getResources().getString(R.string.internet_available_msg), false);
                }
            }
        });
    }

    private void showUsersListPopUp() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_user_list, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        EditText etSearch = promptsView.findViewById(R.id.et_search);
        ListView lvProperties = promptsView.findViewById(R.id.lv_users);
        TextView tvProperties = promptsView.findViewById(R.id.tv_option);
        ImageView ivCross = promptsView.findViewById(R.id.iv_close);
        tvProperties.setText(getResources().getString(R.string.list_of_properties));
        lvProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView tvSelectedProperty = view.findViewById(R.id.tv_name);
                TextView tvID = view.findViewById(R.id.tv_id);
                etFindProperty.setText("" + tvSelectedProperty.getText().toString());
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

        ArrayList<Users> users = new ArrayList<>();
        String[] assignedProperties = Helpers.getPreferenceValue(CleaningGuideActivity.this, getResources().getString(R.string.assigned_properties)).split(",");
        if (assignedProperties.length == 0) {
            DialogManager.showInfoDialog(CleaningGuideActivity.this, "", getResources().getString(R.string.no_property_assignment), false);
        } else {
            for (int i = 0; i < assignedProperties.length; i++) {
                if (!(assignedProperties[i].trim().equals(""))) {
                    Users users1 = new Users();
                    users1.setUserName(assignedProperties[i]);
                    users1.setUserId("");
                    users.add(users1);
                }
            }
        }
        ListViewSearchUsersAdapter adapter = new ListViewSearchUsersAdapter(CleaningGuideActivity.this, users);
        lvProperties.setAdapter(adapter);
    }
}