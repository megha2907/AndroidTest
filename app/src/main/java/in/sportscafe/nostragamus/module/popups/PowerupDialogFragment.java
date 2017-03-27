package in.sportscafe.nostragamus.module.popups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.ShadowLayout;

import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.play.dummygame.DummyGameActivity;

/**
 * Created by Jeeva on 28/02/17.
 */

public class PowerupDialogFragment extends NostragamusDialogFragment implements View.OnClickListener {

    private static final String SAMPLE_GAME = "sampleGame";

    private LinearLayout mLlPowerUpHolder;

    private LayoutInflater mLayoutInflater;

    private String[] mPowerUps = new String[]{
            Powerups.XX,
            Powerups.NO_NEGATIVE,
            Powerups.AUDIENCE_POLL,
            SAMPLE_GAME
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_powerup_popup, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLlPowerUpHolder = (LinearLayout) findViewById(R.id.popup_ll_powerup_holder);

        mLayoutInflater = LayoutInflater.from(getContext());

        populatePowerups();

        findViewById(R.id.popup_btn_sample_game).setOnClickListener(this);
        findViewById(R.id.popup_iv_close).setOnClickListener(this);
    }

    private void populatePowerups() {
        String powerUp;
        View powerUpView = null;
        for (int i = 0; i < mPowerUps.length; i++) {
            powerUp = mPowerUps[i];

            switch (powerUp) {
                case Powerups.XX:
                    powerUpView = getPowerUpView("Doubler Powerup", "This doubles the points scored for a question.\nEg. (+10/-4) becomes (+20/-8)", R.drawable.powerup_2x_white, R.color.dodger_blue);
                    break;
                case Powerups.NO_NEGATIVE:
                    powerUpView = getPowerUpView("No Negative Powerup", "This removes negative marking for a question.\nEg. (+10/-4) becomes (+10)", R.drawable.powerup_nonegs_white, R.color.amaranth);
                    break;
                case Powerups.AUDIENCE_POLL:
                    powerUpView = getPowerUpView("Audience Poll Powerup", "This tells you how other players have answered", R.drawable.powerup_audience_poll_white, R.color.greencolor);
                    break;
                case SAMPLE_GAME:
                    powerUpView = getSampleGameView("How To Play?", "Click the button below to play a sample game", R.drawable.info_sample_game_icon);
                    break;
            }
            mLlPowerUpHolder.addView(powerUpView, i);
        }
    }

    private View getPowerUpView(String title, String desc, int iconRes, int bgColorRes) {
        View powerUpView = mLayoutInflater.inflate(R.layout.inflater_popup_row, mLlPowerUpHolder, false);

        ((TextView) powerUpView.findViewById(R.id.powerup_tv_title)).setText(title);
        ((TextView) powerUpView.findViewById(R.id.powerup_tv_desc)).setText(desc);

        ImageView imageView = (ImageView) powerUpView.findViewById(R.id.powerup_iv_icon);
        imageView.setImageResource(iconRes);

        ((ShadowLayout) powerUpView.findViewById(R.id.powerup_sl_icon_bg)).setFillColor(ContextCompat.getColor(getContext(), bgColorRes));

        return powerUpView;
    }

    private View getSampleGameView(String title, String desc, int iconRes) {
        View powerUpView = mLayoutInflater.inflate(R.layout.inflater_sample_game_row, mLlPowerUpHolder, false);

        ((TextView) powerUpView.findViewById(R.id.powerup_tv_title)).setText(title);
        ((TextView) powerUpView.findViewById(R.id.powerup_tv_desc)).setText(desc);

        ImageView imageView = (ImageView) powerUpView.findViewById(R.id.powerup_iv_icon);
        imageView.setImageResource(iconRes);

        return powerUpView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popup_btn_sample_game:
                startActivity(new Intent(getContext(), DummyGameActivity.class));
                dismiss();
                break;
            case R.id.popup_iv_close:
                dismiss();
                break;
        }
    }
}