package com.example.android_b19.ui.login;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_b19.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private EditText mEditTextEmail;
    private EditText mEditPassword;
    private Button mButtonLogin;
    private TextView mTextViewRegister;
    private FirebaseAuth mAuth;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finishAffinity();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        requireActivity().setTitle(requireActivity().getResources().getString(R.string.login_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditTextEmail = view.findViewById(R.id.et_register_age);
        mEditPassword = view.findViewById(R.id.et_register_password);
        mButtonLogin = view.findViewById(R.id.btn_register);
        mTextViewRegister = view.findViewById(R.id.tv_register);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sign out again just it case this activity started
                // without sign out, it ain't hurt nobody
                mAuth.signOut();
                validateAndLogin();
            }
        });

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment fragmentRegister = RegisterFragment.newInstance();
                fm.beginTransaction()
                        .replace(R.id.login_fragment_container, fragmentRegister)
                        .commit();
            }
        });
    }

    private void validateAndLogin() {
        String email = mEditTextEmail.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        if (email.isEmpty()) {
            mEditTextEmail.setError("Email is required!");
            mEditTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            mEditPassword.setError("Password is required!");
            mEditPassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEditTextEmail.setError("Please provide valid email");
            mEditTextEmail.requestFocus();
            return;
        }
        login(email, password);
    }

    private void login(String email, String password) {
        // firebase sign in
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("debug", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getActivity(), "Authentication ok." + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            // close login activity
                            getActivity().finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("debug", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}