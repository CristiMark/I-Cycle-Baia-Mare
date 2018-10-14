package vyrus.bikegps.Activitys;



import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.geojson.GeoJsonLayer;

import org.json.JSONException;

import java.io.IOException;

import vyrus.bikegps.DataBase;
import vyrus.bikegps.Descriptions;
import vyrus.bikegps.R;

/**
 * Created by Vyrus on 04/12/2016.
 */
//@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class SponsorActivity extends Activity implements Descriptions.OnTaskCompleted {

    DataBase db;
    ProgressBar progressBar;
    ViewFlipper SponsorViewFlipper;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    Descriptions desc;

    private Descriptions.OnTaskCompleted listener = new Descriptions.OnTaskCompleted() {
        public void onTaskCompleted() {
            Intent intent = new Intent(SponsorActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.sponsor_fragm);

      //  ImageView mImageView;
       // mImageView = (ImageView) findViewById(R.id.Splash);

        SponsorViewFlipper = (ViewFlipper) findViewById(R.id.sponsorViewFlipper);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

        // set the animation type to ViewFlipper
        SponsorViewFlipper.setInAnimation(in);
        SponsorViewFlipper.setOutAnimation(out);

        SponsorViewFlipper.setFlipInterval(2500);
        SponsorViewFlipper.startFlipping();

      //  mImageView.setImageResource(R.drawable.sponsor);
        // progressBar = (ProgressBar) findViewById(R.id.progressBar);
        db = new DataBase(getApplicationContext());


    //  setProgressBarIndeterminateVisibility(true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Cursor have = db.getData("Select desc_name from bike_description ");

                if (have == null || have.getCount() <= 0) {
                    try {

                        GoogleMap googleMap = null;
                        GeoJsonLayer layer = new GeoJsonLayer(googleMap, R.raw.mapjust, getApplicationContext());
                        desc = new Descriptions(listener,layer, SponsorActivity.this, db);
                        desc.execute();
                        //   setProgressBarIndeterminateVisibility(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ;
                }else{
                    Permission();
                    SponsorViewFlipper.stopFlipping();
                    Intent intent = new Intent(SponsorActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, 1500);


    }

    private void Permission() {

        if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                !checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                !checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        !checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)||
                !checkPermission(Manifest.permission.GET_ACCOUNTS)) {

            requestPermission();


        }
    }

    private boolean checkPermission(String permision) {
        int result = ActivityCompat.checkSelfPermission(getApplicationContext(), permision);
        // int resultFine = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }

    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.GET_ACCOUNTS)) {

            // Toast.makeText(context,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_SHORT).show();
            // getLocations();
            startInstalledAppDetailsActivity(this);
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, 1);
        }
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Snackbar.make(view, "Permission Granted, Now you can access location data.", Snackbar.LENGTH_LONG).show();
                    // getLocations();
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                 //   googleMap.setMyLocationEnabled(true);
                    //  googleMap.getUiSettings().setTiltGesturesEnabled(true);
                    //  googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                } else {


                    //  Toast.makeText(this, "Permission Denied, You cannot access location data.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onTaskCompleted() {

    }
}
