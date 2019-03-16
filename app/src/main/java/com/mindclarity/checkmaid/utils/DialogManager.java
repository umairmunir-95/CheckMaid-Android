package com.mindclarity.checkmaid.utils;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.ui.activities.ClockInClockOutActivity;
import com.mindclarity.checkmaid.ui.activities.ClockInClockOutDetailsActivity;
import com.mindclarity.checkmaid.ui.activities.LoginActivity;
import com.mindclarity.checkmaid.ui.activities.PropertiesActivity;
import com.mindclarity.checkmaid.ui.activities.PropertyDetailsActivity;
import com.mindclarity.checkmaid.ui.activities.SettingsActivity;
import com.mindclarity.checkmaid.ui.activities.UsersActivity;
import com.mindclarity.checkmaid.viewmodel.UserRegistrationViewModel;
import com.mindclarity.checkmaid.viewmodel.UsersViewModel;

public class DialogManager {

    public static void showInfoDialog(final Context context, final String title, final String body, final boolean proceed) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.custom_alert_dialog, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                ImageView ivClose = promptsView.findViewById(R.id.iv_close);
                TextView tvTitle = promptsView.findViewById(R.id.tv_option);
                TextView tvBody = promptsView.findViewById(R.id.tv_body);
                Button btnNo = promptsView.findViewById(R.id.btn_no);
                Button btnYes = promptsView.findViewById(R.id.btn_yes);
                ivClose.setVisibility(View.VISIBLE);
                btnNo.setVisibility(View.GONE);
                tvTitle.setVisibility(View.GONE);
                tvTitle.setText(title);
                tvBody.setText(body);
                btnYes.setText(context.getResources().getString(R.string.dismiss));
                tvBody.setTextColor(context.getResources().getColor(R.color.black));
                tvBody.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivClose.setBackground(proceed?context.getResources().getDrawable(R.drawable.ic_done):context.getResources().getDrawable(R.drawable.ic_info));

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(proceed)
                        {
                            context.startActivity(new Intent(context,ClockInClockOutDetailsActivity.class));
                        }
                        alertDialog.cancel();
                    }
                });
                alertDialogBuilder.setCancelable(false);
                alertDialog.show();
            }
        });
    }


    public static void showVerificationPinPopuop(final Context context, final ProgressBar progressBar,final boolean addingCleaner) {
        final UserRegistrationViewModel userRegistrationViewModel = ViewModelProviders.of((FragmentActivity) context).get(UserRegistrationViewModel.class);

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.dialog_pin_entry, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                final PinView etPinView= promptsView.findViewById(R.id.pin_view);
                final Button btnSubmit=promptsView.findViewById(R.id.btn_submit);

                etPinView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                        if(etPinView.getText().toString().equals(Helpers.getPreferenceValue(context,context.getResources().getString(R.string.verification_code)))) {
                            String username=Helpers.getPreferenceValue(context,context.getResources().getString(R.string.username));
                            String email=Helpers.getPreferenceValue(context,context.getResources().getString(R.string.email));
                            String password=Helpers.getPreferenceValue(context,context.getResources().getString(R.string.password));
                            String userType=Helpers.getPreferenceValue(context,context.getResources().getString(R.string.user_type));
                            userRegistrationViewModel.registerUser(context,progressBar,username,email,password,userType,false);
                        }
                        else{
                            Toast.makeText(context, context.getResources().getString(R.string.verification_error), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialog.setCancelable(false);
                alertDialogBuilder.setCancelable(false);
                if(addingCleaner)
                {
                    String username=Helpers.getPreferenceValue(context,context.getResources().getString(R.string.username));
                    String email=Helpers.getPreferenceValue(context,context.getResources().getString(R.string.email));
                    String password=Helpers.getPreferenceValue(context,context.getResources().getString(R.string.password));
                    String userType=Helpers.getPreferenceValue(context,context.getResources().getString(R.string.user_type));
                    userRegistrationViewModel.registerUser(context,progressBar,username,email,password,userType,true);
                }
                else {
                    alertDialog.show();
                }
            }
        });
    }

    public static void showForgetPasswordVerificationPinPopuop(final Context context, final ProgressBar progressBar, final EditText etPassword,final EditText etConfirmPassword,final Button btnChangePassword,final Button btnSubmit) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.dialog_pin_entry, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                final PinView etPinView= promptsView.findViewById(R.id.pin_view);
                final Button btnSubmit=promptsView.findViewById(R.id.btn_submit);

                etPinView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                        if(etPinView.getText().toString().equals(Helpers.getPreferenceValue(context,context.getResources().getString(R.string.verification_code)))) {
                            etPassword.setVisibility(View.VISIBLE);
                            etConfirmPassword.setVisibility(View.VISIBLE);
                            btnChangePassword.setVisibility(View.VISIBLE);
                            btnSubmit.setVisibility(View.GONE);
                        }
                        else{
                            Toast.makeText(context, context.getResources().getString(R.string.verification_error), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialog.setCancelable(false);
                alertDialogBuilder.setCancelable(false);
                alertDialog.show();
            }
        });
    }


    public static void showNetworkInfo(final Context context, final String title, final String body, final String requestType) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.custom_alert_dialog, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                ImageView ivClose = promptsView.findViewById(R.id.iv_close);
                TextView tvTitle = promptsView.findViewById(R.id.tv_option);
                TextView tvBody = promptsView.findViewById(R.id.tv_body);
                Button btnNo = promptsView.findViewById(R.id.btn_no);
                Button btnYes = promptsView.findViewById(R.id.btn_yes);
                ivClose.setVisibility(View.VISIBLE);
                btnNo.setVisibility(View.GONE);
                tvTitle.setVisibility(View.GONE);
                tvTitle.setText(title);
                tvBody.setText(body);
                btnYes.setText(context.getResources().getString(R.string.dismiss));
                tvBody.setTextColor(context.getResources().getColor(R.color.black));
                tvBody.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivClose.setBackground(context.getResources().getDrawable(R.drawable.ic_info));

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(requestType.equals("userRegistration"))
                        {
                            context.startActivity(new Intent(context,LoginActivity.class));
                        }
                        else if(requestType.equals("addCleaner"))
                        {
                            context.startActivity(new Intent(context,UsersActivity.class));
                        }
                        else if(requestType.equals("addProperty"))
                        {
                            context.startActivity(new Intent(context,PropertiesActivity.class));
                        }
                        else if(requestType.equals("createGuide"))
                        {
                            context.startActivity(new Intent(context,PropertyDetailsActivity.class));
                        }
                        else if(requestType.equals("clockIn"))
                        {
                            context.startActivity(new Intent(context,ClockInClockOutDetailsActivity.class));
                        }
                        else if(requestType.equals("clockOut"))
                        {
                            context.startActivity(new Intent(context,ClockInClockOutActivity.class));
                        }
                        else if(requestType.equals("createPhotoLog"))
                        {
                            context.startActivity(new Intent(context,ClockInClockOutDetailsActivity.class));
                        }
                        else if(requestType.equals("deleteAccount"))
                        {
                            SharedPreferencesHelper sharedPreferencesHelper=new SharedPreferencesHelper(context);
                            sharedPreferencesHelper.clearPreferenceStore();
                            context.startActivity(new Intent(context,LoginActivity.class));
                        }
                        alertDialog.cancel();
                    }
                });
                alertDialogBuilder.setCancelable(false);
                alertDialog.show();
            }
        });
    }


    public static void showSettingsDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.needpermission));
        builder.setMessage(context.getResources().getString(R.string.permissionmessage));
        builder.setPositiveButton(context.getResources().getString(R.string.gotosettings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings(context);
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
    private static void openSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        ((Activity)context).startActivityForResult(intent, 101);
    }

    public static void gpsNotEnabledPopup(final Context context) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.custom_alert_dialog, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                ImageView ivClose = promptsView.findViewById(R.id.iv_close);
                TextView tvTitle = promptsView.findViewById(R.id.tv_option);
                TextView tvBody = promptsView.findViewById(R.id.tv_body);
                Button btnNo = promptsView.findViewById(R.id.btn_no);
                Button btnYes = promptsView.findViewById(R.id.btn_yes);
                ivClose.setVisibility(View.GONE);
                btnNo.setVisibility(View.GONE);
                tvTitle.setText(R.string.message);
                tvBody.setText(R.string.gps_not_enabled);
                btnYes.setText(context.getResources().getString(R.string.enable));
                tvBody.setTextColor(context.getResources().getColor(R.color.black));
                tvBody.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        ((Activity) context).startActivity(intent);
                    }
                });
                alertDialogBuilder.setCancelable(false);
                alertDialog.show();
            }
        });
    }


    public static void showConfirmationPopup(final Context context, final String confirmationType,final ProgressBar progressBar) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.custom_alert_dialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        ImageView ivClose = promptsView.findViewById(R.id.iv_close);
        TextView tvTitle = promptsView.findViewById(R.id.tv_option);
        TextView tvBody = promptsView.findViewById(R.id.tv_body);
        Button btnNo = promptsView.findViewById(R.id.btn_no);
        Button btnYes = promptsView.findViewById(R.id.btn_yes);
        ivClose.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        btnYes.setText(context.getResources().getString(R.string.dismiss));
        tvBody.setTextColor(context.getResources().getColor(R.color.black));
        tvBody.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        ivClose.setBackground(context.getResources().getDrawable(R.drawable.ic_info));
        btnNo.setText(context.getResources().getString(R.string.no));
        btnYes.setText(context.getResources().getString(R.string.yes));
        if(confirmationType.equals(context.getResources().getString(R.string.logout))) {
            tvBody.setText(context.getResources().getString(R.string.logout_confirmation));
        }
        else if(confirmationType.equals(context.getResources().getString(R.string.delete_acconunt)))
        {
            tvBody.setText(context.getResources().getString(R.string.delete_confirmation));
        }
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(confirmationType.equals(context.getResources().getString(R.string.logout)))
                {
                    SharedPreferencesHelper sharedPreferencesHelper=new SharedPreferencesHelper(context);
                    sharedPreferencesHelper.clearPreferenceStore();
                    Intent intent=new Intent(context,LoginActivity.class);
                    context.startActivity(intent);
                }
                else if(confirmationType.equals(context.getResources().getString(R.string.delete_acconunt)))
                {
                    String url= App.REGISTER_USER+"/"+App.DELETE_ACCOUNT+"/"+Helpers.getPreferenceValue(context,context.getResources().getString(R.string.user_id));
                    UsersViewModel usersViewModel=ViewModelProviders.of((FragmentActivity) context).get(UsersViewModel.class);
                    usersViewModel.deleteAccount(context,progressBar,url);
                }
                alertDialog.cancel();
            }
        });
        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
    }
}
