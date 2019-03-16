package com.mindclarity.checkmaid.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindclarity.checkmaid.BuildConfig;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.ui.activities.TimeSheetDetailsActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Helpers {

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static String getPreferenceValue(Context context,String key)
    {
        String value="";
        SharedPreferencesHelper sharedPreferencesHelper=new SharedPreferencesHelper(context);
        if(sharedPreferencesHelper!=null)
        {
            if(sharedPreferencesHelper.getString(key)!=null)
            {
                value=sharedPreferencesHelper.getString(key);
            }
        }
        return value;
    }

    public static void setEditTextError(final Context context, final EditText editText)
    {
        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(editText.getText().toString().trim().equals(""))
                {
                    editText.setError(context.getResources().getString(R.string.required));
                }
            }
        });
    }

    public static boolean isEditTextEmpty(EditText editText)
    {
        if(editText.getText().toString().trim().equals(""))
            return true;
        else
            return false;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        }
        else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public static String generateVerificationCode()
    {
        return String.format("%04d", new Random().nextInt(10000));
    }

    public static String getPrefferenceValues(Context context,String value)
    {
        String prefferenceValue="";
        SharedPreferencesHelper sharedPreferencesHelper=new SharedPreferencesHelper(context);
        if(sharedPreferencesHelper!=null)
        {
            if(sharedPreferencesHelper.getString(value)!=null)
            {
                prefferenceValue=sharedPreferencesHelper.getString(value);
            }
        }
        return prefferenceValue;
    }

    public static boolean isLoggedIn(Context context)
    {
        return Helpers.getPreferenceValue(context, context.getResources().getString(R.string.user_id)).equals("")?false:true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getCurrectDate(String format)
    {
        return new SimpleDateFormat(format,Locale.ENGLISH).format(Calendar.getInstance().getTime());
    }


    public static void createPdf(Context context,Bitmap bitmap) throws FileNotFoundException, DocumentException {
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
        share(context);
    }

    public static void share(Context context)
    {
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "image" + ".pdf";
        File myFile= new File(mPath);
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",myFile);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(share);
    }


}
