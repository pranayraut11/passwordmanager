package com.manager.password.layout;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.manager.password.R;
import com.manager.password.entity.PasswordInfo;
import com.manager.password.security.PasswordSecurity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class PasswordPopup extends PopupLayout {

    public void show(View view, int resourceId, PasswordInfo passwordInfo) {
        this.create(view, resourceId);
        Button okButton = popupView.findViewById(R.id.okBtnOnPasswordPopup);
        TextView usernameTxtView = popupView.findViewById(R.id.txtViewUsername);
        TextView passwordTxtView = popupView.findViewById(R.id.txtViewPassword);
        try {
            passwordTxtView.setText(PasswordSecurity.decryptMessage(passwordInfo.getPassword(), passwordInfo.getIv()));
            usernameTxtView.setText(passwordInfo.getUsername());
        } catch (IOException | UnrecoverableEntryException | CertificateException |
                 KeyStoreException | NoSuchAlgorithmException |
                 InvalidKeyException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException |
                 InvalidAlgorithmParameterException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        okButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }
}
