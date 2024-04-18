package com.example.altayattractions.service;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.altayattractions.R;
import com.example.altayattractions.db.DataBase;
import com.example.altayattractions.domain.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class MapService implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private final Context context;

    public MapService(Context context) {
        this.context = context;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        for (Place p:
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
                TextView item_adress = dialog.getWindow().findViewById(R.id.item_adress);
                TextView item_info = dialog.getWindow().findViewById(R.id.item_info);
                ImageView imageView = dialog.getWindow().findViewById(R.id.item_image);

                item_name.setText(Objects.requireNonNull(place).getName());
                item_adress.setText(Objects.requireNonNull(place).getAdress());
                item_info.setText(Objects.requireNonNull(place).getInformations());

                return false;
            }
        });
    }
}
