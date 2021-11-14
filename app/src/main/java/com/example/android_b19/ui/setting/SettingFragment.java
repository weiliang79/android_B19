package com.example.android_b19.ui.setting;

import android.content.Intent;
import android.os.Bundle;

import com.example.android_b19.R;
import com.example.android_b19.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingFragment extends PreferenceFragmentCompat {

    private FirebaseAuth mAuth;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        if(preference.getKey().equals(getResources().getString(R.string.logout_key))){
            //do logout task
            mAuth.signOut();

            if (mAuth.getCurrentUser() == null) {
                // start login flow
                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
            }

        }

        return super.onPreferenceTreeClick(preference);
    }
}
