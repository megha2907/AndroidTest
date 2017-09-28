package in.sportscafe.nostragamus.module.inPlay.ui.headless.matches;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.JoinPseudoContestApiModelImpl;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.NewChallengesMatchesDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.JoinPseudoContestResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengeMatchesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.play.myresults.dto.MyResultScreenData;
import in.sportscafe.nostragamus.module.prediction.playScreen.PredictionActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;
import in.sportscafe.nostragamus.utils.AlertsHelper;

public class InPlayHeadLessMatchesFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = InPlayHeadLessMatchesFragment.class.getSimpleName();

    private InPlayHeadLessMatchFragmentListener mInPlayHeadLessMatchFragmentListener;
    private RecyclerView mMatchesRecyclerView;
    private InPlayHeadLessMatchesAdapter mMatchesAdapter;
    private InPlayContestDto mInPlayContest;

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
        TextView tvContestHeading = (TextView) rootView.findViewById(R.id.matches_timeline_heading);
        TextView tvContestSubHeadingOne = (TextView) rootView.findViewById(R.id.matches_timeline_subheading_one);
        TextView tvContestSubHeadingTwo = (TextView) rootView.findViewById(R.id.newchallenge_matches_subheading_two);
        Button joinContestBtn = (Button) rootView.findViewById(R.id.new_challenge_matches_join_button);
        TextView tvMatchesLeft = (TextView) rootView.findViewById(R.id.matches_timeline_matches_left);
        TextView tvContestExpiry = (TextView) rootView.findViewById(R.id.matches_timeline_match_expires_in);

        mMatchesRecyclerView = (RecyclerView)rootView.findViewById(R.id.match_timeline_rv);
        mMatchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mMatchesRecyclerView.setHasFixedSize(true);

        joinContestBtn.setOnClickListener(this);
    }

    @NonNull
    private InPlayHeadLessMatchAdapterListener getMatchAdapterListener() {
        return new InPlayHeadLessMatchAdapterListener() {

            @Override
            public void onMatchActionClicked(int actionType, Bundle args) {
                switch (actionType) {
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

    private void launchPlayScreen(Bundle matchArgs) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing() &&
                mInPlayContest != null) {

            Bundle bundle = new Bundle();
            if (matchArgs != null && matchArgs.containsKey(Constants.BundleKeys.INPLAY_MATCH)) {
                InPlayMatch match = Parcels.unwrap(matchArgs.getParcelable(Constants.BundleKeys.INPLAY_MATCH));
                PlayScreenDataDto playData = getPlayScreenData(match);

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
                com.jeeva.android.Log.e(TAG, "No Contest in Bundle to launch Play screen");
                handleError(-1);
            }
        }
    }

    private PlayScreenDataDto getPlayScreenData(InPlayMatch inPlayMatch) {
        PlayScreenDataDto dataDto = null;

        if (inPlayMatch != null && mInPlayContest != null) {
            dataDto = new PlayScreenDataDto();

            dataDto.setChallengeId(mInPlayContest.getChallengeId());
            dataDto.setMatchId(inPlayMatch.getMatchId());
            dataDto.setRoomId(mInPlayContest.getRoomId());
            dataDto.setPowerUp(mInPlayContest.getPowerUp());
            dataDto.setSubTitle(mInPlayContest.getContestName());

            if (inPlayMatch.getMatchParties() != null && inPlayMatch.getMatchParties().size() == 2) {
                dataDto.setMatchPartyTitle1(inPlayMatch.getMatchParties().get(0).getPartyName());
                dataDto.setMatchPartyTitle1(inPlayMatch.getMatchParties().get(1).getPartyName());
            }
        }

        return dataDto;
    }

    private void launchResultsScreen(Bundle args) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            MyResultScreenData data = null;

            if (args != null && args.containsKey(Constants.BundleKeys.INPLAY_MATCH)) {
                InPlayMatch match = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.INPLAY_MATCH));

                if (match != null && mInPlayContest != null) {
                    data = new MyResultScreenData();
                    data.setMatchId(match.getMatchId());
                    data.setChallengeId(match.getChallengeId());
                    data.setRoomId(mInPlayContest.getRoomId());
                }
            }

            if (data != null) {
                args.putParcelable(Constants.BundleKeys.MY_RESULT_SCREEN_DATA, Parcels.wrap(data));

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
        loadDataFromServer();
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BundleKeys.INPLAY_CONTEST)) {
            mInPlayContest = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.INPLAY_CONTEST));
        }
    }

    private void loadDataFromServer() {
        if (mInPlayContest != null) {
            InPlayMatchesDataProvider inPlayMatchesDataProvider = new InPlayMatchesDataProvider();
            inPlayMatchesDataProvider.getInPlayMatches(mInPlayContest.getRoomId(),
                    mInPlayContest.getChallengeId(),
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
        if (mInPlayContest != null && mInPlayHeadLessMatchFragmentListener != null) {
            ContestScreenData screenData = new ContestScreenData();
            screenData.setChallengeId(mInPlayContest.getChallengeId());
            screenData.setChallengeName(mInPlayContest.getChallengeName());

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.CONTEST_SCREEN_DATA, Parcels.wrap(screenData));
            args.putInt(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT, ContestsActivity.LaunchedFrom.IN_PLAY_HEAD_LESS_MATCHES);

            mInPlayHeadLessMatchFragmentListener.launchContestActivity(ContestsActivity.LaunchedFrom.IN_PLAY_HEAD_LESS_MATCHES, args);
        }
    }

}
