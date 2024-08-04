package com.wakeonlan.app.utilities;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MacTextFiltering implements TextWatcher {
    // string containing allowed values
    private String regex = "^([0-9A-F]{2}[:-]){5}[0-9A-F]{2}$";
    private static final int MAX_LENGTH = 17; // max length of the mac

    private EditText editText;


    public MacTextFiltering(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String input = editable.toString().toUpperCase(); // automatically setting letters to upper case
        String cleanInput = input.replaceAll("[^0-9A-F]", ""); // removing all non-hex characters
        StringBuilder formattedInput = new StringBuilder();

        // putting [-] after every 2 hex numbers
        // also checking that the string doesn't exceed max size
        for (int i = 0; i < cleanInput.length() && formattedInput.length() < MAX_LENGTH; i++) {
            formattedInput.append(cleanInput.charAt(i));
            if ((i % 2 == 1) && formattedInput.length() < MAX_LENGTH - 1) {
                formattedInput.append("-");
            }
        }

        // if the last character is a [-] it is removed
        if (formattedInput.length() > 0 && formattedInput.charAt(formattedInput.length() - 1) == '-') {
            formattedInput.deleteCharAt(formattedInput.length() - 1);
        }

        // setting the formatted text without triggering another change
        editText.removeTextChangedListener(this);
        editText.setText(formattedInput.toString());
        editText.setSelection(formattedInput.length());
        editText.addTextChangedListener(this);

    }

    public boolean checkMacValidity(String string){
        return string.matches(regex);
    }
}
