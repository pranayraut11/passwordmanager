package com.manager.password.layout;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.manager.password.R;
import com.manager.password.adapter.CustomListAdapter;
import com.manager.password.database.DatabaseHelper;
import com.manager.password.entity.PasswordInfo;

import java.nio.charset.StandardCharsets;

public class WebsitePopup extends PopupLayout {


    public void show(View view, int resourceId, CustomListAdapter customListAdapter){
        super.create(view,resourceId);
        Button buttonEdit = popupView.findViewById(R.id.button4);
        buttonEdit.setOnClickListener(v -> {
            //As an example, display the message

            PasswordInfo passwordInfo = new PasswordInfo();
            EditText websiteName = popupView.findViewById(R.id.editTextText3);
            EditText password = popupView.findViewById(R.id.editTextText4);
            EditText username = popupView.findViewById(R.id.editTextUsername);
            passwordInfo.setWebsiteName(websiteName.getText().toString());
            passwordInfo.setUsername(username.getText().toString());
            passwordInfo.setPassword(password.getText().toString().getBytes(StandardCharsets.UTF_8));
            DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());
            databaseHelper.addRecord(passwordInfo);
            Toast.makeText(v.getContext(), "Saved", Toast.LENGTH_SHORT).show();
            customListAdapter.updateList();

            Log.d("List","Update list");
            popupWindow.dismiss();
        });




    }
}
