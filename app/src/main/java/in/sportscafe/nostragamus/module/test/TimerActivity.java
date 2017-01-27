package in.sportscafe.nostragamus.module.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.sportscafe.nostragamus.R;

/**
 * Created by Jeeva on 27/01/17.
 */

public class TimerActivity extends AppCompatActivity {

    private Button startButton;

    private Button pauseButton;

    private TextView timerValue;

    private Handler customHandler = new Handler();

    long updatedTime = 0L;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerValue = (TextView) findViewById(R.id.tvTimerValue);

        startButton = (Button) findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                customHandler.postDelayed(updateTimerThread, 1000);

            }
        });

        pauseButton = (Button) findViewById(R.id.pauseButton);

        pauseButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                customHandler.removeCallbacks(updateTimerThread);
            }
        });

    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            updatedTime += 1000;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            mins = mins % 60;
            secs = secs % 60;

            timerValue.setText(
                    String.format("%02d", hours) + ":"
                    + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs)
            );
            customHandler.postDelayed(this, 1000);
        }

    };
}
