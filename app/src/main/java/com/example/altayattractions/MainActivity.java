package com.example.altayattractions;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.altayattractions.db.DataBase;
import com.example.altayattractions.domain.Place;
import com.example.altayattractions.service.MapService;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements CreatorMap {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Создаем и запускаем новый поток для чтения данных из Firebase и их передачи в DataBase
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Получаем ссылку на узел "places" в Firebase Realtime Database
                DatabaseReference databaseRef = FirebaseDatabase.
                        getInstance().getReference().child("places");

                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                            String name = placeSnapshot.child("name").getValue(String.class);
                            String pathToImage = placeSnapshot.child("pathToImage").getValue(String.class);
                            String address = placeSnapshot.child("address").getValue(String.class);
                            String informations = placeSnapshot.child("informations").getValue(String.class);
                            double latitude = placeSnapshot.child("latitude").getValue(Double.class);
                            double longitude = placeSnapshot.child("longitude").getValue(Double.class);


                            DataBase.add(new Place(name, pathToImage, address, informations, latitude, longitude));
                        }

                        // Передаем список в основной поток после завершения чтения данных из Firebase
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                createMap();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }

                });
            }
        }).start();
    }

    @Override
    public void createMap() {

        setContentView(R.layout.activity_main);
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(new MapService(this));
    }
}
