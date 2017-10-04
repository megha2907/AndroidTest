package in.sportscafe.nostragamus.module.newChallenges.ui.matches;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.ui.ContestsActivity;
import in.sportscafe.nostragamus.module.inPlay.adapter.MatchesAdapterAction;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeMatchAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeMatchesAdapter;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.JoinPseudoContestApiModelImpl;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.NewChallengesMatchesDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.JoinPseudoContestResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengeMatchesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengeMatchesScreenData;
import in.sportscafe.nostragamus.module.prediction.playScreen.PredictionActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;
import in.sportscafe.nostragamus.utils.AlertsHelper;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 9/1/17.
 */

public class NewChallengesMatchesFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = NewChallengesMatchesFragment.class.getSimpleName();

    private NewChallengeMatchFragmentListener mNewChallengeMatchFragmentListener;
    private RecyclerView mMatchesRecyclerView;
    private NewChallengeMatchesAdapter mMatchesAdapter;
    private NewChallengeMatchesScreenData mScreenData;
    private TextView mContestTimerTextView;

    public NewChallengesMatchesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewChallengeMatchFragmentListener) {
            mNewChallengeMatchFragmentListener = (NewChallengeMatchFragmentListener) context;
        } else {
            throw new RuntimeException(TAG + " Activity must implement " +
                    NewChallengeMatchFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_challenge_matches, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        TextView tvContestHeading = (TextView) rootView.findViewById(R.id.matches_timeline_heading);
        TextView tvContestSubHeadingOne = (TextView) rootView.findViewById(R.id.matches_timeline_subheading_one);
        TextView tvContestSubHeadingTwo = (TextView) rootView.findViewById(R.id.newchallenge_matches_subheading_two);
        Button joinContestBtn = (Button) rootView.findViewById(R.id.new_challenge_matches_join_button);
        TextView tvMatchesLeft = (TextView) rootView.findViewById(R.id.matches_timeline_matches_left);
        mContestTimerTextView = (TextView) rootView.findViewById(R.id.matches_timeline_match_expires_in);

        mMatchesRecyclerView = (RecyclerView)rootView.findViewById(R.id.match_timeline_rv);
        mMatchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mMatchesRecyclerView.setHasFixedSize(true);

        joinContestBtn.setOnClickListener(this);
    }

    private void setTimer() {
        if (mScreenData != null && !TextUtils.isEmpty(mScreenData.getStartTime())) {
            long startTimeMs = TimeUtils.getMillisecondsFromDateString(mScreenData.getStartTime(), Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE, Constants.DateFormats.GMT);
            TimeAgo timeAgo = TimeUtils.calcTimeAgo(Nostragamus.getInstance().getServerTime(), startTimeMs);

            CountDownTimer countDownTimer = new CountDownTimer(timeAgo.totalDiff, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mContestTimerTextView.setText(TimeUtils.getDateStringFromDate(new Date(millisUntilFinished), "HH:mm:ss"));
                }

                @Override
                public void onFinish() {

                    onMatchStarted();
                }
            };
            countDownTimer.start();
        }
    }

    private void onMatchStarted() {
        // TODO
    }

    @NonNull
    private NewChallengeMatchAdapterListener getmatchAdapterListener() {
        return new NewChallengeMatchAdapterListener() {

            @Override
            public void onMatchActionClicked(int actionType, Bundle args) {
                InPlayMatch match = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.INPLAY_MATCH));

                switch (actionType) {
                    case MatchesAdapterAction.PLAY:
                        joinPseudoContest(match);
                        break;
                }
            }
        };
    }

    private void joinPseudoContest(final InPlayMatch match) {
        if (mScreenData != null) {
            JoinPseudoContestApiModelImpl joinPseudoContestApiModel = new JoinPseudoContestApiModelImpl();
            joinPseudoContestApiModel.joinPseudoContest(mScreenData.getChallengeId(),
                    new JoinPseudoContestApiModelImpl.JoinPseudoContestApiListener() {
                @Override
                public void onData(int status, @Nullable JoinPseudoContestResponse responses) {
                    launchPlayScreen(match, responses);
                }

                @Override
                public void onError(int status) {
                    Log.d(TAG, "Join pseudo contest failed");
                    AlertsHelper.showAlert(getContext(), "Error!", Constants.Alerts.SOMETHING_WRONG, null);
                }
            });
        }
    }

    private void launchPlayScreen(InPlayMatch match, JoinPseudoContestResponse response) {
        if (match != null && mScreenData != null && response != null && response.getUserRoom() != null) {
            PlayScreenDataDto playData = new PlayScreenDataDto();

            playData.setChallengeId(match.getChallengeId());
            playData.setRoomId(response.getUserRoom().getRoomId());
            playData.setMatchId(match.getMatchId());
            playData.setSubTitle(mScreenData.getChallengeName());
            playData.setPowerUp(response.getUserRoom().getPowerUp());
            playData.setPlayingPseudoGame(true);

            if (match.getMatchParties() != null && match.getMatchParties().size() == 2) {
                playData.setMatchPartyTitle1(match.getMatchParties().get(0).getPartyName());
                playData.setMatchPartyTitle2(match.getMatchParties().get(1).getPartyName());
            }

            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.BundleKeys.PLAY_SCREEN_DATA, Parcels.wrap(playData));

            Intent predictionIntent = new Intent(getActivity(), PredictionActivity.class);
            predictionIntent.putExtras(bundle);
            predictionIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT,
                    PredictionActivity.LaunchedFrom.NEW_CHALLENGES_SCREEN_PSEUDO_PLAY);
            getActivity().startActivity(predictionIntent);

        } else {
            AlertsHelper.showAlert(getContext(), "Error", Constants.Alerts.SOMETHING_WRONG, null);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initMembers();
        setTimer();
        loadDataFromServer();
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null) {
            mScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.NEW_CHALLENGE_MATCHES_SCREEN_DATA));
        }
    }

    private void loadDataFromServer() {
        if (mScreenData != null) {
            NewChallengesMatchesDataProvider dataProvider = new NewChallengesMatchesDataProvider();
            dataProvider.getNewChallengesMatches(mScreenData.getChallengeId(), new NewChallengesMatchesDataProvider.NewChallengesApiListener() {
                @Override
                public void onData(int status, @Nullable NewChallengeMatchesResponse responses) {
                    onSuccessMatchResponse(responses);
                }

                @Override
                public void onError(int status) {
                    handleError(status);
                }
            });
        }
    }

    private void handleError(int status) {
        switch (status) {
            default:
                AlertsHelper.showAlert(getContext(), "Error", Constants.Alerts.SOMETHING_WRONG, null);
                break;
        }
    }

    private void onSuccessMatchResponse(NewChallengeMatchesResponse responses) {
        if (responses != null && mMatchesRecyclerView != null && responses.getMatchList() != null) {
            mMatchesAdapter = new NewChallengeMatchesAdapter(responses.getMatchList(), getmatchAdapterListener());
            mMatchesRecyclerView.setAdapter(mMatchesAdapter);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_challenge_matches_join_button:
                onJoinContestClicked();
                break;
        }
    }

    private void onJoinContestClicked() {
        if (mScreenData != null && mNewChallengeMatchFragmentListener != null) {
            ContestScreenData screenData = new ContestScreenData();
            screenData.setChallengeId(mScreenData.getChallengeId());
            screenData.setChallengeName(mScreenData.getChallengeName());

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.CONTEST_SCREEN_DATA, Parcels.wrap(screenData));
            args.putInt(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT, ContestsActivity.LaunchedFrom.NEW_CHALLENGE_MATCHES);
            mNewChallengeMatchFragmentListener.launchContestActivity(ContestsActivity.LaunchedFrom.NEW_CHALLENGE_MATCHES, args);
        }
    }

}
