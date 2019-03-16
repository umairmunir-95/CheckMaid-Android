package com.mindclarity.checkmaid.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.Properties;
import com.mindclarity.checkmaid.data.common_models.Users;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.data.remote.models.users.AssignPropertiesRequestModel;
import com.mindclarity.checkmaid.ui.adapters.GridViewPropertiesAdapter;
import com.mindclarity.checkmaid.ui.adapters.ListViewSearchUsersAdapter;
import com.mindclarity.checkmaid.ui.adapters.RecyclerViewAssignPropertiesAdapter;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.MaterialTimePicker;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.viewmodel.PropertiesViewModel;
import com.mindclarity.checkmaid.viewmodel.UsersViewModel;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack,ivAddNewUser;
    private RelativeLayout rlAddNewUser;
    private Button btnSave;
    private ProgressBar progressBar;
    private EditText etSelectUser,etSelectedUserId;
    private RecyclerView recyclerView;
    private ArrayList<Properties> properties;
    private UsersViewModel usersViewModel;
    private PropertiesViewModel propertiesViewModel;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        initializeViews();
        loadProperties();
    }

    public void onBackPressed() {

    }


    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        rlAddNewUser=findViewById(R.id.rl_add_new_user);
        ivAddNewUser=findViewById(R.id.iv_add_new_user);
        etSelectUser=findViewById(R.id.et_select_user);
        etSelectedUserId=findViewById(R.id.et_user_id);
        recyclerView=findViewById(R.id.recycler_view);
        btnSave=findViewById(R.id.btn_save);
        progressBar=findViewById(R.id.progress_bar);
        tvHeader.setVisibility(View.VISIBLE);
        rlAddNewUser.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.users));

        usersViewModel=ViewModelProviders.of(this).get(UsersViewModel.class);
        propertiesViewModel=ViewModelProviders.of(this).get(PropertiesViewModel.class);
        sharedPreferencesHelper=new SharedPreferencesHelper(UsersActivity.this);
        sharedPreferencesHelper.setString(getResources().getString(R.string.assigned_properties),"");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UsersActivity.this, PropertiesActivity.class);
                startActivity(i);
            }
        });

        etSelectUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showUsersListPopUp();
                    return true;
                }
                return false;
            }
        });

        ivAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(UsersActivity.this,AddNewCleanerActivity.class);
                startActivity(i);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkManager.isNetworkAvailable(UsersActivity.this))
                {
                    if(etSelectUser.getText().toString().equals(""))
                    {
                        DialogManager.showInfoDialog(UsersActivity.this,"",getResources().getString(R.string.please_select_user_first),false);
                    }
                    else {
                        if(Helpers.getPreferenceValue(UsersActivity.this,getResources().getString(R.string.assigned_properties)).equals(""))
                        {
                            DialogManager.showInfoDialog(UsersActivity.this,"",getResources().getString(R.string.select_property),false);
                        }
                        else {
                            usersViewModel.assignProperties(UsersActivity.this, progressBar, App.LIST_ALL_USERS+"/"+etSelectedUserId.getText().toString(), assignPropertiesRequestModel());
                        }
                    }
                }
                else
                {
                    DialogManager.showInfoDialog(UsersActivity.this,"",getResources().getString(R.string.internet_available_msg),false);
                }
            }
        });
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
                etSelectUser.setText(""+tvSelectedUser.getText().toString());
                etSelectedUserId.setText(""+tvID.getText().toString());
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
        if(NetworkManager.isNetworkAvailable(UsersActivity.this)) {
            usersViewModel.listAllUsers(UsersActivity.this, listProgressBar, lvUsers, App.LIST_ALL_USERS);
        }
        else
        {
            DialogManager.showInfoDialog(UsersActivity.this,"",getResources().getString(R.string.internet_available_msg),false);
        }

    }

    private void loadProperties() {
        if (NetworkManager.isNetworkAvailable(UsersActivity.this)) {
            propertiesViewModel.getAllProperties(UsersActivity.this, progressBar,recyclerView, App.LIST_ALL_PROPERTIES,true);
        } else {
            DialogManager.showInfoDialog(UsersActivity.this, "", getResources().getString(R.string.internet_available_msg), false);
        }   
    }

    private AssignPropertiesRequestModel assignPropertiesRequestModel()
    {
        AssignPropertiesRequestModel assignPropertiesRequestModel=new AssignPropertiesRequestModel();
        assignPropertiesRequestModel.setAssignedProperties(Helpers.getPreferenceValue(UsersActivity.this,getResources().getString(R.string.assigned_properties)));
        return assignPropertiesRequestModel;
    }
}
