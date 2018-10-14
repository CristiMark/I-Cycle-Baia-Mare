package vyrus.bikegps;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location Location; // location
   static double latitude; // latitude
   static double longitude; // longitude
    float accuracy;

    private static final int TWO_MINUTES = 1000 * 60 * 2;


    long MIN_DISTANCE_FOR_UPDATES = 10; // 10 meters
    long MIN_TIME_BW_UPDATE = 1000 * 10; // 20 sec

    /*  // The minimum distance to change Updates in meters
      private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

      // The minimum time between updates in milliseconds
      private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
  */
    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context, long MIN_DISTANCE_FOR_UPDATES, long MIN_TIME_BW_UPDATE) {
        this.mContext = context;
        this.MIN_DISTANCE_FOR_UPDATES = MIN_DISTANCE_FOR_UPDATES;
        this.MIN_TIME_BW_UPDATE = MIN_TIME_BW_UPDATE;
        getLocation();
       // getLocationWithCriteria();
    }

    public GPSTracker(Context context) {
        this.mContext = context;
        //getLocation();
        getLocationWithCriteria();
    }

    public GPSTracker(Context context, long MIN_DISTANCE_FOR_UPDATES, long MIN_TIME_BW_UPDATE, String with) {
        this.mContext = context;
        this.MIN_DISTANCE_FOR_UPDATES = MIN_DISTANCE_FOR_UPDATES;
        this.MIN_TIME_BW_UPDATE = MIN_TIME_BW_UPDATE;
        // getLocation();
    }

    public Location getLocationWithCriteria() {

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        locationManager = (LocationManager) mContext
                .getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);
        Location mostRecentLocation = locationManager.getLastKnownLocation(provider);

        if (mostRecentLocation != null) {
            latitude = mostRecentLocation.getLatitude();
            longitude = mostRecentLocation.getLongitude();
            accuracy = mostRecentLocation.getAccuracy();
        }

        return mostRecentLocation;
    }

    // @TargetApi(23)
    public Location getLocation() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
               // Log.i("This", String.valueOf(isGPSEnabled));
                if (isGPSEnabled) {
                    isNetworkEnabled = false;
                    if (Location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATE,
                                MIN_DISTANCE_FOR_UPDATES, this);
                        Log.i("This", "GPS Enabled");
                        if (locationManager != null) {
                            Location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATE,
                                    MIN_DISTANCE_FOR_UPDATES, this);
                            if (Location != null) {
                                latitude = Location.getLatitude();
                                longitude = Location.getLongitude();
                                accuracy = Location.getAccuracy();
                            }
                        }
                    }
                }

                if (isNetworkEnabled) {
                    isGPSEnabled = false;
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATE,
                            MIN_DISTANCE_FOR_UPDATES, this);
                    Log.i("This", "Network");
                    if (locationManager != null) {
                        Location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (Location != null) {
                            latitude = Location.getLatitude();
                            longitude = Location.getLongitude();
                            accuracy = Location.getAccuracy();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Location;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }


    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    // @TargetApi(23)
    public void stopUsingGPS() {
        if (locationManager != null) {

            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (Location != null) {
            latitude = Location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (Location != null) {
            longitude = Location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to get Accuracy
     * */
    public double getAccuracy() {
        if (Location != null) {
            accuracy = Location.getAccuracy();
        }

        // return longitude
        return accuracy;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(final Context context) {
        try {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            Log.i("This", e.getMessage());

        }
    }

    @Override
    public void onLocationChanged(Location location) {

//        Log.i("This", String.valueOf(location.getTime()));
        if (location != null && location.getAccuracy()<10) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            accuracy = location.getAccuracy();
            Log.i("This", "OnLocationChange");
            Log.i("This", String.valueOf(accuracy));
            Log.i("This", String.valueOf(location.getProvider()));
        } else {
            isGPSEnabled = false;
            if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATE,
                    MIN_DISTANCE_FOR_UPDATES, this);
            Log.i("This", "Network");
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    accuracy = location.getAccuracy();
                    Log.i("This", String.valueOf(accuracy));
                }
                else {
                    Toast.makeText(mContext, "Error Location please run", Toast.LENGTH_SHORT).show();
                }
            }
        }
        }


    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
