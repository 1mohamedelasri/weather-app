package com.devel.weatherapp.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import android.widget.Toast;

import com.devel.weatherapp.MainActivity;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;

public class LocationPresenter {

    private Context sampleView;

    public LocationPresenter(Context view) {
        this.sampleView = view;
    }

    public void destroy() {
        sampleView = null;
    }

    public void onLocationFailed(@FailType int failType) {
        //sampleView.dismissProgress();

        switch (failType) {
            case FailType.TIMEOUT: {
                Toast.makeText(sampleView,"Couldn't get location, and timeout!",Toast.LENGTH_SHORT).show();

                break;
            }
            case FailType.PERMISSION_DENIED: {
                Toast.makeText(sampleView,"Couldn't get location, because user didn't give permission!",Toast.LENGTH_SHORT).show();
                break;
            }
            case FailType.NETWORK_NOT_AVAILABLE: {
                Toast.makeText(sampleView,"Couldn't get location, because network is not accessible!",Toast.LENGTH_SHORT).show();
                break;
            }
            case FailType.GOOGLE_PLAY_SERVICES_NOT_AVAILABLE: {
                Toast.makeText(sampleView,"Couldn't get location, because Google Play Services not available!",Toast.LENGTH_SHORT).show();
                break;
            }
            case FailType.GOOGLE_PLAY_SERVICES_SETTINGS_DIALOG: {
                Toast.makeText(sampleView,"Couldn't display settingsApi dialog!",Toast.LENGTH_SHORT).show();

                break;
            }
            case FailType.GOOGLE_PLAY_SERVICES_SETTINGS_DENIED: {
                Toast.makeText(sampleView,"Couldn't get location, because user didn't activate providers via settingsApi!",Toast.LENGTH_SHORT).show();

                break;
            }
            case FailType.VIEW_DETACHED: {
                Toast.makeText(sampleView,"Couldn't get location, because in the process view was detached!",Toast.LENGTH_SHORT).show();

                break;
            }
            case FailType.VIEW_NOT_REQUIRED_TYPE: {
                Toast.makeText(sampleView,"Couldn't get location, "
                        + "because view wasn't sufficient enough to fulfill given configuration!",Toast.LENGTH_SHORT).show();

                break;
            }
            case FailType.UNKNOWN: {
                Toast.makeText(sampleView,"Couldn't get location, "
                        + "Ops! Something went wrong!",Toast.LENGTH_SHORT).show();

                break;
            }
        }
    }

    public void onProcessTypeChanged(@ProcessType int newProcess) {
        switch (newProcess) {
            case ProcessType.GETTING_LOCATION_FROM_GOOGLE_PLAY_SERVICES: {
                Toast.makeText(sampleView,"Getting Location from Google Play Services...",Toast.LENGTH_SHORT).show();
                break;
            }
            case ProcessType.GETTING_LOCATION_FROM_GPS_PROVIDER: {
                Toast.makeText(sampleView,"Getting Location from GPS...",Toast.LENGTH_SHORT).show();
                break;
            }
            case ProcessType.GETTING_LOCATION_FROM_NETWORK_PROVIDER: {
                Toast.makeText(sampleView,"Getting Location from Network...",Toast.LENGTH_SHORT).show();
                break;
            }
            case ProcessType.ASKING_PERMISSIONS:
            case ProcessType.GETTING_LOCATION_FROM_CUSTOM_PROVIDER:
                // Ignored
                break;
        }
    }

    public void onLocationChanged() {
        Toast.makeText(sampleView,"Location is changed...",Toast.LENGTH_SHORT).show();

    }

    private String getText(Location location) {
        return location.getLatitude() + ", " + location.getLongitude() + "\n";
    }

}
