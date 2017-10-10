package in.sportscafe.nostragamus.module.challengeCompleted.ui.viewPager;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedContestDto;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedContestMatchDto;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.customViews.TimelineHelper;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayMatchAdapterListener;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayMatchesRecyclerAdapter;
import in.sportscafe.nostragamus.module.inPlay.adapter.MatchesAdapterAction;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayMatchesDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponse;
import in.sportscafe.nostragamus.module.inPlay.ui.ResultsScreenDataDto;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedMatchTimelineViewPagerFragment extends NostraBaseFragment {

    private static final String TAG = CompletedMatchTimelineViewPagerFragment.class.getSimpleName();
    private RecyclerView mMatchRecyclerView;
    private CompletedContestDto mCompletedContestDto;

    public CompletedMatchTimelineViewPagerFragment() {}

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
            if (args.containsKey(Constants.BundleKeys.COMPLETED_CONTEST)) {
                mCompletedContestDto = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.COMPLETED_CONTEST));
                loadData(mCompletedContestDto);
                }
            }
    }

    private void loadData(CompletedContestDto completedContestDto) {
        if (completedContestDto != null) {
            showLoadingContent();
            InPlayMatchesDataProvider dataProvider = new InPlayMatchesDataProvider();
            dataProvider.getInPlayMatches(completedContestDto.getRoomId(),
                    completedContestDto.getChallengeId(),
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
            setCompletedMatchesTimeLine(responses);

            InPlayMatchesRecyclerAdapter adapter = new InPlayMatchesRecyclerAdapter(
                    responses.getData().getInPlayMatchList(), getMatchesAdapterListener());
            mMatchRecyclerView.setAdapter(adapter);

        }
    }

    private void setCompletedMatchesTimeLine(InPlayMatchesResponse responses) {

        if (mCompletedContestDto != null && responses != null &&
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

            parent.removeAllViews();
            titleParent.removeAllViews();

             /* Timeline */
            int totalMatches = mCompletedContestDto.getMatches().size();
            for (int temp = 0; temp < totalMatches; temp++) {
                CompletedContestMatchDto match = mCompletedContestDto.getMatches().get(temp);

                boolean isNodeLineRequired = true;
                if (temp == 0){
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
                TimelineHelper.addNode(parent, match.getStatus(), isPlayed,
                        isNodeLineRequired, TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_MATCHES_SCREEN, mCompletedContestDto.getMatches().size());

                    /* Title */
                TimelineHelper.addTextNode(titleParent, "Game " + (temp + 1), mCompletedContestDto.getMatches().size(),
                        match.getStatus(), TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_MATCHES_SCREEN, isPlayed);

            }
        }
    }

    private void showOrHidePowerUps(PowerUp powerUp) {

        LinearLayout powerUpLayout = (LinearLayout)getView().findViewById(R.id.powerup_top_layout);
        ImageView powerUp2xImageView = (ImageView)getView(). findViewById(R.id.in_play_2x_iv);
        ImageView powerUpNoNegativeImageView = (ImageView) getView().findViewById(R.id.in_play_no_neg_iv);
        ImageView powerUpAudienceImageView = (ImageView) getView().findViewById(R.id.in_play_player_poll_iv);

        TextView powerUp2xTextView = (TextView) getView().findViewById(R.id.in_play_2x_count_tv);
        TextView powerUpNoNegativeTextView = (TextView) getView().findViewById(R.id.in_play_no_neg_count_tv);
        TextView powerUpAudienceTextView = (TextView) getView().findViewById(R.id.in_play_player_poll_count_tv);

        if (powerUp!=null) {

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
                if (!match.isMatchCompleted()) {
                    gameLeft++;
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

                switch (action) {
                    case MatchesAdapterAction.ANSWER:
                        launchResultsScreen(args);
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
            if (argument != null && argument.containsKey(Constants.BundleKeys.COMPLETED_CONTEST) &&
                    matchArgs != null && matchArgs.containsKey(Constants.BundleKeys.INPLAY_MATCH)) {

                InPlayMatch match = Parcels.unwrap(matchArgs.getParcelable(Constants.BundleKeys.INPLAY_MATCH));
                CompletedContestDto contestDto = Parcels.unwrap(argument.getParcelable(Constants.BundleKeys.COMPLETED_CONTEST));

                ResultsScreenDataDto resultsScreenData = getResultsScreenData(match, contestDto);
                if (resultsScreenData != null) {
                    bundle.putParcelable(Constants.BundleKeys.RESULTS_SCREEN_DATA, Parcels.wrap(resultsScreenData));
                    bundle.putParcelable(Constants.BundleKeys.COMPLETED_CONTEST,Parcels.wrap(contestDto));

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

    private ResultsScreenDataDto getResultsScreenData(InPlayMatch inPlayMatch, CompletedContestDto contestDto) {
        ResultsScreenDataDto dataDto = null;

        if (inPlayMatch != null && contestDto != null) {
            dataDto = new ResultsScreenDataDto();

            dataDto.setChallengeId(contestDto.getChallengeId());
            dataDto.setMatchId(inPlayMatch.getMatchId());
            dataDto.setRoomId(contestDto.getRoomId());
            dataDto.setSubTitle(contestDto.getContestName());
            dataDto.setChallengeName(contestDto.getChallengeName());
            dataDto.setMatchStatus(inPlayMatch.getMatchStatus());

            if (inPlayMatch.getMatchParties() != null && inPlayMatch.getMatchParties().size() == 2) {
                dataDto.setMatchPartyTitle1(inPlayMatch.getMatchParties().get(0).getPartyName());
                dataDto.setMatchPartyTitle2(inPlayMatch.getMatchParties().get(1).getPartyName());
            }
        }

        return dataDto;
    }

    private void handleError(int status) {
        switch (status) {
            case Constants.DataStatus.NO_INTERNET:
                Snackbar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, Snackbar.LENGTH_LONG).show();
                break;

            default:
                Snackbar.make(getView(), Constants.Alerts.SOMETHING_WRONG, Snackbar.LENGTH_LONG).show();
                break;
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
