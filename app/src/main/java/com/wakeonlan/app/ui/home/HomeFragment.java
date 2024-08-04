package com.wakeonlan.app.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.wakeonlan.app.MacLenghtException;
import com.wakeonlan.app.PacketCreation;
import com.wakeonlan.app.R;
import com.wakeonlan.app.databinding.FragmentHomeBinding;
import com.wakeonlan.app.utilities.GuidelineResizing;
import com.wakeonlan.app.utilities.MacTextFiltering;
import com.wakeonlan.app.utilities.UtilityFeatures;
import java.io.IOException;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    public static String macAddressField = null;
    MacTextFiltering macTextFiltering;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        macTextFiltering = new MacTextFiltering(binding.macText);
        binding.macText.addTextChangedListener(macTextFiltering);
        binding.sendButton.setOnClickListener(this::onClickSendButton);
        binding.bottomLayout.setOnClickListener(this::onClickBottomLayout);
        binding.dragArea.setOnTouchListener((view1, motionEvent) -> 
                GuidelineResizing.handleTouch(motionEvent, binding.guideline, binding.bottomLayout)
        );
        binding.buttonLinkedin.setOnClickListener(v -> {
            tabLinkButtons(v, 0);
        });
        binding.buttonGithub.setOnClickListener(v -> {
            tabLinkButtons(v, 1);
        });
        binding.buttonTwitter.setOnClickListener(v -> {
            tabLinkButtons(v, 2);
        });
        binding.buttonWebsite.setOnClickListener(v -> {
            tabLinkButtons(v, 3);
        });

        return view;
    }

    // by tapping on the bottomLayout aka the whole screen the keyboard closes
    // and the editText loses focus
    private void onClickBottomLayout(View view) {
        UtilityFeatures.closeKeyboard(requireActivity());
        binding.macLayout.clearFocus();
    }

    private void onClickSendButton(View view) {
        if(macTextFiltering.checkMacValidity(Objects.requireNonNull(binding.macText.getText()).toString())){
            binding.macLayout.setErrorEnabled(false);
            UtilityFeatures.closeKeyboard(requireActivity());
            binding.macLayout.clearFocus();
            try {
                PacketCreation.createPacket(Objects.requireNonNull(binding.macText.getText()).toString());
            } catch (MacLenghtException | IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            binding.macLayout.setErrorEnabled(true);
            binding.macLayout.setError(requireContext().getString(R.string.invalid_mac_address));
        }
    }

    // handling different links with each button
    private void tabLinkButtons(View view, int button){
        String url = null;
        switch (button){
            case 0:
                url = "https://www.linkedin.com/in/filippo-baiamonte-b9b8a22a8/";
                break;
            case 1:
                url = "https://github.com/UmbraWKS";
                break;
            case 2:
                url = "https://x.com/UmbraaW";
                break;
            case 3:
                url = "https://filippobaiamonte.com";
                break;
        }
        // starting the browser
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    @Override
    public void onResume(){
        super.onResume();
        if(macAddressField != null)
            binding.macText.setText(macAddressField);
    }
}
