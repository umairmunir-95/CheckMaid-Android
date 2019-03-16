package com.mindclarity.checkmaid.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.Properties;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.utils.Helpers;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAssignPropertiesAdapter extends RecyclerView.Adapter<RecyclerViewAssignPropertiesAdapter.PropertiesHolder> {

    private List<Properties> properties = new ArrayList<>();
    private Context context;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public RecyclerViewAssignPropertiesAdapter(Context context)
    {
        this.context=context;
        sharedPreferencesHelper=new SharedPreferencesHelper(context);
    }

    @NonNull
    @Override
    public RecyclerViewAssignPropertiesAdapter.PropertiesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_assign_properties_item, parent, false);
        return new RecyclerViewAssignPropertiesAdapter.PropertiesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAssignPropertiesAdapter.PropertiesHolder holder, int position) {
        final Properties currentProperty = properties.get(position);
        holder.tvPropertyName.setText(currentProperty.getPropertyName());

        holder.cbAssignProperties.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    String assignedProperties=Helpers.getPreferenceValue(context,context.getResources().getString(R.string.assigned_properties))+","+currentProperty.getPropertyName();
                    sharedPreferencesHelper.setString(context.getResources().getString(R.string.assigned_properties),assignedProperties);
                }
                else
                {
                    String assignedProperties=Helpers.getPreferenceValue(context,context.getResources().getString(R.string.assigned_properties)).replaceAll(currentProperty.getPropertyName()+",","");
                    sharedPreferencesHelper.setString(context.getResources().getString(R.string.assigned_properties),assignedProperties);
                }

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
        private CheckBox cbAssignProperties;

        public PropertiesHolder(View itemView) {
            super(itemView);
            tvPropertyName= itemView.findViewById(R.id.tv_property_name);
            cbAssignProperties= itemView.findViewById(R.id.cb_assign_property);
        }
    }
}
