package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnswerIds;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.feed.dto.TournamentPowerupInfo;
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
public class PredictionModelImpl implements PredictionModel, SwipeFlingAdapterView.OnSwipeListener<Question> {

    private final NostragamusDataHandler mNostragamusDataHandler;

    private boolean mDummyGame = false;

    private PredictionAdapter mPredictionAdapter;

    private OnPredictionModelListener mPredictionModelListener;

    private Match mMyResult;

    private int mInitialCount;

    private Integer mMatchId;

    private boolean mNeitherOptionAvailable = false;

    private int m2xPowerups = 0;

    private int mNonegsPowerups = 0;

    private int mPollPowerups = 0;

    public PredictionModelImpl(OnPredictionModelListener predictionModelListener) {
        this.mPredictionModelListener = predictionModelListener;
        this.mNostragamusDataHandler = NostragamusDataHandler.getInstance();

//        updateInitialPowerups();
    }

    public static PredictionModel newInstance(OnPredictionModelListener predictionModelListener) {
        return new PredictionModelImpl(predictionModelListener);
    }

    /*private void updateInitialPowerups() {
        m2xPowerups = mNostragamusDataHandler.getNumberof2xPowerups();
        mNonegsPowerups = mNostragamusDataHandler.getNumberofNonegsPowerups();
        mPollPowerups = mNostragamusDataHandler.getNumberofAudiencePollPowerups();
    }*/

    @Override
    public void init(Bundle bundle) {

        if (!bundle.containsKey(BundleKeys.IS_DUMMY_GAME)) {
            mMyResult = Parcels.unwrap(bundle.getParcelable(BundleKeys.MATCH_LIST));
            mMatchId = mMyResult.getId();

            if (bundle.containsKey(BundleKeys.SPORT_NAME)) {
                mPredictionModelListener.onGetSportName(bundle.getString(BundleKeys.SPORT_NAME));
            }

            if (bundle.containsKey(BundleKeys.TOURNAMENT_POWERUPS)) {

                TournamentPowerupInfo tournamentPowerupInfo = Parcels.unwrap(bundle.getParcelable(BundleKeys.TOURNAMENT_POWERUPS));
                HashMap<String, Integer> powerUpMap = tournamentPowerupInfo.getPowerUps();

                m2xPowerups = powerUpMap.get(Powerups.XX);
                mNonegsPowerups = powerUpMap.get(Powerups.NO_NEGATIVE);
                mPollPowerups = powerUpMap.get(Powerups.AUDIENCE_POLL);

            }

        } else {
            mDummyGame = bundle.getBoolean(BundleKeys.IS_DUMMY_GAME);
        }
    }

    @Override
    public boolean isDummyGame() {
        return mDummyGame;
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
        return String.valueOf(mMyResult.getParties().get(0).getPartyName() + "  vs  " + mMyResult.getParties().get(1).getPartyName());
    }

    @Override
    public int get2xPowerupCount() {
        return m2xPowerups;
    }

    @Override
    public int getNonegsPowerupCount() {
        return mNonegsPowerups;
    }

    @Override
    public int getPollPowerupCount() {
        return mPollPowerups;
    }

