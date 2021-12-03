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
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hfad.bikeexchange.models.Customer;

import java.util.Objects;

public class SignUpFragment extends Fragment {

    public SignUpFragment() { }

    private Button alreadyHaveAnAccount, signUpBtn;
    private EditText email, password, confirmPassword, firstName, secondName;
    private ImageButton closeBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        alreadyHaveAnAccount = view.findViewById(R.id.button_sign_in);

        email = view.findViewById(R.id.sign_up_email);
        firstName = view.findViewById(R.id.sign_up_first_name);
        secondName = view.findViewById(R.id.sign_up_second_name);

        password = view.findViewById(R.id.sign_up_password);
        confirmPassword = view.findViewById(R.id.sign_up_confirm_password);

        closeBtn = view.findViewById(R.id.sign_up_close);
        signUpBtn = view.findViewById(R.id.button_sign_up);

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
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.register_frameLayout, fragment).commit();
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
        // email regular expression
        String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

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
        String error = Objects.requireNonNull(task.getException()).toString();
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    private void CreateMainActivityIntent() {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        requireActivity().finish();
    }
}