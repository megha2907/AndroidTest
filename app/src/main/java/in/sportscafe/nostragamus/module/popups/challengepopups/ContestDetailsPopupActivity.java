package in.sportscafe.nostragamus.module.popups.challengepopups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jeeva.android.BaseActivity;
import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 8/25/17.
 */

public class ContestDetailsPopupActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_details_popup);

        initView();

    }

    private void initView() {
        (findViewById(R.id.popup_contest_exit_btn)).setOnClickListener(this);
        (findViewById(R.id.popup_contest_cross_btn)).setOnClickListener(this);
        (findViewById(R.id.popup_bg)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_bg:
                onBackPressed();
                break;
            case R.id.popup_contest_cross_btn:
                onBackPressed();
                break;
            case R.id.popup_contest_exit_btn:
                onBackPressed();
                break;
        }
    }
}