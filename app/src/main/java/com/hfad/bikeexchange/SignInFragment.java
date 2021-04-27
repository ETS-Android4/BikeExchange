package com.hfad.bikeexchange;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignInFragment extends Fragment {
    public SignInFragment() { }

    private Button dontHaveAnAccount, signInButton;
    private ImageView closeButton;
    private FrameLayout parentFrameLayout;
    private EditText email, password;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        parentFrameLayout = getActivity().findViewById(R.id.register_frameLayout);
        dontHaveAnAccount = view.findViewById(R.id.button_sign_up);

        email = view.findViewById(R.id.sign_in_email);
        password = view.findViewById(R.id.sign_in_password);

        closeButton = view.findViewById(R.id.sign_in_close_button);
        signInButton = view.findViewById(R.id.button_sign_in);
        progressBar = view.findViewById(R.id.sign_in_progressBar);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { CheckEmailAndPassword(); }
        });

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { setFragment(new SignUpFragment()); }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { CreateMainActivityIntent(); }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity()
                .getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void CheckInputs() {
        if (!TextUtils.isEmpty(email.getText()) &&
                !TextUtils.isEmpty(password.getText())) {
            BtnSignInEnable();
        } else
            BtnSignInDisable();
    }

    private void CheckEmailAndPassword() {
        if (email.getText().toString().matches(emailPattern) &&
                password.length() >= 8) {

            progressBar.setVisibility(View.VISIBLE);
            BtnSignInDisable();

            firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),
                    password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                startActivity(mainIntent);
                                getActivity().finish();
                            } else {
                                showError(task);
                                progressBar.setVisibility(View.INVISIBLE);
                                BtnSignInEnable();
                            }
                        }
                    });
        } else
            Toast.makeText(getActivity(), "Email or password invalid", Toast.LENGTH_SHORT).show();
    }

    private void BtnSignInEnable() {
        signInButton.setEnabled(true);
        signInButton.setTextColor(getResources().getColor(R.color.teal_700));
    }

    private void BtnSignInDisable() {
        signInButton.setEnabled(false);
        signInButton.setTextColor(getResources().getColor(R.color.design_default_color_error));
    }

    private void showError(Task task) {
        String error = task.getException().getMessage();
        Toast.makeText(getActivity(), "Something wrong", Toast.LENGTH_SHORT).show();
    }

    private void CreateMainActivityIntent() {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }
}