package in.sportscafe.nostragamus.module.resultspeek;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
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

    private String mPlayerPhoto;

    private String mPlayerName;

    private String mMatchStage;

    private Integer playerPoints;

    private Integer myPoints;

    private List<ResultsPeek> mResultsPeek = new ArrayList<>();
    ;

    private ResultsPeekModelImpl.ResultsPeekModelListener mResultsPeekModelListener;

    protected ResultsPeekModelImpl(ResultsPeekModelImpl.ResultsPeekModelListener modelListener) {
        this.mResultsPeekModelListener = modelListener;
    }

    public static ResultsPeekModel newInstance(ResultsPeekModelImpl.ResultsPeekModelListener modelListener) {
        return new ResultsPeekModelImpl(modelListener);
    }

    @Override
    public void init(Bundle bundle) {

        mPlayerUserId = bundle.getInt(Constants.BundleKeys.PLAYER_ID);
        mMatchId = bundle.getInt(Constants.BundleKeys.MATCH_ID);
        mPlayerPhoto = bundle.getString(Constants.BundleKeys.PLAYER_PHOTO);
        mPlayerName = bundle.getString(Constants.BundleKeys.PLAYER_NAME);

        mResultsPeekModelListener.setPlayerProfileData(mPlayerPhoto, mPlayerName);

        callPlayerResultsApi();

    }

    private void callPlayerResultsApi() {
        MyWebService.getInstance().getPlayerResultRequest(mPlayerUserId, mMatchId).enqueue(
                new NostragamusCallBack<MyResultsResponse>() {
                    @Override
                    public void onResponse(Call<MyResultsResponse> call, Response<MyResultsResponse> response) {
                        super.onResponse(call, response);

                        if (mResultsPeekModelListener.isThreadAlive()) {
                            if (response.isSuccessful()) {
                                if (!response.body().getMyResults().isEmpty()) {
                                playerPoints = response.body().getMyResults().get(0).getMatchPoints();
                                mMatchStage = response.body().getMyResults().get(0).getStage();
                                List<Question> playerQuestionsList = response.body().getMyResults().get(0).getQuestions();
                                for (Question playerQuestion : playerQuestionsList) {
                                    mResultsPeek.add(new ResultsPeek(playerQuestion));
                                }
                                callMyResultsApi();
                              }else {
                                    mResultsPeekModelListener.onFailedResults();
                                }
                            }
                            else {
                                mResultsPeekModelListener.onFailedResults();
                            }
                        }
                    }
                }
        );
    }

    private void callMyResultsApi() {
        MyWebService.getInstance().getPlayerResultRequest(Integer.valueOf(NostragamusDataHandler.getInstance().getUserId()), mMatchId).enqueue(
                new NostragamusCallBack<MyResultsResponse>() {
                    @Override
                    public void onResponse(Call<MyResultsResponse> call, Response<MyResultsResponse> response) {
                        super.onResponse(call, response);

                        if (mResultsPeekModelListener.isThreadAlive()) {
                            if (response.isSuccessful()) {

                                if (!response.body().getMyResults().isEmpty()) {
                                    int count = 0;
                                    myPoints = response.body().getMyResults().get(0).getMatchPoints();
                                    List<Question> myQuestionsList = response.body().getMyResults().get(0).getQuestions();
                                    for (Question myQuestion : myQuestionsList) {
                                        mResultsPeek.get(count++).setPlayerOneQuestions(myQuestion);
                                    }

                                    mResultsPeekModelListener.onSuccessBothResults(myPoints, playerPoints, mMatchStage);
                                } else {
                                    mResultsPeekModelListener.onSuccessBothResults(myPoints, playerPoints, mMatchStage);
                                }

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

        mResultsPeekAdapter = new ResultsPeekAdapter(context);
        mResultsPeekAdapter.addAll(mResultsPeek);
        return mResultsPeekAdapter;
    }


    public interface ResultsPeekModelListener {

        void onNoInternet();

        void onFailed(String message);

        boolean isThreadAlive();

        void onFailedResults();

        void onSuccessBothResults(Integer myPoints, Integer playerPoints, String matchStage);

        void setPlayerProfileData(String playerPhoto, String playerName);
    }
}