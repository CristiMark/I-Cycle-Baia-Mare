package vyrus.bikegps;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by can on 10.12.2016.
 */

public class MarkerModel implements Parcelable {

    private String title;

    private String detail;

    private double latitude;

    public MarkerModel() {

    }

    protected MarkerModel(Parcel in) {
        title = in.readString();
        detail = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(detail);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MarkerModel> CREATOR = new Creator<MarkerModel>() {
        @Override
        public MarkerModel createFromParcel(Parcel in) {
            return new MarkerModel(in);
        }

        @Override
        public MarkerModel[] newArray(int size) {
            return new MarkerModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double longitude;
}
