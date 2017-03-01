package in.sportscafe.nostragamus.module.bank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.ExceptionTracker;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;
import in.sportscafe.nostragamus.module.user.referral.ReferralActivity;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Jeeva on 01/03/17.
 */
public class BankFragment extends NostragamusFragment implements BankView, View.OnClickListener {

    private TextView mTvRunningLow;

    private BankPrensenter mBankPrensenter;

    public static BankFragment newInstance(HashMap<String, PowerUp> powerUpMaps) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.POWERUPS, Parcels.wrap(powerUpMaps));

        BankFragment fragment = new BankFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bank, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTvRunningLow = (TextView) findViewById(R.id.bank_tv_label_running_low);
        findViewById(R.id.bank_btn_refer_friend).setOnClickListener(this);

        mBankPrensenter = BankPresenterImpl.newInstance(this);
        mBankPrensenter.onCreateBank(getArguments());
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bank_btn_refer_friend) {
            mBankPrensenter.onClickReferFriend();
        }
    }

    @Override
    public void set2xCount(int count, boolean runningLow) {
        setPowerUpCount((TextView) findViewById(R.id.bank_tv_2x), count, runningLow);
    }

    @Override
    public void setNonegsCount(int count, boolean runningLow) {
        setPowerUpCount((TextView) findViewById(R.id.bank_tv_nonegs), count, runningLow);
    }

    @Override
    public void setPollCount(int count, boolean runningLow) {
        setPowerUpCount((TextView) findViewById(R.id.bank_tv_poll), count, runningLow);
    }

    private void setPowerUpCount(TextView textView, int count, boolean runningLow) {
        textView.setText(String.valueOf(count));
        if (runningLow) {
            textView.setBackground(getResources().getDrawable(R.drawable.bank_powerup_count_bg));

            if (mTvRunningLow.getVisibility() == View.GONE) {
                mTvRunningLow.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void navigateToReferFriend() {
        BranchUniversalObject buo = new BranchUniversalObject()
                .setTitle("Refer a Friend")
                .setContentDescription(NostragamusDataHandler.getInstance().getUserInfo().getUserName() + " just invited you to play Nostragamus (beta), the coolest way to predict the latest happenings in the world of sports!")
                .setContentImageUrl("https://cdn-images.spcafe.in/img/es3/screact/game-app/game-logo.png")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata(Constants.BundleKeys.USER_REFERRAL_ID, NostragamusDataHandler.getInstance().getUserId());

        LinkProperties linkProperties = new LinkProperties()
                .addTag("inviteApp")
                .setFeature("inviteApp")
                .setChannel("App")
                .addControlParameter("$android_deeplink_path", "app/invite/");

        buo.generateShortUrl(getContext(), linkProperties,
                new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (null == error) {
                            AppSnippet.doGeneralShare(getApplicationContext(), url);
                        } else {
                            ExceptionTracker.track(error.getMessage());
                        }
                    }
                });
    }
}