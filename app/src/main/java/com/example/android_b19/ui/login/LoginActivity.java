package com.example.android_b19.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.android_b19.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.login_fragment_container);
        if (fragment == null) {
            fragment = LoginFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.login_fragment_container, fragment)
                    .commit();
        }
    }
}