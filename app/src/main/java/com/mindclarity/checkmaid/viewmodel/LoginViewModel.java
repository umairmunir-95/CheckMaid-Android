package com.mindclarity.checkmaid.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import com.mindclarity.checkmaid.data.repositories.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository loginRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository(application);
    }

    public void login(Context context, ProgressBar progressBar, String url) {
        loginRepository.login(context, progressBar, url);
    }
}