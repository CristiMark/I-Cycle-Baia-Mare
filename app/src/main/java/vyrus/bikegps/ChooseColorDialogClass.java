package vyrus.bikegps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
//import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.google.android.gms.maps.model.Polyline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import vyrus.bikegps.Activitys.MainActivity;

/**
 * Created by Vyrus on 30/01/2017.
 */

public class ChooseColorDialogClass extends Dialog{
    public Activity c;
    private int res;
    private Bitmap Images;
    private Context context;

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;



    ViewFlipper simpleViewFlipper;

    public ChooseColorDialogClass(Context context, int res) {
        super(context);
        this.res = res;
        this.context = context;

    }public ChooseColorDialogClass( Context context, int res, Bitmap images) {
        super(context);
        this.Images = images;
        this.res = res;
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(res);

        if(res ==  R.layout.about_us_dialog){AboutSetUp();}
        if(res ==  R.layout.what_and_how_dialog){what_and_howSetUp();}
        if(res ==  R.layout.big_image_layout){BigImageViewer();}

        setCanceledOnTouchOutside(false);
    }

    private void BigImageViewer() {

        //ImageView view = (ImageView) findViewById(R.id.imageViewBigImage);
        ZoomableImageView view = (ZoomableImageView) findViewById(R.id.imageViewBigImage);
        view.bmHeight+= 50;
        view.bmWidth+=50;
         view.setImageBitmap(Images);

    }



    private void what_and_howSetUp() {
        simpleViewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper); // get the reference of ViewFlipper
        simpleViewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // simpleViewFlipper.showNext();
            }
        });


        // Declare in and out animations and load them using AnimationUtils class
        Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);

        // set the animation type to ViewFlipper
        simpleViewFlipper.setInAnimation(in);
        simpleViewFlipper.setOutAnimation(out);

        TextView Intro = (TextView)findViewById(R.id.Intro);
        String formattedText = context.getResources().getString(R.string.Intro);
        Spanned result = Html.fromHtml(formattedText);
        Intro.setText(result);

        TextView nextintro = (TextView)findViewById(R.id.nextIntro);
        String formattedTextnextintro = context.getResources().getString(R.string.Next);
        Spanned result1nextintro = Html.fromHtml(formattedTextnextintro);
        nextintro.setText(result1nextintro);
        nextintro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleViewFlipper.showNext();
            }
        });

        TextView Help1 = (TextView)findViewById(R.id.HelpContaine1);
        String formattedText1 = context.getResources().getString(R.string.Help1);
        Spanned result1 = Html.fromHtml(formattedText1);
        Help1.setText(result1);

        TextView next1 = (TextView)findViewById(R.id.next1);
        String formattedTextnext = context.getResources().getString(R.string.Next);
        Spanned result1next = Html.fromHtml(formattedTextnext);
        next1.setText(result1next);
        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleViewFlipper.showNext();
            }
        });

        TextView Help2 = (TextView)findViewById(R.id.HelpContaine2);
        String formattedText2 = context.getResources().getString(R.string.Help2);
        Spanned result2 = Html.fromHtml(formattedText2);
        Help2.setText(result2);

        TextView next2 = (TextView)findViewById(R.id.next2);
        String formattedTextnex2 = context.getResources().getString(R.string.Next);
        Spanned result1nex2 = Html.fromHtml(formattedTextnex2);
        next2.setText(result1nex2);
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleViewFlipper.showNext();
            }
        });

        TextView Help3 = (TextView)findViewById(R.id.HelpContaine3);
        String formattedText3 = context.getResources().getString(R.string.Help3);
        Spanned result3 = Html.fromHtml(formattedText3);
        Help3.setText(result3);

        TextView next3 = (TextView)findViewById(R.id.next3);
        String formattedTextnex3 = context.getResources().getString(R.string.Next);
        Spanned result1nex3 = Html.fromHtml(formattedTextnex3);
        next3.setText(result1nex3);
        next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleViewFlipper.showNext();
            }
        });

        TextView Help4 = (TextView)findViewById(R.id.HelpContaine4);
        String formattedText4 = context.getResources().getString(R.string.Help4);
        Spanned result4 = Html.fromHtml(formattedText4);
        Help4.setText(result4);

        TextView next4 = (TextView)findViewById(R.id.next4);
        String formattedTextnex4 = context.getResources().getString(R.string.Next);
        Spanned result1nex4 = Html.fromHtml(formattedTextnex4);
        next4.setText(result1nex4);
        next4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleViewFlipper.showNext();
            }
        });

        TextView Help5 = (TextView)findViewById(R.id.HelpContaine5);
        String formattedText5 = context.getResources().getString(R.string.Help5);
        Spanned result5 = Html.fromHtml(formattedText5);
        Help5.setText(result5);

        TextView next5 = (TextView)findViewById(R.id.next5);
        String formattedTextnex5 = context.getResources().getString(R.string.Next);
        Spanned result1nex5 = Html.fromHtml(formattedTextnex5);
        next5.setText(result1nex5);
        next5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleViewFlipper.showNext();
            }
        });

    }



    private void AboutSetUp(){

        TextView content = (TextView)findViewById(R.id.AboutContaine);
        String formattedText = context.getResources().getString(R.string.About);
        Spanned result = Html.fromHtml(formattedText);
        content.setText(result);


    }

    public int getHotspotColor (int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById (hotspotId);
        img.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
        img.setDrawingCacheEnabled(false);
        return hotspots.getPixel(x, y);
    }

    public boolean closeMatch (int color1, int color2, int tolerance) {
        int blue = Color.blue (color1);
        if ((int) Math.abs (Color.red (color1) - Color.red (color2)) > tolerance )
            return false;
        if ((int) Math.abs (Color.green (color1) - Color.green (color2)) > tolerance )
            return false;
        if ((int) Math.abs (Color.blue (color1) - Color.blue (color2)) > tolerance )
            return false;
        return true;
    } // end match



}