package com.bansalankit.openweather.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * This class is used to initialize and use GoogleApiClient for location updates.
 * Pass context and update Listener for initialization.
 * Created by Ankit Khare.
 */

public class MyLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //Google Api clinet used for getting location.
    private GoogleApiClient mApiClient;
    private UpdateUserLocation mNotifylistener;

    //Requesting Location
    private LocationRequest mLocationRequest;

    //Time frame in which you want updates.
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    //Preventing user from creating instance of this class without Context and NotifyListener.
    private MyLocation() {

    }

    /**
     * Parameterised Construtor for setting GoogleApiClinet.
     *
     * @param context   Who ever call's this class.
     * @param iListener who want updates about location
     */
    public MyLocation(Context context, UpdateUserLocation iListener) {
        // Create the location client to start receiving updates
        mApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        //Listener to notify location updates
        mNotifylistener = iListener;
    }

    //Return Instance of GoogleApiClient.
    public GoogleApiClient getApiClient() {
        return mApiClient;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
// Get last known recent location.
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }

    // Trigger new location updates at interval
    private void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient,
                mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        //Set Location Updates
        mNotifylistener.onLocationChanged(location);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mNotifylistener.onError("Can't Get user Location. Please try again after some time");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mNotifylistener.onError(connectionResult.getErrorMessage());
    }


    /**
     * Stop the location updates and disconnect GoogleApiClient.
     */
    public void stopLocationUpdates() {
        // Disconnecting the client invalidates it.
        LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, this);

        // only stop if it's connected, otherwise we crash
        if (mApiClient != null) {
            mApiClient.disconnect();
        }
    }


}
