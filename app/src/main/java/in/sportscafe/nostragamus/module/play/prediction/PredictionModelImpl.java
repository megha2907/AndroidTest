package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.offline.PredictionDataHandler;
import in.sportscafe.nostragamus.module.play.prediction.dto.Answer;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePoll;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollRequest;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.prediction.dto.QuestionsResponse;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

import static in.sportscafe.nostragamus.Constants.BundleKeys;
import static in.sportscafe.nostragamus.Constants.DateFormats;

/**
 * Created by Jeeva on 20/5/16.
 */
public class PredictionModelImpl implements PredictionModel,
        SwipeFlingAdapterView.OnSwipeListener<Question> {

    private PredictionAdapter mPredictionAdapter;

    private OnPredictionModelListener mPredictionModelListener;

    private Map<Integer, Match> mMatchMap = new HashMap<>();

    private List<Match> mMyResultList = new ArrayList<>();

    private Match mMyResult;

    private int mTotalCount;

    private int minitialCount;

    private Integer matchId;

    private int mRemainingTime;

    private String sportName;

    private boolean mPassEnabled = true;

    private Context context;


    public PredictionModelImpl(OnPredictionModelListener predictionModelListener) {
        this.mPredictionModelListener = predictionModelListener;
    }

    public static PredictionModel newInstance(OnPredictionModelListener predictionModelListener) {
        return new PredictionModelImpl(predictionModelListener);
    }

    public PredictionModelImpl(Context context){
        this.context=context;
    }

    @Override
    public void saveData(Bundle bundle) {

        mMyResult = (Match) bundle.getSerializable(BundleKeys.MATCH_LIST);
        matchId = mMyResult.getId();

        if(null != bundle.getString(BundleKeys.SPORT_NAME)){
          mPredictionModelListener.changePlayCardBackground(bundle.getString(BundleKeys.SPORT_NAME));
        }

//        handleAllQuestionsResponse(mMyResult.getQuestions());
        getAllQuestions();
    }

    @Override
    public void getAllQuestions() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callAllQuestionsApi(matchId);
        } else {
            mPredictionModelListener.onNoInternet();
        }
    }

    private void callAllQuestionsApi(Integer matchId) {
        MyWebService.getInstance().getAllQuestions(matchId).enqueue(new NostragamusCallBack<QuestionsResponse>() {
            @Override
            public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
                super.onResponse(call, response);
                if (null == mPredictionModelListener.getContext()) {
                    return;
                }

                if (response.isSuccessful()) {
                    List<Question> allQuestions = response.body().getQuestions();

                    if (null == allQuestions || allQuestions.isEmpty() || allQuestions.size() < 0) {
                        mPredictionModelListener.onNoQuestions();
                        Log.i("null","questions");
                    }
                    else {
                        handleAllQuestionsResponse(allQuestions);
                    }
                } else {
                    mPredictionModelListener.onFailedQuestions(response.message());
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

            if (!myResultMap.containsKey(matchId)) {
                myResult = mMatchMap.get(matchId);
                mMyResultList.add(mMyResult);
                myResultMap.put(matchId, myResult);
            }

            mMyResult.getQuestions().add(question);
        }

        mPredictionModelListener.onSuccessQuestions(questionList);
        myResult = mMyResultList.get(0);
        populateAdapterData(myResult.getQuestions());

    }


    private void populateAdapterData(List<Question> questionList) {
        mTotalCount = questionList.size();
        minitialCount=questionList.size();

        mPredictionAdapter = new PredictionAdapter(mPredictionModelListener.getContext(),
                mTotalCount);
        mPredictionModelListener.onGetAdapter(mPredictionAdapter, this);
        int count = 1;
        for (Question question : questionList) {
            question.setQuestionNumber(count++);
            mPredictionAdapter.add(question);
        }

        mPredictionAdapter.notifyDataSetChanged();
    }

    public Question getTopQuestion(){
        return mPredictionAdapter.getTopQuestion();
    }

    @Override
    public void updatePowerUps(String powerup) {

        if(powerup.equals("2x")){

            mPredictionAdapter.update2xPowerUp();
        }
        else if(powerup.equals("no_negs")){

            mPredictionAdapter.updateNonegsPowerUp();
        }
        else if(powerup.equals("player_poll"))
        {
            Question question = getTopQuestion();
            AudiencePollRequest audiencePollRequest = new AudiencePollRequest();
            audiencePollRequest.setUserId(NostragamusDataHandler.getInstance().getUserId());
            audiencePollRequest.setQuestionId(question.getQuestionId());
            callAudiencePollApi(audiencePollRequest);
        }
        mPredictionAdapter.notifyDataSetChanged();
        mPredictionModelListener.onNegativePowerUpApplied();
    }

    private void callAudiencePollApi(AudiencePollRequest request) {

        MyWebService.getInstance().getAudiencePoll(request).enqueue(new NostragamusCallBack<AudiencePollResponse>() {
            @Override
            public void onResponse(Call<AudiencePollResponse> call, Response<AudiencePollResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful()) {

                    if (null==response.body().getAudiencePoll()){
                        mPredictionModelListener.onFailedAudiencePollResponse("AudiencePoll Fail");
                    }
                    else {
                        List<AudiencePoll> audiencePoll = response.body().getAudiencePoll();
                        String answer1Percentage = audiencePoll.get(0).getAnswerPercentage();
                        String answer2Percentage = audiencePoll.get(1).getAnswerPercentage();
                        mPredictionAdapter.updateAudiencePollPowerUp(answer1Percentage, answer2Percentage);
                        mPredictionAdapter.notifyDataSetChanged();
                        mPredictionModelListener.onNegativePowerUpApplied();
                        mPredictionModelListener.onSuccessAudiencePollResponse();
                    }

                } else {
                    mPredictionModelListener.onFailedAudiencePollResponse(response.message());
                }
            }
        });

    }

    @Override
    public void setFlingCardListener(FlingCardListener flingCardListener) {
//        mPredictionAdapter.setFlingCardListener(flingCardListener);
    }

    @Override
    public String getTournamentName() {
        return mMyResult.getTournamentName();
    }

    @Override
    public String getMatchStage() {
        return mMyResult.getStage();
    }

    @Override
    public String getTournamentPhoto() {
        return mMyResult.getTournamentPhoto();
    }

    @Override
    public String getContestName() {
        return String.valueOf(mMyResult.getParties().get(0).getPartyName()+"  vs  "+mMyResult.getParties().get(1).getPartyName());
    }

    @Override
    public void removeFirstObjectInAdapter(Question dataObject) {
        // mRemainingTime = mPredictionAdapter.getRemainingTime();
        // Log.d("Remaining Time", mRemainingTime + " seconds");

        mTotalCount--;
        // mPredictionAdapter.stopTimer();
        mPredictionAdapter.remove(dataObject);
        mPredictionAdapter.notifyDataSetChanged();
        mPredictionModelListener.dismissPowerUpApplied();

        if (dataObject.getPowerUpId().equalsIgnoreCase("2x")){

            NostragamusDataHandler.getInstance().setNumberof2xPowerups(NostragamusDataHandler.getInstance().getNumberof2xPowerups() - 1);

        } else if(dataObject.getPowerUpId().equalsIgnoreCase("player_poll")) {

            NostragamusDataHandler.getInstance().setNumberofAudiencePollPowerups(NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups() - 1);
        }
        else if(dataObject.getPowerUpId().equalsIgnoreCase("no_negs")){

            NostragamusDataHandler.getInstance().setNumberofNonegsPowerups(NostragamusDataHandler.getInstance().getNumberofNonegsPowerups() - 1);
        }

    }

    @Override
    public void onLeftSwipe(Question dataObject) {
        saveSinglePrediction(dataObject, 1);
    }

    @Override
    public void onRightSwipe(Question dataObject) {
        saveSinglePrediction(dataObject, 2);
    }

    @Override
    public void onBottomSwipe(Question dataObject) {
        saveSinglePrediction(dataObject, 0);
//        mPredictionAdapter.add(dataObject);
    }

    @Override
    public void onTopSwipe(Question dataObject) {
        mPredictionAdapter.add(dataObject);
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {

        if(itemsInAdapter == 0) {
            PredictionDataHandler.getInstance().saveMyResult(mMyResult);

            Bundle bundle = new Bundle();
            bundle.putInt(Constants.BundleKeys.TOURNAMENT_ID, mMyResult.getTournamentId());
            bundle.putString(Constants.BundleKeys.TOURNAMENT_NAME, mMyResult.getTournamentName());
            mPredictionModelListener.onSuccessCompletion(bundle);
            return;
        }

        if(itemsInAdapter == 1 && mTotalCount == 1) {
            mPassEnabled = false;
            mPredictionModelListener.onShowingLastQuestion();
        } else if(mTotalCount == 0) {
            mTotalCount = mPredictionAdapter.getCount();
        }


        mPredictionModelListener.onQuestionChanged(mPredictionAdapter.getItem(0),minitialCount);
//        mPredictionAdapter.changeCardViewBackground();

        // mPredictionAdapter.startTimer();
    }

    @Override
    public boolean needBottomSwipe() {
        return mPassEnabled;
    }

    private void saveSinglePrediction(Question question, int answerId) {
        Answer answer = new Answer (
                question.getMatchId(),
                question.getQuestionId(),
                String.valueOf(answerId),
                TimeUtils.getCurrentTime(DateFormats.FORMAT_DATE_T_TIME_ZONE, DateFormats.GMT),
                question.getPowerUpId()

        );
        PredictionDataHandler.getInstance().savePrediction(answer);
        postAnswerToServer(answer);
    }

    private void postAnswerToServer(Answer answer) {
        new PostAnswerModelImpl(new PostAnswerModelImpl.PostAnswerModelListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onNoInternet() {}

            @Override
            public void onFailed(String message) {
                mPredictionModelListener.onFailedPostAnswerToServer(message);

            }
        }).postAnswer(answer);
    }
//
//    @Override
//    public void onTimeUp() {
//        mPredictionModelListener.onTimeUp();
//    }


    public interface OnPredictionModelListener {

        Context getContext();

        void onGetAdapter(PredictionAdapter predictionAdapter,
                          SwipeFlingAdapterView.OnSwipeListener<Question> listener);

        void onSuccessCompletion(Bundle bundle);

        void onShowingLastQuestion();

        void onShowingPassedQuestions();

//        void onTimeUp();

        void dismissPowerUpApplied();

        void onNoInternet();

        void onSuccessQuestions(List<Question> questionList);

        void onFailedQuestions(String message);

        void onNoQuestions();

        void onQuestionChanged(Question item, int minitialCount);

        void onFailedAudiencePollResponse(String message);

        void onSuccessAudiencePollResponse();

        void onNegativePowerUpApplied();

        void changePlayCardBackground(String sportName);

        void onFailedPostAnswerToServer(String message);
    }
}