package com.example.altayattractions.service;

import static android.app.Service.START_NOT_STICKY;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.example.altayattractions.db.DataBase;
import com.example.altayattractions.domain.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

public class MyService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Получаем ссылку на узел "places" в Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.
                getInstance().getReference().child("places");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Перебираем все дочерние узлы узла "places"
                for (DataSnapshot placeSnapshot :
                        dataSnapshot.getChildren()) {

                    String name = placeSnapshot.child("name").getValue(String.class);
                    String pathToImage = placeSnapshot.child("pathToImage").getValue(String.class);
                    String address = placeSnapshot.child("address").getValue(String.class);
                    String informations = placeSnapshot.child("informations").getValue(String.class);
                    double latitude = placeSnapshot.child("latitude").getValue(Double.class);
                    double longitude = placeSnapshot.child("longitude").getValue(Double.class);

                    DataBase.add(new Place(name, pathToImage, address, informations, latitude, longitude));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при чтении данных из Firebase Realtime Database
            }
        });

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
