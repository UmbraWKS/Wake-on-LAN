package com.wakeonlan.app.ui.saves;

import android.os.Bundle;
import android.view.View;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.wakeonlan.app.Files;
import com.wakeonlan.app.JsonParsing;
import com.wakeonlan.app.R;
import com.wakeonlan.app.databinding.ActivityCreateSaveBinding;
import com.wakeonlan.app.utilities.MacTextFiltering;
import com.wakeonlan.app.utilities.UtilityFeatures;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.Objects;

public class CreateSaveActivity extends AppCompatActivity {

    private ActivityCreateSaveBinding binding;
    private MacTextFiltering macTextFiltering;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityCreateSaveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        enableOnBackPressedCallback();

        macTextFiltering = new MacTextFiltering(binding.macAddressSaveText);
        binding.macAddressSaveText.addTextChangedListener(macTextFiltering);

        binding.saveButton.setOnClickListener(this::onCLickSaveButton);
        binding.bottomLayout.setOnClickListener(this::onClickBottomLayout);
        binding.backArrow.setOnClickListener(this::onClickBackArrow);
    }

    // self explainatory tbh :)
    private void onClickBackArrow(View view) {
        finish();
    }

    // by tapping on the bottomLayout aka the whole screen the keyboard closes
    // and the editTexts lose focus
    private void onClickBottomLayout(View view) {
        UtilityFeatures.closeKeyboard(this);
        binding.nameSaveText.clearFocus();
        binding.macAddressSaveText.clearFocus();
    }

    private void onCLickSaveButton(View view) {
        if(macTextFiltering.checkMacValidity(Objects.requireNonNull(binding.macAddressSaveText.getText()).toString())){
            binding.macAddressSaveLayout.setErrorEnabled(false);

            UtilityFeatures.closeKeyboard(this);

            JSONArray jsonArray;
            try {
                // saving data in the arraylists and creating a jsonArray to save them in the file
                SavesFragment.addData(Objects.requireNonNull(binding.macAddressSaveText.getText()).toString(),
                        Objects.requireNonNull(binding.nameSaveText.getText()).toString());
                jsonArray = JsonParsing.toJson(SavesFragment.macs, SavesFragment.names);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // using a thread to perform file operations
            Thread thread = new Thread(() ->
                    Files.saveFile(jsonArray, CreateSaveActivity.this)
            );
            thread.start();

            // closing the activity
            finish();
        }else{
            binding.macAddressSaveLayout.setErrorEnabled(true);
            binding.macAddressSaveLayout.setError(this.getString(R.string.invalid_mac_address));
        }

    }

    // closing the activity when user goes back
    private void enableOnBackPressedCallback(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
