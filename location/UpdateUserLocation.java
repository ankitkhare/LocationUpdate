package com.bansalankit.openweather.location;

import android.location.Location;

/**
 * Created by ttn on 7/3/17.
 */

public interface UpdateUserLocation {

    void onLocationChanged(Location iLocation);

    void onError(String miMessaage);
}
