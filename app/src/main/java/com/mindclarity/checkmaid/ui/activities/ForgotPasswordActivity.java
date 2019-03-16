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
import android.widget.TextView;

import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.utils.ForgotPassword;
import com.mindclarity.checkmaid.utils.Helpers;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack;
    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btnSubmit, btnResetPassword;
    private ProgressBar progressBar;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeViews();
    }

    public void onBackPressed() {
    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        progressBar=findViewById(R.id.progress_bar);
        etEmail=findViewById(R.id.et_email);
        etPassword=findViewById(R.id.et_password);
        etConfirmPassword=findViewById(R.id.et_confirm_password);
        btnResetPassword=findViewById(R.id.btn_change_password);
        btnSubmit=findViewById(R.id.btn_submit);

        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.change_password));
        sharedPreferencesHelper=new SharedPreferencesHelper(this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helpers.isValidEmail(etEmail.getText().toString())) {
                    sharedPreferencesHelper.setString(getResources().getString(R.string.verification_code), Helpers.generateVerificationCode());
                    new ForgotPassword(ForgotPasswordActivity.this, progressBar, etEmail.getText().toString(), getResources().getString(R.string.forgot_password_request_subject), getResources().getString(R.string.forgot_password_request_body) + "\n\n\t\t Verification Code : " + Helpers.getPreferenceValue(ForgotPasswordActivity.this, getResources().getString(R.string.verification_code)),etPassword,etConfirmPassword,btnResetPassword,btnSubmit).execute();
                }
                else
                {
                    etEmail.setError(getResources().getString(R.string.required));
                }
            }
        });
    }
}
