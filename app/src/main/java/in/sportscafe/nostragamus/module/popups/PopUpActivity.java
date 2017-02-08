package in.sportscafe.nostragamus.module.popups;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;

import static in.sportscafe.nostragamus.R.id.view;

/**
 * Created by deepanshi on 2/2/17.
 */

public class PopUpActivity extends AppCompatActivity implements View.OnClickListener, PopUpModelImpl.OnGetPopUpModelListener {

    private ImageButton mExitPopupBtn;

    private List<PopUp> mpopUpData;

    private int popUpItem = 0;

    private RelativeLayout mpopUpLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        mExitPopupBtn = (ImageButton) findViewById(R.id.popup_exit_btn);
        mExitPopupBtn.setOnClickListener(this);

        mpopUpLayout = (RelativeLayout) findViewById(R.id.popup);

        mpopUpData = Parcels.unwrap(getIntent().getExtras().getParcelable(Constants.BundleKeys.POPUP_DATA));
        setPopUpData();

    }

    private void setPopUpData() {

        HmImageView popUpImage = (HmImageView) findViewById(R.id.popup_image);
        TextView popupTitle = (TextView) findViewById(R.id.popup_title);
        TextView popupDesc = (TextView) findViewById(R.id.popup_desc);
        popUpImage.setImageUrl(mpopUpData.get(popUpItem).getImageUrl());
        popupTitle.setText(mpopUpData.get(popUpItem).getTitle());
        popupDesc.setText(mpopUpData.get(popUpItem).getDescription());

        PopUpModelImpl.newInstance(this).AcknowledgePopups(mpopUpData.get(popUpItem).getName());
        mpopUpData.remove(popUpItem);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_exit_btn:
                if (mpopUpData.size() <= 0) {
                    finish();
                } else {
                    mpopUpLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_up));
                    setPopUpData();
                }
                break;
        }
    }

    @Override
    public void onSuccessGetUpdatedPopUps(List<PopUp> popUps) {

    }

    @Override
    public void onFailedGetUpdatePopUps(String message) {

    }
}
