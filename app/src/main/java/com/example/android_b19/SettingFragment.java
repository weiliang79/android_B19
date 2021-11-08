package com.example.android_b19;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        if(preference.getKey().equals(getResources().getString(R.string.logout_key))){
            //do logout task

            //need try after logout successful
            //requireActivity().finish();
        }

        return super.onPreferenceTreeClick(preference);
    }
}
