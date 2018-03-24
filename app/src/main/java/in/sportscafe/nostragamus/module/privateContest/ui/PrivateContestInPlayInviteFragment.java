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
public class PrivateContestInPlayInviteFragment extends BaseFragment {

    private static final String TAG = PrivateContestInPlayInviteFragment.class.getSimpleName();

    private TextView mShareLinkTextView;
    private InPlayContestDto mInplayContestDto;

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

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createContestShareLink();
    }

    private void createContestShareLink() {
        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        if (userInfo != null && userInfo.getId() != null) {

            BranchUniversalObject branchUniversalObject = new BranchUniversalObject();
            branchUniversalObject.setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);
            branchUniversalObject.addContentMetadata(Constants.PrivateContests.BranchLink.PRIVATE_CONTEST_INVITATION_CODE, "123"/*TODO: change*/);

            LinkProperties linkProperties = new LinkProperties();
            linkProperties.addTag(Constants.PrivateContests.BranchLink.LINKED_PROPERTIES_FEATURE);
            linkProperties.setChannel(Constants.PrivateContests.BranchLink.LINKED_PROPERTIES_CHANNEL);
            linkProperties.setCampaign(Constants.PrivateContests.BranchLink.LINKED_PROPERTIES_CAMPAIGN);
            linkProperties.setFeature(Constants.PrivateContests.BranchLink.LINKED_PROPERTIES_FEATURE);

            branchUniversalObject.generateShortUrl(getContext(), linkProperties,
                    new Branch.BranchLinkCreateListener() {
                        @Override
                        public void onLinkCreate(String shortLink, BranchError branchError) {
                            if (branchError == null) {

                                mShareLinkTextView.setText(shortLink);

                            } else {
                                ExceptionTracker.track(branchError.getMessage());
                            }
                        }
                    });

        } else {
            handleError(Constants.Alerts.SOMETHING_WRONG, -1);
            Log.d(TAG, "UserInfo null! Can not create contest sharing link");
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

    public InPlayContestDto getInplayContestDto() {
        return mInplayContestDto;
    }

    public void setInplayContestDto(InPlayContestDto inplayContestDto) {
        this.mInplayContestDto = inplayContestDto;
    }
}
