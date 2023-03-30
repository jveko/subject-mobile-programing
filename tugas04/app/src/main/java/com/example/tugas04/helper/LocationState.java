package com.example.tugas04.helper;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.example.tugas04.LoginActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class LocationState {
    private LocationManager locationManager;
    private Location lastLocation;
    private Activity context;
    private static LocationState sharedInstance;
    private Geocoder geocoder;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    LocationState(Activity mContext) {
        context = mContext;
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        geocoder = new Geocoder(context, Locale.getDefault());
        checkPermissions();
    }

    public static LocationState sharedInstance(Activity mContext) {
        if (sharedInstance == null) {
            LocationState state = new LocationState(mContext);
            sharedInstance = state;
        }
        return sharedInstance;
    }

    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET}, 10);
            }
            return;
        }
    }

    @SuppressLint("MissingPermission")
    public void updateLocation() {
        checkPermissions();
        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    private Address getLocationRaw() {
        try {
            return geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAddress() {
        if(getLocationRaw() != null){
            return "Alamat " + getLocationRaw().getAddressLine(0);
        }
        return "";
    }
    public String getCountry() {
        if(getLocationRaw() != null){
            return "Negara " + getLocationRaw().getCountryName();
        }
        return "";
    }

    public String getTime() {
        return "Waktu Sekarang " + sdf.format(new Date());
    }

}
