package com.manager.password;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.manager.password.adapter.CustomListAdapter;
import com.manager.password.database.DatabaseHelper;
import com.manager.password.entity.PasswordInfo;
import com.manager.password.layout.PasswordPopup;
import com.manager.password.layout.PopupLayout;
import com.manager.password.layout.WebsitePopup;
import com.manager.password.security.PasswordSecurity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        final ListView listview = findViewById(R.id.website_list);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<PasswordInfo> passwordInfoList = databaseHelper.getAll();

        CustomListAdapter customListAdapter = new CustomListAdapter(getApplicationContext(), passwordInfoList);

        Button plusButton = findViewById(R.id.addButton);
        listview.setAdapter(customListAdapter);
        plusButton.setOnClickListener(v -> {
            WebsitePopup popupLayout = new WebsitePopup();
            popupLayout.show(v,R.layout.popuplayout,customListAdapter);
        });



        listview.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("List", "Item clicked");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            PasswordInfo passwordInfo = (PasswordInfo) customListAdapter.getItem(position);
            showBiometricPrompt(view, passwordInfo.getPassword(),passwordInfo.getIv());
        });
    }
    private void showBiometricPrompt(View view,byte[] password,byte[] iv) {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setDescription("Please authenticate with your biometrics to continue")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG).setNegativeButtonText("Unlock")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(this,
                ContextCompat.getMainExecutor(this),
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(getApplicationContext(), "Authentication successful", Toast.LENGTH_SHORT).show();
                        PasswordPopup passwordPopup = new PasswordPopup();

                        try {
                                passwordPopup.show(view,R.layout.showpassword_popup, PasswordSecurity.decryptMessage(password,iv));
                        } catch (IOException | UnrecoverableEntryException | CertificateException |
                                 KeyStoreException | NoSuchAlgorithmException |
                                 InvalidKeyException | NoSuchPaddingException |
                                 IllegalBlockSizeException | BadPaddingException |
                                 InvalidAlgorithmParameterException | NoSuchProviderException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }
}