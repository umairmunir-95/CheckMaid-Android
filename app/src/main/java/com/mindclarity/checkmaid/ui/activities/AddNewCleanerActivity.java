package com.mindclarity.checkmaid.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.utils.SendEmail;

public class AddNewCleanerActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack;
    private EditText etUsername,etEmail,etPassword,etConfirmPassword;
    private Button btnAddUser;
    private ProgressBar progressBar;
    private SharedPreferencesHelper sharedPreferencesHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_cleaner);
        initializeViews();
    }

    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        progressBar=findViewById(R.id.progress_bar);
        btnAddUser=findViewById(R.id.btn_add_user);
        etUsername=findViewById(R.id.et_username);
        etEmail=findViewById(R.id.et_email);
        etPassword=findViewById(R.id.et_password);
        etConfirmPassword=findViewById(R.id.et_confirm_password);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.add_user));
        sharedPreferencesHelper=new SharedPreferencesHelper(AddNewCleanerActivity.this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNewCleanerActivity.this,UsersActivity.class);
                startActivity(i);
            }
        });

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }

    private void addUser()
    {
        if(NetworkManager.isNetworkAvailable(AddNewCleanerActivity.this)) {
            if (!Helpers.isEditTextEmpty(etUsername) && !Helpers.isEditTextEmpty(etEmail) && !Helpers.isEditTextEmpty(etPassword)) {
                sharedPreferencesHelper.setString(getResources().getString(R.string.username), etUsername.getText().toString());
                sharedPreferencesHelper.setString(getResources().getString(R.string.email), etEmail.getText().toString());
                sharedPreferencesHelper.setString(getResources().getString(R.string.password), etPassword.getText().toString());
                sharedPreferencesHelper.setString(getResources().getString(R.string.user_type), "cleaner");
                if (Helpers.isValidEmail(etEmail.getText().toString())) {
                    if (etConfirmPassword.getText().toString().trim().equals(etPassword.getText().toString().trim())) {
                        new SendEmail(AddNewCleanerActivity.this, progressBar, etEmail.getText().toString(), getResources().getString(R.string.email_subject), getResources().getString(R.string.email_message_cleaner),true).execute();
                    }
                    else
                    {
                        etConfirmPassword.setError(getResources().getString(R.string.password_not_matched));
                    }
                }
                else{
                    DialogManager.showInfoDialog(AddNewCleanerActivity.this,"",getResources().getString(R.string.invalid_email),false);
                }
            }
            else{
                DialogManager.showInfoDialog(AddNewCleanerActivity.this,"",getResources().getString(R.string.user_registration_message),false);
            }
        }
        else {
            DialogManager.showInfoDialog(AddNewCleanerActivity.this,"",getResources().getString(R.string.internet_available_msg),false);
        }
    }
}