    @Override
    public void getAllQuestions() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callAllQuestionsApi(mMatchId);
        } else {
            mPredictionModelListener.onNoInternetForQuestions();
        }
    }

    @Override
    public PredictionAdapter getAdapter(Context context, List<Question> questions) {
        mInitialCount = questions.size();

        mPredictionAdapter = new PredictionAdapter(context);
        int count = 1;
        for (Question question : questions) {
            question.setQuestionNumber(count++);
            mPredictionAdapter.add(question);
        }

        return mPredictionAdapter;
    }

    @Override
    public void setFlingCardListener(FlingCardListener flingCardListener) {
        mPredictionAdapter.setFlingCardListener(flingCardListener);
    }

    @Override
    public void apply2xPowerup() {
        if (isNotPowerupApplied() && m2xPowerups > 0) {
            mPredictionAdapter.update2xPowerUp();
            notifyTopQuestion();

            m2xPowerups--;
            mPredictionModelListener.on2xApplied(m2xPowerups);
        }
    }

    @Override
    public void applyNonegsPowerup() {
        if (isNotPowerupApplied() && mNonegsPowerups > 0) {
            mPredictionAdapter.updateNonegsPowerUp();
            notifyTopQuestion();

            mNonegsPowerups--;
            mPredictionModelListener.onNonegsApplied(mNonegsPowerups);
        }
    }

    @Override
    public void applyPollPowerup() {
        if (isNotPowerupApplied() && mPollPowerups > 0) {
            getAudiencePollPercent();
        }
    }

    @Override
    public void removeFirstObjectInAdapter(Question question) {
        mPredictionAdapter.remove(question);
        mPredictionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLeftSwipe(Question dataObject) {
        saveSinglePrediction(dataObject, AnswerIds.LEFT);
    }

    @Override
    public void onRightSwipe(Question dataObject) {
        saveSinglePrediction(dataObject, AnswerIds.RIGHT);
    }

    @Override
    public void onTopSwipe(Question dataObject) {
        saveSinglePrediction(dataObject, AnswerIds.NEITHER);
    }

    @Override
    public void onBottomSwipe(Question dataObject) {
        mPredictionAdapter.add(dataObject);
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == 0) {
            if (!mDummyGame) {
                Bundle bundle = new Bundle();
                bundle.putInt(BundleKeys.TOURNAMENT_ID, mMyResult.getTournamentId());
                bundle.putString(BundleKeys.TOURNAMENT_NAME, mMyResult.getTournamentName());
                mPredictionModelListener.onSuccessCompletion(bundle);
            } else {
                mPredictionModelListener.onDummyGameCompletion();
            }
            return;
        }

        if (itemsInAdapter == 1) {
            mPredictionModelListener.onShowingLastQuestion();
        }

        Question topQuestion = mPredictionAdapter.getTopQuestion();
        mNeitherOptionAvailable = !TextUtils.isEmpty(topQuestion.getQuestionOption3());

        mPredictionModelListener.onQuestionChanged(topQuestion, mInitialCount, mNeitherOptionAvailable);
    }

    @Override
    public boolean needTopSwipe() {
        return mNeitherOptionAvailable;
    }

    @Override
    public boolean needBottomSwipe() {
        return mPredictionAdapter.getCount() > 1;
    }

    @Override
    public void onCardMoving(float xPercent, float yPercent) {
        mPredictionAdapter.onCardMoving(xPercent, yPercent);
    }

    private Match getDummyGameMatch() {
        String dummyMatchJson = "";
        return MyWebService.getInstance().getObjectFromJson(dummyMatchJson, Match.class);
    }

    private void callAllQuestionsApi(Integer matchId) {
        MyWebService.getInstance().getAllQuestions(matchId).enqueue(new NostragamusCallBack<QuestionsResponse>() {
            @Override
            public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
                super.onResponse(call, response);
                if (!mPredictionModelListener.isThreadAlive()) {
                    return;
                }

                if (response.isSuccessful()) {
                    List<Question> questions = response.body().getQuestions();
                    if (null == questions || questions.isEmpty() || questions.size() < 0) {
                        mPredictionModelListener.onNoQuestions();
                    } else {
                        mPredictionModelListener.onSuccessQuestions(questions, PredictionModelImpl.this);
                    }
                } else {
                    mPredictionModelListener.onFailedQuestions(response.message());
                }
            }
        });
    }

    private boolean isNotPowerupApplied() {
        return null == mPredictionAdapter.getTopQuestion().getPowerUpId();
    }

    private void notifyTopQuestion() {
        mPredictionAdapter.notifyDataSetChanged();
        mPredictionModelListener.notifyTopQuestion();
    }

    private void getAudiencePollPercent() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            AudiencePollRequest audiencePollRequest = new AudiencePollRequest();
            audiencePollRequest.setQuestionId(mPredictionAdapter.getTopQuestion().getQuestionId());

            callAudiencePollApi(audiencePollRequest);
