package com.example.android_b19.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android_b19.R;
import com.example.android_b19.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment {

    private final String TAG = "pepsi";
    private EditText mEditTextName;
    private EditText mEditTextAge;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonRegister;
    private FirebaseAuth mAuth;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditTextName = view.findViewById(R.id.et_register_name);
        mEditTextAge = view.findViewById(R.id.et_register_age);
        mEditTextEmail = view.findViewById(R.id.et_register_email);
        mEditTextPassword = view.findViewById(R.id.et_register_password);
        mButtonRegister = view.findViewById(R.id.btn_register);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndRegister();
            }
        });
    }

    private void validateAndRegister() {
        String name = mEditTextName.getText().toString().trim();
        String age = mEditTextAge.getText().toString().trim();
        String email = mEditTextEmail.getText().toString().trim();
        String password = mEditTextPassword.getText().toString().trim();
        User user = new User(name, age, email);
        if (name.isEmpty()) {
            mEditTextName.setError("Name is required!");
            mEditTextName.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            mEditTextAge.setError("Age is required!");
            mEditTextAge.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            mEditTextEmail.setError("Email is required!");
            mEditTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            mEditTextPassword.setError("Password is required!");
            mEditTextPassword.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEditTextEmail.setError("Please provide valid email");
            mEditTextEmail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            mEditTextPassword.setError("Password should be at least 6 characters");
            mEditTextPassword.requestFocus();
            return;
        }
        register(user, password);
    }

    private void register(User user, String password) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            Log.d(TAG, "getCurrentUser:" + firebaseUser.getEmail());
                            writeIntoDatabase(user);
                        } else {
                            // fail create user
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void writeIntoDatabase(User user) {
        String uniqueId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("Users")
                .child(uniqueId)
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Registration Success!",
                                    Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    }
                });
    }


}