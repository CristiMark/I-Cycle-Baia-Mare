package vyrus.bikegps;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristi Mark on 09.06.2017.
 */

public class CardsDescriptions {

    public String Title;
    public String Marker_color;
    public String Description;
    public LatLng coordonation;
    public List<Bitmap> Image;
    public String Owner;

    public CardsDescriptions(String title, String marker_color, String description, LatLng coordonation, List<Bitmap> image, String owner) {
        Image = new ArrayList<>();
        Title = title;
        Marker_color = marker_color;
        Description = description;
        this.coordonation = coordonation;
        Image = image;
        Owner = owner;
    }


    public String getTitle() {
        return Title;
    }

    public String getMarker_color() {
        return Marker_color;
    }

    public String getDescription() {
        return Description;
    }

    public LatLng getCoordonation() {
        return coordonation;
    }

    public List<Bitmap> getImage() {
        return Image;
    }

    public String getOwner() {return Owner;}
}
