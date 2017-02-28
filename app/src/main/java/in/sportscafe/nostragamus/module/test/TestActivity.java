package in.sportscafe.nostragamus.module.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.felipecsl.gifimageview.library.GifImageView;

import java.io.IOException;
import java.io.InputStream;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.popups.PowerupDialogFragment;

/**
 * Created by Jeeva on 10/6/16.
 */
public class TestActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        PowerupDialogFragment fragment = new PowerupDialogFragment();
        fragment.show(getSupportFragmentManager(), "Powerup");

        /*gifImageView = (GifImageView) findViewById(R.id.gifImageView);

        gifImageView.setOnAnimationStop(new GifImageView.OnAnimationStop() {
            @Override
            public void onAnimationStop() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TestActivity.this, "Animation stopped", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        loadGifs();
    }

    private void loadGifs() {
        InputStream stream = null;
        try {
            byte[] bytes = AppSnippet.streamToBytes(getAssets().open("gifs/dhoni_catch.gif"));
            gifImageView.setBytes(bytes);
            gifImageView.setFramesDisplayDuration(100);
            gifImageView.startAnimation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        gifImageView.stopAnimation();
    }*/
}