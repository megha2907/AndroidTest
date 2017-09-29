package in.sportscafe.nostragamus.module.othersanswers;

import android.content.Context;
import android.os.Bundle;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsResponse;
import in.sportscafe.nostragamus.module.resultspeek.dto.Match;
import in.sportscafe.nostragamus.module.user.playerprofile.PlayerProfileModelImpl;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 15/6/16.
 */
public class OthersAnswersModelImpl implements OthersAnswersModel {

    private boolean mShowAnswerPercentage = true;

    private int mPlayerUserId;

    private int mMatchId;

    private int mRoomId;

    private Match mMatchDetails;

    private OthersAnswersAdapter mOthersAnswersAdapter;

    private OnOthersAnswersModelListener mOthersAnswersModelListener;

    private OthersAnswersModelImpl(OnOthersAnswersModelListener listener) {
        this.mOthersAnswersModelListener = listener;
    }

    public static OthersAnswersModel newInstance(OnOthersAnswersModelListener listener) {
        return new OthersAnswersModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        if (null != bundle) {

            if (bundle.containsKey(BundleKeys.PLAYER_USER_ID)) {
                mPlayerUserId = bundle.getInt(BundleKeys.PLAYER_USER_ID);
            } else {
                mPlayerUserId = Integer.parseInt(NostragamusDataHandler.getInstance().getUserId());
            }

            if (bundle.containsKey(BundleKeys.MATCH_ID)) {
                mMatchId = bundle.getInt(BundleKeys.MATCH_ID);
            }

            if (bundle.containsKey(BundleKeys.MATCH_DETAILS)) {
                mMatchDetails = Parcels.unwrap(bundle.getParcelable(BundleKeys.MATCH_DETAILS));
                mMatchId = mMatchDetails.getId();
                mRoomId =  mMatchDetails.getRoomId();
            }

            if (bundle.containsKey(BundleKeys.SHOW_ANSWER_PERCENTAGE)) {
                mShowAnswerPercentage = bundle.getBoolean(BundleKeys.SHOW_ANSWER_PERCENTAGE);
            }
        }
    }

    @Override
    public void getOthersAnswers() {
        if(null != mMatchDetails) {
            callPlayerResultPercentageApi();
            return;
        }

        if (checkInternet()) {
            callPlayerResultsApi();
        }
    }

    @Override
    public OthersAnswersAdapter getAdapter(Context context) {
        return mOthersAnswersAdapter = new OthersAnswersAdapter(context);
    }

