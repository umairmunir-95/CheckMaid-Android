package com.mindclarity.checkmaid.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;



import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.util.Properties;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.utils.SendEmail;
import com.mindclarity.checkmaid.viewmodel.UserRegistrationViewModel;

public class UserRegistrationActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack,ivHelp;
    private EditText etUsername,etEmail,etPassword,etConfirmPassword;
    private RadioGroup rgAccountType;
    private RadioButton rbAdmin,rbCleaner;
    private Button btnCreateAccount;
    private ProgressBar progressBar;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        initializeViews();
    }

    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        ivHelp=findViewById(R.id.iv_help);
        progressBar=findViewById(R.id.progress_bar);
        btnCreateAccount=findViewById(R.id.btn_create_account);
        etUsername=findViewById(R.id.et_username);
        etEmail=findViewById(R.id.et_email);
        etPassword=findViewById(R.id.et_password);
        etConfirmPassword=findViewById(R.id.et_confirm_password);
        rgAccountType=findViewById(R.id.rg_user_type);
        rbAdmin=findViewById(R.id.rb_admin);
        rbCleaner=findViewById(R.id.rb_cleaner);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.user_registration));
        sharedPreferencesHelper=new SharedPreferencesHelper(UserRegistrationActivity.this);

        Helpers.setEditTextError(UserRegistrationActivity.this,etEmail);
        Helpers.setEditTextError(UserRegistrationActivity.this,etPassword);
        Helpers.setEditTextError(UserRegistrationActivity.this,etUsername);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helpers.getPreferenceValue(UserRegistrationActivity.this,App.INTENT_FROM).equals(getResources().getString(R.string.login)))
                {
                    Intent i = new Intent(UserRegistrationActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(UserRegistrationActivity.this, UsersActivity.class);
                    startActivity(i);
                }
            }
        });

        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.showInfoDialog(UserRegistrationActivity.this,"",getResources().getString(R.string.admin_cleaner_difference),false);
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount()
    {
        if(NetworkManager.isNetworkAvailable(UserRegistrationActivity.this)) {
            if (!Helpers.isEditTextEmpty(etUsername) && !Helpers.isEditTextEmpty(etEmail) && !Helpers.isEditTextEmpty(etPassword)) {
                sharedPreferencesHelper.setString(getResources().getString(R.string.username), etUsername.getText().toString());
                sharedPreferencesHelper.setString(getResources().getString(R.string.email), etEmail.getText().toString());
                sharedPreferencesHelper.setString(getResources().getString(R.string.password), etPassword.getText().toString());
                sharedPreferencesHelper.setString(getResources().getString(R.string.verification_code), Helpers.generateVerificationCode());

                if (rbAdmin.isChecked()) {
                    sharedPreferencesHelper.setString(getResources().getString(R.string.user_type), "admin");
                } else {
                    sharedPreferencesHelper.setString(getResources().getString(R.string.user_type), "cleaner");
                }
                if (Helpers.isValidEmail(etEmail.getText().toString())) {
                    if (etConfirmPassword.getText().toString().trim().equals(etPassword.getText().toString().trim())) {
                        new SendEmail(UserRegistrationActivity.this, progressBar, etEmail.getText().toString(), getResources().getString(R.string.email_subject), getResources().getString(R.string.email_message) + " : " + Helpers.getPreferenceValue(UserRegistrationActivity.this, getResources().getString(R.string.verification_code)),false).execute();
                    }
                    else
                    {
                        etConfirmPassword.setError(getResources().getString(R.string.password_not_matched));
                    }
                }
                else{
                    DialogManager.showInfoDialog(UserRegistrationActivity.this,"",getResources().getString(R.string.invalid_email),false);
                }
            }
            else{
                DialogManager.showInfoDialog(UserRegistrationActivity.this,"",getResources().getString(R.string.user_registration_message),false);
            }
        }
        else {
            DialogManager.showInfoDialog(UserRegistrationActivity.this,"",getResources().getString(R.string.internet_available_msg),false);
        }
    }
}
