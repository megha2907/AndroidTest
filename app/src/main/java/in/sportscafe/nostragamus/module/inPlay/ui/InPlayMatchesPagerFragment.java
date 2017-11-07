package in.sportscafe.nostragamus.module.inPlay.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.customViews.TimelineHelper;
import in.sportscafe.nostragamus.module.inPlay.adapter.MatchesAdapterAction;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayMatchAdapterListener;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayMatchesRecyclerAdapter;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayMatchesDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestMatchDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponse;
import in.sportscafe.nostragamus.module.navigation.help.dummygame.DummyGameActivity;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.popups.timerPopup.TimerFinishDialogHelper;
import in.sportscafe.nostragamus.module.prediction.playScreen.PredictionActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class InPlayMatchesPagerFragment extends NostraBaseFragment {

    private static final String TAG = InPlayMatchesPagerFragment.class.getSimpleName();
    private RecyclerView mMatchRecyclerView;

    private InPlayContestDto mInPlayContestDto;

    public InPlayMatchesPagerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_in_play_match, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        mMatchRecyclerView = (RecyclerView) rootView.findViewById(R.id.inPlay_matches_recyclerView);
        mMatchRecyclerView.setLayoutManager(new LinearLayoutManager(mMatchRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        mMatchRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getContestFromArgs();
    }

    private void getContestFromArgs() {
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.INPLAY_CONTEST)) {
                mInPlayContestDto = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.INPLAY_CONTEST));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (mInPlayContestDto != null) {
            showLoadingContent();
            InPlayMatchesDataProvider dataProvider = new InPlayMatchesDataProvider();

            dataProvider.getInPlayMatches(mInPlayContestDto.getRoomId(),
                    mInPlayContestDto.getChallengeId(),
                    new InPlayMatchesDataProvider.InPlayMatchesDataProviderListener() {
                        @Override
                        public void onData(int status, @Nullable InPlayMatchesResponse responses) {
                            hideLoadingContent();

                            switch (status) {
                                case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
                                    onDataResponse(responses);
                                    break;

                                default:
                                    handleError(status);
                                    break;
                            }
                        }

                        @Override
                        public void onError(int status) {
                            hideLoadingContent();
                            handleError(status);
                        }
                    });
        }
    }

    private void onDataResponse(InPlayMatchesResponse responses) {
        if (mMatchRecyclerView != null && responses != null && responses.getData() != null &&
                responses.getData().getInPlayMatchList() != null) {
            setInPlayMatchesTimeLine(responses);

            InPlayMatchesRecyclerAdapter adapter = new InPlayMatchesRecyclerAdapter(
                    responses.getData().getInPlayMatchList(), getMatchesAdapterListener());
            mMatchRecyclerView.setAdapter(adapter);

        }
    }

    private void setInPlayMatchesTimeLine(InPlayMatchesResponse responses) {

        if (mInPlayContestDto != null && responses != null &&
                responses.getData() != null && responses.getData().getInPlayMatchList() != null
                && getView() != null) {

            /* Set Games Left */
            String gamesLeftStr = getGamesLeftCount(responses.getData().getInPlayMatchList()) + "/" + responses.getData().getInPlayMatchList().size();
            TextView gamesLeftTextView = (TextView) getView().findViewById(R.id.inplay_match_timeline_games_left_textview);
            TextView gamesLeftTextViewText = (TextView) getView().findViewById(R.id.inplay_match_timeline_games_left);
            gamesLeftTextView.setText(gamesLeftStr);
            gamesLeftTextViewText.setText("GAMES LEFT  ");


            /* Set Powerups */
            showOrHidePowerUps(responses.getData().getPowerUp());

            /* Timeline */
            LinearLayout parent = (LinearLayout) getView().findViewById(R.id.match_status_timeline);
            LinearLayout titleParent = (LinearLayout) getView().findViewById(R.id.match_status_timeline_title_parent);
            LinearLayout matchTimelineLayout = (LinearLayout) getView().findViewById(R.id.match_timeline_layout);

            parent.removeAllViews();
            titleParent.removeAllViews();

            if (mInPlayContestDto.getMatches() != null){

             /* Timeline */
                int totalMatches = mInPlayContestDto.getMatches().size();
                for (int temp = 0; temp < totalMatches; temp++) {
                    InPlayContestMatchDto match = mInPlayContestDto.getMatches().get(temp);

                    boolean isNodeLineRequired = true;
                    if (temp == 0) {
                        isNodeLineRequired = false;
                    }

                    int matchAttemptedStatus = match.isPlayed();
                    boolean isPlayed;
                    if (Constants.GameAttemptedStatus.COMPLETELY == matchAttemptedStatus) {
                        isPlayed = true;
                    } else if (Constants.GameAttemptedStatus.PARTIALLY == matchAttemptedStatus) {
                        isPlayed = false;
                    } else {
                        isPlayed = false;
                    }

                    /* Content */
                    TimelineHelper.addNode(parent, match.getStatus(), matchAttemptedStatus, isPlayed,
                            isNodeLineRequired, TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_MATCHES_SCREEN,
                            mInPlayContestDto.getMatches().size());

                    /* Title */
                    TimelineHelper.addTextNode(titleParent, "Game " + (temp + 1), mInPlayContestDto.getMatches().size(),
                            match.getStatus(), TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_MATCHES_SCREEN, isPlayed, matchAttemptedStatus);

                }
            }else {
                matchTimelineLayout.setVisibility(View.GONE);
            }
        }
    }

    private void showOrHidePowerUps(PowerUp powerUp) {

        LinearLayout powerUpLayout = (LinearLayout) getView().findViewById(R.id.powerup_top_layout);
        ImageView powerUp2xImageView = (ImageView) getView().findViewById(R.id.in_play_2x_iv);
        ImageView powerUpNoNegativeImageView = (ImageView) getView().findViewById(R.id.in_play_no_neg_iv);
        ImageView powerUpAudienceImageView = (ImageView) getView().findViewById(R.id.in_play_player_poll_iv);

        TextView powerUp2xTextView = (TextView) getView().findViewById(R.id.in_play_2x_count_tv);
        TextView powerUpNoNegativeTextView = (TextView) getView().findViewById(R.id.in_play_no_neg_count_tv);
        TextView powerUpAudienceTextView = (TextView) getView().findViewById(R.id.in_play_player_poll_count_tv);

        if (powerUp != null) {

            int powerUp2xCount = powerUp.getDoubler();
            int powerUpNonNegsCount = powerUp.getNoNegative();
            int powerUpPlayerPollCount = powerUp.getPlayerPoll();

            if (powerUp2xCount == 0 && powerUpNonNegsCount == 0 && powerUpPlayerPollCount == 0) {
                powerUpLayout.setVisibility(View.GONE);
            } else {
                powerUpLayout.setVisibility(View.VISIBLE);

                powerUp2xImageView.setBackgroundResource(R.drawable.double_powerup_small);
                powerUp2xImageView.setVisibility(View.VISIBLE);
                powerUp2xTextView.setText(String.valueOf(powerUp2xCount));

                powerUpNoNegativeImageView.setBackgroundResource(R.drawable.no_negative_powerup_small);
                powerUpNoNegativeImageView.setVisibility(View.VISIBLE);
                powerUpNoNegativeTextView.setText(String.valueOf(powerUpNonNegsCount));

                powerUpAudienceImageView.setBackgroundResource(R.drawable.audience_poll_powerup_small);
                powerUpAudienceImageView.setVisibility(View.VISIBLE);
                powerUpAudienceTextView.setText(String.valueOf(powerUpPlayerPollCount));

            }
        }
    }

    private String getDateTimeValue(String startTime) {
        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                startTime,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        int dayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(startTimeMs, "d"));
        return dayOfMonth + AppSnippet.ordinalOnly(dayOfMonth) + " " +
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM") + ", "
                + TimeUtils.getDateStringFromMs(startTimeMs, Constants.DateFormats.HH_MM_AA).replace("AM", "am").replace("PM", "pm");
    }

    private int getGamesLeftCount(List<InPlayMatch> matches) {
        int gameLeft = 0;
        if (matches != null && matches.size() > 0) {
            for (InPlayMatch match : matches) {
                if (!DateTimeHelper.isMatchStarted(match.getMatchStartTime())) {
                    if (!TextUtils.isEmpty(match.getMatchStatus())) {
                        if (match.getMatchStatus().equalsIgnoreCase(Constants.MatchStatusStrings.CONTINUE) ||
                                match.getMatchStatus().equalsIgnoreCase(Constants.MatchStatusStrings.PLAY) ||
                                match.getMatchStatus().equalsIgnoreCase(Constants.MatchStatusStrings.COMING_UP) ||
                                match.getMatchStatus().equalsIgnoreCase(Constants.MatchStatusStrings.ANSWER)) {

                            gameLeft++;
                        }
                    }
                }
            }
        }
        return gameLeft;
    }

    @NonNull
    private InPlayMatchAdapterListener getMatchesAdapterListener() {
        return new InPlayMatchAdapterListener() {
            @Override
            public void onMatchClicked(Bundle args) {
                // Same Action (onMatchActionClicked) is taken on match click also
            }

            @Override
            public void onMatchActionClicked(int action, Bundle args) {
                Log.d(TAG, "action button clicked : " + action);

                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.IN_PLAY_GAMES, String.valueOf(action));

                switch (action) {
                    case MatchesAdapterAction.COMING_UP:
                        /* Disabled - No action */
                        break;

                    case MatchesAdapterAction.PLAY:
                    case MatchesAdapterAction.CONTINUE:
                        launchPlayScreen(args);
                        break;

                    case MatchesAdapterAction.ANSWER:
                    case MatchesAdapterAction.DID_NOT_PLAY:
                    case MatchesAdapterAction.POINTS:
                        launchResultsScreen(args);
                        break;
                }

            }
        };
    }

    private void launchResultsScreen(Bundle matchArgs) {

        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {

            Bundle argument = getArguments();
            Bundle bundle = new Bundle();
            if (argument != null && argument.containsKey(Constants.BundleKeys.INPLAY_CONTEST) &&
                    matchArgs != null && matchArgs.containsKey(Constants.BundleKeys.INPLAY_MATCH)) {

                InPlayMatch match = Parcels.unwrap(matchArgs.getParcelable(Constants.BundleKeys.INPLAY_MATCH));
                InPlayContestDto contestDto = Parcels.unwrap(argument.getParcelable(Constants.BundleKeys.INPLAY_CONTEST));

                ResultsScreenDataDto resultsScreenData = getResultsScreenData(match, contestDto);
                if (resultsScreenData != null) {
                    bundle.putParcelable(Constants.BundleKeys.RESULTS_SCREEN_DATA, Parcels.wrap(resultsScreenData));
                    bundle.putParcelable(Constants.BundleKeys.INPLAY_CONTEST, Parcels.wrap(contestDto));
                    Intent resultsIntent = new Intent(getActivity(), MyResultsActivity.class);
                    resultsIntent.putExtras(bundle);
                    resultsIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT,
                            MyResultsActivity.LaunchedFrom.IN_PLAY_SCREEN_MATCH_AWAITING_RESULTS);
                    getActivity().startActivity(resultsIntent);

                } else {
                    handleError(-1);
                }
            } else {
                Log.e(TAG, "No Contest in Bundle to launch Results screen");
                handleError(-1);

            }
        }
    }

    private void launchPlayScreen(Bundle matchArgs) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {

            Bundle argument = getArguments();
            Bundle bundle = new Bundle();
            if (argument != null && argument.containsKey(Constants.BundleKeys.INPLAY_CONTEST) &&
                    matchArgs != null && matchArgs.containsKey(Constants.BundleKeys.INPLAY_MATCH)) {

                InPlayMatch match = Parcels.unwrap(matchArgs.getParcelable(Constants.BundleKeys.INPLAY_MATCH));
                InPlayContestDto contestDto = Parcels.unwrap(argument.getParcelable(Constants.BundleKeys.INPLAY_CONTEST));

                if (TimerFinishDialogHelper.canPlayGame(match.getMatchStartTime())) {

                    PlayScreenDataDto playData = getPlayScreenData(match, contestDto);
                    if (playData != null) {
                        bundle.putParcelable(Constants.BundleKeys.PLAY_SCREEN_DATA, Parcels.wrap(playData));

                        /* If User has not Played first match yet, start dummy Game */
                        if (NostragamusDataHandler.getInstance().isPlayedFirstMatch()) {
                            startPredictionActivity(bundle);
                        } else {
                            startDummyGameActivity(bundle);
                        }
                    }

                } else {
                    TimerFinishDialogHelper.showCanNotPlayGameTimerOutDialog(getChildFragmentManager());
                }

            } else {
                Log.e(TAG, "No Contest in Bundle to launch Play screen");
                handleError(-1);
            }
        }
    }

    private void startPredictionActivity(Bundle bundle) {
        Intent predictionIntent = new Intent(getActivity(), PredictionActivity.class);
        predictionIntent.putExtras(bundle);
        predictionIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT,
                PredictionActivity.LaunchedFrom.IN_PLAY_SCREEN_PLAY_MATCH);
        startActivity(predictionIntent);
    }

    private void startDummyGameActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), DummyGameActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

        /*  Set first match played param */
        NostragamusDataHandler.getInstance().setPlayedFirstMatch(true);
    }

    private PlayScreenDataDto getPlayScreenData(InPlayMatch inPlayMatch, InPlayContestDto contestDto) {
        PlayScreenDataDto dataDto = null;

        if (inPlayMatch != null && contestDto != null) {
            dataDto = new PlayScreenDataDto();

            dataDto.setChallengeId(contestDto.getChallengeId());
            dataDto.setMatchId(inPlayMatch.getMatchId());
            dataDto.setRoomId(contestDto.getRoomId());
            dataDto.setPowerUp(contestDto.getPowerUp());
            dataDto.setMatchStatus(inPlayMatch.getMatchStatus());
            dataDto.setChallengeName(contestDto.getChallengeName());
            dataDto.setChallengeStartTime(contestDto.getChallengeStartTime());
            dataDto.setInPlayContestDto(contestDto);

            String subTitle = "";
            if (!TextUtils.isEmpty(contestDto.getChallengeName())) {
                subTitle = contestDto.getChallengeName();
            }
            if (!TextUtils.isEmpty(contestDto.getContestName())) {
                subTitle = subTitle + " - " + contestDto.getContestName();
            }
            dataDto.setSubTitle(subTitle);

            if (inPlayMatch.getMatchParties() != null && inPlayMatch.getMatchParties().size() == 2) {
                dataDto.setMatchPartyTitle1(inPlayMatch.getMatchParties().get(0).getPartyName());
                dataDto.setMatchPartyTitle2(inPlayMatch.getMatchParties().get(1).getPartyName());
            }
        }

        return dataDto;
    }

    private ResultsScreenDataDto getResultsScreenData(InPlayMatch inPlayMatch, InPlayContestDto contestDto) {
        ResultsScreenDataDto dataDto = null;

        if (inPlayMatch != null && contestDto != null) {
            dataDto = new ResultsScreenDataDto();

            dataDto.setChallengeId(contestDto.getChallengeId());
            dataDto.setMatchId(inPlayMatch.getMatchId());
            dataDto.setRoomId(contestDto.getRoomId());
            dataDto.setSubTitle(contestDto.getContestName());
            dataDto.setChallengeName(contestDto.getChallengeName());
            dataDto.setMatchStatus(inPlayMatch.getMatchStatus());
            dataDto.setChallengeStartTime(contestDto.getChallengeStartTime());

            if (inPlayMatch.getMatchParties() != null && inPlayMatch.getMatchParties().size() == 2) {
                dataDto.setMatchPartyTitle1(inPlayMatch.getMatchParties().get(0).getPartyName());
                dataDto.setMatchPartyTitle2(inPlayMatch.getMatchParties().get(1).getPartyName());
            }
        }

        return dataDto;
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

    private void showLoadingContent() {
        if (getView() != null && !getActivity().isFinishing()) {
            getView().findViewById(R.id.inplay_matches_content_View).setVisibility(View.GONE);
            getView().findViewById(R.id.inplay_matches_loading_View).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingContent() {
        if (getView() != null && !getActivity().isFinishing()) {
            getView().findViewById(R.id.inplay_matches_loading_View).setVisibility(View.GONE);
            getView().findViewById(R.id.inplay_matches_content_View).setVisibility(View.VISIBLE);
        }
    }

}
