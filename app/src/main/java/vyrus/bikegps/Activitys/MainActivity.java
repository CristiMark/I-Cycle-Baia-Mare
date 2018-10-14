package vyrus.bikegps.Activitys;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonGeometry;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonLineString;
import com.google.maps.android.geojson.GeoJsonMultiLineString;
import com.google.maps.android.geojson.GeoJsonMultiPoint;
import com.google.maps.android.geojson.GeoJsonMultiPolygon;
import com.google.maps.android.geojson.GeoJsonPoint;
import com.google.maps.android.geojson.GeoJsonPolygon;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import vyrus.bikegps.CardsDescriptions;
import vyrus.bikegps.ChooseColorDialogClass;
import vyrus.bikegps.CustomTimerTask;
import vyrus.bikegps.DataBase;
import vyrus.bikegps.DrawRoute;
import vyrus.bikegps.PlaceListContract;
import vyrus.bikegps.R;
import vyrus.bikegps.SliderImageAdapter;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleMap.OnMapClickListener, View.OnTouchListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleMap googleMap;
    GeoJsonLayer globalLayout;
    // GPSTracker GPS;
    private boolean getLocate = true;
    static DataBase db;
    LatLng MyPosition;
    private BottomSheetBehavior bottomSheetBehavior;
    private View bottomSheet;
    LinearLayout LinearLayoutContain;
    CountDownTimer ShortestLocationJumpPiriod;

    private GoogleApiClient googleApiClient;

    TextView MarkerTitle, MarkerContent;
    FloatingActionMenu fab;
    FloatingActionButton RouteWithGoogleMap;
    FloatingActionButton GoToTop;
    List<Double> DistanceBtwPoints =new ArrayList<Double>();
    ImageView DialogColor;
    ChooseColorDialogClass cdd, AboutDialog;

    //ShowCaseView
    ShowcaseView NavigationToTut;
    ShowcaseView ColorBarTut;
    ShowcaseView FOBTut;
    ShowcaseView LocationTut;

    //MinimDistance
    int minIndex;

    //Firat time app run
    String MainFirstTime = "isFirstTime";
    String SideMenuFirstTime = "isFirstTime";
    String RouteFirstTime = "isFirstTime";

    SharedPreferences MainsharedTime;
    SharedPreferences SidesharedTime;
    SharedPreferences RoutesharedTime;

    //Location timer object
    Timer timer;
    TimerTask updateProfile;

    Polyline polyline;
    Marker marker;
    MarkerOptions markerOptions;
    List<LatLng> lineCordinates = new ArrayList<LatLng>();
    List<Polyline> polylines = new ArrayList<Polyline>();
    List<View> CardViews = new ArrayList<View>();
    private CopyOnWriteArrayList<Marker> mMarkerArray = new CopyOnWriteArrayList<Marker>();
    private CopyOnWriteArrayList<Marker> mMarkerIsVisible = new CopyOnWriteArrayList<Marker>();

    int ScreenHeight;
    int ScreenWight;

    String AllCoord = "";
    ChooseColorDialogClass cpp;
    private long MyUid = 42;


    // @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_sheets);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutContain = (LinearLayout) findViewById(R.id.ContentList);
        //  Colorlayout = (LinearLayout) findViewById(R.id.all_buttons);
        MarkerTitle = (TextView) findViewById(R.id.MarkerTile);
        MarkerContent = (TextView) findViewById(R.id.MarkerContent);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ScreenWight = size.x;
        ScreenHeight = size.y;

        db = new DataBase(getApplicationContext());

        MainsharedTime = getSharedPreferences(MainFirstTime,0);
        SidesharedTime = getSharedPreferences(SideMenuFirstTime,0);
        RoutesharedTime = getSharedPreferences(RouteFirstTime,0);

        RouteWithGoogleMap = (FloatingActionButton)findViewById(R.id.routeWithGoogleMap);
        RouteWithGoogleMap.setAlpha(0.50f);
        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        int BootomSheetHeight = 0;
        if(ScreenHeight <= 820){BootomSheetHeight = 150;}
                            else{BootomSheetHeight = 270;}

        bottomSheetBehavior.setPeekHeight(BootomSheetHeight);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

//        final CircularImageView circularImageView = (CircularImageView)findViewById(R.id.movingIconImageView);
//        circularImageView.bringToFront();
        GoToTop = (FloatingActionButton)findViewById(R.id.goUp);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // Toast.makeText(MainActivity.this, "yyyyyyyyyyyy", Toast.LENGTH_SHORT).show();
                    fab.showMenu(true);
                    bottomSheet.scrollTo(0, 0);
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    // Toast.makeText(MainActivity.this, "yyyyyyyyyyyy", Toast.LENGTH_SHORT).show();
                    fab.hideMenu(true);
                    GoToTop.setVisibility(View.VISIBLE);
                    bottomSheet.requestLayout();
                    bottomSheet.invalidate();
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // Toast.makeText(MainActivity.this, "yyyyyyyyyyyy", Toast.LENGTH_SHORT).show();
                    GoToTop.setVisibility(View.INVISIBLE);
                    fab.showMenu(true);
                    bottomSheet.requestLayout();
                    bottomSheet.invalidate();
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
//                com.github.clans.fab.FloatingActionButton bt = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.goUp);
//                if(slideOffset == 1) {
//                    bt.setVisibility(View.VISIBLE);
//                }else if(slideOffset == 0){
//                    bt.setVisibility(View.INVISIBLE);
//                }
                //  circularImageView.setRotation(90);
                //  Toast.makeText(MainActivity.this, "Se Roteste", Toast.LENGTH_SHORT).show();
            }
        });


        int rightHeight = 0;
        if(ScreenHeight <= 820) {
             rightHeight = 100;
        }else{
             rightHeight = 180;
        }
       fab = (FloatingActionMenu) findViewById(R.id.fab);
       fab.setPadding(0,0,20, rightHeight);

       fab.setClosedOnTouchOutside(true);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                view = v;
