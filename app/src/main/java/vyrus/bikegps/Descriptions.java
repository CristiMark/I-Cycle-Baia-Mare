package vyrus.bikegps;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.contextmanager.fence.internal.FenceQueryRequestImpl;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonGeometry;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonLineString;
import com.google.maps.android.geojson.GeoJsonMultiLineString;
import com.google.maps.android.geojson.GeoJsonMultiPoint;
import com.google.maps.android.geojson.GeoJsonMultiPolygon;
import com.google.maps.android.geojson.GeoJsonPoint;
import com.google.maps.android.geojson.GeoJsonPolygon;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vyrus.bikegps.Activitys.MainActivity;
import vyrus.bikegps.Activitys.SponsorActivity;


/**
 * Created by Vyrus on 07/12/2016.
 */

public class Descriptions extends AsyncTask {

    public static ArrayList<CardsDescriptions> CardDetails = new ArrayList<CardsDescriptions>() ;



    private static View card;
    private static LinearLayout LinearLayout;
    private static ImageView iv;
    private static GeoJsonLayer layout;
    private static String color;
    private static Context context;
    private DataBase db;
    private OnTaskCompleted listener;

    public interface OnTaskCompleted{
        void onTaskCompleted();
    }

    public Descriptions(OnTaskCompleted listener, GeoJsonLayer layout, Context context, DataBase db) {

        this.LinearLayout = LinearLayout;
        Descriptions.layout = layout;
        Descriptions.context = context;
        this.db = db;
        this.listener = listener;
    }


