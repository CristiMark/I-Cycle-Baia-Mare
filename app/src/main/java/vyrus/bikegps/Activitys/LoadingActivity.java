package vyrus.bikegps.Activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import vyrus.bikegps.DataBase;
import vyrus.bikegps.R;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class LoadingActivity extends Activity implements  TextureView.SurfaceTextureListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    static DataBase db;
     MediaPlayer player;
   // SurfaceView surfaceView;
   // SurfaceHolder surfaceHolder;
    boolean pausing = false;;
    View view;
    ImageView splash;
    TextureView videoHolder;
   // String uriPath = "android.resource://vyrus.bikegps/"+ R.raw.v5;


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

     //   fadeSplashOut();
        done();

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private void fadeSplashOut() {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        view.setAlpha(0f);

        view.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        view.animate()
                .alpha(1f)
                .setDuration(3000)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        splash.animate()
                .alpha(0f)
                .setDuration(3000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        splash.setVisibility(View.GONE);
                    }
                });
    }

    class C04751 implements MediaPlayer.OnPreparedListener {
        C04751() {
        }

        public void onPrepared(MediaPlayer mp) {

            int videoWidth = player.getVideoWidth();
            int videoHeight = player.getVideoHeight();
            float videoProportion = (float) videoWidth / (float) videoHeight;
            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
            int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
            float screenProportion = (float) screenWidth / (float) screenHeight;
            android.view.ViewGroup.LayoutParams lp = videoHolder.getLayoutParams();

            if (videoProportion > screenProportion) {
                lp.width = screenWidth;
                lp.height = (int) ((float) screenWidth / videoProportion);
            } else {
                lp.width = (int) (videoProportion * (float) screenHeight);
                lp.height = screenHeight;
            }
            videoHolder.setLayoutParams(lp);

            if (!player.isPlaying()) {
                LoadingActivity.this.player.start();
            }
            videoHolder.setClickable(true);
           // LoadingActivity.this.mediaPlayer.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        view = this.findViewById(android.R.id.content);
       // splash = (ImageView) this.findViewById(R.id.Splash);

       // db = new DataBase(getApplicationContext());

        videoHolder = (TextureView)findViewById(R.id.textureView);

        this.videoHolder.setSurfaceTextureListener(this);


        new Thread(new Runnable() {
            public void run() {
                try {

                } catch (Exception e) { // I can split the exceptions to get which error i need.
                    //showToast("Error while playing video");
                  //  Log.i(TAG, "Error");
                    e.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.player != null) {
            this.player.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.player != null) {
            this.player.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.player != null) {
            this.player.stop();
            this.player.release();
            this.player = null;
        }
    }

    public void setBlurred(boolean z) {
      //  if (this.mVideoBackground != null) {
        //    this.mBlackTint.setVisibility(z ? 0 : 8);
       // }
    }


    public void done(){

        Intent i = new Intent(this,SponsorActivity.class);
        startActivity(i);
        finish();
    }


    static void insert (String id, String lat, String lon, String traseu, String timestamp){
        db.insertGPS(id, lat, lon, traseu, timestamp);

        //db.insertGPS(id, lat, lon, traseu, timestamp);

        Log.i("This", "insert: ");

    }

//    public void RouteOnClick(View v){
//       done();
//
//    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface surfaceTexture = new Surface(surface);

      //   Uri uri = Uri.parse(uriPath);
      //  if (isUriValid(parse)) {
            this.player = new MediaPlayer();
            try {
             //   this.player.setDataSource(getApplicationContext(), uri);
                this.player.setSurface(surfaceTexture);
                this.player.setLooping(false);
                this.player.prepareAsync();
                this.player.setOnErrorListener(this);
                this.player.setOnPreparedListener(new C04751());
                this.player.setOnCompletionListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
       /* } else if (getActivity() instanceof SignupChoiceActivity) {
            ((SignupChoiceActivity) getActivity()).setVdoInvalid(true);
        }*/
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
