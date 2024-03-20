package com.manager.password.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.manager.password.R;
import com.manager.password.entity.PasswordInfo;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    Context context;
    List<PasswordInfo> passwordInfos;
    LayoutInflater inflater;

    public CustomListAdapter(Context context, List<PasswordInfo> passwordInfos) {
        this.context = context;
        this.passwordInfos = passwordInfos;

        this.inflater  = (LayoutInflater.from(context));;
    }

    @Override
    public int getCount() {
        return passwordInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return passwordInfos.get(position);
    }
    @Override
    public void notifyDataSetChanged() // Create this function in your adapter class
    {
        super.notifyDataSetChanged();
    }

    public void updateList(List<PasswordInfo> passwordInfoList){
        this.passwordInfos = passwordInfoList;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.website_listview, parent,false);

        }
        view = inflater.inflate(R.layout.website_listview, parent,false);
        TextView country = view.findViewById(R.id.textView);
        country.setText(passwordInfos.get(i).getWebsiteName());
        return view;
    }
}