//
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                bottomSheet.scrollTo(0, 0);
//
//            }
//        });
//
//        fab.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                cdd.show();
//                return false;
//            }
//
//        });


        //  fab.setVisibility(View.INVISIBLE);

        GoogleMapOptions options = new GoogleMapOptions();
        options.liteMode(true)
                .compassEnabled(true)
                .scrollGesturesEnabled(true)
                .zoomGesturesEnabled(true)
                .rotateGesturesEnabled(true);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // redRoute.setBackgroundColor(Color.RED);

        //setUsernameAndEmail();
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                if (slideOffset == 1) {
                    bottomSheetBehavior.setHideable(true);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                    if(ScreenHeight <= 820){
                        fab.setVisibility(View.INVISIBLE);
                    }

                  //  SharedPreferences sharedTime = getSharedPreferences(SideMenuFirstTime,0);
                  //  if (sharedTime.getBoolean("SlidefirstTime",true))
                    if (SidesharedTime.getBoolean("SlidefirstTime",true))
                    {
                    Menu nav_Menu = navigationView.getMenu();
                //    MenuItem r =  nav_Menu.findItem(R.id.nav_help);
                    NavigationMenuView navView = (NavigationMenuView) navigationView.getChildAt(0);
                    View p = navView.getChildAt(1);

                        int MenuTutorialHeight = 0;
                        if(ScreenHeight < 800){ navView.scrollToPosition(10); MenuTutorialHeight = 7;}
                         else if(ScreenHeight == 800){ navView.scrollToPosition(10);MenuTutorialHeight = 9;}
                                            else{MenuTutorialHeight = 10;}

                    final ShowcaseView Menu_SiteTut =ShowTutorial(MainActivity.this, navView.getChildAt(MenuTutorialHeight), 0, R.string.Menu_SiteTutTitle, R.string.Menu_SiteTutText);
                    Menu_SiteTut.hide();
                    Menu_SiteTut.overrideButtonClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { Menu_SiteTut.hide();}
                    });

                        if(ScreenHeight < 800){MenuTutorialHeight = 4;}
                        else if(ScreenHeight == 800){MenuTutorialHeight = 8;}
                                            else{MenuTutorialHeight = 8;}
                    final ShowcaseView Menu_bottomTut =ShowTutorial(MainActivity.this,navView.getChildAt(MenuTutorialHeight), 0, R.string.Menu_bottomTutTitle, R.string.Menu_bottomTutText);
                    Menu_bottomTut.hide();
                    Menu_bottomTut.overrideButtonClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { Menu_bottomTut.hide();Menu_SiteTut.show();}
                    });

                        if(ScreenHeight < 800){MenuTutorialHeight = 1;}
                        else if(ScreenHeight == 800){MenuTutorialHeight = 3;}
                                            else{MenuTutorialHeight = 4;}
                    final ShowcaseView DificultyTut = ShowTutorial(MainActivity.this,navView.getChildAt(MenuTutorialHeight),0, R.string.DificultyTutTitle, R.string.DificultyTutText);
                    DificultyTut.show();
                    DificultyTut.overrideButtonClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { DificultyTut.hide();Menu_bottomTut.show();}
                    });
                        SidesharedTime.edit().putBoolean("SlidefirstTime",false).apply();
                 //   sharedTime.edit().putBoolean("SlidefirstTime",false).apply();
                }
                }if (slideOffset == 0) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomSheetBehavior.setHideable(false);
                    if(ScreenHeight <= 820){fab.setVisibility(View.VISIBLE);}

                }
            }
        });
        drawer.addDrawerListener(toggle);

        toggle.syncState();



        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
    }


    public void GoToTopOnClick(View v){
        bottomSheet.scrollTo(0,0);
    }
    public void restart() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


    public void getLocations() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (getLocate == true) {
            //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //         .setAction("Action", null).show();

            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setTiltGesturesEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            //googleMap.setPadding(0,0,0,100);
            // getLocation();

            getLocate = false;

        } else if (getLocate == false) {
            googleMap.setMyLocationEnabled(false);
            getLocate = true;
        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        googleApiClient.disconnect();

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        if (AboutDialog != null && AboutDialog.isShowing()) {
            AboutDialog.hide();
        }
        if (cdd != null && cdd.isShowing()) {
            cdd.hide();
        }

//        if (!cdd.isShowing()) {
//            cdd.hide();
//        } else {
//            cdd.show();
//        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (Colorlayout.getVisibility() == View.GONE) {
//            resetsingleMarker();
//            return;
//        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
        }
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation__slider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_about) {
            SetAbout();
        }*/
        if (id == R.id.nav_easy) {
            SetRoute(vyrus.bikegps.Settings.Easy, "Light");
            ZoomCamera(15, new LatLng(47.662349, 23.576890));
        }
        if (id == R.id.nav_mediu) {
            SetRoute(vyrus.bikegps.Settings.Mediu, "Medium");
            ZoomCamera(14, new LatLng(47.660080, 23.573015));
        }
        if (id == R.id.nav_hard) {
            SetRoute(vyrus.bikegps.Settings.Hard, "Hard");
            ZoomCamera(11, new LatLng(47.718170, 23.618475));
        }
        if (id == R.id.nav_extra_hard) {
            SetRoute(vyrus.bikegps.Settings.ExtraHard, "Extrem");
            ZoomCamera(11, new LatLng(47.718170, 23.618475));
        }
       /* else if (id == R.id.nav_events) {
            SetEvents();*/

        //
        else if (id == R.id.nav_address) {

            String url = "http://www.muntzomani.ro";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        }
