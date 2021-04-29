package co.dekhok.railway;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.Nullable;

public class LocationService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null && locationResult.getLastLocation() != null) {
                currentLocation = locationResult.getLastLocation();
//                Log.d("LOCATION", "Current: " + currentLocation);
                if (locationInterface != null) {
                    if (isTracking) {
                        if (startLocation == null) {
                            startLocation = locationResult.getLastLocation();
                        } else {
                            Location endLocation = locationResult.getLastLocation();
                            if (endLocation != null) {
//                                Log.d("LOCATION DISTANCE", endLocation.distanceTo(startLocation) + "m");
                                locationInterface.distanceUpdate(endLocation.distanceTo(startLocation));
                                if (endLocation.distanceTo(startLocation) > 50) {
                                    locationInterface.distanceMoreThanLimit();
                                    stopLocationServices();
                                }
                            }
                        }
                    }
                    else {
                        locationInterface.onLocationUpdated(currentLocation.getLatitude(),currentLocation.getLongitude());
                    }
                }
            }
            super.onLocationResult(locationResult);
        }
    };
    private LocationInterface locationInterface;
    private boolean isTracking;
    private Location startLocation;
    private Location currentLocation;

    @SuppressLint("MissingPermission")
    public void startLocationService() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(50);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(this)
                    .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }
    public void stopLocationServices() {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        } else {
            stopSelf();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startLocationService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent != null) {
//            String action = intent.getAction();
//
//            if (action != null) {
//                if (action.equals(ACTION_START_LOCATION_SERVICE)) {
//                    startLocationService();
//                }
//                else if(action.equals(ACTION_STOP_LOCATION_SERVICE)) {
//                    stopLocationServices();
//                }
//            }
//        }
//        startLocationService();
        return super.onStartCommand(intent, flags, startId);
    }



    public LocationInterface getLocationInterface() {
        return locationInterface;
    }

    public void setLocationInterface(LocationInterface locationInterface) {
        this.locationInterface = locationInterface;
    }

    public boolean isTracking() {
        return isTracking;
    }

    public void setTracking(boolean tracking) {
        isTracking = tracking;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
