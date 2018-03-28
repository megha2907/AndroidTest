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
                break;
        }
    }

    private void onShareButtonClicked() {
        String shareText = "Invite your friends to join " + mShareLink + " private contest!";
        AppSnippet.doGeneralShare(getContext().getApplicationContext(), shareText);
    }
}
