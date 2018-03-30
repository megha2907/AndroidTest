package in.sportscafe.nostragamus.module.recentActivity.announcement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;

/**
 * Created by deepanshi on 3/26/18.
 */

public class AnnouncementActivity extends NostragamusActivity {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.ANNOUNCEMENT;
    }

    private AnnouncementScreenData mAnnouncementScreenData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        initToolbar();
        getData();
    }

    private void getData() {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.ANNOUNCEMENT_SCREEN_DATA)) {
                mAnnouncementScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.ANNOUNCEMENT_SCREEN_DATA));
                initView(mAnnouncementScreenData);
            }
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.announcement_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.announcement_toolbar_tv);
        tvToolbar.setText("Activity Details");
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

    private void initView(AnnouncementScreenData announcementScreenData) {

        if (announcementScreenData != null) {
            ((TextView) findViewById(R.id.announcement_title)).setText(announcementScreenData.getAnnouncementTitle());
            ((TextView) findViewById(R.id.announcement_desc)).setText(announcementScreenData.getAnnouncementDesc());

            if (!TextUtils.isEmpty(announcementScreenData.getAnnouncementDate())) {
                ((TextView) findViewById(R.id.announcement_date)).setText(DateTimeHelper.getFormattedDate(announcementScreenData.getAnnouncementDate()));
            }
        }

    }

}
