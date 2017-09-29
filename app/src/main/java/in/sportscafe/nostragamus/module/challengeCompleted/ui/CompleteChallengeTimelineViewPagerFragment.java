package in.sportscafe.nostragamus.module.challengeCompleted.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeCompleted.adapter.CompletedChallengeMatchAdapterListener;
import in.sportscafe.nostragamus.module.challengeCompleted.adapter.CompletedChallengeMatchesRecyclerAdapter;
import in.sportscafe.nostragamus.module.challengeCompleted.dataProvider.CompletedChallengeMatchesDataProvider;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedContestDto;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedMatch;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedMatchesResponse;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.customViews.TimelineHelper;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayMatchAdapterListener;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayMatchesRecyclerAdapter;
import in.sportscafe.nostragamus.module.inPlay.adapter.MatchesAdapterAction;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayMatchesDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponse;
import in.sportscafe.nostragamus.module.inPlay.ui.InPlayMatchTimelineViewPagerFragment;
import in.sportscafe.nostragamus.module.prediction.playScreen.PredictionActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;
import in.sportscafe.nostragamus.utils.AlertsHelper;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompleteChallengeTimelineViewPagerFragment extends NostraBaseFragment {

    private static final String TAG = CompleteChallengeTimelineViewPagerFragment.class.getSimpleName();
    private RecyclerView mMatchRecyclerView;
    private InPlayContestDto mInPlayContest;

    public CompleteChallengeTimelineViewPagerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_in_play_match_timeline, container, false);
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
        loadData();
    }

    private void getContestFromArgs() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BundleKeys.INPLAY_CONTEST)) {
            mInPlayContest = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.INPLAY_CONTEST));
        }
    }

    private void loadData() {
        if (mInPlayContest != null) {
            showLoadingContent();
           CompletedChallengeMatchesDataProvider dataProvider = new CompletedChallengeMatchesDataProvider();
            dataProvider.getCompletedChallengeMatches(mInPlayContest.getRoomId(),
                    /*mInPlayContest.getChallengeId()*/ 482 /* TODO: remove hardcoded value */,
                    new CompletedChallengeMatchesDataProvider.CompletedChallengeMatchesDataProviderListener() {
                        @Override
                        public void onData(int status, @Nullable CompletedMatchesResponse responses) {
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

    private void onDataResponse(CompletedMatchesResponse responses) {
        if (mMatchRecyclerView != null && responses != null && responses.getData() != null &&
                responses.getData().getCompletedMatchList() != null) {
            setMatchesTimeLine(responses);

            CompletedChallengeMatchesRecyclerAdapter adapter = new CompletedChallengeMatchesRecyclerAdapter(
                    responses.getData().getCompletedMatchList(), getMatchesAdapterListener());
            mMatchRecyclerView.setAdapter(adapter);

        }
    }

    private void setMatchesTimeLine(CompletedMatchesResponse responses) {
        if (responses != null && responses.getData() != null && responses.getData().getCompletedMatchList() != null && getView() != null) {
            int totalNodes = responses.getData().getCompletedMatchList().size();
            int totalLines = totalNodes - 1;

            String gamesLeftStr = getGamesLeftCount(responses.getData().getCompletedMatchList()) + "/" + responses.getData().getCompletedMatchList().size() + " GAMES LEFT";
            TextView gamesLeftTextView = (TextView) getView().findViewById(R.id.inplay_match_timeline_games_left_textView);
            gamesLeftTextView.setText(gamesLeftStr);

            /* Timeline */
            LinearLayout parent = (LinearLayout) getView().findViewById(R.id.match_status_timeline);
            LinearLayout titleParent = (LinearLayout) getView().findViewById(R.id.match_status_timeline_title_parent);
            LinearLayout bottomParent = (LinearLayout) getView().findViewById(R.id.match_status_timeline_bottom_parent);

            if (responses.getData().getCompletedMatchList().size() > 0) {
                for (int temp = 0 ; temp < responses.getData().getCompletedMatchList().size(); temp++) {

                    CompletedMatch match = responses.getData().getCompletedMatchList().get(temp);
                    boolean isNodeLineRequired = true;
                    if (temp == totalLines) {
                        isNodeLineRequired = false;
                    }
                    /* Content */
                    TimelineHelper.addNode(parent, match.getMatchStatus(), match.isPlayed(),
                            isNodeLineRequired, TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_MATCHES_SCREEN, responses.getData().getCompletedMatchList().size());

                    /* Title */
                    TimelineHelper.addTextNode(titleParent, "Game " + (temp+1), responses.getData().getCompletedMatchList().size(),
                            match.getMatchStatus(), TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_MATCHES_SCREEN,match.isPlayed());

                    /* Footer */
                    String dateTime = responses.getData().getCompletedMatchList().get(temp).getMatchStartTime();
                    TimelineHelper.addTextNode(bottomParent, getDateTimeValue(dateTime),
                            responses.getData().getCompletedMatchList().size(), match.getMatchStatus(),
                            TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_MATCHES_SCREEN,match.isPlayed());
                }
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

    private int getGamesLeftCount(List<CompletedMatch> matches) {
        int gameLeft = 0;
        if (matches != null && matches.size() > 0) {
            for (CompletedMatch match : matches) {
                if (!match.isMatchCompleted()) {
                    gameLeft++;
                }
            }
        }
        return gameLeft;
    }

    @NonNull
    private CompletedChallengeMatchAdapterListener getMatchesAdapterListener() {
        return new CompletedChallengeMatchAdapterListener() {
            @Override
            public void onMatchClicked(Bundle args) {
                // Same Action (onMatchActionClicked) is taken on match click also
            }

            @Override
            public void onMatchActionClicked(int action, Bundle args) {
                Log.d(TAG, "action button clicked : " + action);

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
                        launchPlayScreen(args);
                        break;
                }
            }
        };
    }

    private void launchResultsScreen() {

    }

    private void launchPlayScreen(Bundle matchArgs) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {

            Bundle argument = getArguments();
            Bundle bundle = new Bundle();
            if (argument != null && argument.containsKey(Constants.BundleKeys.COMPLETED_CONTEST) &&
                    matchArgs != null && matchArgs.containsKey(Constants.BundleKeys.COMPLETED_MATCH)) {

                CompletedMatch match = Parcels.unwrap(matchArgs.getParcelable(Constants.BundleKeys.COMPLETED_MATCH));
                CompletedContestDto contestDto = Parcels.unwrap(argument.getParcelable(Constants.BundleKeys.COMPLETED_CONTEST));

                PlayScreenDataDto playData = getPlayScreenData(match, contestDto);
                if (playData != null) {
                    bundle.putParcelable(Constants.BundleKeys.PLAY_SCREEN_DATA, Parcels.wrap(playData));

                    Intent predictionIntent = new Intent(getActivity(), PredictionActivity.class);
                    predictionIntent.putExtras(bundle);
                    predictionIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT,
                            PredictionActivity.LaunchedFrom.IN_PLAY_SCREEN_PLAY_MATCH);
                    getActivity().startActivity(predictionIntent);

                } else {
                    handleError(-1);
                }
            } else {
                Log.e(TAG, "No Contest in Bundle to launch Play screen");
                handleError(-1);
            }
        }
    }

    private PlayScreenDataDto getPlayScreenData(CompletedMatch completedMatch, CompletedContestDto contestDto) {
        PlayScreenDataDto dataDto = null;

        if (completedMatch != null && contestDto != null) {
            dataDto = new PlayScreenDataDto();

            dataDto.setChallengeId(contestDto.getChallengeId());
            dataDto.setMatchId(completedMatch.getMatchId());
            dataDto.setRoomId(contestDto.getRoomId());
            dataDto.setPowerUp(contestDto.getPowerUp());
            dataDto.setSubTitle(contestDto.getContestName());

            if (completedMatch.getMatchParties() != null && completedMatch.getMatchParties().size() == 2) {
                dataDto.setMatchPartyTitle1(completedMatch.getMatchParties().get(0).getPartyName());
                dataDto.setMatchPartyTitle2(completedMatch.getMatchParties().get(1).getPartyName());
            }
        }

        return dataDto;
    }

    private void handleError(int status) {
        switch (status) {
            case Constants.DataStatus.NO_INTERNET:
                AlertsHelper.showAlert(getContext(), "No Internet", "Please tern ON internet to continue", null);
                break;

            default:
                AlertsHelper.showAlert(getContext(), "Error", Constants.Alerts.SOMETHING_WRONG, null);
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
