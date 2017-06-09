package in.sportscafe.nostragamus.module.popups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_new);

        mPopUpList = Parcels.unwrap(getIntent().getExtras().getParcelable(BundleKeys.POPUP_DATA));
        showTopLevelPopup();
    }

    private void showTopLevelPopup() {
        PopUp popUp = mPopUpList.get(0);

        ((HmImageView) findViewById(R.id.popup_image)).setImageUrl(popUp.getImageUrl());
        ((TextView) findViewById(R.id.popup_title)).setText(popUp.getTitle());
        ((TextView) findViewById(R.id.popup_desc)).setText(popUp.getDescription());
        Button exitBtn = (Button) findViewById(R.id.popup_exit_btn);
        ImageView crossBtn = (ImageView) findViewById(R.id.popup_cross_btn);
        exitBtn.setOnClickListener(this);
        crossBtn.setOnClickListener(this);

        PopUpModelImpl.newInstance().acknowledgePopups(popUp);
        mPopUpList.remove(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popup_exit_btn:
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
