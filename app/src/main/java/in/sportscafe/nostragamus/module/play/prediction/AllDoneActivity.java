package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jeeva.android.widgets.customfont.CustomButton;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;

/**
 * Created by Jeeva on 16/6/16.
 */
public class AllDoneActivity extends NostragamusActivity {

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_all_done);

        CustomButton mbutton = (CustomButton) findViewById(R.id.go_to_timeline_btn);
        mbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);

            }
        });
    }
}