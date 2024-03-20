package com.manager.password.layout;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.manager.password.R;

public class PasswordPopup extends PopupLayout{

    public void show(View view, int resourceId,String password){
        this.create(view,resourceId);
        Button okButton = popupView.findViewById(R.id.okBtnOnPasswordPopup);
        TextView passwordTxtView = popupView.findViewById(R.id.passwordTxtView);
        passwordTxtView.setText(password);
        okButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }
}