//        } else if (id == R.id.rentAbike) {
//
//                String url = "https://muntzomani.ro/bike_rent";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
//
  //  }
         else if (id == R.id.nav_events) {

            String url = "https://muntzomani.ro/cultural-events/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        }
        else if (id == R.id.nav_help) {
            Help();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Help() {

        MainFirstTime = "isFirstTime";
        SideMenuFirstTime = "isFirstTime";
        RouteFirstTime = "isFirstTime";

        MainsharedTime.edit().putBoolean("MainfirstTime",true).apply();
        SidesharedTime.edit().putBoolean("SlidefirstTime",true).apply();
        RoutesharedTime.edit().putBoolean("RoutefirstTime",true).apply();

        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        MakeAllTutorials();

       // HelpDialog = new ChooseColorDialogClass(MainActivity.this, R.layout.what_and_how_dialog);
       // HelpDialog.show();


    }

    private void SetRoute(String color, String Level) {

        justRoute("not");
        justRoute(Level);
        removePolylline();
        ColorRoundFAB("0");
        ColorRoundFAB(Level);
        ListRouteParseByColor("0");
        ListRouteParseByColor(Level);
        RouteWithGoogleMap.setVisibility(View.VISIBLE);

      //  SharedPreferences sharedTime = getSharedPreferences(RouteFirstTime,0);
      //  if (sharedTime.getBoolean("MenufirstTime",true))
        if (RoutesharedTime.getBoolean("RoutefirstTime",true))
        {
        final ShowcaseView RouteWithGoogleMapTut = ShowTutorial(this,null, R.id.routeWithGoogleMap, R.string.routeWithGoogleMapTitle, R.string.routeWithGoogleMapText);
        RouteWithGoogleMapTut.hide();
        RouteWithGoogleMapTut.overrideButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) { RouteWithGoogleMapTut.hide();}
        });

        final ShowcaseView MapTut =  ShowTutorial(this,null, R.id.map, R.string.MapTutTitle, R.string.MapTutText);
        MapTut.show();
        MapTut.overrideButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) { MapTut.hide(); RouteWithGoogleMapTut.show();
//                FOBbTut.show();
            }
        });
            RoutesharedTime.edit().putBoolean("RoutefirstTime",false).apply();
        }

        addPoly(DrawRoute.polylineOptionses, color);
    }

    private void ListRouteParseByColor(String level) {

        ArrayList<Marker> tempMarkerArray = new ArrayList<Marker>();

        for (View card : CardViews) {

            CardsDescriptions markerTag = (CardsDescriptions) card.getTag();
            String Route = markerTag.getOwner();
            if (!Route.contains(level)) {

                card.setVisibility(View.GONE);

            }

            if (level.equals("0")) {
                RelativeLayout ColorBarCard = (RelativeLayout) card.findViewById(R.id.ColorBar);
                ColorBarCard.setBackgroundColor(ForCardBaner(""));
                card.setVisibility(View.VISIBLE);
            }
            if (Route.contains(level)) {

                RelativeLayout ColorBarCard = (RelativeLayout) card.findViewById(R.id.ColorBar);
                ColorBarCard.setBackgroundColor(ForCardBaner(Route));


            }
        }
    }

    private void SetEvents() {


        AboutDialog = new ChooseColorDialogClass(MainActivity.this, R.layout.about_us_dialog);
        AboutDialog.show();

    }

    private void SetAbout() {

        AboutDialog = new ChooseColorDialogClass(MainActivity.this, R.layout.about_us_dialog);
        AboutDialog.show();
    }

    public void ZoomCamera(int zoom, LatLng coord) {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(coord)      // Sets the center of the map to location user
                .zoom(zoom)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        map.getUiSettings().setTiltGesturesEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
        permissionStaffForMap();

        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(47.6666667, 23.5833333))      // Sets the center of the map to location user
                .zoom(12)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        retrieveFileFromResource();

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
              //  bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
              //  bottomSheet.scrollTo(0, 0);
                // resetsingleMarker();
            }
        });

        map.setOnMarkerClickListener(this);

        MakeAllTutorials();
      //  cdd = new ChooseColorDialogClass(MainActivity.this, R.layout.choose_color_dialog);
      //  cdd.show();

    //    DialogColor = (ImageView) cdd.findViewById(R.id.DialogImage);
   //     DialogColor.setOnTouchListener(this);
    }
    @Override
    public boolean onMyLocationButtonClick() {

        if (googleApiClient != null) {
            googleApiClient.connect();
        }

//        justRoute("not");
//        removePolylline();
//        ColorRoundFAB("0");
//        ListRouteParseByColor("0");

        if(googleApiClient.isConnected()){


            CheckIfHaveLocation();

//            if(minIndex == 0){
//                GetShortestPoint(lastLocation);
//            }
//            ShortestPoint();
        }

        return true;
    }


    private void ShortestPoint(){

        int p = mMarkerIsVisible.size();
        int min = minIndex;

        if (minIndex > -1) {


            try {
                timer = new Timer();
                ShortestLocationJumpPiriod = new CountDownTimer(10000, 2000) {



                    public void onTick(long millisUntilFinished) {
                        //  Timer timer = new Timer();
                        updateProfile = new CustomTimerTask(MainActivity.this, mMarkerIsVisible.get(minIndex));
                        timer.scheduleAtFixedRate(updateProfile, 10, 2000);

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarkerIsVisible.get(minIndex).getPosition(), 14));

                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        //   mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {

                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        timer.purge();
                        timer.cancel();

                        //  mTextField.setText("done!");
                    }

                }.start();

            }
            catch (Exception e){

            }

        }else{
            if(lastLocation != null) {
               CheckIfHaveLocation();
            }
        }

    }

    private void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        Toast.makeText(this, "Lat: "+latitude +" "+ "Lng: "+longitude, Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onMarkerClick(Marker mafacerker) {

       // Colorlayout.setVisibility(View.GONE);

        for (View OnlyOne : CardViews) {
            OnlyOne.setVisibility(View.GONE);          //String gettitle = marker.getTitle();
          //  String gettitle = marker.getTitle();
            String gettitle = mafacerker.getTitle();
           // String temptitle = OnlyOne.getTag().toString().substring(0, OnlyOne.getTag().toString().indexOf("¬"));
            CardsDescriptions r = (CardsDescriptions) OnlyOne.getTag();
            String temptitle = r.getTitle();
            //String colortitle = OnlyOne.getTag().toString().substring( OnlyOne.getTag().toString().indexOf("¬")+1);
            String colortitle = r.getMarker_color();
            if (gettitle.equals(temptitle)) {

                RelativeLayout ColorBarCard = (RelativeLayout) OnlyOne.findViewById(R.id.ColorBar);
                ColorBarCard.setBackgroundColor(ForCardBaner(colortitle));
                ColorRoundFAB(colortitle);

                ImageButton route = (ImageButton) OnlyOne.findViewById(R.id.navigateToo);
                route.setVisibility(View.VISIBLE);
                OnlyOne.setVisibility(View.VISIBLE);
            }
        }
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {

      //  Log.i("X & Y", String.valueOf(event.getRawX()) + " " + String.valueOf(event.getRawY()));

      //  cdd.ClickButonColor( DialogColor, ev.getX(), ev.getY());
      //  ColorRoundOnClick(DialogColor);

        final int action = ev.getAction();
        // (1)
        final int evX = (int) ev.getX();
        final int evY = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN :
               /* if (currentResource == R.drawable.p2_ship_default) {
                    nextImage = R.drawable.p2_ship_pressed;
                }*/
                break;
            case MotionEvent.ACTION_UP :

                int touchColor = cdd.getHotspotColor (R.id.image_areas, evX, evY);

                int tolerance = 25;

                if (cdd.closeMatch (Color.parseColor(vyrus.bikegps.Settings.Red), touchColor, tolerance)) { DialogColor.setTag("red_btn"); }
                if (cdd.closeMatch (Color.parseColor("#213e9a"), touchColor, tolerance)) { DialogColor.setTag("blue_btn"); }
                if (cdd.closeMatch (Color.parseColor(vyrus.bikegps.Settings.Yellow), touchColor, tolerance)) {  DialogColor.setTag("yellow_btn");  }
                if (cdd.closeMatch (Color.argb(255,255,255,255), touchColor, tolerance)) {  DialogColor.setTag("white_btn"); }
                if (cdd.closeMatch (Color.parseColor("#0ba14a"), touchColor, tolerance)) { DialogColor.setTag("green_btn"); }// green
                if (cdd.closeMatch (Color.parseColor("#7f3e98"), touchColor, tolerance)) { DialogColor.setTag("purple_btn"); }//purple

                ColorRoundOnClick(DialogColor);

                break;
        } // end switch

        return true;
    }

    public void resetsingleMarker() {
        //Colorlayout.setVisibility(View.VISIBLE);

        for (View OnlyOne : CardViews) {
            ImageButton route = (ImageButton) OnlyOne.findViewById(R.id.navigateToo);
            route.setVisibility(View.VISIBLE);
            OnlyOne.setVisibility(View.VISIBLE);
            ColorRoundFAB("0");

        }
    }

    private void retrieveFileFromResource() {
        try {
            GeoJsonLayer layer = new GeoJsonLayer(googleMap, R.raw.mapjust, this);
            addGeoJsonLayerToMap(layer);
        } catch (IOException e) {
            Log.e("This", "GeoJSON file could not be read");
        } catch (JSONException e) {
            Log.e("This", "GeoJSON file could not be converted to a JSONObject");
        }
    }

    private void addGeoJsonLayerToMap(GeoJsonLayer layer) {

        addMarkers(layer);
        new DrawRoute(googleMap, layer).execute();
        addDetaileOnList(layer);
        //  layer.addLayerToMap();
        globalLayout = layer;

        // Demonstrate receiving features via GeoJsonLayer clicks.
        layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
            @Override
            public void onFeatureClick(GeoJsonFeature feature) {
                updateBottomSheetContent(feature);

//                Toast.makeText(MainActivity.this,
//                        "Feature clicked: " + feature.getProperty("name"),
//                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addMarkers(GeoJsonLayer layer) {
        // Iterate over all the features stored in the layer

        for (GeoJsonFeature feature : layer.getFeatures()) {
//            // Check if the magnitude property exists
//
            String color = feature.getProperty("marker-color");
            String title = feature.getProperty("name");
            String  OwnerRoute = feature.getProperty("owner");
            String col = (color != null) ? color : "";
            String owner = (OwnerRoute != null) ? OwnerRoute : "";
            if (color != null) {
                GeoJsonGeometry points = feature.getGeometry();
                List<LatLng> coordonation = getCoordinatesFromGeometry(points);

                if (points.getType() == "Point") {
                    markerOptions = new MarkerOptions()
                            .position(coordonation.get(0))
                            .title(title)
                            //.icon(BitmapDescriptorFactory.defaultMarker(ToColor(col)));
                            .icon(ToIcon(col));
                    marker = googleMap.addMarker(markerOptions);
                    marker.setTag(col + "¬" + owner);
                    mMarkerArray.add(marker);
                    mMarkerIsVisible = mMarkerArray;
                }

            }
        }

    }

    private void ListParseByColor(String colorHex) {
        ArrayList<View> SwapPoints = new ArrayList<View>();

        for (View card : CardViews) {


            CardsDescriptions markerTag = (CardsDescriptions) card.getTag();
           // String markerTag = card.getTag().toString();
            String color = markerTag.getMarker_color();
           // String color = markerTag.substring(markerTag.indexOf('¬') + 1);

            if (!color.equals(colorHex)) {

                card.setVisibility(View.GONE);

            }

            if (colorHex.equals("0")) {
                RelativeLayout ColorBarCard = (RelativeLayout) card.findViewById(R.id.ColorBar);
                ColorBarCard.setBackgroundColor(ForCardBaner(""));
                card.setVisibility(View.VISIBLE);
            }
            if (color.equals(colorHex)) {

                RelativeLayout ColorBarCard = (RelativeLayout) card.findViewById(R.id.ColorBar);
                ColorBarCard.setBackgroundColor(ForCardBaner(color));


            }
        }
    }

    private float ToColor(String color) {
        if (color.equals("#00ff00")) {
            return BitmapDescriptorFactory.HUE_GREEN;
        }
        if (color.equals("#ffff00")) {
            return BitmapDescriptorFactory.HUE_YELLOW;
        }
        if (color.equals("#800080")) {
            return BitmapDescriptorFactory.HUE_VIOLET;
        }
        if (color.equals("#11afec")) {
            return BitmapDescriptorFactory.HUE_BLUE;
        }
        if (color.equals("#ff0000")) {
            return BitmapDescriptorFactory.HUE_RED;
        } else {
            return BitmapDescriptorFactory.HUE_CYAN;
        }

    }

    private BitmapDescriptor ToIcon(String color) {
        if (color.equals( vyrus.bikegps.Settings.Green )) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.green_btn);
        }
        if (color.equals( vyrus.bikegps.Settings.Yellow )) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.yellow_btn);
        }
        if (color.equals( vyrus.bikegps.Settings.Purple )) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.purple_btn);
        }
        if (color.equals( vyrus.bikegps.Settings.Blue )) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.blue_btn);
        }
        if (color.equals( vyrus.bikegps.Settings.Red )) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.red_btn);
        } else {
            return BitmapDescriptorFactory.fromResource(R.mipmap.white_btn);
        }

    }

    private void updateBottomSheetContent(GeoJsonFeature marker) {
//        TextView name = (TextView) bottomSheet.findViewById(R.id.detail_name);
//        name.setText(marker.getProperty("name"));

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    private int ForCardBaner(String color) {

        if (color.equals(vyrus.bikegps.Settings.Green )) {
            return Color.parseColor("#0E973D");
        }
        if (color.equals(vyrus.bikegps.Settings.Yellow )) {
            return Color.parseColor("#FDDB11");
        }
        if (color.equals(vyrus.bikegps.Settings.Purple )) {
            return Color.parseColor("#84358B");
        }
        if (color.equals(vyrus.bikegps.Settings.Blue )) {
            return Color.parseColor("#28358C");
        }
        if (color.equals(vyrus.bikegps.Settings.Red )) {
            return Color.parseColor("#E10915");
        }
        if (color.contains("Light")) {
            return Color.argb(255,249,141,70);
        }
        if (color.contains("Medium")) {
            return Color.argb(255,249,135,57);
        }
        if (color.contains("Hard")) {
            return Color.argb(255,255,125,43);
        }
        if (color.contains("Extrem")) {
            return Color.argb(255,255,100,0);
        } else {
            return Color.LTGRAY;
        }

    }

    public void addDetaileOnList(GeoJsonLayer layer) {

     //   final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
    //    int margins = (int) (16 * scale);


       /* Cursor have = db.getData("Select desc_name from bike_description ");
        if (have == null || have.getCount() <= 0) {
            try {
                new Descriptions(layer, getApplicationContext(), db).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            };
        }*/

      Cursor cs = db.getData("Select * from bike_description where desc_color is not null  group by desc_name order by  case " +
              "WHEN desc_name LIKE 'Step%' THEN 1 " +
              "WHEN desc_name LIKE '%Victor Gorduza%' THEN 3 " +
              "WHEN desc_name LIKE '%Planetarium%' THEN 5 " +
              "WHEN desc_name LIKE '%Colony%' THEN 7  " +
              "WHEN desc_name LIKE '%Fossil%' THEN 8  " +
              "WHEN desc_name LIKE '%„Firiza” dam%' THEN 9 ELSE 10 END ");
        int cati = cs.getCount();
        while (cs.moveToNext()) {
            String color = cs.getString(cs.getColumnIndex("desc_color"));
            String desc_title = cs.getString(cs.getColumnIndex("desc_name"));
            String desc = cs.getString(cs.getColumnIndex("desc_description"));
            String coordonate = cs.getString(cs.getColumnIndex("desc_coordonate"));
            String owner = cs.getString(cs.getColumnIndex("owner"));
            Bitmap img = getImage(cs.getBlob(cs.getColumnIndex("desc_img")));
            byte[] orgImg = cs.getBlob(cs.getColumnIndex("desc_imgsec"));
            Bitmap imgsec = getImage(orgImg);


            View card = getLayoutInflater().inflate(R.layout.junk_cardview_google, null);
            //card.setTag(feature.getProperty("name") + "¬" + feature.getProperty("marker-color"));
            card.setTag( new CardsDescriptions(desc_title, color,null,null,null,owner));
            //card.setTag(desc_title + "¬" + color);


            CardViews.add(card);
            ImageButton nav = (ImageButton) card.findViewById(R.id.navigateToo);
            String latlong = coordonate;
            nav.setTag(latlong);
            TextView title = (TextView) card.findViewById(R.id.MarkerTile);
          //  ImageView picture = (ImageView) card.findViewById(R.id.MarkerImage);
            final TextView content = (TextView) card.findViewById(R.id.MarkerContent);
            RelativeLayout ColorBarCard = (RelativeLayout) card.findViewById(R.id.ColorBar);
            ColorBarCard.setBackgroundColor(ForCardBaner(""));
            title.setText(desc_title);
            title.setTag(desc_title);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Marker marker : mMarkerArray) {

                        if (marker.getTitle().equals(v.getTag().toString())) {

                            marker.showInfoWindow();
                            ZoomCamera(13,marker.getPosition());
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    }
                }
            });
         //   String all = feature.getProperty("description");


            final ViewPager mViewPager = (ViewPager) card.findViewById(R.id.MarkerImage);
            final TabLayout tabLayout = (TabLayout) card.findViewById(R.id.tabDots);
            SliderImageAdapter adapterView = new SliderImageAdapter(this);
            Bitmap[] sliderImagesBitmap;
            if(orgImg == null)
            {
                sliderImagesBitmap = new Bitmap[]{
                        img
                };
            }else {
                sliderImagesBitmap = new Bitmap[]{
                        img, imgsec
                };
            }

          adapterView.SetImages(sliderImagesBitmap);
            mViewPager.setAdapter(adapterView);
            mViewPager.setTag(sliderImagesBitmap);
            if(orgImg != null)
            {
                tabLayout.setupWithViewPager(mViewPager);
            }

           mViewPager.setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View v, MotionEvent event) {

                   int action = event.getActionMasked();

                   switch (action) {

                       case (MotionEvent.ACTION_UP):

                           int id = tabLayout.getSelectedTabPosition();
                         boolean c =  mViewPager.beginFakeDrag();
                         if(c == true) {
                         // boolean plm = mViewPager.beginFakeDrag();
                             Bitmap[] images = (Bitmap[]) mViewPager.getTag();
                             cdd = new ChooseColorDialogClass(MainActivity.this, R.layout.big_image_layout, (id == -1)? images[0]: images[id] );
                             cdd.show();
                             mViewPager.endFakeDrag();
                         }
                           return false;

                       default:
                           return false;
                   }
               }
           });

           // picture.setImageBitmap(img);
            String all = desc;
            if (all != null && all.contains("<br><br>")) {
                content.setText(all.substring(all.lastIndexOf("</>")+1
                )+ "\n");

          }
             else if(all != null && all.contains("<br>")){
                            content.setText(all.replace("<br>", "\n") + "\n");
                        }
            else {
                content.setText(all + "\n");
            }
            content.setAutoLinkMask(Linkify.WEB_URLS);
            content.setLinksClickable(true);
            content.setLinkTextColor(Color.BLUE);
            content.setMovementMethod(LinkMovementMethod.getInstance());

            LinearLayoutContain.addView(card);


        }
    }

    public Bitmap getImage(byte[] image) {
        if (image != null) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } else {
            return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_icon);
        }
    }

    private List<LatLng> getCoordinatesFromGeometry(GeoJsonGeometry geometry) {

        List<LatLng> coordinates = new ArrayList<>();

        // GeoJSON geometry types:
        // http://geojson.org/geojson-spec.html#geometry-objects

        switch (geometry.getType()) {
            case "Point":
                coordinates.add(((GeoJsonPoint) geometry).getCoordinates());
                lineCordinates.add(((GeoJsonPoint) geometry).getCoordinates());
                break;
            case "MultiPoint":
                List<GeoJsonPoint> points = ((GeoJsonMultiPoint) geometry).getPoints();
                for (GeoJsonPoint point : points) {
                    coordinates.add(point.getCoordinates());
                }
                break;
            case "LineString":
                coordinates.addAll(((GeoJsonLineString) geometry).getCoordinates());
                break;
            case "MultiLineString":
                List<GeoJsonLineString> lines =
                        ((GeoJsonMultiLineString) geometry).getLineStrings();
                for (GeoJsonLineString line : lines) {
                    coordinates.addAll(line.getCoordinates());
                }
                break;
            case "Polygon":
                List<? extends List<LatLng>> lists =
                        ((GeoJsonPolygon) geometry).getCoordinates();
                for (List<LatLng> list : lists) {
                    coordinates.addAll(list);
                }
                break;
            case "MultiPolygon":
                List<GeoJsonPolygon> polygons =
                        ((GeoJsonMultiPolygon) geometry).getPolygons();
                for (GeoJsonPolygon polygon : polygons) {
                    for (List<LatLng> list : polygon.getCoordinates()) {
                        coordinates.addAll(list);
                    }
                }
                break;
        }

        return coordinates;
    }

    public void NoGPSActivate() {

        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS is settings");
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    // finish();
                }
            });
            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    // finish();
                }
            });
            alertDialog.show();

        } catch (Exception e) {
            Log.i("This", e.getMessage());

        }

    }

    public void RouteOnnClick(View v) {

        String latlog = v.getTag().toString().substring(v.getTag().toString().lastIndexOf('(') + 1, v.getTag().toString().indexOf(')'));
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + latlog));
        startActivity(intent);


    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

