package com.mindclarity.checkmaid.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.ImageUtils;
import com.mukesh.DrawingView;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.io.File;

public class PaintActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private TextView tvHeader;
    private ImageView ivBack;

    private Button saveButton, penButton, eraserButton, penColorButton, backgroundColorButton,loadButton, clearButton;
    private DrawingView drawingView;
    private SeekBar penSizeSeekBar, eraserSizeSeekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        initializeViews();
        initializeUI();
        setListeners();
    }

    public void onBackPressed() {

    }

    private void initializeViews() {
        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.edit_picture));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helpers.getPreferenceValue(PaintActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.checkin_guide))) {
                    Intent i = new Intent(PaintActivity.this, CheckInGuideDetailsActivity.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(PaintActivity.this, CleaningGuideDetailsActivity.class);
                    startActivity(i);
                }
            }
        });
    }


    private void setListeners() {
        saveButton.setOnClickListener(this);
        penButton.setOnClickListener(this);
        eraserButton.setOnClickListener(this);
        penColorButton.setOnClickListener(this);
        backgroundColorButton.setOnClickListener(this);
        penSizeSeekBar.setOnSeekBarChangeListener(this);
        eraserSizeSeekBar.setOnSeekBarChangeListener(this);
        loadButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
    }

    private void initializeUI() {
        drawingView = findViewById(R.id.scratch_pad);
        saveButton = findViewById(R.id.save_button);
        loadButton = findViewById(R.id.load_button);
        penButton = findViewById(R.id.pen_button);
        eraserButton = findViewById(R.id.eraser_button);
        penColorButton = findViewById(R.id.pen_color_button);
        backgroundColorButton = findViewById(R.id.background_color_button);
        penSizeSeekBar = findViewById(R.id.pen_size_seekbar);
        eraserSizeSeekBar = findViewById(R.id.eraser_size_seekbar);
        clearButton = findViewById(R.id.clear_button);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
                saveEditedImage();
                break;
            case R.id.load_button:
                loadImage();
                break;
            case R.id.pen_button:
                drawingView.initializePen();
                break;
            case R.id.eraser_button:
                drawingView.initializeEraser();
                break;
            case R.id.clear_button:
                drawingView.clear();
                break;
            case R.id.pen_color_button:
                final ColorPicker colorPicker = new ColorPicker(PaintActivity.this, 100, 100, 100);
                colorPicker.setCallback(
                        new ColorPickerCallback() {
                            @Override
                            public void onColorChosen(int color) {
                                drawingView.setPenColor(color);
                                colorPicker.dismiss();
                            }
                        });
                colorPicker.show();
                break;
            case R.id.background_color_button:
                final ColorPicker backgroundColorPicker = new ColorPicker(PaintActivity.this, 100, 100, 100);
                backgroundColorPicker.setCallback(
                        new ColorPickerCallback() {
                            @Override
                            public void onColorChosen(int color) {
                                drawingView.setBackgroundColor(color);
                                backgroundColorPicker.dismiss();
                            }
                        });
                backgroundColorPicker.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int seekBarId = seekBar.getId();
        if (seekBarId == R.id.pen_size_seekbar) {
            drawingView.setPenSize(i);
        } else if (seekBarId == R.id.eraser_size_seekbar) {
            drawingView.setEraserSize(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void saveEditedImage()
    {
        if(Helpers.getPreferenceValue(PaintActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.checkin_guide))) {
            File file = new File(AppStore.CHECK_IN_IMAGE_PATH);
            file.delete();
            Bitmap bitmap = ImageUtils.loadBitmapFromView(drawingView);
            String imagePath = ImageUtils.tempFileImage(PaintActivity.this, bitmap, AppStore.CHECK_IN_ID + AppStore.CHECK_IN_PROPERTY_NAME);
            AppStore.CHECK_IN_IMAGE = ImageUtils.bitmapToBase64(bitmap);
            AppStore.CHECK_IN_IMAGE_PATH = imagePath;
            Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();
            Intent i=new Intent(PaintActivity.this,CheckInGuideDetailsActivity.class);
            startActivity(i);
        }
        else
        {
            File file = new File(AppStore.CLEANING_IMAGE_PATH);
            file.delete();
            Bitmap bitmap = ImageUtils.loadBitmapFromView(drawingView);
            String imagePath = ImageUtils.tempFileImage(PaintActivity.this, bitmap, AppStore.CLEANING_ID + AppStore.CLEANING_PROPERTY_NAME);
            AppStore.CLEANING_IMAGE = ImageUtils.bitmapToBase64(bitmap);
            AppStore.CLEANING_IMAGE_PATH = imagePath;
            Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();
            Intent i=new Intent(PaintActivity.this,CleaningGuideDetailsActivity.class);
            startActivity(i);

        }
    }

    private void loadImage()
    {
        Bitmap bitmap=null;
        if(Helpers.getPreferenceValue(PaintActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.checkin_guide))) {
            bitmap = BitmapFactory.decodeFile(new File(AppStore.CHECK_IN_IMAGE_PATH).getAbsolutePath());
            drawingView.loadImage(bitmap);
        }
        else
        {
            bitmap = BitmapFactory.decodeFile(new File(AppStore.CLEANING_IMAGE_PATH).getAbsolutePath());
            drawingView.loadImage(bitmap);
        }
    }


}