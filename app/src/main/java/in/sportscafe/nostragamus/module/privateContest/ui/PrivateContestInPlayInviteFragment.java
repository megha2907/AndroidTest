package in.sportscafe.nostragamus.module.privateContest.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.ExceptionTracker;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateContestInPlayInviteFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = PrivateContestInPlayInviteFragment.class.getSimpleName();

    private TextView mShareLinkTextView;
    private InPlayContestDto mInplayContestDto;
    private String mShareLink = "";

    public PrivateContestInPlayInviteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_contest_in_play_invite, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mShareLinkTextView = (TextView) view.findViewById(R.id.private_contest_invitation_share_link_textView);

        view.findViewById(R.id.private_contest_share_btn).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showPrivateCode();
        createContestShareLink();
    }

    private void showPrivateCode() {
        if (getView() != null) {
            if (mInplayContestDto != null && !TextUtils.isEmpty(mInplayContestDto.getPrivateCode())) {
                TextView privateCodeTextView = (TextView) getView().findViewById(R.id.private_contest_invitation_code);
                privateCodeTextView.setText(mInplayContestDto.getPrivateCode());
            }
        }
    }

    private void createContestShareLink() {
        if (mInplayContestDto != null && !TextUtils.isEmpty(mInplayContestDto.getPrivateCode())) {
            BranchUniversalObject branchUniversalObject = new BranchUniversalObject();
            branchUniversalObject.setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);
            branchUniversalObject.addContentMetadata(Constants.PrivateContests.BranchLink.PRIVATE_CONTEST_INVITATION_CODE,
                    mInplayContestDto.getPrivateCode());

            if (Nostragamus.getInstance().getServerDataManager().getUserInfo() != null) {
                branchUniversalObject.addContentMetadata(Constants.PrivateContests.BranchLink.USER_NICK,
                        Nostragamus.getInstance().getServerDataManager().getUserInfo().getUserNickName());
                branchUniversalObject.addContentMetadata(Constants.PrivateContests.BranchLink.USER_PHOTO_URL,
                        Nostragamus.getInstance().getServerDataManager().getUserInfo().getPhoto());
            }

            LinkProperties linkProperties = new LinkProperties();
            linkProperties.addTag(Constants.PrivateContests.BranchLink.LINKED_PROPERTIES_FEATURE);
            linkProperties.setChannel(Constants.PrivateContests.BranchLink.LINKED_PROPERTIES_CHANNEL);
            linkProperties.setCampaign(Constants.PrivateContests.BranchLink.LINKED_PROPERTIES_CAMPAIGN);
            linkProperties.setFeature(Constants.PrivateContests.BranchLink.LINKED_PROPERTIES_FEATURE);
            linkProperties.addControlParameter("$android_deeplink_path", Constants.PrivateContests.BranchLink.LINKED_PATH);

            branchUniversalObject.generateShortUrl(getContext(), linkProperties,
                    new Branch.BranchLinkCreateListener() {
                        @Override
                        public void onLinkCreate(String shortLink, BranchError branchError) {
                            if (branchError == null) {

                                mShareLink = shortLink;
                                mShareLinkTextView.setText(mShareLink);
                            } else {
                                ExceptionTracker.track(branchError.getMessage());
                            }
                        }
                    });
        } else {
            Log.d(TAG, "No Private Code!!");
            handleError("", -1);
        }
    }

    private void handleError(String msg, int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            if (!TextUtils.isEmpty(msg)) {
                CustomSnackBar.make(getView(), msg, CustomSnackBar.DURATION_LONG).show();

            } else {
                switch (status) {
                    case Constants.DataStatus.NO_INTERNET:
                        CustomSnackBar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, CustomSnackBar.DURATION_LONG).show();
                        break;

                    default:
                        CustomSnackBar.make(getView(), Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_LONG).show();
                        break;
                }
            }
        }
    }

    public void setInplayContestDto(InPlayContestDto inplayContestDto) {
        this.mInplayContestDto = inplayContestDto;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.private_contest_share_btn:
                onShareButtonClicked();
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.IN_PLAY_GAMES, Constants.AnalyticsClickLabels.SHARE_PRIVATE_CONTEST_CODE);
                break;
        }
    }

    private void onShareButtonClicked() {
        if (mInplayContestDto != null && !TextUtils.isEmpty(mShareLink)) {
            String pvtContestName = (mInplayContestDto.getContestName().length() > 20) ?
                    mInplayContestDto.getContestName().substring(0, 20) + "... " :
                    mInplayContestDto.getContestName();

            String socialMsg = String.format("Think you know sports better than me? Beat me at my own game and prove it!\n" +
                            "Join my Private Group to play the ₹%1$s %2$s - %3$s contest with me on Nostra Pro - The Live Sports Predictions game to win ₹%4$s\n" +
                            "by clicking on the %5$s\n" +
                            "or by using the following code %6$s",
                    mInplayContestDto.getEntryFee(), mInplayContestDto.getChallengeName(), pvtContestName,
                    (long)mInplayContestDto.getWinningAmount(),
                    mShareLink,
                    mInplayContestDto.getPrivateCode());

            AppSnippet.doGeneralShare(getContext().getApplicationContext(), socialMsg);
        }
    }
}
