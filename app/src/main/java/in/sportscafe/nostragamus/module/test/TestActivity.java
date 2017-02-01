package in.sportscafe.nostragamus.module.test;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.sportscafe.nostragamus.R;

/**
 * Created by Jeeva on 10/6/16.
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inflater_powerups_row);

        findViewById(R.id.powerups_iv_2x).setBackground(getPowerupDrawable(R.color.greencolor));
        findViewById(R.id.powerups_iv_nonegs).setBackground(getPowerupDrawable(R.color.radical_red));
        findViewById(R.id.powerups_iv_poll).setBackground(getPowerupDrawable(R.color.dodger_blue));
    }

    private Drawable getPowerupDrawable(int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.RECTANGLE);
        powerupDrawable.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.dp_5));
        powerupDrawable.setColor(getResources().getColor(colorRes));
        return powerupDrawable;
    }
}