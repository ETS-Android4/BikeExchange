package com.hfad.bikeexchange.screen.sign.fragment;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.hfad.bikeexchange.R;
import java.util.Objects;

public class ResetPasswordFragment extends Fragment {
    public ResetPasswordFragment() { }

    private ImageView closeButton;
    private EditText emailReset;
    private Button resetButton;
    private FrameLayout parentFrameLayout;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private TextView emailResetInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        closeButton = view.findViewById(R.id.reset_pass_close_btn);
        emailReset = view.findViewById(R.id.reset_pass_email);

        progressBar = view.findViewById(R.id.reset_password_progressBar);
        emailResetInfo = view.findViewById(R.id.email_reset_info);

        resetButton = view.findViewById(R.id.reset_button);

        parentFrameLayout = requireActivity().findViewById(R.id.register_frameLayout);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resetBtnSetDisable();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { setSignInFragment(new SignInFragment()); }
        });

        emailReset.addTextChangedListener(new TextWatcher() {
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

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.sendPasswordResetEmail(emailReset.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    createToast();
                                } else {
                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                    emailResetInfo.setText(error);
                                    emailResetInfo.setTextColor(getResources().getColor(R.color.design_default_color_error));
                                    emailResetInfo.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                                resetBtnSetEnable();
                            }
                        });
            }
        });
    }

    private void setSignInFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_from_left, R.anim.slide_from_left)
                .replace(parentFrameLayout.getId(), fragment)
                .setReorderingAllowed(true)
                .addToBackStack("sign")
                .commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(emailReset.getText()))
            resetBtnSetEnable();
         else
            resetBtnSetDisable();
    }

    private void createToast() {
        Toast.makeText(getActivity(), "Mail has been sent to your email", Toast.LENGTH_SHORT).show();
    }

    private void resetBtnSetDisable() {
        resetButton.setEnabled(false);
        resetButton.setTextColor(getResources().getColor(R.color.design_default_color_error));
    }

    private void resetBtnSetEnable() {
        resetButton.setEnabled(true);
        resetButton.setTextColor(getResources().getColor(R.color.teal_700));
    }
}