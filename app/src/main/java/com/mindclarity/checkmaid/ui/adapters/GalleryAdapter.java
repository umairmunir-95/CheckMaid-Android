package com.mindclarity.checkmaid.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.GalleryModel;
import com.mindclarity.checkmaid.ui.activities.PhotoLogsGalleryActivity;
import com.mindclarity.checkmaid.ui.activities.PhotoLogsSliderActivity;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.ImageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private Context mContext;
    private List<GalleryModel> imagesList;
    private ImageView selectedImageView;

    public GalleryAdapter(Context mContext, List<GalleryModel> imagesList) {
        this.mContext = mContext;
        this.imagesList = imagesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sliding_image_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GalleryModel galleryModel = imagesList.get(position);

        if(Helpers.getPreferenceValue(mContext, App.INTENT_FROM).equals(mContext.getResources().getString(R.string.photo_logs))) {
            if(ImageUtils.loadBitmapFromMemory(mContext,mContext.getResources().getString(R.string.photo_logs),galleryModel.getImageName())!=null)
            {
                holder.ivImage.setImageBitmap(ImageUtils.loadBitmapFromMemory(mContext,mContext.getResources().getString(R.string.photo_logs),galleryModel.getImageName()));
            }
            else
            {
                holder.ivImage.setBackgroundResource(R.drawable.common_image);
            }
        }
        else
        {
            if(ImageUtils.loadBitmapFromMemory(mContext,mContext.getResources().getString(R.string.damage_reports),galleryModel.getImageName())!=null)
            {
                holder.ivImage.setImageBitmap(ImageUtils.loadBitmapFromMemory(mContext,mContext.getResources().getString(R.string.damage_reports),galleryModel.getImageName()));
            }
            else
            {
                holder.ivImage.setBackgroundResource(R.drawable.common_image);
            }
        }


        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,PhotoLogsSliderActivity.class);
                mContext.startActivity(i);
            }
        });


    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView tvImageName;
        public ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);
            cardView=view.findViewById(R.id.card_view);
            tvImageName = view.findViewById(R.id.tv_title);
            ivImage = view.findViewById(R.id.iv_image);
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

}
