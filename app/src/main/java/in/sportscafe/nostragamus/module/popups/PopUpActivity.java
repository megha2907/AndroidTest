package in.sportscafe.nostragamus.module.popups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 2/2/17.
 */

public class PopUpActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton mExitPopupBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        mExitPopupBtn = (ImageButton) findViewById(R.id.popup_exit_btn);
        mExitPopupBtn.setOnClickListener(this);

        setPopUpData(getIntent().getExtras());

    }

    private void setPopUpData(Bundle bundle) {

        List<PopUp> popUpData = Parcels.unwrap(bundle.getParcelable(Constants.BundleKeys.POPUP_DATA));

        HmImageView popUpImage = (HmImageView) findViewById(R.id.popup_image);
        TextView popupTitle = (TextView) findViewById(R.id.popup_title);
        TextView popupDesc = (TextView) findViewById(R.id.popup_desc);
        popUpImage.setImageUrl(popUpData.get(0).getImageUrl());
        popupTitle.setText(popUpData.get(0).getTitle());
        popupDesc.setText(popUpData.get(0).getDescription());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_exit_btn:
                finish();
                break;
        }
    }
}
