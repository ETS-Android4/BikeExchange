package com.hfad.bikeexchange.screen.menu.discount.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.cart.fragment.ShoppingCartFragment;
import com.hfad.bikeexchange.screen.home.MainActivity;

import java.util.HashMap;
import java.util.Objects;

public class DiscountedItemFragment extends Fragment {
    public DiscountedItemFragment() {}

    public String TAG_ITEM ="item";
    private DatabaseReference allItemsRef;
    private DatabaseReference shoppingCartRef;
    private FirebaseAuth firebaseAuth;

    private ImageView itemImgView;
    private TextView itemNameTxtView, itemDescriptionTxtView, itemCommonPriceTxtView, itemDiscountPriceTxtView;
    private String itemKey, itemImageValue, itemNameValue, itemDescriptionValue;
    private int itemPriceValue;

    private Button buyNowBtn, addToCartBtn, addToSavedBtn;
    private ImageButton toShopCartImgBtn, closeImgBtn;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discounted_item,
                container,
                false);

        allItemsRef = FirebaseDatabase.getInstance().getReference("items");
        shoppingCartRef = FirebaseDatabase.getInstance().getReference("cart");
        firebaseAuth = FirebaseAuth.getInstance();

        itemImgView = view.findViewById(R.id.discounted_item_image);
        itemNameTxtView = view.findViewById(R.id.discounted_item_name);
        itemDescriptionTxtView = view.findViewById(R.id.discounted_item_description);
        itemCommonPriceTxtView = view.findViewById(R.id.discounted_common_price);
        itemDiscountPriceTxtView = view.findViewById(R.id.discounted_price);

        closeImgBtn = view.findViewById(R.id.dsc_item_close);
        addToCartBtn = view.findViewById(R.id.dsc_add_to_cart);
        toShopCartImgBtn = view.findViewById(R.id.go_to_shopping_cart_img_btn);

        int itemIdFromArgs = getIntentArguments();
        getItemFromFirebase(itemIdFromArgs);

        closeImgBtn.setOnClickListener(v -> startMainActivity());
        addToCartBtn.setOnClickListener(v -> addBikeToCart());
        toShopCartImgBtn.setOnClickListener(v -> setFragment(new ShoppingCartFragment()));

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        addToCartBtn.setEnabled(currentUser != null);
        toShopCartImgBtn.setEnabled(currentUser != null);
    }

    private int getIntentArguments() {
        Bundle args = getArguments();
        assert args != null;
        return args.getInt("id");
    }

    private void getItemFromFirebase(int itemIdFromArgs) {
        allItemsRef.orderByChild("id").equalTo(itemIdFromArgs)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            itemKey = itemSnapshot.getKey();
                            itemImageValue = itemSnapshot.child("image").getValue(String.class);
                            itemNameValue = itemSnapshot.child("name").getValue(String.class);
                            itemDescriptionValue = itemSnapshot.child("description").getValue(String.class);
                            itemPriceValue = Objects.requireNonNull(itemSnapshot.child("price").getValue(Integer.class));
                        }
                        setViewComponents(itemImageValue);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w( "onCancelled", error.toException());
                    }
                });
    }

    private void addBikeToCart() {
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        HashMap<String, Object> cartItem = new HashMap<>();
        cartItem.put("image", itemImageValue);
        cartItem.put("name", itemNameValue);
        cartItem.put("price", itemPriceValue);
        cartItem.put("uid", userId);

        shoppingCartRef.child(itemKey).updateChildren(cartItem)
                .addOnSuccessListener(unused ->
                        Toast.makeText(getContext(),"Item has been added to cart",
                            Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Something went wrong",
                            Toast.LENGTH_SHORT).show());
    }

    private void setViewComponents(String itemImageValue) {
        Glide.with(this)
                .load(itemImageValue)
                .into(itemImgView);

        itemNameTxtView.setText(itemNameValue);
        itemDescriptionTxtView.setText(itemDescriptionValue);
        itemCommonPriceTxtView.setText(String.format("$ %s", itemPriceValue));
        itemCommonPriceTxtView.setPaintFlags(itemCommonPriceTxtView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        itemDiscountPriceTxtView.setText(String.format("$ %s", (int)(itemPriceValue * 0.8)));
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.discounted_item_activity_content_frame, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("item")
                .commit();
    }

    private void startMainActivity() {
        Intent mainActivityIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainActivityIntent);
        requireActivity().finish();
    }
}