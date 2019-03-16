package com.mindclarity.checkmaid.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.ImageUtils;

import java.io.File;

public class CreateCheckInGuideActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack,ivImage;
    private Button btnNext;
    private LinearLayout llCreateGuide;
    private EditText etNotes,etNotes2,etNotes3,etNotes4,etNotes5;
    private EditText etNotes6,etNotes7,etNotes8,etNotes9;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_checkin_guide);
        initializeViews();
        populateFields();
    }

    public void onBackPressed() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivImage.setImageBitmap(bitmap);
            File file = new File(AppStore.CHECK_IN_IMAGE_PATH);
            file.delete();
            String imagePath = ImageUtils.tempFileImage(CreateCheckInGuideActivity.this, bitmap,AppStore.CHECK_IN_ID+AppStore.CHECK_IN_PROPERTY_NAME);
            AppStore.CHECK_IN_IMAGE=ImageUtils.bitmapToBase64(bitmap);
            AppStore.CHECK_IN_IMAGE_PATH=imagePath;
        }
    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        btnNext=findViewById(R.id.btn_next);
        ivImage=findViewById(R.id.iv_picture);
        etNotes=findViewById(R.id.et_notes);
        etNotes2=findViewById(R.id.et_notes2);
        etNotes3=findViewById(R.id.et_notes3);
        etNotes4=findViewById(R.id.et_notes4);
        etNotes5=findViewById(R.id.et_notes5);
        etNotes6=findViewById(R.id.et_notes6);
        etNotes7=findViewById(R.id.et_notes7);
        etNotes8=findViewById(R.id.et_notes8);
        etNotes9=findViewById(R.id.et_notes9);
        llCreateGuide=findViewById(R.id.ll_1);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        etNotes.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
        llCreateGuide.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.checkin_guide));
        sharedPreferencesHelper=new SharedPreferencesHelper(CreateCheckInGuideActivity.this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateCheckInGuideActivity.this, PropertyDetailsActivity.class);
                startActivity(i);
            }
        });

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNotes.getText().toString().trim().equals("") && etNotes2.getText().toString().trim().equals("")
                        && etNotes3.getText().toString().trim().equals("") && etNotes4.getText().toString().trim().equals("")
                        && etNotes5.getText().toString().trim().equals("") && etNotes6.getText().toString().trim().equals("")
                        && etNotes7.getText().toString().trim().equals("") && etNotes8.getText().toString().trim().equals("")
                        && etNotes9.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please add atleast one step to proceed.",Toast.LENGTH_LONG).show();
                }
                else {
                    ivImage.buildDrawingCache();
                    Bitmap bitmap = ivImage.getDrawingCache();
                    String imagePath = ImageUtils.tempFileImage(CreateCheckInGuideActivity.this, bitmap,AppStore.CHECK_IN_ID+AppStore.CHECK_IN_PROPERTY_NAME);
                    AppStore.CHECK_IN_PROPERTY_NAME=AppStore.PROPERTY_NAME;
                    AppStore.CHECK_IN_PROPERTY_ADDRESS=AppStore.PROPERTY_ADDRESS;
                    AppStore.CHECK_IN_NOTES=etNotes.getText().toString().trim();
                    AppStore.CHECK_IN_IMAGE_PATH=imagePath;

                    AppStore.CHECK_IN_NOTES=etNotes.getText().toString().trim()+"~"+
                                            etNotes2.getText().toString().trim()+"~"+
                                            etNotes3.getText().toString().trim()+"~"+
                                            etNotes4.getText().toString().trim()+"~"+
                                            etNotes5.getText().toString().trim()+"~"+
                                            etNotes6.getText().toString().trim()+"~"+
                                            etNotes7.getText().toString().trim()+"~"+
                                            etNotes8.getText().toString().trim()+"~"+
                                            etNotes9.getText().toString().trim()+"~";
                    Intent i = new Intent(CreateCheckInGuideActivity.this, CheckInGuideDetailsActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void populateFields()
    {
        ivImage.setImageBitmap(ImageUtils.base64ToBitmap(AppStore.CHECK_IN_IMAGE));
        String[] notesSteps = AppStore.CHECK_IN_NOTES.split("~");
        for(int i=0;i<notesSteps.length;i++)
        {
            if(!notesSteps[i].toString().trim().equals(""))
            {
                if(i==0)
                {
                    etNotes.setText(notesSteps[0].trim());
                }
                else if(i==1)
                {
                    etNotes2.setText(notesSteps[1].trim());
                }
                else if(i==2)
                {
                    etNotes3.setText(notesSteps[2].trim());
                }
                else if(i==3)
                {
                    etNotes4.setText(notesSteps[3].trim());
                }
                else if(i==4)
                {
                    etNotes5.setText(notesSteps[4].trim());
                }
                else if(i==5)
                {
                    etNotes6.setText(notesSteps[5].trim());
                }
                else if(i==6)
                {
                    etNotes7.setText(notesSteps[6].trim());
                }
                else if(i==7)
                {
                    etNotes8.setText(notesSteps[7].trim());
                }
                else if(i==8)
                {
                    etNotes9.setText(notesSteps[8].trim());
                }
             }
        }
    }
}
