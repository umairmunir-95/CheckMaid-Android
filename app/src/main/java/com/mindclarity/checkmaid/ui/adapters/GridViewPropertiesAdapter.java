package com.mindclarity.checkmaid.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.Properties;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.ui.activities.CreateCheckInGuideActivity;
import com.mindclarity.checkmaid.ui.activities.PropertiesActivity;
import com.mindclarity.checkmaid.ui.activities.PropertyDetailsActivity;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridViewPropertiesAdapter extends RecyclerView.Adapter<GridViewPropertiesAdapter.PropertiesHolder> {

    private List<Properties> properties = new ArrayList<>();
    private Context context;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public GridViewPropertiesAdapter(Context context)
    {
        this.context=context;
        sharedPreferencesHelper=new SharedPreferencesHelper(context);
    }

    @NonNull
    @Override
    public PropertiesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_property_item, parent, false);
        return new PropertiesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PropertiesHolder holder, int position) {
        Properties currentProperty = properties.get(position);
        holder.tvPropertyName.setText(currentProperty.getPropertyName());
        holder.tvPropertyAddress.setText(currentProperty.getPropertyAddress());
        holder.ivImage.setImageBitmap(ImageUtils.base64ToBitmap(currentProperty.getImage()));
        holder.tvPropertyID.setText(currentProperty.getId());
        holder.tvLatitude.setText(currentProperty.getLatitude());
        holder.tvLongitude.setText(currentProperty.getLongitude());
        holder.tvClockinRadius.setText(currentProperty.getClockInRadius());
        holder.tvCheckinTime.setText(currentProperty.getCheckInTime());
        holder.tvCheckoutTime.setText(currentProperty.getCheckOutTime());

        holder.llSingleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesHelper.setString(App.INTENT_FROM,context.getResources().getString(R.string.edit_property));
                holder.ivImage.buildDrawingCache();
                Bitmap bitmap= holder.ivImage.getDrawingCache();
                AppStore.PROPERTY_ID=holder.tvPropertyID.getText().toString().trim();
                AppStore.PROPERTY_NAME=holder.tvPropertyName.getText().toString().trim();
                AppStore.PROPERTY_ADDRESS=holder.tvPropertyAddress.getText().toString().trim();
                AppStore.PROPERTY_LATITUDE=holder.tvLatitude.getText().toString().trim();
                AppStore.PROPERTY_LONGITUDE=holder.tvLongitude.getText().toString().trim();
                AppStore.PROPERTY_CLOCK_IN_RADIUS=holder.tvClockinRadius.getText().toString().trim();
                AppStore.PROPERTY_CHECK_IN_TIME=holder.tvCheckinTime.getText().toString().trim();
                AppStore.PROPERTY_CHECK_OUT_TIME=holder.tvCheckoutTime.getText().toString().trim();
                String imagePath= ImageUtils.tempFileImage(context,bitmap,AppStore.PROPERTY_ID+"_"+AppStore.PROPERTY_NAME.trim());
                AppStore.PROPERTY_IMAGE_PATH=imagePath;
                Intent i = new Intent(context, PropertyDetailsActivity.class);
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public void setProperties(List<Properties> properties) {
        this.properties=properties;
        notifyDataSetChanged();
    }

    class PropertiesHolder extends RecyclerView.ViewHolder {

        private TextView tvPropertyName;
        private TextView tvPropertyAddress;
        private TextView tvPropertyID;
        private TextView tvLatitude;
        private TextView tvLongitude;
        private TextView tvClockinRadius;
        private TextView tvCheckinTime;
        private TextView tvCheckoutTime;
        private ImageView ivImage;
        private LinearLayout llSingleItem;

        public PropertiesHolder(View itemView) {
            super(itemView);
            tvPropertyName= itemView.findViewById(R.id.tv_property_name);
            tvPropertyAddress = itemView.findViewById(R.id.tv_property_address);
            tvPropertyID = itemView.findViewById(R.id.tv_property_id);
            tvLatitude = itemView.findViewById(R.id.tv_latitude);
            tvLongitude = itemView.findViewById(R.id.tv_longitude);
            tvClockinRadius = itemView.findViewById(R.id.tv_checking_radius);
            tvCheckinTime = itemView.findViewById(R.id.tv_clockin_time);
            tvCheckoutTime = itemView.findViewById(R.id.tv_clockout_time);
            ivImage = itemView.findViewById(R.id.iv_picture);
            llSingleItem=itemView.findViewById(R.id.ll_single_item);
        }
    }
}