    private boolean checkInternet() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            return true;
        }
        mOthersAnswersModelListener.onNoInternet();
        return false;
    }

    private void callPlayerResultsApi() {
        MyWebService.getInstance().getPlayerResultRequest(mPlayerUserId, mMatchId,mRoomId).enqueue(
                new NostragamusCallBack<MyResultsResponse>() {
                    @Override
                    public void onResponse(Call<MyResultsResponse> call, Response<MyResultsResponse> response) {
                        super.onResponse(call, response);

                        if(mOthersAnswersModelListener.isThreadAlive()) {
                            if (response.isSuccessful()) {
                                handleOthersAnswersResponse(response.body().getMyResults());
                            } else {
                                mOthersAnswersModelListener.onFailedOthersAnswers();
                            }
                        }
                    }
                }
        );
    }

    private void handleOthersAnswersResponse(Match othersAnswers) {
        if (othersAnswers!=null) {
            mOthersAnswersModelListener.onEmpty();
            return;
        }

        mMatchDetails = othersAnswers;
        if(mShowAnswerPercentage) {
            callPlayerResultPercentageApi();
        } else {
            callPlayerProfileApi();
        }

//        loadAdapterData(getCategorizedList(mMatchDetails, new HashMap<Integer, MatchAnswerStats>()));
//        mOthersAnswersModelListener.onSuccessOthersAnswers();
    }

    private void loadAdapterData(Match match) {
        mOthersAnswersAdapter.clear();
//        addMoreInAdapter(feedList);
        mOthersAnswersAdapter.add(match);
    }

    /*private void addMoreInAdapter(List<Feed> feedList) {
        int count = mOthersAnswersAdapter.getItemCount();
        for (Feed feed : feedList) {
            mOthersAnswersAdapter.add(feed, count++);
        }
    }*/

    /*private List<Feed> getCategorizedList(Match Match, Map<Integer, MatchAnswerStats> questionAnswersMap) {
        Map<String, Tournament> tourMap = new HashMap<>();
        Map<String, Feed> feedMap = new HashMap<>();
        List<Feed> feedList = new ArrayList<>();

        String date;
        Feed feed;
        int tourId;
        Tournament tour;
        date = TimeUtils.getFormattedDateString(
                Match.getStartTime(),
                Constants.DateFormats.DD_MM_YYYY,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        if (feedMap.containsKey(date)) {
            feed = feedMap.get(date);
        } else {
            feed = new Feed(TimeUtils.getMillisecondsFromDateString(
                    date,
                    Constants.DateFormats.DD_MM_YYYY,
                    Constants.DateFormats.GMT
            ));
            feedMap.put(date, feed);
            feedList.add(feed);
        }

        tourId = Match.getTournamentId();

        if (tourMap.containsKey(date + tourId)) {
            tour = tourMap.get(date + tourId);
        } else {
            tour = new Tournament(tourId, Match.getTournamentName());
            tourMap.put(date + tourId, tour);
            feed.addTournament(tour);
        }

        tour.addMatches(Match);

        Match.updateAnswerPercentage(questionAnswersMap);

        Collections.sort(feedList, new Comparator<Feed>() {
            @Override
            public int compare(Feed lhs, Feed rhs) {
                return (int) (lhs.getDate() - rhs.getDate());
            }
        });

        return feedList;
    }*/

    private void callPlayerResultPercentageApi() {
        MyWebService.getInstance().getPlayerResultPercentageRequest(mMatchId)
                .enqueue(new NostragamusCallBack<MatchAnswerStatsResponse>() {
                    @Override
                    public void onResponse(Call<MatchAnswerStatsResponse> call, Response<MatchAnswerStatsResponse> response) {
                        super.onResponse(call, response);

                        if(response.isSuccessful()) {
                            handlePlayerPercentageResponse(response.body().getQuestionAnswers());
                        } else {
                            mOthersAnswersModelListener.onFailedOthersAnswers();
                        }
                    }
                });
    }

    private void handlePlayerPercentageResponse(List<MatchAnswerStats> questionAnswers) {
        if(questionAnswers.isEmpty()) {
            mOthersAnswersModelListener.onEmpty();
            return;
        }

        Map<Integer, MatchAnswerStats> questionAnswersMap = new HashMap<>();
        for (MatchAnswerStats questionAnswer : questionAnswers) {
            questionAnswersMap.put(questionAnswer.getQuestionId(), questionAnswer);
        }

        mMatchDetails.updateAnswerPercentage(questionAnswersMap);
        loadAdapterData(mMatchDetails);
        mOthersAnswersModelListener.onSuccessOthersAnswers();
    }

    private void callPlayerProfileApi() {
        PlayerProfileModelImpl.newInstance(new PlayerProfileModelImpl.OnProfileModelListener() {
            @Override
            public void onNoInternet() {
                mOthersAnswersModelListener.onNoInternet();
            }

            @Override
            public void onSuccessPlayerInfo(PlayerInfo playerInfo) {
                handlePlayerProfileResponse(playerInfo);
            }

            @Override
            public void onFailedPlayerInfo() {
                mOthersAnswersModelListener.onFailedOthersAnswers();
            }
        }).getPlayerInfoFromServer(mPlayerUserId);
    }

    private void handlePlayerProfileResponse(PlayerInfo playerInfo) {
        mOthersAnswersAdapter.setPlayerInfo(playerInfo);

        loadAdapterData(mMatchDetails);
        mOthersAnswersModelListener.onSuccessOthersAnswers();
    }

    public interface OnOthersAnswersModelListener {

        void onSuccessOthersAnswers();

        void onFailedOthersAnswers();

        void onNoInternet();

        void onEmpty();

        boolean isThreadAlive();
    }
}