package in.sportscafe.nostragamus.module.newChallenges.ui.matches;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.ui.ContestsActivity;
import in.sportscafe.nostragamus.module.inPlay.adapter.MatchesAdapterAction;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.navigation.help.dummygame.DummyGameActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeMatchAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeMatchesAdapter;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.JoinPseudoContestApiModelImpl;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.NewChallengesMatchesDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.JoinPseudoContestResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengeMatchesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengeMatchesScreenData;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;
import in.sportscafe.nostragamus.module.popups.timerPopup.TimerFinishDialogHelper;
import in.sportscafe.nostragamus.module.prediction.playScreen.PredictionActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;
import in.sportscafe.nostragamus.utils.AlertsHelper;

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
    private TextView mMatchesLeftTextView;
    private Button mJoinContestButton;
    private TextView mPlayContestTip;
    private TextView mTip;

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
        if (rootView!=null) {
            mJoinContestButton = (Button) rootView.findViewById(R.id.new_challenge_matches_join_button);
            mMatchesLeftTextView = (TextView) rootView.findViewById(R.id.matches_timeline_matches_left);
            mContestTimerTextView = (TextView) rootView.findViewById(R.id.matches_timeline_match_expires_in);
            ImageView backButtonImgView = (ImageView) rootView.findViewById(R.id.back_button);
            mPlayContestTip = (TextView) rootView.findViewById(R.id.games_tip_textView);
            mTip = (TextView) rootView.findViewById(R.id.games_tip_text_textView);
            mMatchesRecyclerView = (RecyclerView) rootView.findViewById(R.id.match_timeline_rv);
            mMatchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            mMatchesRecyclerView.setHasFixedSize(true);

            mJoinContestButton.setOnClickListener(this);
            backButtonImgView.setOnClickListener(this);
            rootView.findViewById(R.id.toolbar_wallet_rl).setOnClickListener(this);
        }
    }

    private void setTimer() {
        if (mScreenData != null && !TextUtils.isEmpty(mScreenData.getStartTime())) {
            long futureTime = TimerHelper.getCountDownFutureTime(mScreenData.getStartTime());

            CountDownTimer countDownTimer = new CountDownTimer(futureTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mContestTimerTextView.setText(TimerHelper.getTimerFormatFromMillis(millisUntilFinished));
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
        String msg = String.format(Constants.Alerts.CHALLENGE_STARTED_ALERT_FOR_TIMER, mScreenData.getChallengeName());
        AlertsHelper.showAlert(getContext(), "Challenge Started!", msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mNewChallengeMatchFragmentListener != null) {
                    mNewChallengeMatchFragmentListener.onBackClicked();
                }
            }
        });
    }

    @NonNull
    private NewChallengeMatchAdapterListener getMatchAdapterListener() {
        return new NewChallengeMatchAdapterListener() {

            @Override
            public void onMatchActionClicked(int actionType, Bundle args) {
                InPlayMatch match = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.INPLAY_MATCH));

                switch (actionType) {
                    case MatchesAdapterAction.PLAY:
                        joinPseudoContest(match);
                        NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NEW_CHALLENGES_GAMES, "Play");
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
                            handleError(status);
                        }
                    });
        }
    }

    private void handleError(int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            switch (status) {
                case Constants.DataStatus.NO_INTERNET:
                    Snackbar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, Snackbar.LENGTH_LONG).show();
                    break;

                default:
                    Snackbar.make(getView(), Constants.Alerts.SOMETHING_WRONG, Snackbar.LENGTH_LONG).show();
                    break;
            }
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
            playData.setChallengeName(mScreenData.getChallengeName());
            playData.setChallengeStartTime(mScreenData.getStartTime());

            if (match.getMatchParties() != null && match.getMatchParties().size() == 2) {
                playData.setMatchPartyTitle1(match.getMatchParties().get(0).getPartyName());
                playData.setMatchPartyTitle2(match.getMatchParties().get(1).getPartyName());
            }

            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.BundleKeys.PLAY_SCREEN_DATA, Parcels.wrap(playData));

            if (TimerFinishDialogHelper.canPlayGame(match.getMatchStartTime())) {

                /* If User has not Played first match yet, start dummy Game */
                if (NostragamusDataHandler.getInstance().isPlayedFirstMatch()) {
                    startPredictionActivity(bundle);
                } else {
                    startDummyGameActivity(bundle);
                }

            } else {
                TimerFinishDialogHelper.showCanNotPlayGameTimerOutDialog(getChildFragmentManager());
            }

        } else {
            handleError(-1);
        }
    }

    private void startPredictionActivity(Bundle bundle) {
        if (getActivity() != null) {
            Intent predictionIntent = new Intent(getActivity(), PredictionActivity.class);
            predictionIntent.putExtras(bundle);
            predictionIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT,
                    PredictionActivity.LaunchedFrom.NEW_CHALLENGES_SCREEN_PSEUDO_PLAY);
            startActivity(predictionIntent);
        }
    }

    private void startDummyGameActivity(Bundle bundle) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), DummyGameActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        /*  Set first match played param */
            NostragamusDataHandler.getInstance().setPlayedFirstMatch(true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initMembers();
        setValues();
        setTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataFromServer();
    }

    private void setValues() {
        View view = getView();
        if (mScreenData != null && view != null) {

            TextView titleTextView = (TextView) view.findViewById(R.id.toolbar_heading_one);
            TextView walletAmtTextView = (TextView) view.findViewById(R.id.toolbar_wallet_money);

            titleTextView.setText(mScreenData.getChallengeName());
            walletAmtTextView.setText(String.valueOf((int)WalletHelper.getTotalBalance()));

        }

    }

    private void setMatchesLeftValue(List<InPlayMatch> matches){
        if (matches!=null && !matches.isEmpty()) {
            mMatchesLeftTextView.setText(String.valueOf(matches.size()));
            mTip.setText("TIP : ");
            mPlayContestTip.setText("Play all "+String.valueOf(matches.size())+" games to have the best chance of winning prizes.");
        }
    }

    private int getGamesLeftCount(List<InPlayMatch> matches) {
        int gameLeft = 0;
        if (matches != null && matches.size() > 0) {
            for (InPlayMatch match : matches) {
                if (!DateTimeHelper.isMatchStarted(match.getMatchStartTime())) {
                    if (match.getMatchStatus().equalsIgnoreCase(Constants.MatchStatusStrings.CONTINUE) ||
                            match.getMatchStatus().equalsIgnoreCase(Constants.MatchStatusStrings.PLAY) ||
                            match.getMatchStatus().equalsIgnoreCase(Constants.MatchStatusStrings.COMING_UP) ||
                            match.getMatchStatus().equalsIgnoreCase(Constants.MatchStatusStrings.ANSWER)) {

                        gameLeft++;
                    }
                }
            }
        }
        return gameLeft;
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

    private void onSuccessMatchResponse(NewChallengeMatchesResponse responses) {
        setChallengeInfoAsPerUserPerspective(responses);
        if (responses != null && mMatchesRecyclerView != null && responses.getMatchList() != null) {
            mMatchesAdapter = new NewChallengeMatchesAdapter(responses.getMatchList(), getMatchAdapterListener());
            mMatchesRecyclerView.setAdapter(mMatchesAdapter);
            setMatchesLeftValue(responses.getMatchList());
        }
    }

    private void setChallengeInfoAsPerUserPerspective(NewChallengeMatchesResponse responses) {
        if (responses != null && getView() != null && !getActivity().isFinishing()) {
            ImageView imgView = (ImageView) getView().findViewById(R.id.games_info_icon_imgView);
            TextView infoHeadingTextView = (TextView) getView().findViewById(R.id.games_info_heading_textView);
            TextView infoMsgTextView = (TextView) getView().findViewById(R.id.games_info_msg_textView);

            if (responses.getContestJoined() > 0) {
                mJoinContestButton.setText("Join Another Contest");
                imgView.setImageResource(R.drawable.win_more);
                infoHeadingTextView.setText("Join more contests, to win more!");
                infoHeadingTextView.setText("You are playing "+ responses.getContestJoined() +" contests...");
                infoMsgTextView.setText("Continue playing these contests from the 'In Play' section. Or, you can join another contest and give yourself another chance to win!");
 
            } else if (responses.getHeadLessJoined() > 0) {
                mJoinContestButton.setText("Join Another Contest");
                imgView.setImageResource(R.drawable.play_contest_card);
                infoHeadingTextView.setText("You started a game earlier");
                infoMsgTextView.setText("Continue playing these contests from the 'In Play' section. Or, join another contest and give yourself more chances to win.");

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_challenge_matches_join_button:
                onJoinContestClicked();
                break;

            case R.id.back_button:
                onBackButtonClicked();
                break;

            case R.id.toolbar_wallet_rl:
                onWalletClicked();
                break;

        }
    }

    private void onWalletClicked() {
        if (mNewChallengeMatchFragmentListener != null) {
            mNewChallengeMatchFragmentListener.onWalletClicked();
            NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NEW_CHALLENGES_GAMES, Constants.AnalyticsClickLabels.WALLET);
        }
    }

    private void onBackButtonClicked() {
        if (mNewChallengeMatchFragmentListener != null) {
            mNewChallengeMatchFragmentListener.onBackClicked();
        }
    }

    private void onJoinContestClicked() {
        if (mScreenData != null && mNewChallengeMatchFragmentListener != null) {
            ContestScreenData screenData = new ContestScreenData();
            screenData.setChallengeId(mScreenData.getChallengeId());
            screenData.setChallengeName(mScreenData.getChallengeName());
            screenData.setChallengeStartTime(mScreenData.getStartTime());

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.CONTEST_SCREEN_DATA, Parcels.wrap(screenData));
            args.putInt(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT, ContestsActivity.LaunchedFrom.NEW_CHALLENGE_MATCHES);
            mNewChallengeMatchFragmentListener.launchContestActivity(ContestsActivity.LaunchedFrom.NEW_CHALLENGE_MATCHES, args);
            NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NEW_CHALLENGES_GAMES, Constants.AnalyticsClickLabels.JOIN_CONTEST);
        }
    }

}
