package com.manager.password;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.manager.password.layout.WebsitePopup;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    CustomListAdapter customListAdapter = null;
    DatabaseHelper databaseHelper = null;
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
        databaseHelper = new DatabaseHelper(this);
        customListAdapter = new CustomListAdapter(getApplicationContext(), databaseHelper);

        Button plusButton = findViewById(R.id.addButton);
        listview.setAdapter(customListAdapter);
        plusButton.setOnClickListener(v -> {
            WebsitePopup popupLayout = new WebsitePopup();
            popupLayout.show(v, R.layout.popuplayout, customListAdapter);
        });


        listview.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("List", "Item clicked");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            PasswordInfo passwordInfo = (PasswordInfo) customListAdapter.getItem(position);
            showBiometricPrompt(view, passwordInfo);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchwebsite);

        Log.d("Search", "Search item");
        SearchView searchView = (SearchView) searchItem.getActionView();
        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("On Submit Search", query);
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                updateListOnSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Log.d("New text Search", newText);
//                Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_SHORT).show();
//                updateListOnSearch(newText);
                return false;
            }
        });
        return true;
    }

    private void updateListOnSearch(String query){
        List<PasswordInfo> passwordInfos = databaseHelper.search(query);
        this.customListAdapter.updateList(passwordInfos);
    }

    private void showBiometricPrompt(View view, PasswordInfo passwordInfo) {
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


                        passwordPopup.show(view, R.layout.showpassword_popup, passwordInfo);


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