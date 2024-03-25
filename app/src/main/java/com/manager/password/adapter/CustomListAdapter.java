package com.manager.password.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.manager.password.R;
import com.manager.password.database.DatabaseHelper;
import com.manager.password.entity.PasswordInfo;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    Context context;
    List<PasswordInfo> passwordInfos;
    LayoutInflater inflater;

    private DatabaseHelper databaseHelper;

    public CustomListAdapter(Context context,DatabaseHelper databaseHelper) {
        this.context = context;
        this.passwordInfos = databaseHelper.getAll();
        this.databaseHelper = databaseHelper;
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

    public void updateList(){
        this.passwordInfos = this.databaseHelper.getAll();
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
        Button deleteRecordBtn = view.findViewById(R.id.deleteButton);
        deleteRecordBtn.setOnClickListener(v -> {
            Log.d("List","Item clicked "+i);
            DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());
            PasswordInfo passwordInfo = (PasswordInfo)getItem(i);
            databaseHelper.deleteRecord(passwordInfo.getId());
            updateList();
        });
        country.setText(passwordInfos.get(i).getWebsiteName());
        return view;
    }
}
