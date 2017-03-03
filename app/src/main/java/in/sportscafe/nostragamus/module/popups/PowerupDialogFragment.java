package in.sportscafe.nostragamus.module.popups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.ShadowLayout;

import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;

/**
 * Created by Jeeva on 28/02/17.
 */

public class PowerupDialogFragment extends NostragamusDialogFragment {

    private LinearLayout mLlPowerUpHolder;

    private LayoutInflater mLayoutInflater;

    private String[] mPowerUps = new String[]{Powerups.XX, Powerups.NO_NEGATIVE, Powerups.AUDIENCE_POLL};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_powerup_popup, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLlPowerUpHolder = (LinearLayout) getView().findViewById(R.id.popup_ll_powerup_holder);

        mLayoutInflater = LayoutInflater.from(getContext());

        populatePowerups();

        getView().findViewById(R.id.popup_btn_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void populatePowerups() {
        String powerUp;
        View powerUpView = null;
        for (int i = 0; i < mPowerUps.length; i++) {
            powerUp = mPowerUps[i];

            switch (powerUp) {
                case Powerups.XX:
                    powerUpView = getPowerUpView("Bonus Points", "Double the points scored for one question", R.drawable.powerup_2x_white, R.color.dodger_blue);
                    break;
                case Powerups.NO_NEGATIVE:
                    powerUpView = getPowerUpView("No Negative", "Removes negative marking for one question", R.drawable.powerup_nonegs_white, R.color.amaranth);
                    break;
                case Powerups.AUDIENCE_POLL:
                    powerUpView = getPowerUpView("Audience Poll", "See how your friends answered", R.drawable.powerup_audience_poll_white, R.color.greencolor);
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

        ((ShadowLayout) powerUpView.findViewById(R.id.powerup_sl_icon_bg)).setFillColor(getContext().getColor(bgColorRes));

        return powerUpView;
    }


}