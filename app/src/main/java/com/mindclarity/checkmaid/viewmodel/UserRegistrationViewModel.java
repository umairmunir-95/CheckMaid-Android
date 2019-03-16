package com.mindclarity.checkmaid.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import com.mindclarity.checkmaid.data.repositories.UserRegistrationRepository;

public class UserRegistrationViewModel extends AndroidViewModel {

    private UserRegistrationRepository userRegistrationRepository;

    public UserRegistrationViewModel(@NonNull Application application)
    {
        super(application);
        userRegistrationRepository= new UserRegistrationRepository(application);

    }

    public void registerUser(Context context,ProgressBar progressBar, String userName, String email, String password, String userType,boolean addingCleaner)
    {
        userRegistrationRepository.registerUser(context,progressBar,userName,email,password,userType,addingCleaner);
    }

 }


