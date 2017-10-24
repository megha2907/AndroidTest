package in.sportscafe.nostragamus.module.referral;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

/**
 * Created by deepanshi on 12/3/16.
 */

public class ReferralActivity extends AppCompatActivity implements
        View.OnClickListener {


    private Toolbar mtoolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        initToolBar();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.referral_btn:
                onClickShareCode();
                break;
        }
    }

    private void onClickShareCode() {

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();

        BranchUniversalObject buo = new BranchUniversalObject()
                .setTitle("Refer a Friend")
                .setContentDescription(userInfo.getUserName()+ " just invited you to play Nostragamus (beta), the coolest way to predict the latest happenings in the world of sports!" )
                .setContentImageUrl("https://cdn-images.spcafe.in/img/es3/screact/game-app/game-logo.png")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata(Constants.BundleKeys.USER_REFERRAL_ID, String.valueOf(userInfo.getId()));


        LinkProperties linkProperties = new LinkProperties()
                .addTag("inviteApp")
                .setFeature("inviteApp")
                .setChannel("App")
                .addControlParameter("$android_deeplink_path", "app/invite/");

        buo.generateShortUrl(this, linkProperties,
                new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if(null == error) {
                            AppSnippet.doGeneralShare(getApplicationContext(), url);
                        } else {
                            ExceptionTracker.track(error.getMessage());
                        }
                    }
                });
    }


    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.refer_toolbar);
        mtoolbar.setTitle("Refer Friends");

        setSupportActionBar(mtoolbar);

        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }

        );
    }
}
