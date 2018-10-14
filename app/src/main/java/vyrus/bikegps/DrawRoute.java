package vyrus.bikegps;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vyrus on 07/12/2016.
 */

public class DrawRoute extends AsyncTask {

    public static ArrayList<PolylineOptions> polylineOptionses = new ArrayList<PolylineOptions>() ;
    private static GoogleMap mMap;
    private static GeoJsonLayer layout;
    private static String color;

    public DrawRoute(GoogleMap map, GeoJsonLayer layout) {
        this.mMap = map;
        this.polylineOptionses = polylineOptionses;
        this.color = color;
        this.layout = layout;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        PolylineOptions options = new PolylineOptions();

        for (GeoJsonFeature feature : layout.getFeatures()) {
//            // Check if the magnitude property exists
            GeoJsonGeometry points = feature.getGeometry();


            if(points.getType() == "LineString")
            {
                String color = feature.getProperty("stroke");
                String col = (color != null) ? color : "";

                    List<LatLng> coordonation = getCoordinatesFromGeometry(points);
                for (int i = 0; i < coordonation.size(); i++) {

                    options = new PolylineOptions().width(18).color(ToColorPoly(col));
                    options.addAll(coordonation);

                }
//                     options = new PolylineOptions().width(10).color(ToColorPoly(col));
//                    for (int i = 0; i < coordonation.size(); i++) {
//                        options.addAll(coordonation);
//
//                }
                 polylineOptionses.add(options);
            }


        }
        return options;
    }

    protected void onPostExecute(Object result) {
      //  Polyline line = mMap.addPolyline((PolylineOptions) result);
       // polylineOptionses.add((PolylineOptions) result);
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

    public static int ToColorPoly(String color) {

        if (color.equals(Settings.Easy)) {
            return Color.argb(255,249,141,70);
        }
        if (color.equals(Settings.Mediu)) {
            return Color.argb(255,249,135,57);
        }
        if (color.equals(Settings.Hard)) {
            return Color.argb(255,255,125,43);
        }
        if (color.equals(Settings.ExtraHard)) {
            return Color.argb(255,255,100,0);
        }
        if(color.equals("#ff0000")){
            return Color.RED;
        }else{
            return Color.CYAN;
        }
    }
}
