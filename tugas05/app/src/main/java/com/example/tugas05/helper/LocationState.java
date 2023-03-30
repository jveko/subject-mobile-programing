package com.example.tugas05.helper;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;

public class LocationState {
    private final LocationManager locationManager;
    private LocationListener listener;
    private Location lastLocation;
    private final Activity context;
    private static LocationState sharedInstance;
    private final Geocoder geocoder;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    LocationState(Activity mContext) {
        context = mContext;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(context, Locale.getDefault());
        listener = location -> {
            lastLocation = location;
            System.out.println(lastLocation);
        };
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

    @SuppressLint("NewApi")
    public void updateLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET}, 10);
            }
            return;
        }
        locationManager.getCurrentLocation(
                LocationManager.GPS_PROVIDER,
                null,
                ContextCompat.getMainExecutor(context),
                location -> {
                    lastLocation = location;
                    return;
                });

    }

    private Address getLocationRaw() {
        if(lastLocation != null){
            try {
                return geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1).get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
