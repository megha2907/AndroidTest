package in.sportscafe.scgame.module.play.prediction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jeeva.android.widgets.customfont.CustomButton;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.home.HomeActivity;

/**
 * Created by Jeeva on 16/6/16.
 */
public class AllDoneActivity extends ScGameActivity {

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