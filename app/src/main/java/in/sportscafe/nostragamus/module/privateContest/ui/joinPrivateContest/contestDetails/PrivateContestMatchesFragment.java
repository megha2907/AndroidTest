package in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.contestDetails;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.widgets.CustomProgressbar;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeMatchAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.NewChallengesMatchesDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengeMatchesResponse;
import in.sportscafe.nostragamus.module.privateContest.adapter.PrivateContestMatchesRecyclerAdapter;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.PrivateContestDetailsScreenData;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateContestMatchesFragment extends BaseFragment {

    private static final String TAG = PrivateContestMatchesFragment.class.getSimpleName();

    private PrivateContestDetailsScreenData mScreenData;
    private PrivateContestMatchesRecyclerAdapter mMatchesAdapter;

    private RecyclerView mRecyclerView;

    public PrivateContestMatchesFragment() {
    }

    public void setScreenData(PrivateContestDetailsScreenData screenData) {
        this.mScreenData = screenData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_contest_matches, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.private_contest_match_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        if (mScreenData != null && mScreenData.getPrivateContestDetailsResponse() != null
                && mScreenData.getPrivateContestDetailsResponse().getData() != null) {

            NewChallengesMatchesDataProvider dataProvider = new NewChallengesMatchesDataProvider();
            int challengeId = mScreenData.getPrivateContestDetailsResponse().getData().getChallengeId();

            CustomProgressbar.getProgressbar(getContext()).show();
            dataProvider.getNewChallengesMatches(challengeId,
                    new NewChallengesMatchesDataProvider.NewChallengesApiListener() {
                @Override
                public void onData(int status, @Nullable NewChallengeMatchesResponse responses) {
                    CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                    onSuccessMatchResponse(responses);
                }

                @Override
                public void onError(int status) {
                    CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                    handleError("", status);
                }
            });
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

    private void onSuccessMatchResponse(NewChallengeMatchesResponse responses) {
        if (!getActivity().isFinishing() && responses != null && mRecyclerView != null && responses.getMatchList() != null) {

            showTipMsg(responses.getMatchList().size());

            mMatchesAdapter = new PrivateContestMatchesRecyclerAdapter(responses.getMatchList(), getMatchAdapterListener());
            mRecyclerView.setAdapter(mMatchesAdapter);
        }
    }

    private void showTipMsg(int size) {
        if (size > 0 && getView() != null) {
            TextView tipTextView = (TextView) getView().findViewById(R.id.games_tip_textView);
            tipTextView.setText("Play all " + size + " games to have best chance of winning prizes.");
        }
    }

    @NonNull
    private NewChallengeMatchAdapterListener getMatchAdapterListener() {
        return new NewChallengeMatchAdapterListener() {

            @Override
            public void onMatchActionClicked(int actionType, Bundle args) {
                InPlayMatch match = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.INPLAY_MATCH));

                // NO action
            }
        };
    }

}
