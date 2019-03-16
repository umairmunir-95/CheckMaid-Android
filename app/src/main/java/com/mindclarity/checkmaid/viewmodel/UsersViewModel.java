package com.mindclarity.checkmaid.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mindclarity.checkmaid.data.remote.models.users.AssignPropertiesRequestModel;
import com.mindclarity.checkmaid.data.repositories.UsersRepository;

public class UsersViewModel  extends AndroidViewModel {

    private UsersRepository usersRepository;

    public UsersViewModel(@NonNull Application application) {
        super(application);
        usersRepository = new UsersRepository(application);
    }

    public void listAllUsers(Context context, ProgressBar progressBar, ListView listView, String url) {
        usersRepository.listAllUsers(context, progressBar, listView,url);
    }

    public void assignProperties(Context context, ProgressBar progressBar, String url, AssignPropertiesRequestModel assignPropertiesRequestModel) {
        usersRepository.assignProperties(context, progressBar, url,assignPropertiesRequestModel);
    }
    public void deleteAccount(Context context, ProgressBar progressBar, String url) {
        usersRepository.deleteAccount(context, progressBar,url);
    }
}
