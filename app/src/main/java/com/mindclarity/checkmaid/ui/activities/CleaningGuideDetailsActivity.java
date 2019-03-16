package com.mindclarity.checkmaid.ui.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.data.remote.models.guides.create_guide.CreateGuideRequest;
import com.mindclarity.checkmaid.data.remote.models.guides.update_guide.UpdateGuideRquestModel;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.ImageUtils;
import com.mindclarity.checkmaid.viewmodel.GuidesViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class CleaningGuideDetailsActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack,ivEdit,ivPicture;
    private RelativeLayout rlEdit;
    private LinearLayout llCreateNotes;
    private LinearLayout viewFlipperLayout;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private GuidesViewModel guidesViewModel;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cleaning_guide);
        initializeViews();
        populateFields();
        splitCheckInNotes();
    }

    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        ivEdit=findViewById(R.id.iv_edit);
        ivPicture=findViewById(R.id.iv_picture);
        rlEdit=findViewById(R.id.rl_edit_property);
        btnSubmit=findViewById(R.id.btn_submit);
        llCreateNotes=findViewById(R.id.ll_4);
        progressBar=findViewById(R.id.progress_bar);
        viewFlipperLayout=findViewById(R.id.view_flipper_root);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        rlEdit.setVisibility(View.VISIBLE);
        llCreateNotes.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.cleaning_guide));
        sharedPreferencesHelper=new SharedPreferencesHelper(this);
        guidesViewModel=ViewModelProviders.of(this).get(GuidesViewModel.class);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CleaningGuideDetailsActivity.this,CreateCleaningGuideActivity.class);
                startActivity(i);
            }
        });

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForEdit();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppStore.CLEANING_ID.equals(""))
                { 
                    guidesViewModel.createGuide(CleaningGuideDetailsActivity.this,progressBar,createGuideRequest());
                }
                else
                {
                    guidesViewModel.updateGuide(CleaningGuideDetailsActivity.this,progressBar, App.LIST_ALL_GUIDES+"/"+AppStore.CLEANING_ID,updateGuideRquestModel());
                }
            }
        });
    }

    private void populateFields()
    {
        ivPicture.setImageBitmap(ImageUtils.base64ToBitmap(AppStore.CLEANING_IMAGE));
    }

    private void goForEdit()
    {
        sharedPreferencesHelper.setString(App.INTENT_FROM,getResources().getString(R.string.cleaning_guide));
        Intent i=new Intent(CleaningGuideDetailsActivity.this,PaintActivity.class);
        startActivity(i);
    }

    private CreateGuideRequest createGuideRequest()
    {
        CreateGuideRequest createGuideRequest=new CreateGuideRequest();
        createGuideRequest.setPropertyName(AppStore.CLEANING_PROPERTY_NAME.trim());
        createGuideRequest.setPropertyAddress(AppStore.CLEANING_PROPERTY_ADDRESS.trim());
        createGuideRequest.setNotes(AppStore.CLEANING_NOTES.trim());
        createGuideRequest.setGuideType("cleaning");
        createGuideRequest.setRequireGPSProof(false);
        createGuideRequest.setImage(ImageUtils.bitmapToBase64(ImageUtils.imageViewToBitmap(CleaningGuideDetailsActivity.this,ivPicture)));
        return createGuideRequest;
    }

    private UpdateGuideRquestModel updateGuideRquestModel()
    {
        UpdateGuideRquestModel updateGuideRquestModel=new UpdateGuideRquestModel();
        updateGuideRquestModel.setImage(ImageUtils.bitmapToBase64(ImageUtils.imageViewToBitmap(CleaningGuideDetailsActivity.this,ivPicture)));
        updateGuideRquestModel.setNotes(AppStore.CLEANING_NOTES);
        updateGuideRquestModel.setRequireGPSProof(false);
        return updateGuideRquestModel;
    }

    private void splitCheckInNotes()
    {
        int totalSteps=0;
        ArrayList<String> stepNotes=new ArrayList<>();
        String[] notesSteps = AppStore.CLEANING_NOTES.split("~");
        for(int i=0;i<notesSteps.length;i++)
        {
            if(!notesSteps[i].toString().trim().equals(""))
            {
                stepNotes.add(notesSteps[i].toString().trim());
                totalSteps++;
            }
        }
        final ViewFlipper viewFlipper = new ViewFlipper(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewFlipper.setLayoutParams(layoutParams);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(false);

        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);

        for(int i=0;i<notesSteps.length;i++) {
            TextView textView = new TextView(this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            textView.setLayoutParams(params);
            textView.setTextSize(20f);
            textView.setText(""+i+") "+ notesSteps[i]);
            viewFlipper.addView(textView);

        }
        viewFlipperLayout.addView(viewFlipper);
        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        });
        viewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {

                int displayedChild = viewFlipper.getDisplayedChild();
                int childCount = viewFlipper.getChildCount();
                if(displayedChild == childCount - 1)
                {
                    viewFlipper.stopFlipping();
                }
            }
        });
    }
}
