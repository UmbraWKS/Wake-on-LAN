package com.wakeonlan.app;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.wakeonlan.app.databinding.ActivityAuthenticationBinding;
import java.util.concurrent.Executor;

public class AuthenticationActivity extends AppCompatActivity {

    private ActivityAuthenticationBinding binding;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    public static CustomActions callback; // callback used to implement the interface

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authentication();

        biometricPrompt.authenticate(promptInfo);

    }

    private void authentication(){
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                getBaseContext().getString(R.string.auth_error) + errString    , Toast.LENGTH_SHORT)
                        .show();
                if (errorCode == BiometricPrompt.ERROR_CANCELED || errorCode == BiometricPrompt.ERROR_USER_CANCELED){
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                AuthSession.setUserAuthStatus(AuthenticationActivity.this, true);
                if (callback != null) {
                    callback.afterAuthActions();
                }
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(AuthenticationActivity.this, getBaseContext().getString(R.string.auth_failed),
                                Toast.LENGTH_SHORT)
                        .show();
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        // message shown to user and options of auth
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getBaseContext().getString(R.string.unlock_to_see_saved_data))
                .setAllowedAuthenticators(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)
                .build();
    }

    // interface used to customize the actions after the successful auth
    public interface CustomActions {
        void afterAuthActions();
    }
}
