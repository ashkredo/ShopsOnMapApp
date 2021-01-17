package com.example.shopsonmapapp;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import com.example.shopsonmapapp.Database.ShopDB;
import com.example.shopsonmapapp.Models.Shop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ShopDB sDB;

    private LocationManager lm;
    private double la, lo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        checkPermission();
        localization();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fillMap(mMap);
        localization();
        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        //Toast.makeText(this, whereAmI+"", Toast.LENGTH_SHORT).show();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.231905, 21.006545), 11.0f));
    }

    public void fillMap(GoogleMap googleMap){
        List<Shop> shops;

        sDB = new ShopDB(MapsActivity.this);
        shops = sDB.getAllShops();

        for(Shop shop: shops){
            LatLng pos = new LatLng(shop.getLatitude(), shop.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(pos).title(shop.getName().toString()));
            googleMap.addCircle(new CircleOptions().center(pos).radius(100).fillColor(Color.BLUE));
        }
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    public void localization(){
        int minCzas = 0;
        int minDystans = 0;

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new LocationListener() {

            public void onLocationChanged(Location location) {
                la = location.getLatitude();
                lo = location.getLongitude();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {

            }
        };
        checkPermission();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minCzas, minDystans, ll);
    }
    public void message(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }
}
