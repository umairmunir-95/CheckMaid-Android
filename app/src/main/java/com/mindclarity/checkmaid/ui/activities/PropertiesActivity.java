package com.mindclarity.checkmaid.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.NavigationDrawer;
import com.mindclarity.checkmaid.data.common_models.Properties;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.ui.adapters.ExpandableListViewNavDrawerAdapter;
import com.mindclarity.checkmaid.ui.adapters.GridViewPropertiesAdapter;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.utils.RecyclerViewTouchListener;
import com.mindclarity.checkmaid.viewmodel.PropertiesViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PropertiesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddNew;
    private TextView tvHeaderTitle;
    private ImageView ivDrawer;
    private ProgressBar progressBar;
    private Button btnSettings,btnMenu;
    private DrawerLayout drawer,mDrawerLayout;
    private ExpandableListViewNavDrawerAdapter mMenuAdapter;
    private NavigationView navigationView;
    private ExpandableListView expandableList;
    private List<NavigationDrawer> listDataHeader;
    private HashMap<NavigationDrawer, List<String>> listDataChild;
    private PropertiesViewModel propertiesViewModel;
    private SharedPreferencesHelper sharedPreferencesHelper;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties);
        initializeViews();
        loadProperties();
    }
    private void initializeViews()
    {
        recyclerView=findViewById(R.id.recycler_view);
        fabAddNew=findViewById(R.id.fab_add_new);
        drawer = findViewById(R.id.drawer_layout);
        tvHeaderTitle = findViewById(R.id.tv_title);
        ivDrawer = findViewById(R.id.iv_drawer);
        progressBar=findViewById(R.id.progress_bar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        expandableList = findViewById(R.id.navigationmenu);
        navigationView = findViewById(R.id.nav_view);
        tvHeaderTitle.setVisibility(View.VISIBLE);
        ivDrawer.setVisibility(View.VISIBLE);
        tvHeaderTitle.setText(getResources().getString(R.string.properties));

        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        btnSettings=header.findViewById(R.id.btn_settings);
        btnMenu=header.findViewById(R.id.btn_menu);

        propertiesViewModel = ViewModelProviders.of(this).get(PropertiesViewModel.class);
        sharedPreferencesHelper=new SharedPreferencesHelper(PropertiesActivity.this);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PropertiesActivity.this,SettingsActivity.class));
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(navigationView);
            }
        });
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String selectedItem = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                Log.v("Group", "click");
                return false;
            }
        });
        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        prepareListData();
        mMenuAdapter = new ExpandableListViewNavDrawerAdapter(PropertiesActivity.this, listDataHeader, listDataChild, expandableList);
        expandableList.setAdapter(mMenuAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppStore.PROPERTY_ID="";
                sharedPreferencesHelper.setString(App.INTENT_FROM,getResources().getString(R.string.add_property));
                Intent i=new Intent(PropertiesActivity.this,AddNewPropertyActivity.class);
                startActivity(i);
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        NavigationDrawer properties = new NavigationDrawer();
        properties.setIconName(getString(R.string.properties));
        properties.setIconImg(R.drawable.ic_properties);
        properties.setIsEnabled(true);
        NavigationDrawer users = new NavigationDrawer();
        users.setIconName(getString(R.string.users));
        users.setIconImg(R.drawable.ic_users);
        users.setIsEnabled(true);
        NavigationDrawer timeSheets = new NavigationDrawer();
        timeSheets.setIconName(getString(R.string.timesheets));
        timeSheets.setIconImg(R.drawable.ic_timesheets);
        timeSheets.setIsEnabled(true);
        NavigationDrawer photoLogs = new NavigationDrawer();
        photoLogs.setIconName(getString(R.string.photologs));
        photoLogs.setIconImg(R.drawable.ic_photologs);
        photoLogs.setIsEnabled(true);
        NavigationDrawer damageReports = new NavigationDrawer();
        damageReports.setIconName(getString(R.string.damage_reports));
        damageReports.setIconImg(R.drawable.ic_damage_reports);
        damageReports.setIsEnabled(true);
        //////////////////////////User Portion///////////////////////////
        NavigationDrawer findProperty = new NavigationDrawer();
        findProperty.setIconName(getString(R.string.find_a_property));
        findProperty.setIconImg(R.drawable.ic_find_user);
        findProperty.setIsEnabled(true);
        NavigationDrawer cleaningGuide = new NavigationDrawer();
        cleaningGuide.setIconName(getString(R.string.cleaning_guides));
        cleaningGuide.setIconImg(R.drawable.ic_guides);
        cleaningGuide.setIsEnabled(true);
        NavigationDrawer clockInClockOut = new NavigationDrawer();
        clockInClockOut.setIconName(getString(R.string.clock_in_clock_out));
        clockInClockOut.setIconImg(R.drawable.ic_clockin_clockout);
        clockInClockOut.setIsEnabled(true);
        NavigationDrawer timeSheet = new NavigationDrawer();
        timeSheet.setIconName(getString(R.string.timesheet));
        timeSheet.setIconImg(R.drawable.ic_timesheets);
        timeSheet.setIsEnabled(true);

        if(Helpers.getPreferenceValue(PropertiesActivity.this,getResources().getString(R.string.user_type)).equals("admin")) {
            listDataHeader.add(properties);
            listDataHeader.add(users);
            listDataHeader.add(timeSheets);
            listDataHeader.add(photoLogs);
            listDataHeader.add(damageReports);
        }
        else {
            listDataHeader.add(findProperty);
            listDataHeader.add(cleaningGuide);
            listDataHeader.add(clockInClockOut);
            listDataHeader.add(timeSheet);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }
    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return false;
    }

    private void loadProperties() {
        if (NetworkManager.isNetworkAvailable(PropertiesActivity.this)) {
            propertiesViewModel.getAllProperties(PropertiesActivity.this, progressBar,recyclerView, App.LIST_ALL_PROPERTIES,false);
        } else {
            DialogManager.showInfoDialog(PropertiesActivity.this, "", getResources().getString(R.string.internet_available_msg), false);
        }
    }

}
