package com.mindclarity.checkmaid.ui.activities;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.BuildConfig;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.viewmodel.ClockInClockOutViewModel;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserTimeSheetActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack;
    private TextView tvTotalTime;
    private ProgressBar progressBar;
    private TableLayout tblTimeLogs;
    private FloatingActionButton fabShare;
    private ClockInClockOutViewModel clockInClockOutViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_timesheet);
        initializeViews();
        loadUserLogs();
    }

    public void onBackPressed() {
    }

    private void initializeViews() {
        tblTimeLogs = findViewById(R.id.tbl_time_logs);
        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        tvTotalTime=findViewById(R.id.tv_total_logs);
        fabShare = findViewById(R.id.fab_share);
        progressBar = findViewById(R.id.progress_bar);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.timesheet));
        fabShare.setEnabled(false);
        fabShare.setAlpha(0.3f);

        clockInClockOutViewModel = ViewModelProviders.of(this).get(ClockInClockOutViewModel.class);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helpers.getPreferenceValue(UserTimeSheetActivity.this,getResources().getString(R.string.user_type)).equals(getResources().getString(R.string.admin)))
                {
                    Intent i = new Intent(UserTimeSheetActivity.this, PropertiesActivity.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(UserTimeSheetActivity.this, FindPropertyActivity.class);
                    startActivity(i);
                }
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScrollView scrollView = findViewById(R.id.layout);
                Bitmap bitmap = getBitmapFromView(scrollView, scrollView.getChildAt(0).getHeight(), scrollView.getChildAt(0).getWidth());
//                saveBitmap(bitmap);
                try
                {
                    createPdf(bitmap);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (DocumentException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void loadUserLogs() {
        if (NetworkManager.isNetworkAvailable(UserTimeSheetActivity.this)) {
            String url = App.LIST_ALL_TIME_LOGS + "/" + App.BY_USER + "/" + Helpers.getPreferenceValue(UserTimeSheetActivity.this, getResources().getString(R.string.user_id));
            clockInClockOutViewModel.getLogsByUser(UserTimeSheetActivity.this, progressBar, tvTotalTime,tblTimeLogs, url,fabShare,fabShare);
        } else {
            DialogManager.showInfoDialog(UserTimeSheetActivity.this, "", getResources().getString(R.string.internet_available_msg), false);
        }
    }

    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    public void saveBitmap(Bitmap bitmap) {

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
        File imagePath = new File(mPath);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(getApplicationContext(), imagePath.getAbsolutePath() + "", Toast.LENGTH_LONG).show();
            Log.e("ImageSave", "Saveimage");
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    private void createPdf(Bitmap bitmap) throws FileNotFoundException, DocumentException {
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "image" + ".pdf";
        File myFile= new File(mPath);
        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
        Image myImg = null;
        try {
            myImg = Image.getInstance(stream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.add(myImg);

        document.close();
        share();
    }

    private void share()
    {
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "image" + ".pdf";
        File myFile= new File(mPath);
        Uri uri = FileProvider.getUriForFile(UserTimeSheetActivity.this, BuildConfig.APPLICATION_ID + ".provider",myFile);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(share);
    }
}