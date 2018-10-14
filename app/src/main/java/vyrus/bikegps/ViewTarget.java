package vyrus.bikegps;

import android.app.Activity;
import android.graphics.Point;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.amlcurran.showcaseview.targets.Target;

import java.lang.reflect.Field;

/**
 * Created by Cristi Mark on 02.04.2018.
 */

public class ViewTarget implements Target {

    private final View mView;

    public ViewTarget(View view) {
        mView = view;
    }

    public ViewTarget(int viewId, Activity activity) {
        mView = activity.findViewById(viewId);
    }

    @Override
    public Point getPoint() {
        int[] location = new int[2];
        mView.getLocationInWindow(location);
        int x = location[0] + mView.getWidth() / 2;
        int y = location[1] + mView.getHeight() / 2;
        return new Point(x, y);
    }
}