//          Toast.makeText(this, "Radius Value " + valueResult + "   KM  " + kmInDec
//                  + " Meter   " + meterInDec, Toast.LENGTH_SHORT).show();

        return Radius * c;
    }

    public void addPoly(ArrayList<PolylineOptions> polylinesOpt, String color) {

        String AllCoord = "";

        polylines.clear();
        for (PolylineOptions polylineOption : polylinesOpt) {
            if (polylineOption.getColor() == DrawRoute.ToColorPoly(color)) {

               polyline= googleMap.addPolyline(polylineOption);
                polylines.add(polyline);
                List<LatLng> linepoints = polyline.getPoints();
                for(LatLng points : linepoints){

                 //  AllCoord += points.latitude +","+ points.longitude + "+to:";

                }


            }
        }

//        cdd = new ChooseColorDialogClass(MainActivity.this, R.layout.choose_color_dialog);
//        cdd.show();
//        AllCoord = AllCoord.substring(0, AllCoord.length()- 4);
//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                Uri.parse("http://maps.google.com/maps?daddr=" + AllCoord.trim()));
//               // Uri.parse("http://maps.google.com/maps?daddr=" + oneTow));
//        startActivity(intent);
    }

    public void ColorRoundOnClick(View v) {

        if (DrawRoute.polylineOptionses != null) {

            RouteWithGoogleMap.setVisibility(View.INVISIBLE);
//            cdd.hide();


            switch (String.valueOf(v.getTag())) {

                case "blue_btn":
                    justColor(String.valueOf(""));
                    removePolylline();
                    ListParseByColor("0");
                    justColor(String.valueOf( vyrus.bikegps.Settings.Blue ));
                   // addPoly(DrawRoute.polylineOptionses, "#11afec");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomSheet.scrollTo(0, 0);
                    ListParseByColor( vyrus.bikegps.Settings.Blue );
                    ZoomCamera(13, new LatLng(47.660928, 23.537552));
                  //  fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.blue_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.blue_btn);
                    fab.close(true);
                  //  fab.setMenuButtonColorNormalResId(Color.parseColor(vyrus.bikegps.Settings.Blue));


                    break;

                case "purple_btn":
                    justColor(String.valueOf(""));
                    removePolylline();
                    ListParseByColor("0");
                    justColor(String.valueOf(vyrus.bikegps.Settings.Purple));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomSheet.scrollTo(0, 0);
                   // addPoly(DrawRoute.polylineOptionses, "#800080");
                    ListParseByColor(vyrus.bikegps.Settings.Purple);
                    ZoomCamera(15, new LatLng(47.662349, 23.576890));
                   // fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.purple_btn, null));
                //    fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.purple_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.purple_btn);
                    fab.close(true);
                 //   fab.setBackgroundColor(Color.parseColor(vyrus.bikegps.Settings.Purple));


                    break;

                case "green_btn":
                    justColor(String.valueOf(""));
                    removePolylline();
                    ListParseByColor("0");
                    justColor(String.valueOf(vyrus.bikegps.Settings.Green));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomSheet.scrollTo(0, 0);
                    // addPolylines(globalLayout, "#00ff00");
                   // addPoly(DrawRoute.polylineOptionses, "#00ff00");
                    ZoomCamera(11, new LatLng(47.723870, 23.592410));
                    ListParseByColor(vyrus.bikegps.Settings.Green);
                   // fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.green_btn, null));
                 //   fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.green_btn, null));
                   // fab.setBackgroundColor(Color.parseColor(vyrus.bikegps.Settings.Green));
                    fab.getMenuIconView().setImageResource( R.mipmap.green_btn);
                    fab.close(true);
                    //   fab.setBackgroundColor(Color.parseColor(vyrus.bikegps.Settings.Purple));

                    break;

                case "yellow_btn":
                    justColor(String.valueOf(""));
                    removePolylline();
                    ListParseByColor("0");
                    justColor(String.valueOf(vyrus.bikegps.Settings.Yellow));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomSheet.scrollTo(0, 0);
                    //addPolylines(globalLayout, "#ffff00");
                   // addPoly(DrawRoute.polylineOptionses, "#ffff00");
                    ListParseByColor(vyrus.bikegps.Settings.Yellow);
                    ZoomCamera(14, new LatLng(47.660200, 23.582038));
                  //  fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.yellow_btn, null));
                 //   fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.yellow_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.yellow_btn);
                    fab.close(true);
                    //   fab.setBackgroundColor(Color.parseColor(vyrus.bikegps.Settings.Purple));
                    break;

                case "white_btn":
                    justColor(String.valueOf(""));
                    removePolylline();
                    ListParseByColor("0");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomSheet.scrollTo(0, 0);
                    ZoomCamera(12, new LatLng(47.6666667, 23.5833333));
                   // fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.white_btn, null));
                   // fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.white_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.white_btn);
                    fab.close(true);
                    //   fab.setBackgroundColor(Color.parseColor(vyrus.bikegps.Settings.Purple));

//                    ;
                    break;

                case "red_btn":
                    justColor(String.valueOf(""));
                    removePolylline();
                    ListParseByColor("0");
                    justColor(String.valueOf(vyrus.bikegps.Settings.Red));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomSheet.scrollTo(0, 0);
                  //  addPoly(DrawRoute.polylineOptionses, "#ff0000");
                    ListParseByColor(vyrus.bikegps.Settings.Red);
                    ZoomCamera(14, new LatLng(47.660551, 23.574187));
                 //   fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.red_btn, null));
               //     fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.red_btn, null));
                //    fab.setBackgroundColor(Color.parseColor(vyrus.bikegps.Settings.Red));
                    fab.getMenuIconView().setImageResource( R.mipmap.red_btn);
                    fab.close(true);
                    //   fab.setBackgroundColor(Color.parseColor(vyrus.bikegps.Settings.Purple));


                    break;
                default:
                    Toast.makeText(this, "Sorry", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void ColorRoundFAB(String color) {

            switch (String.valueOf(color)) {

                case vyrus.bikegps.Settings.Blue:
                   // fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.blue_btn, null));
                 //   fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.blue_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.blue_btn);
                    break;
                case vyrus.bikegps.Settings.Purple:
                  //  fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.purple_btn, null));
                 //   fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.purple_btn, null));
                 //   fab.setBackgroundColor(Color.parseColor(vyrus.bikegps.Settings.Purple));
                    fab.getMenuIconView().setImageResource( R.mipmap.purple_btn);

                    break;
                case vyrus.bikegps.Settings.Green:
                    //fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.green_btn, null));
                  //  fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.green_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.green_btn);

                    break;
                case vyrus.bikegps.Settings.Yellow:
                 //   fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.yellow_btn, null));
                  //  fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.yellow_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.yellow_btn);

                    break;
                case "0":
                 //   fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.white_btn, null));
                  //  fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.white_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.white_btn);

                    break;
                case vyrus.bikegps.Settings.Red:
                   // fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.red_btn, null));
                //    fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.red_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.red_btn);

                    break;
                case "Light":
                  //  fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.light_btn, null));
                 //   fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.light_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.light_btn);

                    break;
                case "Medium":
                    //fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.mediu_btn, null));
                 //   fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.mediu_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.mediu_btn);

                    break;
                case "Hard":
                    //fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.hard_btn, null));
                 //   fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.hard_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.hard_btn);

                    break;
                case "Extrem":
                    //fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.extrem_btn, null));
                  //  fab.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.extrem_btn, null));
                    fab.getMenuIconView().setImageResource( R.mipmap.extrem_btn);

                    break;

                default:
                    Toast.makeText(this, "Sorry", Toast.LENGTH_SHORT).show();
            }
        }

    public void justColor(String Color) {

        mMarkerIsVisible = new CopyOnWriteArrayList<>();
        minIndex = 0;

        StopShortestDistJump();

        for (Marker marker : mMarkerArray) {

            String col = marker.getTag().toString().substring(0, marker.getTag().toString().indexOf("¬"));


            if (!col.equals(Color)) {
                marker.setVisible(false);
            }
            if (col.equals(Color)) {
                mMarkerIsVisible.add(marker);

            }
            if (Color.equals("")) {
                marker.setVisible(true);

            }
        }

    }

    boolean StopShortestDistJump(){

        try {

            if (ShortestLocationJumpPiriod != null){
                ShortestLocationJumpPiriod.cancel();
                if(updateProfile != null) {
                    updateProfile.cancel();
                }
                if(timer != null) {
                    timer.cancel();
                }
               googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                return true;
            }else  {
                return false;
            }

        }catch (Exception e){
                return false;
        }
    }

    public void justRoute(String Owner) {

        mMarkerIsVisible = new CopyOnWriteArrayList<>();
        minIndex = 0;

        StopShortestDistJump();


        for (Marker marker : mMarkerArray) {

            //String tag = marker.getTag().toString();
            String owner = marker.getTag().toString().substring( marker.getTag().toString().indexOf("¬")+1);

            if (!owner.contains(Owner)) {
                marker.setVisible(false);
            }else
            {
                    AllCoord += marker.getPosition().latitude +","+ marker.getPosition().longitude + "+to:";
                    mMarkerIsVisible.add(marker);
            }
            if (Owner.equals("not")) {
                marker.setVisible(true);
            }

        }

      //  GoogleMapsRoute.setBackgroundResource(R.mipmap.hard_btn);
       // cpp = new ChooseColorDialogClass(MainActivity.this, R.layout.go_with_net_gps);
       // cpp.show();


    }

    public void RouteWithNetOnClick(View v) {
       // cpp.hide();
//        cdd.dismiss();
        if(!AllCoord.isEmpty())
            AllCoord = AllCoord.substring(0, AllCoord.length()- 4);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + AllCoord.trim() + "&mode=walking"));
        // Uri.parse("http://maps.google.com/maps?daddr=" + oneTow));
        AllCoord = "";
        startActivity(intent);
    }

    public void FABOnClick(View v) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void removePolylline() {

        for (Polyline line : polylines) {
            line.setVisible(false);
        }
    }

    private void MakeAllTutorials(){

       // SharedPreferences sharedTime = getSharedPreferences(MainFirstTime,0);
       // if (sharedTime.getBoolean("MainfirstTime",true))
        if (MainsharedTime.getBoolean("MainfirstTime",true))
        {

            NavigationToTut =  ShowTutorial(this,null, R.id.navigateToo,R.string.NavigationToTitle, R.string.ColorBarText);
            NavigationToTut.hide();
            LocationTut =  ShowTutorial(this,null, R.id.InvLocationButton,R.string.LocationTutTitle, R.string.LocationTutText);
            LocationTut.hide();
            ColorBarTut =  ShowTutorial(this,null, R.id.ColorBar, R.string.ColorBarTitle, R.string.ColorBarText);
            ColorBarTut.hide();
            FOBTut =  ShowTutorial(this,null, R.id.inv, R.string.InvTitle, R.string.InvText);
            FOBTut.show();
            FOBTut.overrideButtonClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {  FOBTut.hide();  ColorBarTut.show();
                }
            });
            ColorBarTut.overrideButtonClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {ColorBarTut.hide();  LocationTut.show();
                }
            });
            LocationTut.overrideButtonClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocationTut.hide(); NavigationToTut.show();
                }
            });
            NavigationToTut.overrideButtonClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationToTut.hide();
                }
            });

         //   sharedTime.edit().putBoolean("MainfirstTime",false).apply();
            MainsharedTime.edit().putBoolean("MainfirstTime",false).apply();
        }


    }

    private ShowcaseView ShowTutorial(final Activity thisActivity,View Inflate, int ResView, int Title, int Content){

        Button customButton = (Button) getLayoutInflater().inflate(R.layout.sv_custom_button, null);
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);

        vyrus.bikegps.ViewTarget VT = null;

        if(ResView == 0){
            VT = new vyrus.bikegps.ViewTarget(Inflate);
        }else {
            VT = new vyrus.bikegps.ViewTarget(ResView, this);
        }

            ShowcaseView ThisShowCastView = new ShowcaseView.Builder(thisActivity)
                 //
                    .withMaterialShowcase()
                    .setTarget(VT)
                    .setStyle(R.style.CustomShowcaseTheme)
                    .setContentTitle(Title)
                    .setContentText(Content)
                    .replaceEndButton(customButton)
                    .blockAllTouches()
                    .build();


        ThisShowCastView.show();
        ThisShowCastView.setButtonPosition(lps);


      return  ThisShowCastView;

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
    public void onMapClick(LatLng latLng) {

    }

    public void GetShortestPoint(Location lastLocation) {

        double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
        MyPosition = new LatLng(lat, lon);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(MyPosition));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        //  Toast.makeText(this, "Lat: "+lat +" "+ "Lng: "+lon, Toast.LENGTH_SHORT).show();

        if (MyPosition != null) {

            DistanceBtwPoints.clear();
            for (int i = 0; i < mMarkerIsVisible.size(); i++) {
                //   double distance = CalculationByDistance(MyPosition,mMarkerIsVisible.get(i).getPosition());
                DistanceBtwPoints.add(CalculationByDistance(MyPosition, mMarkerIsVisible.get(i).getPosition()));
                // String tag = (String)mMarkerIsVisible.get(i).getTag();
                //  marker.setTag(tag + "¬" + distance);
            }


            minIndex = findMinIndex(DistanceBtwPoints);

        }
    }

    Location lastLocation;
    public void CheckIfHaveLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ) {

             lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(lastLocation == null){
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }else {
                GetShortestPoint(lastLocation);
                ShortestPoint();

            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

       CheckIfHaveLocation();
    }

    public static <T extends Comparable<T>> int findMinIndex(final List<T> xs) {
        int minIndex;
        if (xs.isEmpty()) {
            minIndex = -1;
        } else {
            final ListIterator<T> itr = xs.listIterator();
            T min = itr.next(); // first element as the current minimum
            minIndex = itr.previousIndex();
            while (itr.hasNext()) {
                final T curr = itr.next();
                if (curr.compareTo(min) < 0) {
                    min = curr;
                    minIndex = itr.previousIndex();
                }
            }
        }
        return minIndex;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(MainActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }

   //permiions
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
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)/*||*/
                /*ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.GET_ACCOUNTS)*/) {

            // Toast.makeText(context,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_SHORT).show();
            // getLocations();
            startInstalledAppDetailsActivity(this);
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            /*ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, 1);*/
        }
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

                    googleMap.setMyLocationEnabled(true);
                    //  googleMap.getUiSettings().setTiltGesturesEnabled(true);
                    //  googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                } else {


                    //  Toast.makeText(this, "Permission Denied, You cannot access location data.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void permissionStaffForMap() {

        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                ) {
          /*  checkPermission(Manifest.permission.GET_ACCOUNTS)*/
            //Snackbar.make(view,"Permission already granted.",Snackbar.LENGTH_LONG).show();
            // getLocations();
            googleMap.setMyLocationEnabled(true);


        } else {

            //Snackbar.make(view,"Please request permission.",Snackbar.LENGTH_LONG).show();
            if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    !checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    !checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                            !checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    ){

                /*!checkPermission(Manifest.permission.GET_ACCOUNTS)*/
                requestPermission();


            } else {

                // Snackbar.make(view,"Permission already granted.",Snackbar.LENGTH_LONG).show();
                // getLocations();
                googleMap.setMyLocationEnabled(true);
                //googleMap.getUiSettings().setTiltGesturesEnabled(true);
                //  googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        }
    }


}
