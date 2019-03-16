package com.mindclarity.checkmaid.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewSearchUsersAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<Users> usersCollection = null;
    private ArrayList<Users> arraylist;
  
    public ListViewSearchUsersAdapter(Context context, List<Users> usersCollection) {
        mContext = context;
        this.usersCollection = usersCollection;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(usersCollection);
    }

    public class ViewHolder {
        TextView tvUserName,tvUserId;
    }

    @Override
    public int getCount() {
        return usersCollection.size();
    }

    @Override
    public Users getItem(int position) {
        return usersCollection.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent)
    {
        final ListViewSearchUsersAdapter.ViewHolder holder;
        if (view == null)
        {
            holder = new ListViewSearchUsersAdapter.ViewHolder();
            view = inflater.inflate(R.layout.lv_users_item, null);
            holder.tvUserName = view.findViewById(R.id.tv_name);
            holder.tvUserId = view.findViewById(R.id.tv_id);
            view.setTag(holder);
        }
        else
        {
            holder = (ListViewSearchUsersAdapter.ViewHolder) view.getTag();
        }
        holder.tvUserName.setText(usersCollection.get(position).getUserName());
        holder.tvUserId.setText(usersCollection.get(position).getUserId());
        return view;
    }
}
