package com.mindclarity.checkmaid.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mindclarity.checkmaid.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class ImageUtils {

    public static Bitmap base64ToBitmap(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String bitmapToBase64(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);


        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap imageViewToBitmap(Context context,ImageView imageView)
    {
        Bitmap bitmap=null;
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        try {
            bitmap = drawable.getBitmap();
        }
        catch (Exception e)
        {
            bitmap=drawableToBitmap(context.getDrawable(R.drawable.common_image));
        }
        return bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable bitmapToDrawable(Context context,Bitmap bitmap)
    {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static String tempFileImage(Context context, Bitmap bitmap, String name) {
        File outputDir = context.getCacheDir();
        File imageFile = new File(outputDir, name + ".jpg");
        if(imageFile.exists())
            imageFile.delete();
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing file", e);
        }
        return imageFile.getAbsolutePath();
    }

    public static void createTempLogsCache(Context context, Bitmap bitmap,String folderName,String fileName) {
        File folder = context.getDir(folderName, Context.MODE_PRIVATE);
        File file = new File(folder, fileName);
        OutputStream os;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing file", e);
        }
    }

    public static void deleteAllImages(Context context)
    {
        File cacheDir = context.getCacheDir();
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files)
                file.delete();
        }
    }

    public static void deleteLogs(Context context,String folderName)
    {
        File dir = context.getDir(folderName,Context.MODE_PRIVATE);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files)
                file.delete();
        }
    }

    public static void deleteCheckingImage(Context context,String imageName)
    {
        File cacheDir = context.getCacheDir();
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains(imageName)) {
                    file.delete();
                }
            }
        }
    }
    public static void deleteCleaningImage(Context context,String imageName)
    {
        File cacheDir = context.getCacheDir();
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains(imageName)) {
                    file.delete();
                }
            }
        }
    }

    public static Bitmap loadBitmapFromMemory(Context context,String folderName,String fileName)
    {
        File folder = context.getDir(folderName,Context.MODE_PRIVATE);
        File file=new File(folder,fileName);
        Bitmap bitmap = null;
        try
        {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e) {
            Toast.makeText(context,"Bitmap Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return bitmap;
    }


    public static Bitmap getBitmapFromScrollView(View view, int height, int width) {
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

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap bitmap;
        v.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }


    public static boolean downloadImage(Bitmap bitmap) {
        boolean isDownloaded=false;
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/CheckMaids");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        File file = new File(myDir, timeStamp+".jpg");
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            isDownloaded=true;

        } catch (Exception e) {
            isDownloaded=false;
            e.printStackTrace();
        }
        return isDownloaded;
    }

    public static void shareImage(Context context, ImageView imageView)
    {
        Drawable drawable =imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Image Description", null);
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        ((Activity) context).startActivityForResult((Intent.createChooser(intent, context.getResources().getString(R.string.share_via))),1);
    }

    public static void deleteAfterShare(Context context)
    {
        String[] retCol = { MediaStore.Audio.Media._ID };
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, retCol,MediaStore.Images.Media.TITLE + "='Image Description'", null, null);
        try
        {
            if (cursor.getCount() == 0)
            {
                return;
            }
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            int cnt = context.getContentResolver().delete(uri, null, null);
        }
        finally
        {
            cursor.close();
        }
    }
}
