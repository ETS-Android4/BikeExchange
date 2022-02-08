package com.hfad.bikeexchange.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.io.InputStream;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, Registry registry) {
        registry.append(StorageReference.class, InputStream.class, new FirebaseImageLoader.Factory());
    }
}
