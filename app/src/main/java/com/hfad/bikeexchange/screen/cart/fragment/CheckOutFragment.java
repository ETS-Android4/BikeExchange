package com.hfad.bikeexchange.screen.cart.fragment;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.home.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckOutFragment extends Fragment implements View.OnClickListener {
    public CheckOutFragment() {
    }

    private final static Pattern VALID_PHONE_NUMBER_REGEX =
            Pattern.compile("\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|\n" +
            "2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|\n" +
            "4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$");

    int totalPayment;
    EditText checkoutCountry, checkoutCity, checkoutPostCode, checkoutAddress, checkoutPhoneNumber;
    Button toThePaymentButton;
    ArrayList<Integer> itemsList = new ArrayList<>();

    FirebaseAuth firebaseAuth;
    DatabaseReference dbRef;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        checkoutCountry = view.findViewById(R.id.checkout_country);
        checkoutCity = view.findViewById(R.id.checkout_city);
        checkoutPostCode = view.findViewById(R.id.checkout_postcode);
        checkoutAddress = view.findViewById(R.id.checkout_address);
        checkoutPhoneNumber = view.findViewById(R.id.checkout_phone_number);

        toThePaymentButton = view.findViewById(R.id.to_the_payment);
        toThePaymentButton.setOnClickListener(this);

        dbRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        totalPayment = requireArguments().getInt("totalPayment");
        itemsList = requireArguments().getIntegerArrayList("itemsList");
        btnToPaymentDisabled();

        checkoutCountry.addTextChangedListener(new TextWatcher() {
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

        checkoutCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        checkoutAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        checkoutPostCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        checkoutPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private boolean isUserAuth() {
        if (firebaseAuth.getCurrentUser() == null){
            Toast.makeText(getActivity(), "Please, sign in or sign up in application.", Toast.LENGTH_LONG).show();
            return  false;
        }
        else return true;
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(checkoutCountry.getText()) &&
                !TextUtils.isEmpty(checkoutCity.getText()) &&
                !TextUtils.isEmpty(checkoutPostCode.getText()) &&
                !TextUtils.isEmpty(checkoutAddress.getText()) &&
                isValidPhoneNumber(checkoutPhoneNumber.getText().toString())) {
            btnToPaymentEnable();
        } else
            btnToPaymentDisabled();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        Matcher phoneNumberMatcher = VALID_PHONE_NUMBER_REGEX.matcher(phoneNumber);
        return phoneNumberMatcher.find();
    }

    private void btnToPaymentEnable() {
        toThePaymentButton.setEnabled(true);
        toThePaymentButton.setTextColor(getResources().getColor(R.color.teal_700));
    }

    private void btnToPaymentDisabled() {
        toThePaymentButton.setEnabled(false);
        toThePaymentButton.setTextColor(getResources().getColor(R.color.design_default_color_error));
    }

    private void startMainActivity(){
        Intent mainActivityIntent = new Intent(getActivity(), MainActivity.class);
        requireActivity().finish();
        startActivity(mainActivityIntent);
    }

    @Override
    public void onClick(View v) {
        if (isUserAuth()) {
            createOrder(itemsList);
        }
        else
            startMainActivity();
    }

    private void setPaymentFragment(String orderKey) {
        Bundle totalCostBundle = new Bundle();
        totalCostBundle.putInt("totalPayment", totalPayment);
        totalCostBundle.putString("orderKey", orderKey);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.checkout_content_frame, PaymentFragment.class, totalCostBundle, orderKey)
                .setReorderingAllowed(true)
                .addToBackStack("item")
                .commit();
    }

    private void createOrder(ArrayList<Integer> itemsList) {
        String orderKey = dbRef.push().getKey();
        String country = checkoutCountry.getText().toString();
        String city = checkoutCity.getText().toString();
        String postCode = checkoutPostCode.getText().toString();
        String address = checkoutAddress.getText().toString();
        String phoneNumber = checkoutPhoneNumber.getText().toString();
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("userId", userId);
        orderMap.put("country", country);
        orderMap.put("city", city);
        orderMap.put("postCode", postCode);
        orderMap.put("address", address);
        orderMap.put("phoneNumber", phoneNumber);
        orderMap.put("completed", false);

        dbRef.child("orders").child(Objects.requireNonNull(orderKey)).setValue(orderMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setPaymentFragment(orderKey);
                    }
                    else {
                        Toast.makeText(getActivity(), "Something went wrong",
                                Toast.LENGTH_SHORT).show();
                        btnToPaymentDisabled();
                    }
                });

        for (int i : itemsList) {
            HashMap<String, Object> itemsMap = new HashMap<>();
            itemsMap.put("itemId", i);
            itemsMap.put("userId", userId);

            String purchaseKey = dbRef.push().getKey();
            assert purchaseKey != null;

            dbRef.child("purchases").child(purchaseKey).updateChildren(itemsMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Items were added to the order",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Something went wrong",
                                    Toast.LENGTH_SHORT).show();
                            btnToPaymentDisabled();
                        }
                    });
        }
    }
}
