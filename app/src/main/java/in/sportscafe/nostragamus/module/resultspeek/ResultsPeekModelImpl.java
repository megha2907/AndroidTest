package in.sportscafe.nostragamus.module.resultspeek;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.jeeva.android.ExceptionTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsResponse;
import in.sportscafe.nostragamus.module.resultspeek.dto.Match;
import in.sportscafe.nostragamus.module.resultspeek.dto.Question;
import in.sportscafe.nostragamus.module.resultspeek.dto.ResultsPeek;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 2/16/17.
 */

public class ResultsPeekModelImpl implements ResultsPeekModel {

    private ResultsPeekAdapter mResultsPeekAdapter;

    private Integer mPlayerUserId;

    private Integer mMatchId;

    private int mRoomId = 0;

    private String mPlayerPhoto;

    private String mPlayerName;

    private String mSubTitle;

    private String mMatchParties;

    private int playerPoints=0;

    private int myPoints=0;

    private int mHighestPlayerRoomId = 0;

    private List<Question> playerQuestionsList = new ArrayList<>();

    private HashMap<Integer, ResultsPeek> mResultPeekMap = new HashMap<>();

    private ResultsPeekModelImpl.ResultsPeekModelListener mResultsPeekModelListener;

    protected ResultsPeekModelImpl(ResultsPeekModelImpl.ResultsPeekModelListener modelListener) {
        this.mResultsPeekModelListener = modelListener;
    }

    public static ResultsPeekModel newInstance(ResultsPeekModelImpl.ResultsPeekModelListener modelListener) {
        return new ResultsPeekModelImpl(modelListener);
    }

    @Override
    public void init(Bundle bundle) {

        if (bundle != null) {
            mPlayerUserId = bundle.getInt(Constants.BundleKeys.PLAYER_ID);
            mMatchId = bundle.getInt(Constants.BundleKeys.MATCH_ID);
            mRoomId = bundle.getInt(Constants.BundleKeys.ROOM_ID);
            if (bundle.containsKey(Constants.BundleKeys.HIGHEST_PLAYER_ROOM_ID)) {
                mHighestPlayerRoomId = bundle.getInt(Constants.BundleKeys.HIGHEST_PLAYER_ROOM_ID);
            }else {
                mHighestPlayerRoomId = mRoomId;
            }
            mPlayerPhoto = bundle.getString(Constants.BundleKeys.PLAYER_PHOTO);
            mPlayerName = bundle.getString(Constants.BundleKeys.PLAYER_NAME);
            mResultsPeekModelListener.setPlayerProfileData(mPlayerPhoto, mPlayerName);
            callPlayerResultsApi();
        }else {
            mResultsPeekModelListener.onFailedResults();
        }
    }

    private void callPlayerResultsApi() {
        MyWebService.getInstance().getPlayerResultRequest(mPlayerUserId, mMatchId, mHighestPlayerRoomId).enqueue(
                new NostragamusCallBack<MyResultsResponse>() {
                    @Override
                    public void onResponse(Call<MyResultsResponse> call, Response<MyResultsResponse> response) {
                        super.onResponse(call, response);

                        if (mResultsPeekModelListener.isThreadAlive()) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    Match match = response.body().getMyResults();
                                    if (match != null) {
                                        playerPoints = match.getMatchPoints();
                                        if (match.getParties()!=null) {
                                            mMatchParties = match.getParties().get(0).getPartyName()
                                                    + " vs " + match.getParties().get(1).getPartyName();
                                        }
                                        mSubTitle = match.getChallengeName()+ " - " + match.getContestName();
                                        playerQuestionsList = match.getQuestions();
                                        callMyResultsApi();
                                    } else {
                                        mResultsPeekModelListener.onFailedResults();
                                    }
                                }
                            } else {
                                mResultsPeekModelListener.onFailedResults();
                            }
                        }
                    }
                }
        );
    }

    private void callMyResultsApi() {
        MyWebService.getInstance().getPlayerResultRequest(Integer.valueOf(NostragamusDataHandler.getInstance().getUserId()), mMatchId, mRoomId).enqueue(
                new NostragamusCallBack<MyResultsResponse>() {
                    @Override
                    public void onResponse(Call<MyResultsResponse> call, Response<MyResultsResponse> response) {
                        super.onResponse(call, response);

                        if (mResultsPeekModelListener.isThreadAlive()) {
                            String resultPublished = null;
                            if (response.isSuccessful()) {
                                List<Question> myQuestionsList = new ArrayList<>();
                                try {
                                    Match match = response.body().getMyResults();
                                    if (match!=null) {
                                        myPoints = match.getMatchPoints();
                                        myQuestionsList = match.getQuestions();
                                        resultPublished = match.getResult();
                                    }

                                } catch (Exception e) {
                                    ExceptionTracker.track(e);
                                }

                                for (Question question : myQuestionsList) {
                                    mResultPeekMap.put(question.getQuestionId(), new ResultsPeek(question));
                                }

                                ResultsPeek resultsPeek;
                                for (Question question : playerQuestionsList) {
                                    resultsPeek = mResultPeekMap.get(question.getQuestionId());
                                    if (null == resultsPeek) {
                                        mResultPeekMap.put(question.getQuestionId(), resultsPeek = new ResultsPeek());
                                    }
                                    resultsPeek.setPlayerTwoQuestions(question);
                                }

                                mResultsPeekModelListener.onSuccessBothResults(myPoints,
                                        playerPoints, mMatchParties, mSubTitle,resultPublished);

                            } else {
                                mResultsPeekModelListener.onFailedResults();
                            }
                        }
                    }
                }
        );
    }


    @Override
    public RecyclerView.Adapter getResultsPeekAdapter(Context context) {
        return mResultsPeekAdapter = new ResultsPeekAdapter(context, mResultPeekMap);
    }


    public interface ResultsPeekModelListener {

        void onNoInternet();

        void onFailed(String message);

        boolean isThreadAlive();

        void onFailedResults();

        void onSuccessBothResults(int myPoints, int playerPoints, String matchStage, String challengeName ,String resultPublished);

        void setPlayerProfileData(String playerPhoto, String playerName);
    }
}