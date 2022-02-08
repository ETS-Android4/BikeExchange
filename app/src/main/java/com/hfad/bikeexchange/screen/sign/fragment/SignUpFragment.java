package com.hfad.bikeexchange.screen.sign.fragment;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.home.MainActivity;
import com.hfad.bikeexchange.screen.sign.fragment.SignInFragment;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {
    public SignUpFragment() { }

    private Button alreadyHaveAnAccount, signUpBtn;
    private EditText email, password, confirmPassword, firstName, secondName;
    private ImageButton closeBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            Pattern.CASE_INSENSITIVE);

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
        dbRef = FirebaseDatabase.getInstance().getReference();

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
                createMainActivityIntent();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.register_frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("sign")
                .commit();
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
        String emailString = email.getText().toString();
        String passString = password.getText().toString();
        String confirmPassString = confirmPassword.getText().toString();

        if (isValidEmailAddress(emailString) &&
                passString.equals(confirmPassString)) {

            BtnSignUpDisable();

            firebaseAuth.createUserWithEmailAndPassword(emailString,
                    passString)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("firstName", firstName.getText().toString());
                                userMap.put("secondName", secondName.getText().toString());

                                dbRef.child("users").child(uId).setValue(userMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                    createMainActivityIntent();
                                                else {
                                                    BtnSignUpEnable();
                                                    Toast.makeText(getActivity(), "Something went wrong",
                                                        Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                BtnSignUpEnable();
                                showAuthError(task);
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

    private void showAuthError(Task<AuthResult> task) {
        String error = Objects.requireNonNull(task.getException()).toString();
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    private void showAddCustomerError(Task<DocumentReference> task) {
        String error = Objects.requireNonNull(task.getException()).toString();
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    private void createMainActivityIntent() {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        requireActivity().finish();
        startActivity(mainIntent);
    }

    private static boolean isValidEmailAddress(String emailAddress) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailAddress);
        return matcher.find();
    }
}