//            handleAudiencePollResponse(getDummyAudiencePoll().getAudiencePoll());
//            mPredictionModelListener.onSuccessAudiencePollResponse();
        } else {
            mPredictionModelListener.onNoInternet();
        }
    }

    private AudiencePollResponse getDummyAudiencePoll() {
        AudiencePollResponse response = new AudiencePollResponse();

        List<AudiencePoll> audiencePollList = new ArrayList<>();
        response.setAudiencePoll(audiencePollList);

        AudiencePoll audiencePoll = new AudiencePoll();
        audiencePoll.setAnswerId(1);
        audiencePoll.setAnswerPercentage("30%");
        audiencePollList.add(audiencePoll);

        audiencePoll = new AudiencePoll();
        audiencePoll.setAnswerId(2);
        audiencePoll.setAnswerPercentage("70%");
        audiencePollList.add(audiencePoll);

        return response;
    }

    private void callAudiencePollApi(AudiencePollRequest request) {
        MyWebService.getInstance().getAudiencePoll(request).enqueue(new NostragamusCallBack<AudiencePollResponse>() {
            @Override
            public void onResponse(Call<AudiencePollResponse> call, Response<AudiencePollResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful()) {
                    List<AudiencePoll> audiencePoll = response.body().getAudiencePoll();
                    if (null == audiencePoll || audiencePoll.isEmpty()) {
                        mPredictionModelListener.onFailedAudiencePollResponse();
                    } else {
                        handleAudiencePollResponse(audiencePoll);
                        mPredictionModelListener.onSuccessAudiencePollResponse();
                    }
                } else {
                    mPredictionModelListener.onFailedAudiencePollResponse();
                }
            }
        });
    }

    private void handleAudiencePollResponse(List<AudiencePoll> audiencePoll) {
        int leftAnswerPercent = Integer.parseInt(audiencePoll.get(0).getAnswerPercentage().replaceAll("%", ""));
        int rightAnswerPercent = Integer.parseInt(audiencePoll.get(1).getAnswerPercentage().replaceAll("%", ""));

        mPredictionAdapter.updateAudiencePollPowerUp(leftAnswerPercent, rightAnswerPercent);
        notifyTopQuestion();

        mPollPowerups--;
        mPredictionModelListener.onAudiencePollApplied(mPollPowerups);

        //mNostragamusDataHandler.setNumberofAudiencePollPowerups(mPollPowerups);
    }

    private void saveSinglePrediction(Question question, int answerId) {
        question.setAnswerId(answerId);
        String powerupId = question.getPowerUpId();

        Answer answer = new Answer(
                question.getMatchId(),
                question.getQuestionId(),
                answerId,
                TimeUtils.getCurrentTime(DateFormats.FORMAT_DATE_T_TIME_ZONE, DateFormats.GMT),
                powerupId
        );

        postAnswerToServer(answer, question.isMinorityAnswer(), mPredictionAdapter.getCount() == 0);

        if (null != powerupId) {
            if (Powerups.XX.equalsIgnoreCase(powerupId)) {
               // mNostragamusDataHandler.setNumberof2xPowerups(m2xPowerups);
            } else if (Powerups.NO_NEGATIVE.equalsIgnoreCase(powerupId)) {
               //
                // mNostragamusDataHandler.setNumberofNonegsPowerups(mNonegsPowerups);
            }
        }
    }

    private void postAnswerToServer(Answer answer, boolean minorityOption, Boolean matchComplete) {
        new PostAnswerModelImpl(new PostAnswerModelImpl.PostAnswerModelListener() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onNoInternet() {
                mPredictionModelListener.onNoInternet();
            }

            @Override
            public void onFailed(String message) {
                mPredictionModelListener.onFailedPostAnswerToServer(message);

            }
        }).postAnswer(answer, minorityOption, matchComplete);
    }

    public interface OnPredictionModelListener {

        boolean isThreadAlive();

        void onSuccessCompletion(Bundle bundle);

        void onShowingLastQuestion();

        void onNoInternet();

        void onNoInternetForQuestions();

        void onSuccessQuestions(List<Question> questions, SwipeFlingAdapterView.OnSwipeListener<Question> listener);

        void onFailedQuestions(String message);

        void onNoQuestions();

        void onQuestionChanged(Question question, int initialCount, boolean neitherAvailable);

        void onSuccessAudiencePollResponse();

        void onFailedAudiencePollResponse();

        void notifyTopQuestion();

        void onGetSportName(String sportName);

        void onFailedPostAnswerToServer(String message);

        void on2xApplied(int count);

        void onNonegsApplied(int count);

        void onAudiencePollApplied(int count);

        void onDummyGameCompletion();
    }
}