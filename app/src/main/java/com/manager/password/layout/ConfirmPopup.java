package com.manager.password.layout;

import android.view.View;
import android.widget.Button;

import com.manager.password.R;
import com.manager.password.adapter.CustomListAdapter;
import com.manager.password.database.DatabaseHelper;
import com.manager.password.entity.PasswordInfo;

public class ConfirmPopup extends PopupLayout {

    public void show(View view, int resourceId, CustomListAdapter customListAdapter, int position) {
        super.create(view, resourceId);
        Button yesBtn = popupView.findViewById(R.id.confirm_yes_btn);
        Button noBtn = popupView.findViewById(R.id.confirm_no_btn);
        yesBtn.setOnClickListener(v -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
            PasswordInfo passwordInfo = (PasswordInfo) customListAdapter.getItem(position);
            databaseHelper.deleteRecord(passwordInfo.getId());
            customListAdapter.updateList();
        });

        noBtn.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }
}
