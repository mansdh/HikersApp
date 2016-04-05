package com.example.hewlett_packard.hikerswatchapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.location.LocationListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String provider;
    private GoogleMap mMap;
    LocationManager locationManager;
    String adrs;
    Double lat, lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String location_context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(location_context);
        testProviders();

    }

    public void testProviders() {
        TextView tv = (TextView) findViewById(R.id.myTextView);
        StringBuilder sb = new StringBuilder("DATA: \n");
        String providerName = LocationManager.GPS_PROVIDER;
        LocationProvider gpsProvider;
        gpsProvider = locationManager.getProvider(providerName);


        boolean enabledOnly = true;


        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);

        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 1000, 0, new LocationListener() {
            public void onLocationChanged(Location location) {
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

        });


        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            float acr = location.getAccuracy();
            float spd = location.getSpeed();
            float brg = location.getBearing();
            double alt = location.getAltitude();


            Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());

            try{
                List<Address> addresses = gc.getFromLocation(lat,lng,1);
                StringBuilder sb2 = new StringBuilder();
                if(addresses.size()>0){

                    Address address = addresses.get(0);
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                        sb2.append(address.getAddressLine(i)).append("\n");

                        sb2.append(address.getLocality()).append("\n");
                        sb2.append(address.getPostalCode()).append("\n");
                        sb2.append(address.getCountryName()).append("\n");
                }
                adrs = sb2.toString();

            }catch (IOException e){}



            sb.append("Latitude: ").append(lat).append("\nLongitude:  ").append(lng).append("\nAccuracy: ").append(acr).append(" m").append("\nSpeed: ").append(spd).append(" m/s").append("\nBearing: ").append(brg).append("\nAltitude: ").append(alt).append(" m").append("\nAdress: ").append(adrs);

        } else {
            sb.append("No Location");
        }

        tv.setText(sb);


    }

    public void onClickMap() {

        Intent intent = new Intent(this,MapsActivity2.class);
        intent.putExtra("lat2",lat);
        intent.putExtra("lng2",lng);

        startActivity(intent);



    }


}
