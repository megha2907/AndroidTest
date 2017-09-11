package in.sportscafe.nostragamus.module.inPlay.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayMatchAction;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayMatchAdapterListener;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayMatchesRecyclerAdapter;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayMatchesDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponse;

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
        showLoadingContent();
        InPlayMatchesDataProvider dataProvider = new InPlayMatchesDataProvider();
        dataProvider.getInPlayMatches(200, new InPlayMatchesDataProvider.InPlayMatchesDataProviderListener() {
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

            LinearLayout parent = (LinearLayout) getView().findViewById(R.id.match_status_timeline);
            if (totalNodes > 1) {
                View nodeView = null, lineView = null;
                int w = (int)getResources().getDimension(R.dimen.dim_20);
                int width = (int)getResources().getDimension(R.dimen.dim_50);
                int height = (int)getResources().getDimension(R.dimen.dim_4);

                for (int temp = 0 ; temp < responses.getInPlayMatchList().size(); temp++) {
                    InPlayMatch match = responses.getInPlayMatchList().get(temp);

                    nodeView = getNode(match.isMatchCompleted(), match.isPlayed());
                    if (nodeView != null) {
                        parent.addView(nodeView, parent.getChildCount(), new ViewGroup.LayoutParams(w, w));
                    }

                    lineView = getLineView(match.isMatchCompleted());
                    if (temp != totalLines) {
                        parent.addView(lineView, parent.getChildCount(), new ViewGroup.LayoutParams(width, height));
                    }
                }
            }
        }
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

    private View getLineView(boolean isCompleted) {
        View view = View.inflate(getContext(), R.layout.match_timeline_line_view, null);
        if (isCompleted) {
            view.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.games_status_timeline_node));
        } else {
            view.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.games_status_timeline_node));
        }
        return view;
    }

    private View getNode(boolean isCompleted, boolean played) {
        View view = View.inflate(getContext(), R.layout.match_timeline_node_view, null);
        if (isCompleted) {
            view.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.games_status_timeline_node));
        } else {
            if (played) {
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.blue_104468));
            } else {
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.yellowcolor));
            }
        }
        return view;
    }

    @NonNull
    private InPlayMatchAdapterListener getMatchesAdapterListener() {
        return new InPlayMatchAdapterListener() {
            @Override
            public void onMatchClicked(Bundle args) {
                Log.d(TAG, "MAtch clickd");
            }

            @Override
            public void onMatchActionClicked(int action, Bundle args) {
                Log.d(TAG, "action button clicked : " + action);
                switch (action) {
                    case InPlayMatchAction.COMMING_UP:
                        break;

                    case InPlayMatchAction.PLAY:
                        break;

                    case InPlayMatchAction.CONTINUE:
                        break;

                    case InPlayMatchAction.ANSWER:
                        break;

                    case InPlayMatchAction.DID_NOT_PLAY:
                        break;

                    case InPlayMatchAction.POINTS:
                        break;
                }
            }
        };
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
