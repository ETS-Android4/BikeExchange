package com.hfad.bikeexchange;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class BikeDetailsFragment extends Fragment {

    private DatabaseReference allBikesDbRef;
    private ImageView bikeImage;
    private TextView frameNameTxtView, bikeDescriptionTxtView, priceTxtView;
    private String imageRefValue;
    private String frameNameValue;
    private String bikeDescriptionValue;
    private String cartListBikeKey;
    private String categoryBike;
    private int priceValue;
    private final ArrayList<String> bikeFramesInShopCart = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_bike_details, container, false);

        allBikesDbRef = FirebaseDatabase.getInstance().getReference("bikes");

        bikeImage = view.findViewById(R.id.bike_details_image);
        frameNameTxtView = view.findViewById(R.id.bike_details_frame_name);
        bikeDescriptionTxtView = view.findViewById(R.id.bike_details_description);
        priceTxtView = view.findViewById(R.id.bike_details_price);
        Button addToCartButton = view.findViewById(R.id.add_to_cart);
        Button goToShoppingCart = view.findViewById(R.id.go_to_shopping_cart);

        String frameNameFromArgs = getIntentArguments();
        getDataFromFirebase(frameNameFromArgs);

        addToCartButton.setOnClickListener(v -> addBikeToCart());
        goToShoppingCart.setOnClickListener(v -> setFragment(new ShoppingCartFragment()));

        return  view;
    }

    private String getIntentArguments() {
        Bundle args = getArguments();
        assert args != null;
        return args.getString("frameName");
    }

    private void getDataFromFirebase(String frameName) {
        allBikesDbRef.orderByChild("frame").equalTo(frameName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot bikeSnapshot: snapshot.getChildren()) {
                            cartListBikeKey = bikeSnapshot.getKey();
                            categoryBike = bikeSnapshot.child("category").getValue(String.class);
                            imageRefValue = bikeSnapshot.child("image").getValue(String.class);
                            frameNameValue = bikeSnapshot.child("frame").getValue(String.class);
                            bikeDescriptionValue = bikeSnapshot.child("description").getValue(String.class);
                            priceValue = (Objects.requireNonNull(bikeSnapshot.child("price").getValue(Integer.class)));
                        }
                        setViewComponents(imageRefValue);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w( "onCancelled", error.toException());
                    }
                });
    }

    private void setViewComponents(String imageRef) {
        Glide.with(this).load(imageRef).into(bikeImage);
        frameNameTxtView.setText(frameNameValue);
        bikeDescriptionTxtView.setText(bikeDescriptionValue);
        priceTxtView.setText(String.format( "$ %s",priceValue));
    }

    private void addBikeToCart() {
        DatabaseReference shoppingCartList = FirebaseDatabase.getInstance().getReference("cart list");

        HashMap<String, Object> bikeDataMap = new HashMap<>();
        bikeDataMap.put("category", categoryBike);
        bikeDataMap.put("image", imageRefValue);
        bikeDataMap.put("frame", frameNameValue);
        bikeDataMap.put("description", bikeDescriptionValue);
        bikeDataMap.put("price", priceValue);

        shoppingCartList.child("bikes").child(cartListBikeKey)
                .updateChildren(bikeDataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(),"Bike has been added to shopping cart",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Something went wrong",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.bike_details_activity_content_frame, fragment).commit();
    }

    private void startSingleCategoryActivity(){
        Intent singleCategoryActivityIntent = new Intent(getActivity(), SingleCategoryActivity.class);
        singleCategoryActivityIntent.putExtra("category", categoryBike);
        startActivity(singleCategoryActivityIntent);
    }

    public void myOnKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            startSingleCategoryActivity();
    }

    private Fragment setArrayListToShopCartFragment() {
        Fragment shopCartFragment = new ShoppingCartRecycler();
        Bundle args = new Bundle();
        args.putStringArrayList("shopCartBikeList", bikeFramesInShopCart);
        shopCartFragment.setArguments(args);
        return shopCartFragment;
    }
}