   @Override
    protected Object doInBackground(Object params[]) {

       List<String> Pic = new ArrayList<String>();
       try {
        //   LinkListFromURLThread PicList = new LinkListFromURLThread("http://kc.16mb.com/BikeGPS_Pics");
           LinkListFromURLThread PicList = new LinkListFromURLThread("http://muntzomani.ro/BikeMapApp_Pics/");
           PicList.start();
           PicList.join();
           Pic = PicList.GetPicList();
       } catch (InterruptedException e) {
           e.printStackTrace();
       }

        for (GeoJsonFeature feature : layout.getFeatures()) {

            String ne = feature.getProperty("name");

            if(ne.contains("Medieval Village")){

                String p = "Am gasit";
            }
             String all =feature.getProperty("description");
            String desc = "";
            String owner = "";
            if( all !=null && all.contains("<br><br>")) {
                desc = all.substring(0, all.lastIndexOf("<br><br>"));
            }
            else{desc = all; }
           /* if( all !=null) {
                desc = all;
            }*/
            Bitmap img = null;
            List<Bitmap> Allimg = new ArrayList<>();


            if (feature.getProperty("owner") !=null){
                owner = feature.getProperty("owner");
            }

            /*if (feature.getProperty("gx_media_links") !=null)

            {*/
            //  DownloadImageThread dwn = new DownloadImageThread(feature.getProperty("gx_media_links"), img);
                String getURL = null;
                for (String r: Pic) {
                                String name = feature.getProperty("name");
                    try {

                        getURL = r;
                       String c = java.net.URLDecoder.decode(r, "UTF-8");
                        if(c.toLowerCase().contains("/".toLowerCase()))
                        {
                            r = java.net.URLDecoder.decode(r.substring(r.lastIndexOf('/') + 1), "UTF-8");
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    if(r.toLowerCase().contains("arte") && name.toLowerCase().contains("arte"))
                    {
                        r = "ARTE+Fundation";
                        name = "ARTE+Fundation";
                    }


                    if(r.toLowerCase().contains(name.toLowerCase())){

                       // DownloadImageThread dwn = new DownloadImageThread(GetPicFromServer(getURL.replace("\"", "")), img);
                        DownloadImageThread dwn = new DownloadImageThread(GetPicFromServer(getURL.replace("\"", "")), img);
                        dwn.start();
                        try {
                            dwn.join();
                            img = dwn.getImage();
                            Allimg.add(img);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }


//                new DownloadImageTask(biy)
//                        .execute(feature.getProperty("gx_media_links"));
            //}
            GeoJsonGeometry points = feature.getGeometry();
            List<LatLng> coordonation = getCoordinatesFromGeometry(points);
            CardsDescriptions cardDetail = new CardsDescriptions(feature.getProperty("name"),
                                                                 feature.getProperty("marker-color"),
                                                                 desc,
                                                                 coordonation.get(0),
                                                                 Allimg,
                                                                 owner);



                CardDetails.add(cardDetail);

//
        }

      //  Collections.sort(CardDetails, new StudentComparator());

        for(CardsDescriptions s : CardDetails) {

            Bitmap SecondIMG;
            Bitmap FirstIMG = null;
            try {
                FirstIMG = s.Image.get(0);
                SecondIMG = s.Image.get(1);
            }
           catch ( Exception e){
               SecondIMG = null;
           }

             db.insertDesc(s.Title,s.Marker_color,s.Description,s.coordonation.toString(),getBytes(FirstIMG),getBytes(SecondIMG), s.Owner);

            //getBytes(s.Image);
            Log.i("This", "insert: ");
        }


        return layout ;
    }

    protected void onPostExecute(Object result) {
      //  Polyline line = mMap.addPolyline((PolylineOptions) result);
       // polylineOptionses.add((PolylineOptions) result);
        super.onPostExecute(result);
        // Call the interface method
        if (listener != null)
            listener.onTaskCompleted();
    }

    public static byte[] getBytes(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
        } else {
            return null;
        }
    }
    public String GetPicFromServer(String Title) {

        String y = Settings.PicAddress + Title;
        //String y = "ftp://kc.16mb.com/BikeGPS_Pics/Stephen%E2%80%99s%20Tower%20-%20the%20image%20of%20Baia%20Mare%20city%C2%AC2.jpg";
        return y;
    }


    public static ArrayList<CardsDescriptions> getCardDetails() {
        return CardDetails;
    }

    private List<LatLng> getCoordinatesFromGeometry(GeoJsonGeometry geometry) {

        List<LatLng> coordinates = new ArrayList<>();

        // GeoJSON geometry types:
        // http://geojson.org/geojson-spec.html#geometry-objects

        switch (geometry.getType()) {
            case "Point":
                coordinates.add(((GeoJsonPoint) geometry).getCoordinates());
                //lineCordinates.add(((GeoJsonPoint) geometry).getCoordinates());
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


    private static int ToColorPoly(String color) {

        if (color.equals("#00ff00")) {
            return Color.GREEN;
        }
        if (color.equals("#ffff00")) {
            return Color.YELLOW;
        }
        if (color.equals("#800080")) {
            return Color.argb(255,128,0,128);
        }
        if (color.equals("#11afec")) {
            return Color.BLUE;
        }
        if(color.equals("#ff0000")){
            return Color.RED;
        }else{
            return Color.CYAN;
        }
    }
}


class LinkListFromURLThread extends Thread {
    String url;
    List<String> rez = new ArrayList<String>();

    public LinkListFromURLThread(String URL) {
        this.url = URL;
    }

    public List<String> GetPicList() {

        return rez;
    }


    public void run() {
        StringBuilder str = new StringBuilder();


        URL Url;
        HttpURLConnection urlConnection = null;
        try {
            Url = new URL(url);

            urlConnection = (HttpURLConnection) Url.openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                str.append(current);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

       rez = getTagValues(str.toString());

    }

    private static List<String> getTagValues(final String str) {
        final Pattern TAG_REGEX = Pattern.compile("<a href=(.+?)>");

        final List<String> tagValues = new ArrayList<String>();
        final Matcher matcher = TAG_REGEX.matcher(str);
        while (matcher.find()) {
            tagValues.add(matcher.group(1));
        }
        return tagValues;
    }
}


class DownloadImageThread extends Thread {

    private String url;
    private Bitmap image;

    public DownloadImageThread(String url, Bitmap image) {
        this.url = url;
        this.image = image;
    }

    public void run() {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
           image = mIcon11;
    }

    public Bitmap getImage() {
        return image;
    }
}


 class StudentComparator implements Comparator<CardsDescriptions> {
     @Override
     public int compare(CardsDescriptions s, CardsDescriptions t) {
         int f = s.Title.compareTo(t.Title);

        // return (f != 0) ? f : s.date.compareTo(t.date);
         return f;
     }
 }