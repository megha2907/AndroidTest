package in.sportscafe.scgame.module.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.util.HashMap;

import in.sportscafe.scgame.Constants.BundleKeys;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.getstart.GetStartActivity;
import in.sportscafe.scgame.module.user.group.joingroup.JoinGroupActivity;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Branch.getInstance().initSession(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError branchError) {

                /* In case the clicked link has $android_deeplink_path the Branch will launch the MonsterViewer automatically since AutoDeeplinking feature is enabled.
                 * Launch Monster viewer activity if a link clicked without $android_deeplink_path
                 */
                if(null != branchUniversalObject) {
                    HashMap<String, String> metadata = branchUniversalObject.getMetadata();
                    String path = metadata.get("$android_deeplink_path");
                    if (null != path) {
                        if (path.equalsIgnoreCase("group/invite/")) {
                            if(ScGameDataHandler.getInstance().getFavoriteSportsIdList().size() > 0) {
                                navigateToJoinGroup(metadata.get(BundleKeys.GROUP_CODE));
                                return;
                            } else {
                                ScGameDataHandler.getInstance().setInstallGroupCode(metadata.get(BundleKeys.GROUP_CODE));
                                ScGameDataHandler.getInstance().setInstallGroupName(metadata.get(BundleKeys.GROUP_NAME));
                            }
                        }
                    }
                }

                navigateToGetStarted();
            }
        }, this.getIntent().getData(), this);
    }

    private void navigateToGetStarted() {
        startActivity(new Intent(this, GetStartActivity.class));
        finish();
    }

    private void navigateToJoinGroup(String groupCode) {
        Log.d("SplashActivity", "Join Group start called");
        Intent intent = new Intent(this, JoinGroupActivity.class);
        intent.putExtra(BundleKeys.GROUP_CODE, groupCode);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}