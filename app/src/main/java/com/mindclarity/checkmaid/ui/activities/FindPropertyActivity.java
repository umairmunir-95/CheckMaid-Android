package com.mindclarity.checkmaid.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.NavigationDrawer;
import com.mindclarity.checkmaid.data.common_models.Users;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.ui.adapters.ExpandableListViewNavDrawerAdapter;
import com.mindclarity.checkmaid.ui.adapters.ListViewSearchUsersAdapter;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.MaterialDatePicker;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.viewmodel.GuidesViewModel;
import com.mindclarity.checkmaid.viewmodel.PropertiesViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindPropertyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tvHeader;
    private ImageView ivPicture;
    private EditText etFindProperty;
    private Button btnGetInfo;
    private ProgressBar progressBar;
    private LinearLayout viewFlipperLayout;

    private LinearLayout llPicture,llNotes;
    private ImageView ivDrawer;
    private Button btnSettings,btnMenu;
    private DrawerLayout drawer,mDrawerLayout;
    private ExpandableListViewNavDrawerAdapter mMenuAdapter;
    private NavigationView navigationView;
    private ExpandableListView expandableList;


    private List<NavigationDrawer> listDataHeader;
    private HashMap<NavigationDrawer, List<String>> listDataChild;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private GuidesViewModel guidesViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_property);
        initializeViews();
        if(Helpers.getPreferenceValue(FindPropertyActivity.this,getResources().getString(R.string.assigned_properties)).equals(""))
        {
            DialogManager.showInfoDialog(FindPropertyActivity.this,"",getResources().getString(R.string.no_property_assignment),false);
        }
    }

    private void initializeViews() {
        drawer = findViewById(R.id.drawer_layout);
        ivDrawer = findViewById(R.id.iv_drawer);
        progressBar=findViewById(R.id.progress_bar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        expandableList = findViewById(R.id.navigationmenu);
        navigationView = findViewById(R.id.nav_view);
        tvHeader = findViewById(R.id.tv_title);
        llNotes=findViewById(R.id.ll_notes);
        llPicture=findViewById(R.id.ll_picture);
        viewFlipperLayout=findViewById(R.id.view_flipper_root);
        ivPicture=findViewById(R.id.iv_picture);
        btnGetInfo=findViewById(R.id.btn_get_info);
        etFindProperty=findViewById(R.id.et_find_property);
        progressBar=findViewById(R.id.progress_bar);

        ivDrawer.setVisibility(View.VISIBLE);
        tvHeader.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.find_a_property));

        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        btnSettings=header.findViewById(R.id.btn_settings);
        btnMenu=header.findViewById(R.id.btn_menu);

        sharedPreferencesHelper=new SharedPreferencesHelper(FindPropertyActivity.this);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindPropertyActivity.this,SettingsActivity.class));
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
        mMenuAdapter = new ExpandableListViewNavDrawerAdapter(FindPropertyActivity.this, listDataHeader, listDataChild, expandableList);
        expandableList.setAdapter(mMenuAdapter);

        guidesViewModel=ViewModelProviders.of(this).get(GuidesViewModel.class);

        
        etFindProperty.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showUsersListPopUp();
                    return true;
                }
                return false;
            }
        });

        btnGetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkManager.isNetworkAvailable(FindPropertyActivity.this))
                {
                    if(!etFindProperty.getText().toString().trim().equals(""))
                    {
                        guidesViewModel.findProperty(FindPropertyActivity.this,progressBar,App.LIST_ALL_GUIDES+"/"+etFindProperty.getText().toString().trim(),true,llPicture,llNotes,ivPicture,viewFlipperLayout);
                    }
                }
                else {
                    DialogManager.showInfoDialog(FindPropertyActivity.this,"",getResources().getString(R.string.internet_available_msg),false);
                }
               }
        });
    }

    private void showUsersListPopUp()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_user_list, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        EditText etSearch=promptsView.findViewById(R.id.et_search);
        ListView lvProperties=promptsView.findViewById(R.id.lv_users);
        TextView tvProperties=promptsView.findViewById(R.id.tv_option);
        ImageView ivCross=promptsView.findViewById(R.id.iv_close);
        tvProperties.setText(getResources().getString(R.string.list_of_properties));
        lvProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView tvSelectedProperty = view.findViewById(R.id.tv_name);
                TextView tvID = view.findViewById(R.id.tv_id);
                etFindProperty.setText(""+tvSelectedProperty.getText().toString());
                alertDialog.cancel();
            }
        });
        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        ArrayList<Users> users=new ArrayList<>();
        String[] assignedProperties= Helpers.getPreferenceValue(FindPropertyActivity.this,getResources().getString(R.string.assigned_properties)).split(",");
        if(assignedProperties.length==0) {
            DialogManager.showInfoDialog(FindPropertyActivity.this, "", getResources().getString(R.string.no_property_assignment), false);
        }
        else{
            for(int i=0;i<assignedProperties.length;i++)
            {
                if(!(assignedProperties[i].trim().equals(""))) {
                    Users users1 = new Users();
                    users1.setUserName(assignedProperties[i]);
                    users1.setUserId("");
                    users.add(users1);
                }
            }
        }
        ListViewSearchUsersAdapter adapter=new ListViewSearchUsersAdapter(FindPropertyActivity.this,users);
        lvProperties.setAdapter(adapter);
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

        if(Helpers.getPreferenceValue(FindPropertyActivity.this,getResources().getString(R.string.user_type)).equals("admin")) {
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
}
