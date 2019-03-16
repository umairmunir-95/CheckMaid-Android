package com.mindclarity.checkmaid.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.viewmodel.LoginViewModel;
import com.mindclarity.checkmaid.viewmodel.UserRegistrationViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister,tvForgotPassword;
    private ProgressBar progressBar;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
    }

    public void onBackPressed() {

    }


    private void initializeViews()
    {
        etEmail=findViewById(R.id.et_email);
        etPassword=findViewById(R.id.et_password);
        btnLogin=findViewById(R.id.btn_login);
        progressBar=findViewById(R.id.progress_bar);
        tvRegister=findViewById(R.id.tv_register);
        tvForgotPassword=findViewById(R.id.tv_forgot_password);
        sharedPreferencesHelper=new SharedPreferencesHelper(LoginActivity.this);
        loginViewModel=ViewModelProviders.of(this).get(LoginViewModel.class);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesHelper.setString(App.INTENT_FROM,getResources().getString(R.string.login));
                Intent i=new Intent(LoginActivity.this,UserRegistrationActivity.class);
                startActivity(i);
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        if(Helpers.isLoggedIn(LoginActivity.this))
        {
            if(Helpers.getPreferenceValue(LoginActivity.this,getResources().getString(R.string.user_type)).equals(getResources().getString(R.string.admin).toLowerCase()))
            {
                Intent i = new Intent(LoginActivity.this, PropertiesActivity.class);
                startActivity(i);
            }
            else
            {
                Intent i = new Intent(LoginActivity.this, FindPropertyActivity.class);
                startActivity(i);
            }
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkManager.isNetworkAvailable(LoginActivity.this)) {
                    if (etEmail.getText().toString().trim().equals("")) {
                        etEmail.setError(getResources().getString(R.string.required));
                    } else if (etPassword.getText().toString().trim().equals("")) {
                        etPassword.setError(getResources().getString(R.string.required));
                    }
                    else{
                        loginViewModel.login(LoginActivity.this,progressBar,App.LOGIN+etEmail.getText().toString().trim()+"_"+etPassword.getText().toString().trim());
                    }
                }
                else {
                    DialogManager.showInfoDialog(LoginActivity.this, "", getResources().getString(R.string.internet_available_msg), false);
                }
            }
        });
    }

}
