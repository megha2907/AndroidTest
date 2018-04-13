package in.sportscafe.nostragamus.module.recentActivity.announcement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraTagHandler;
import in.sportscafe.nostragamus.module.common.NostraTextViewLinkClickMovementMethod;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.resultspeek.FeedWebView;

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
            ((TextView) findViewById(R.id.announcement_title)).setText(Html.fromHtml(announcementScreenData.getAnnouncementTitle(), null, new NostraTagHandler()));

            final TextView announcementDesc = (TextView) findViewById(R.id.announcement_desc);
            announcementDesc.setText(Html.fromHtml(announcementScreenData.getAnnouncementDesc(), null, new NostraTagHandler()));
            announcementDesc.setMovementMethod(new NostraTextViewLinkClickMovementMethod() {
                @Override
                public void onLinkClick(String url) {
                    OpenWebView(announcementDesc, url);
                }
            });

            if (!TextUtils.isEmpty(announcementScreenData.getAnnouncementDate())) {
                ((TextView) findViewById(R.id.announcement_date)).setText(DateTimeHelper.getFormattedDate(announcementScreenData.getAnnouncementDate()));
            }
        }

    }


    /**
     * On on click of link open NostragamusWebView Activity for handling links
     */
    private void OpenWebView(View view, String url) {
        if (url != null && view != null && view.getContext() != null) {
            view.getContext().startActivity(new Intent(view.getContext(), FeedWebView.class).putExtra("url", url));
        }
    }

}
