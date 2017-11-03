package com.shop_mart.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.shop_mart.listeners.CurrentLocationListener;
import com.shop_mart.trackers.SettingsLocationTracker;

/**
 * Created by parallels on 11/3/17.
 */

public class CurrentLocationReceiver extends BroadcastReceiver {

    private CurrentLocationListener currentLocationListener;

    public CurrentLocationReceiver(){
    }

    public CurrentLocationReceiver(CurrentLocationListener currentLocationListener) {
        this.currentLocationListener = currentLocationListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (null != intent && intent.getAction().equals(SettingsLocationTracker.ACTION_CURRENT_LOCATION_BROADCAST)) {
            Location locationData = (Location) intent.getParcelableExtra(SettingsLocationTracker.LOCATION_MESSAGE);
            currentLocationListener.onCurrentLocation(locationData);
        }

        if (null != intent && intent.getAction().equals(SettingsLocationTracker.ACTION_PERMISSION_DENIED)) {
            currentLocationListener.onPermissionDenied();
        }

    }
}
