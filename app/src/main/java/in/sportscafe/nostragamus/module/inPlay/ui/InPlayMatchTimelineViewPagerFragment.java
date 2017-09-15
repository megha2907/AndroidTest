package in.sportscafe.nostragamus.module.inPlay.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.customViews.TimelineHelper;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayMatchAction;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayMatchAdapterListener;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayMatchesRecyclerAdapter;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayMatchesDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponse;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class InPlayMatchTimelineViewPagerFragment extends NostraBaseFragment {

    private static final String TAG = InPlayMatchTimelineViewPagerFragment.class.getSimpleName();
    private RecyclerView mMatchRecyclerView;

    public InPlayMatchTimelineViewPagerFragment() {}

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
        loadData();
    }

    private void loadData() {
        int contestId = 200; //getContestIdFromBundle();    TODO: remove hardcoded contestId

        showLoadingContent();
        InPlayMatchesDataProvider dataProvider = new InPlayMatchesDataProvider();
        dataProvider.getInPlayMatches(contestId, new InPlayMatchesDataProvider.InPlayMatchesDataProviderListener() {
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

    private int getContestIdFromBundle() {
        int contestId = -1;
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BundleKeys.INPLAY_CONTEST)) {
            InPlayContestDto inplayContest = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.INPLAY_CONTEST));
            if (inplayContest != null) {
                contestId = inplayContest.getContestId();
            }
        }
        return contestId;
    }

    private void onDataResponse(InPlayMatchesResponse responses) {
        if (mMatchRecyclerView != null && responses != null && responses.getInPlayMatchList() != null) {
            setMatchesTimeLine(responses);

            InPlayMatchesRecyclerAdapter adapter = new InPlayMatchesRecyclerAdapter(responses.getInPlayMatchList(), getMatchesAdapterListener());
            mMatchRecyclerView.setAdapter(adapter);

        }
    }

    private void setMatchesTimeLine(InPlayMatchesResponse responses) {
        if (responses != null && responses.getInPlayMatchList() != null && getView() != null) {
            int totalNodes = responses.getInPlayMatchList().size();
            int totalLines = totalNodes - 1;

            String gamesLeftStr = getGamesLeftCount(responses.getInPlayMatchList()) + "/" + responses.getInPlayMatchList().size() + " GAMES LEFT";
            TextView gamesLeftTextView = (TextView) getView().findViewById(R.id.inplay_match_timeline_games_left_textView);
            gamesLeftTextView.setText(gamesLeftStr);

            /* Timeline */
            LinearLayout parent = (LinearLayout) getView().findViewById(R.id.match_status_timeline);
            LinearLayout titleParent = (LinearLayout) getView().findViewById(R.id.match_status_timeline_title_parent);
            LinearLayout bottomParent = (LinearLayout) getView().findViewById(R.id.match_status_timeline_bottom_parent);

            if (responses.getInPlayMatchList().size() > 0) {
                for (int temp = 0 ; temp < responses.getInPlayMatchList().size(); temp++) {

                    InPlayMatch match = responses.getInPlayMatchList().get(temp);
                    boolean isNodeLineRequired = true;
                    if (temp == totalLines) {
                        isNodeLineRequired = false;
                    }
                    /* Content */
                    TimelineHelper.addNode(parent, match.isMatchCompleted(), match.isPlayed(), isNodeLineRequired);

                    /* Title */
                    TimelineHelper.addTextNode(titleParent, "Game " + (temp+1));

                    /* Footer */
                    String dateTime = responses.getInPlayMatchList().get(temp).getMatchStartTime();
                    TimelineHelper.addTextNode(bottomParent, getDateTimeValue(dateTime));
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
                    case InPlayMatchAction.COMMING_UP:
                        /* Disabled - No action */
                        break;

                    case InPlayMatchAction.PLAY:
                    case InPlayMatchAction.CONTINUE:
                        launchPlayScreen();
                        break;

                    case InPlayMatchAction.ANSWER:
                    case InPlayMatchAction.DID_NOT_PLAY:
                    case InPlayMatchAction.POINTS:
                        launchResultsScreen();
                        break;
                }
            }
        };
    }

    private void launchResultsScreen() {

    }

    private void launchPlayScreen() {

    }

    private void handleError(int status) {
        // TODO: show error content
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
