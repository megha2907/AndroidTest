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

    private Integer mRoomId;

    private String mPlayerPhoto;

    private String mPlayerName;

    private String mChallengeName;

    private String mMatchStage;

    private Integer playerPoints;

    private Integer myPoints;

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
            mPlayerPhoto = bundle.getString(Constants.BundleKeys.PLAYER_PHOTO);
            mPlayerName = bundle.getString(Constants.BundleKeys.PLAYER_NAME);
            mResultsPeekModelListener.setPlayerProfileData(mPlayerPhoto, mPlayerName);
            callPlayerResultsApi();
        }else {
            mResultsPeekModelListener.onFailedResults();
        }
    }

    private void callPlayerResultsApi() {
        MyWebService.getInstance().getPlayerResultRequest(mPlayerUserId, mMatchId, mRoomId).enqueue(
                new NostragamusCallBack<MyResultsResponse>() {
                    @Override
                    public void onResponse(Call<MyResultsResponse> call, Response<MyResultsResponse> response) {
                        super.onResponse(call, response);

                        if (mResultsPeekModelListener.isThreadAlive()) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    Match match = response.body().getMyResults();
                                    if (match != null) {
                                        playerPoints = response.body().getMyResults().getMatchPoints();
                                        mMatchStage = response.body().getMyResults().getStage();
                                        mChallengeName = response.body().getMyResults().getChallengeName();
                                        playerQuestionsList = response.body().getMyResults().getQuestions();
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
                            if (response.isSuccessful()) {
                                List<Question> myQuestionsList = new ArrayList<>();
                                try {
                                    myPoints = response.body().getMyResults().getMatchPoints();
                                    myQuestionsList = response.body().getMyResults().getQuestions();
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

                                mResultsPeekModelListener.onSuccessBothResults(myPoints, playerPoints, mMatchStage, mChallengeName);

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

        void onSuccessBothResults(Integer myPoints, Integer playerPoints, String matchStage, String challengeName);

        void setPlayerProfileData(String playerPhoto, String playerName);
    }
}