package com.example.sns;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

class MyLocationListener implements LocationListener {
    Context baseContext;
    TextToSpeech textToSpeech;

    String lastKnownLocation;

    public MyLocationListener(Context baseContext, TextToSpeech textToSpeech) {
        this.baseContext = baseContext;
        this.textToSpeech = textToSpeech;
    }

    @Override
    public void onLocationChanged(Location loc) {
        Toast.makeText(
                this.baseContext,
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v(TAG, longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v(TAG, latitude);

        /*------- To get city name from coordinates -------- */
        String cityName = null;
        String addressLine = null;
        Geocoder gcd = new Geocoder(this.baseContext, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
                addressLine = addresses.get(0).getThoroughfare();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String s = cityName + "\n"
                + addressLine;

        if(this.lastKnownLocation != null && !this.lastKnownLocation.equals(addressLine)) {
            textToSpeech.speak(addressLine, TextToSpeech.QUEUE_FLUSH, null, "id");
        }
        this.lastKnownLocation = addressLine;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
