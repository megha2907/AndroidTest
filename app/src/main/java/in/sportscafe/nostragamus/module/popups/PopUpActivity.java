package in.sportscafe.nostragamus.module.popups;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.BaseActivity;
import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 2/2/17.
 */

public class PopUpActivity extends BaseActivity implements View.OnClickListener {

    private List<PopUp> mPopUpList;
    private HmImageView popUpImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_new);

        mPopUpList = Parcels.unwrap(getIntent().getExtras().getParcelable(BundleKeys.POPUP_DATA));
        showTopLevelPopup();
    }

    private void showTopLevelPopup() {
        PopUp popUp = mPopUpList.get(0);

        checkPopUpType(popUp);

        ((HmImageView) findViewById(R.id.popup_image)).setImageUrl(popUp.getImageUrl());
        ((TextView) findViewById(R.id.popup_title)).setText(popUp.getTitle());
        ((TextView) findViewById(R.id.popup_desc)).setText(popUp.getDescription());
        Button exitBtn = (Button) findViewById(R.id.popup_exit_btn);
        ImageView crossBtn = (ImageView) findViewById(R.id.popup_cross_btn);
        findViewById(R.id.popup_bg).setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        crossBtn.setOnClickListener(this);

        PopUpModelImpl.newInstance().acknowledgePopups(popUp);
        mPopUpList.remove(0);
    }

    private void checkPopUpType(PopUp popUp) {

        Boolean popUpAlternateDesign = popUp.getAlternateDesign();

        if (popUpAlternateDesign != null) {

            if (popUpAlternateDesign) {

                TextView popupHeading = (TextView) findViewById(R.id.popup_tv_heading);
                TextView popupTitle = (TextView) findViewById(R.id.popup_title);
                TextView popupDesc = (TextView) findViewById(R.id.popup_desc);
                LinearLayout popupHeadingBg = (LinearLayout) findViewById(R.id.popup_ll_heading_bg);
                Button exitBtn = (Button) findViewById(R.id.popup_exit_btn);
                ImageView popupImageBg = (ImageView) findViewById(R.id.popup_image_bg);
                ImageView popupImage = (ImageView) findViewById(R.id.popup_image);
                LinearLayout layout = (LinearLayout)findViewById(R.id.popup_ll_heading_bg);
                RelativeLayout popupImageRl = (RelativeLayout) findViewById(R.id.popup_image_rl);

                TextView achievementPopupHeading = (TextView) findViewById(R.id.popup_achievement_tv_heading);

                ViewGroup.LayoutParams params = layout.getLayoutParams();
                params.height = getResources().getDimensionPixelSize(R.dimen.dim_56);
                layout.setLayoutParams(params);

//                popupImage.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.dim_100);
//                popupImage.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.dim_100);


                achievementPopupHeading.setVisibility(View.GONE);
                popupHeading.setVisibility(View.VISIBLE);
                popupHeading.setText(popUp.getHeading());
                popupHeadingBg.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackground));
                popupImageBg.setVisibility(View.GONE);

                Typeface face = Typeface.createFromAsset(getAssets(),
                        "fonts/lato/Lato-Bold.ttf");
                popupTitle.setTypeface(face);
                popupTitle.setTextSize(14);
                popupDesc.setTextSize(12);
                popupDesc.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

                LinearLayout.LayoutParams paramsFour = (LinearLayout.LayoutParams) popupImageRl.getLayoutParams();
                paramsFour.topMargin = getResources().getDimensionPixelSize(R.dimen.dim_10);
                popupImageRl.setLayoutParams(paramsFour);

            }

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popup_exit_btn:
                onBackPressed();
                break;
            case R.id.popup_bg:
                onBackPressed();
                break;
            case R.id.popup_cross_btn:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mPopUpList.isEmpty()) {
            super.onBackPressed();
        } else {
            findViewById(R.id.popup).startAnimation(
                    AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up)
            );
            showTopLevelPopup();
        }
    }
}
