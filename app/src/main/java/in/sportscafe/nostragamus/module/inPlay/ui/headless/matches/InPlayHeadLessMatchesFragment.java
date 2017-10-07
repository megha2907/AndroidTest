package in.sportscafe.nostragamus.module.inPlay.ui.headless.matches;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.ui.ContestsActivity;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayHeadLessMatchAdapterListener;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayHeadLessMatchesAdapter;
import in.sportscafe.nostragamus.module.inPlay.adapter.MatchesAdapterAction;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayMatchesDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponse;
import in.sportscafe.nostragamus.module.inPlay.ui.ResultsScreenDataDto;
import in.sportscafe.nostragamus.module.inPlay.ui.headless.dto.HeadLessMatchScreenData;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.PredictionActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;
import in.sportscafe.nostragamus.utils.AlertsHelper;

public class InPlayHeadLessMatchesFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = InPlayHeadLessMatchesFragment.class.getSimpleName();

    private InPlayHeadLessMatchFragmentListener mInPlayHeadLessMatchFragmentListener;
    private RecyclerView mMatchesRecyclerView;
    private InPlayHeadLessMatchesAdapter mMatchesAdapter;
    private HeadLessMatchScreenData mHeadLessMatchScreenData;
    private TextView mTimerTextView;
    private TextView mMatchesLeftTextView;

    public InPlayHeadLessMatchesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InPlayHeadLessMatchFragmentListener) {
            mInPlayHeadLessMatchFragmentListener = (InPlayHeadLessMatchFragmentListener) context;
        } else {
            throw new RuntimeException(TAG + " Activity must implement " +
                    InPlayHeadLessMatchFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inplay_headless_matches, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        Button joinContestBtn = (Button) rootView.findViewById(R.id.new_challenge_matches_join_button);
        mMatchesLeftTextView = (TextView) rootView.findViewById(R.id.matches_timeline_matches_left);
        mTimerTextView = (TextView) rootView.findViewById(R.id.matches_timeline_match_expires_in);

        mMatchesRecyclerView = (RecyclerView)rootView.findViewById(R.id.match_timeline_rv);
        mMatchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mMatchesRecyclerView.setHasFixedSize(true);

        joinContestBtn.setOnClickListener(this);

    }

    private void setTimer() {
        if (mHeadLessMatchScreenData != null && !TextUtils.isEmpty(mHeadLessMatchScreenData.getStartTime())) {
            CountDownTimer countDownTimer = new CountDownTimer(TimerHelper.getCountDownFutureTime(mHeadLessMatchScreenData.getStartTime()), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimerTextView.setText(TimerHelper.getTimerFormatFromMillis(millisUntilFinished));
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
        String msg = String.format(Constants.Alerts.CHALLENGE_STARTED_ALERT_FOR_TIMER, mHeadLessMatchScreenData.getChallengeName());
        AlertsHelper.showAlert(getContext(), "Challenge Started!", msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mInPlayHeadLessMatchFragmentListener != null) {
                    mInPlayHeadLessMatchFragmentListener.onBackClicked();
                }
            }
        });
    }

    @NonNull
    private InPlayHeadLessMatchAdapterListener getMatchAdapterListener() {
        return new InPlayHeadLessMatchAdapterListener() {

            @Override
            public void onMatchActionClicked(int actionType, Bundle args) {
                switch (actionType) {
                    case MatchesAdapterAction.COMING_UP:
                        /* Not possible as headless state is only created only match is played  */
                        break;

                    case MatchesAdapterAction.PLAY:
                        /* Playing new Match from headless state can not be allowed */
                        break;

                    case MatchesAdapterAction.CONTINUE:
                        launchPlayScreen(args);
                        break;

                    case MatchesAdapterAction.ANSWER:
                        launchResultsScreen(args);
                        break;

                    case MatchesAdapterAction.DID_NOT_PLAY:
                    case MatchesAdapterAction.POINTS:
                        /* As points are possible only after results announce, since user did not join contest and into headless state,
                         * This scenario not going to happen in this case, as challenge/headless state will be removed from inplay */
                        break;
                }
            }
        };
    }

    private void launchPlayScreen(Bundle matchArgs) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing() &&
                mHeadLessMatchScreenData != null) {

            Bundle bundle = new Bundle();
            if (matchArgs != null && matchArgs.containsKey(Constants.BundleKeys.INPLAY_MATCH)) {
                InPlayMatch match = Parcels.unwrap(matchArgs.getParcelable(Constants.BundleKeys.INPLAY_MATCH));
                PlayScreenDataDto playData = getPlayScreenData(match);

                if (playData != null) {
                    bundle.putParcelable(Constants.BundleKeys.PLAY_SCREEN_DATA, Parcels.wrap(playData));
                    bundle.putBoolean(Constants.BundleKeys.IS_HEADLESS_FLOW, true);

                    Intent predictionIntent = new Intent(getActivity(), PredictionActivity.class);
                    predictionIntent.putExtras(bundle);
                    predictionIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT,
                            PredictionActivity.LaunchedFrom.IN_PLAY_SCREEN_PLAY_MATCH);
                    startActivity(predictionIntent);

                } else {
                    handleError(-1);
                }
            } else {
                com.jeeva.android.Log.e(TAG, "No Contest in Bundle to launch Play screen");
                handleError(-1);
            }
        }
    }

    private PlayScreenDataDto getPlayScreenData(InPlayMatch inPlayMatch) {
        PlayScreenDataDto dataDto = null;

        if (inPlayMatch != null && mHeadLessMatchScreenData != null) {
            dataDto = new PlayScreenDataDto();

            dataDto.setChallengeId(mHeadLessMatchScreenData.getChallengeId());
            dataDto.setMatchId(inPlayMatch.getMatchId());
            dataDto.setRoomId(mHeadLessMatchScreenData.getRoomId());
            dataDto.setPowerUp(mHeadLessMatchScreenData.getPowerUp());
            dataDto.setSubTitle(mHeadLessMatchScreenData.getContestName());
            dataDto.setChallengeName(mHeadLessMatchScreenData.getChallengeName());

            if (inPlayMatch.getMatchParties() != null && inPlayMatch.getMatchParties().size() == 2) {
                dataDto.setMatchPartyTitle1(inPlayMatch.getMatchParties().get(0).getPartyName());
                dataDto.setMatchPartyTitle2(inPlayMatch.getMatchParties().get(1).getPartyName());
            }
        }

        return dataDto;
    }

    private void launchResultsScreen(Bundle args) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            ResultsScreenDataDto data = null;

            if (args != null && args.containsKey(Constants.BundleKeys.INPLAY_MATCH)) {
                InPlayMatch match = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.INPLAY_MATCH));

                if (match != null && mHeadLessMatchScreenData != null) {
                    data = new ResultsScreenDataDto();
                    data.setMatchId(match.getMatchId());
                    data.setChallengeId(match.getChallengeId());
                    data.setRoomId(mHeadLessMatchScreenData.getRoomId());
                    data.setChallengeName(mHeadLessMatchScreenData.getChallengeName());
                    data.setMatchStatus(match.getMatchStatus());

                    if (match.getMatchParties() != null && match.getMatchParties().size() == 2) {
                        data.setMatchPartyTitle1(match.getMatchParties().get(0).getPartyName());
                        data.setMatchPartyTitle2(match.getMatchParties().get(1).getPartyName());
                    }
                }
            }

            if (data != null) {
                args.putParcelable(Constants.BundleKeys.RESULTS_SCREEN_DATA, Parcels.wrap(data));

                Intent intent = new Intent(getActivity(), MyResultsActivity.class);
                intent.putExtras(args);
                getActivity().startActivity(intent);
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initMembers();
        setValues();
        setTimer();
        loadDataFromServer();
    }

    private void setValues() {
        if (mHeadLessMatchScreenData != null) {
            if (mHeadLessMatchScreenData.getTotalMatches() > 0) {
                mMatchesLeftTextView.setText(mHeadLessMatchScreenData.getMatchesLeft() + "/" + mHeadLessMatchScreenData.getTotalMatches());
            }
        }
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BundleKeys.HEADLESS_MATCH_SCREEN_DATA)) {
            mHeadLessMatchScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.HEADLESS_MATCH_SCREEN_DATA));
        }
    }

    private void loadDataFromServer() {
        if (mHeadLessMatchScreenData != null) {
            InPlayMatchesDataProvider inPlayMatchesDataProvider = new InPlayMatchesDataProvider();
            inPlayMatchesDataProvider.getInPlayMatches(mHeadLessMatchScreenData.getRoomId(),
                    mHeadLessMatchScreenData.getChallengeId(),
                    new InPlayMatchesDataProvider.InPlayMatchesDataProviderListener() {
                        @Override
                        public void onData(int status, @Nullable InPlayMatchesResponse responses) {
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
        if (getView() != null) {
            switch (status) {
                default:
                    Snackbar.make(getView(), Constants.Alerts.SOMETHING_WRONG, Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private void onSuccessMatchResponse(InPlayMatchesResponse responses) {
        if (mMatchesRecyclerView != null && responses != null &&
                responses.getData() != null && responses.getData().getInPlayMatchList() != null) {

            mMatchesAdapter = new InPlayHeadLessMatchesAdapter(responses.getData().getInPlayMatchList(), getMatchAdapterListener());
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
        if (mHeadLessMatchScreenData != null && mInPlayHeadLessMatchFragmentListener != null) {
            ContestScreenData screenData = new ContestScreenData();
            screenData.setChallengeId(mHeadLessMatchScreenData.getChallengeId());
            screenData.setChallengeName(mHeadLessMatchScreenData.getChallengeName());
            screenData.setHeadLessFlow(true);
            screenData.setPseudoRoomId(mHeadLessMatchScreenData.getRoomId());   // For Headless contest to join, pass room Id as pseudoRoomId

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.CONTEST_SCREEN_DATA, Parcels.wrap(screenData));
            args.putInt(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT, ContestsActivity.LaunchedFrom.IN_PLAY_HEAD_LESS_MATCHES);

            mInPlayHeadLessMatchFragmentListener.launchContestActivity(ContestsActivity.LaunchedFrom.IN_PLAY_HEAD_LESS_MATCHES, args);
        }
    }

}
