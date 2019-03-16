package com.mindclarity.checkmaid.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;

public class SettingsActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack;
    private Button btnLogOut,btnDeleteAccount;
    private ProgressBar progressBar;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeViews();
    }

    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        btnDeleteAccount=findViewById(R.id.btn_delete_account);
        btnLogOut=findViewById(R.id.btn_logout);
        progressBar=findViewById(R.id.progress_bar);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.settings));

        sharedPreferencesHelper=new SharedPreferencesHelper(SettingsActivity.this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helpers.getPreferenceValue(SettingsActivity.this,getResources().getString(R.string.user_type)).equals(getResources().getString(R.string.admin)))
                {
                    Intent i = new Intent(SettingsActivity.this, PropertiesActivity.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(SettingsActivity.this, FindPropertyActivity.class);
                    startActivity(i);
                }
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.showConfirmationPopup(SettingsActivity.this,getResources().getString(R.string.logout),progressBar);
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.showConfirmationPopup(SettingsActivity.this,getResources().getString(R.string.delete_acconunt),progressBar);
            }
        });
    }
}

