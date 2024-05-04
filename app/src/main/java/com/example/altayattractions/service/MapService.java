package com.example.altayattractions.service;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.altayattractions.R;
import com.example.altayattractions.db.DataBase;
import com.example.altayattractions.domain.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class MapService implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private final Context context;
    private final String pathToImageStorage = "gs://alaty-map.appspot.com";

    public MapService(Context context) {
        this.context = context;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Toast.makeText(context, latLng.latitude + " " + latLng.longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        Toast.makeText(context, "Long " + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        for (Place p :
                DataBase.getPlaces()) {
            googleMap.addMarker(new MarkerOptions().position(p.getLatLng()).title(p.getName()));

        }
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Place place = DataBase.getPlaceByName(marker.getTitle());
                Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.dialog_fragment);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                TextView item_name = dialog.getWindow().findViewById(R.id.item_name);
                TextView item_address = dialog.getWindow().findViewById(R.id.item_address);
                TextView item_info = dialog.getWindow().findViewById(R.id.item_info);
                ImageView imageView = dialog.getWindow().findViewById(R.id.item_image);

                item_name.setText(Objects.requireNonNull(place).getName());
                item_address.setText(Objects.requireNonNull(place).getAddress());
                item_info.setText(Objects.requireNonNull(place).getInformations());

                FirebaseApp.initializeApp(context);
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance(pathToImageStorage);
                StorageReference reference = firebaseStorage.getReference(place.getPathToImage());
                Glide.with(context).load(reference).into(imageView);
                return false;
            }
        });
    }
}
