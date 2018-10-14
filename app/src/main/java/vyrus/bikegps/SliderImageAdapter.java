package vyrus.bikegps;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Cristi Mark on 08.05.2017.
 */

public class SliderImageAdapter extends PagerAdapter {
    Context mContext;
   /* private int[] sliderImagesId = new int[]{
            //   R.drawable.image1, R.drawable.image2, R.drawable.cat,
            //  R.drawable.image1, R.drawable.image2, R.drawable.cat,
    };*/
    private Bitmap[] sliderImagesBitmap = new Bitmap[]{
            //   R.drawable.image1, R.drawable.image2, R.drawable.cat,
            //  R.drawable.image1, R.drawable.image2, R.drawable.cat,
    };



    public SliderImageAdapter(Context context) {
        this.mContext = context;
    }

    /*@Override
    public int getCount() {
        return sliderImagesId.length;
    }*/

    @Override
    public int getCount() {
        return sliderImagesBitmap.length;
    }

    public void SetImages(Bitmap[] pic){
        sliderImagesBitmap = pic;
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == ((ImageView) obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        ImageView mImageView = new ImageView(mContext);
      //  mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
       // mImageView.setImageResource(sliderImagesId[i]);
        mImageView.setImageBitmap(sliderImagesBitmap[i]);
        ((ViewPager) container).addView(mImageView, 0);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        ((ViewPager) container).removeView((ImageView) obj);
    }
}
