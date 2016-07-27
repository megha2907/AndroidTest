package in.sportscafe.scgame.module.play;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.module.feed.dto.Match;
import in.sportscafe.scgame.module.feed.dto.MatchesResponse;
import in.sportscafe.scgame.module.play.prediction.dto.Question;
import in.sportscafe.scgame.module.play.prediction.dto.QuestionsResponse;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/7/16.
 */
public class PlayModelImpl implements PlayModel {

    private Map<Integer, Match> mMatchMap = new HashMap<>();

    private List<Match> mMyResultList = new ArrayList<>();

    private OnPlayModelListener mPlayModelListener;

    public PlayModelImpl(OnPlayModelListener listener) {
        this.mPlayModelListener = listener;
    }

    public static PlayModel newInstance(OnPlayModelListener listener) {
        return new PlayModelImpl(listener);
    }

    @Override
    public void getAllQuestions() {
        if (ScGame.getInstance().hasNetworkConnection()) {
            callMatchesApi();
        } else {
            mPlayModelListener.onNoInternet();
        }
    }

    private void callMatchesApi() {
        MyWebService.getInstance().getMatches().enqueue(new ScGameCallBack<MatchesResponse>() {
            @Override
            public void onResponse(Call<MatchesResponse> call, Response<MatchesResponse> response) {
                if (response.isSuccessful()) {
                    List<Match> matchList = response.body().getMatches();

                    if (null == matchList || matchList.isEmpty()) {
                        mPlayModelListener.onNoQuestions();
                        return;
                    }

                    handleMatches(matchList);
                } else {
                    mPlayModelListener.onFailedQuestions(response.message());
                }
            }
        });
    }

    private void handleMatches(List<Match> matchList) {
        for (Match match : matchList) {
            mMatchMap.put(match.getId(), match);
        }

        callAllQuestionsApi();
    }

    private void callAllQuestionsApi() {
        MyWebService.getInstance().getAllQuestions().enqueue(new ScGameCallBack<QuestionsResponse>() {
            @Override
            public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
                if (null == mPlayModelListener.getContext()) {
                    return;
                }

                if (response.isSuccessful()) {
                    List<Question> allQuestions = response.body().getQuestions();

                    if (null == allQuestions || allQuestions.isEmpty()) {
                        mPlayModelListener.onNoQuestions();
                    }

                    handleAllQuestionsResponse(allQuestions);
                } else {
                    mPlayModelListener.onFailedQuestions(response.message());
                }
            }
        });
    }

    private void handleAllQuestionsResponse(List<Question> questionList) {
        Map<Integer, Match> myResultMap = new HashMap<>();

        Integer matchId;
        Match myResult;
        for (Question question : questionList) {
            matchId = question.getMatchId();

            if (myResultMap.containsKey(matchId)) {
                myResult = myResultMap.get(matchId);
            } else {
                myResult = mMatchMap.get(matchId);
                mMyResultList.add(myResult);
                myResultMap.put(matchId, myResult);
            }

            myResult.getQuestions().add(question);
        }

        mPlayModelListener.onSuccessQuestions();
    }

    @Override
    public Bundle getNextContestQuestions() {
        Match myResult = mMyResultList.get(0);
        skipCurrentMatch();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BundleKeys.CONTEST_QUESTIONS, myResult);
        return bundle;
    }

    @Override
    public boolean isNextContest() {
        return mMyResultList.size() > 0;
    }

    @Override
    public void skipCurrentMatch() {
        if (!mMyResultList.isEmpty()) {
            mMyResultList.remove(0);
        }
    }

    public interface OnPlayModelListener {

        void onSuccessQuestions();

        void onNoQuestions();

        void onFailedQuestions(String message);

        void onNoInternet();

        Context getContext();
    }
}