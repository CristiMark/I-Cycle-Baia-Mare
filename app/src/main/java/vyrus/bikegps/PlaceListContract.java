package vyrus.bikegps;

/**
 * Created by can on 10.12.2016.
 */

public interface PlaceListContract {

    interface View {

        void setPresenter(Presenter presenter);

        void openNavigationMap(double latitude, double longitude);

    }

    interface Presenter {

        void callMapForPlace(double latitude, double longitude);
    }
}
