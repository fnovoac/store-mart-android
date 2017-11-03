package com.shop_mart.listeners;

import android.location.Location;

/**
 * Created by parallels on 11/3/17.
 */

public interface CurrentLocationListener {

    /**
     * get current location
     */
    void onCurrentLocation(Location location);

    /**
     * Permission denied
     */
    void onPermissionDenied();
}
