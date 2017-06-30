package in.sportscafe.nostragamus.module.navigation.referfriends;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;

/**
 * Created by deepanshi on 6/30/17.
 */

public class SuccessfulReferralActivity extends NostragamusActivity {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.SUCCESSFUL_REFERRAL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);
        initialize();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.refer_friend_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.refer_friend_toolbar_tv);
        tvToolbar.setText("Refer a Friend and earn cash");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }
}
