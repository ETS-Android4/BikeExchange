package com.hfad.bikeexchange;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hfad.bikeexchange.models.Customer;

public class SignUpFragment extends Fragment {

    public SignUpFragment() { }

    private Button alreadyHaveAnAccount, signUpBtn;
    private FrameLayout parentFrameLayout;
    private EditText email, password, confirmPassword, firstName, secondName;
    private ImageButton closeBtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        parentFrameLayout = getActivity().findViewById(R.id.register_frameLayout);

        alreadyHaveAnAccount = view.findViewById(R.id.button_sign_in);

        email = view.findViewById(R.id.sign_up_email);
        firstName = view.findViewById(R.id.sign_up_first_name);
        secondName = view.findViewById(R.id.sign_up_second_name);

        password = view.findViewById(R.id.sign_up_password);
        confirmPassword = view.findViewById(R.id.sign_up_confirm_password);

        closeBtn = view.findViewById(R.id.sign_up_close_button);
        signUpBtn = view.findViewById(R.id.button_sign_up);

        progressBar = view.findViewById(R.id.sign_up_progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
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
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkEmailAndPassword(); }
        });

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { setFragment(new SignInFragment()); }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateMainActivityIntent();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity()
                .getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText()) &&
                !TextUtils.isEmpty(password.getText()) &&
                !TextUtils.isEmpty(confirmPassword.getText()) &&
                password.length() >= 8 ) {
            BtnSignUpEnable();
        } else
            BtnSignUpDisable();
    }

    private void checkEmailAndPassword() {
        if (email.getText().toString().matches(emailPattern) &&
                password.getText().toString().equals(confirmPassword.getText().toString())) {

            BtnSignUpDisable();

            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                    password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Customer customer = new Customer(
                                        email.getText().toString(),
                                        firstName.getText().toString(),
                                        secondName.getText().toString());

                                firebaseFirestore.collection("CUSTOMERS")
                                        .add(customer)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()) {
                                                    CreateMainActivityIntent();
                                                } else {
                                                    BtnSignUpEnable();
                                                    showError(task);
                                                }
                                            }
                                        });
                            } else {
                                BtnSignUpEnable();
                                showError(task);
                            }
                        }
                    });
            } else {
                confirmPassword.setError("Email address or password isn't valid.");
        }
    }

    private void BtnSignUpEnable() {
        signUpBtn.setEnabled(true);
        signUpBtn.setTextColor(getResources().getColor(R.color.teal_700));
    }

    private void BtnSignUpDisable() {
        signUpBtn.setEnabled(false);
        signUpBtn.setTextColor(getResources().getColor(R.color.design_default_color_error));
    }

    private void showError(Task task) {
        String error = task.getException().toString();
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    private void CreateMainActivityIntent() {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }
}