package com.example.shopsonmapapp;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shopsonmapapp.Database.ShopDB;
import com.example.shopsonmapapp.Models.Shop;

import java.util.List;

public class ListActivity extends AppCompatActivity{

    private RecyclerView rv;
    private MyAdapter ma;
    private ShopDB sDB;

    private double lo, la;
    private LocationManager lm;

    private String PROX_ALERT_INTENT = "com.example.shopsonmapapp.intent.action.PROX_ALERT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        rv = findViewById(R.id.list_l);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(), llm.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);

        localization();
        fill();
    }

    public void goToAdd(View view) {
        addNew();
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void fill(){
        ma = new MyAdapter(getShops(), ListActivity.this);
        rv.setAdapter(ma);
    }

    private List<Shop> getShops() {
        List<Shop> list;

        sDB = new ShopDB(ListActivity.this);
        list = sDB.getAllShops();

        return list;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        final Shop shopToEdit = ma.getProductAtIndex(item.getGroupId());
        if(item.getTitle() == "Delete") {

            sDB.deleteShop(shopToEdit);

            fill();
        }
        return super.onContextItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    public void addNew(){
        AlertDialog.Builder ad = new AlertDialog.Builder(ListActivity.this);
        ad.setTitle("Add NEW");

        LinearLayout layout = new LinearLayout(ListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText name = new EditText(ListActivity.this);
        name.setHint("Name");

        layout.addView(name);

        ad.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(name.getText()!=null){
                            localization();
                            message(la+" "+lo+" "+name.getText().toString());
                            sDB = new ShopDB(ListActivity.this);

                            sDB.addShop(new Shop(name.getText().toString(),la, lo));
                            fill();

                            addProximityAlert(la, lo, 100.0f);
                        }
                    }
                });
        ad.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        ad.setView(layout, 50, 0, 50, 0);
        ad.show();
    }

    public void message(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
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

    private void addProximityAlert(double latitude, double longitude, double radius){
        Intent i = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, i, 0);

        int requestcode = 0;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, requestcode);
        }


        lm.addProximityAlert(latitude, longitude, (float)radius, -1, proximityIntent);

        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(new MyAlert(), filter);
    }
}
