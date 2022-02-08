package com.hfad.bikeexchange.screen.item.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.cart.fragment.ShoppingCartFragment;

import java.util.HashMap;
import java.util.Objects;

public class ItemDetailsFragment extends Fragment {
    public ItemDetailsFragment() {}

    private DatabaseReference allItemsDbRef;
    private DatabaseReference shoppingCartList;
    private FirebaseAuth auth;

    private ImageView itemImgView;
    private TextView itemNameTxtView, itemDescriptionTxtView, itemPriceTxtView;
    private String itemKey;
    private String itemImageRefValue;
    private String itemNameValue;
    private String itemDescriptionValue;
    private int itemPriceValue;
    private boolean isDiscountedItem;

    private Button addToCartButton;
    private Button goToShoppingCart;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_position_details, container, false);

        allItemsDbRef = FirebaseDatabase.getInstance().getReference("items");
        shoppingCartList = FirebaseDatabase.getInstance().getReference("cart");
        auth = FirebaseAuth.getInstance();

        itemImgView = view.findViewById(R.id.bike_details_image);
        itemNameTxtView = view.findViewById(R.id.bike_details_frame_name);
        itemDescriptionTxtView = view.findViewById(R.id.bike_details_description);
        itemPriceTxtView = view.findViewById(R.id.bike_details_price);
        addToCartButton = view.findViewById(R.id.add_to_cart);
        goToShoppingCart = view.findViewById(R.id.go_to_shopping_cart);

        int itemIdFromArgs = getIntentArguments();
        getDataFromFirebase(itemIdFromArgs);

        addToCartButton.setOnClickListener(v -> addBikeToCart(itemIdFromArgs));
        goToShoppingCart.setOnClickListener(v -> setFragment(new ShoppingCartFragment()));

        return  view;
    }

    private int getIntentArguments() {
        Bundle args = getArguments();
        assert args != null;
        return args.getInt("id");
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        addToCartButton.setEnabled(currentUser != null);
        goToShoppingCart.setEnabled(currentUser != null);
    }

    private void getDataFromFirebase(int itemId) {
        allItemsDbRef.orderByChild("id").equalTo(itemId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                            itemKey = itemSnapshot.getKey();
                            itemImageRefValue = itemSnapshot.child("image").getValue(String.class);
                            itemNameValue = itemSnapshot.child("name").getValue(String.class);
                            itemDescriptionValue = itemSnapshot.child("description").getValue(String.class);
                            itemPriceValue = Objects.requireNonNull(itemSnapshot.child("price").getValue(Integer.class));
                            isDiscountedItem = Objects.requireNonNull(itemSnapshot.child("discount").getValue(Boolean.class));
                        }
                        setViewComponents(itemImageRefValue);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w( "onCancelled", error.toException());
                    }
                });
    }

    private void setViewComponents(String imageRef) {
        Glide.with(this)
                .load(imageRef)
                .into(itemImgView);

        itemNameTxtView.setText(itemNameValue);
        itemDescriptionTxtView.setText(itemDescriptionValue);

        if (isDiscountedItem)
            itemPriceTxtView.setText(String.format( "$ %s", (int)(itemPriceValue * 0.8)));
        else
            itemPriceTxtView.setText(String.format("$ %s", itemPriceValue));
    }

    private void addBikeToCart(int itemId) {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        HashMap<String, Object> cartItem = new HashMap<>();
        cartItem.put("id", itemId);
        cartItem.put("name", itemNameValue);
        cartItem.put("image", itemImageRefValue);
        cartItem.put("price", itemPriceValue);
        cartItem.put("uid", userId);

        shoppingCartList.child(itemKey).updateChildren(cartItem)
                .addOnSuccessListener(unused ->
                        Toast.makeText(getContext(),"Item has been added to cart",
                                Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Something went wrong",
                                Toast.LENGTH_SHORT).show());
    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.item_details_activity_content_frame, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("home")
                .commit();
    }
}
