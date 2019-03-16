package com.mindclarity.checkmaid.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mindclarity.checkmaid.data.remote.models.properties.add_property.request.AddPropertyRequest;
import com.mindclarity.checkmaid.data.repositories.PropertiesRepository;
import com.mindclarity.checkmaid.data.repositories.UserRegistrationRepository;

public class PropertiesViewModel extends AndroidViewModel {

    private PropertiesRepository propertiesRepository;

    public PropertiesViewModel(@NonNull Application application)
    {
        super(application);
        propertiesRepository = new PropertiesRepository(application);
    }

    public void addProperty(Context context, ProgressBar progressBar, AddPropertyRequest addPropertyRequest)
    {
        propertiesRepository.addProperty(context,progressBar,addPropertyRequest);
    }

    public void editProperty(Context context, ProgressBar progressBar,String url, AddPropertyRequest addPropertyRequest)
    {
        propertiesRepository.editProperty(context,progressBar,url,addPropertyRequest);
    }

    public void getAllProperties(Context context, ProgressBar progressBar, RecyclerView recyclerView,String url,boolean isAssinProperties)
    {
        propertiesRepository.getAllProperties(context,progressBar,recyclerView,url, isAssinProperties);
    }

    public void getPropertyByName(Context context, ProgressBar progressBar, Button button, EditText editText,String url)
    {
        propertiesRepository.getPropertyByName(context,progressBar,button,editText,url);
    }
}