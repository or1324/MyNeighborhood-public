package com.amitbartfeld.myneighborhood.operations;

import android.widget.EditText;

import com.amitbartfeld.myneighborhood.R;

public class PhoneNumberOperations {
    public static String getPhoneNumber(EditText editText) throws Exception {
        String phoneNumber = editText.getText().toString().trim().replaceAll(" ", "").replaceAll("-", "");
        if (phoneNumber.length() >= 10) {
            String phoneNumberWithoutCountryCode = phoneNumber.substring(phoneNumber.length() - 10);
            if (phoneNumberWithoutCountryCode.charAt(0) == '0') {
                String countryCode = phoneNumber.substring(0, phoneNumber.length() - 10);
                String phoneNumberWithoutCountryCodeAndLeadingZero = phoneNumber.substring(phoneNumber.length() - 9);
                phoneNumber = countryCode + phoneNumberWithoutCountryCodeAndLeadingZero;
            }
        } else
            throw new Exception("Less than 10 characters. Does not contain country code for sure.");
        if (!phoneNumber.startsWith("+"))
            throw new Exception("No plus. Does not contain country code for sure.");
        return phoneNumber;
    }
}
