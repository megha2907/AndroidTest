package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;

import com.fasterxml.jackson.core.type.TypeReference;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.Constants.AnswerIds;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeUserInfo;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePoll;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollRequest;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.prediction.dto.QuestionsResponse;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

import static in.sportscafe.nostragamus.Constants.BundleKeys;

/**
 * Created by Jeeva on 20/5/16.
 */
public class PredictionModelImpl implements PredictionModel, SwipeFlingAdapterView.OnSwipeListener<Question> {

    private final NostragamusDataHandler mNostragamusDataHandler;

    private PredictionAdapter mPredictionAdapter;

    private OnPredictionModelListener mPredictionModelListener;

    private PostAnswerModelImpl mPostAnswerModel;

    private Match mMyResult;

    private Challenge mChallegeInfo;

    private boolean mDummyGame = false;

    private boolean mFromSettings = false;

    private int mInitialCount;

    private Integer mMatchId;

    private boolean mNeitherOptionAvailable = false;

    private int m2xPowerups = 0;

    private int mNonegsPowerups = 0;

    private int mPollPowerups = 0;

    private int mLastQuestionNumber = -1;

    private long mQuestionSeenTimeInMs = -1;

    private long mAnswerLockedTimeInMs;

    private boolean mPowerUpUpdated = false;

    public PredictionModelImpl(OnPredictionModelListener predictionModelListener) {
        this.mPredictionModelListener = predictionModelListener;
        this.mNostragamusDataHandler = NostragamusDataHandler.getInstance();
        this.mPostAnswerModel = PostAnswerModelImpl.newInstance(postAnsModellistener);
    }

    public static PredictionModel newInstance(OnPredictionModelListener predictionModelListener) {
        return new PredictionModelImpl(predictionModelListener);
    }

    @Override
    public void init(Bundle bundle) {
        if (!bundle.containsKey(BundleKeys.IS_DUMMY_GAME)) {
            mMyResult = Parcels.unwrap(bundle.getParcelable(BundleKeys.MATCH_LIST));
            mMatchId = mMyResult.getId();

            if (bundle.containsKey(BundleKeys.SPORT_ID)) {
                mPredictionModelListener.onGetSport(bundle.getInt(BundleKeys.SPORT_ID));
            }

            if (bundle.containsKey(BundleKeys.CHALLENGE_INFO)) {
                mChallegeInfo = Parcels.unwrap(bundle.getParcelable(BundleKeys.CHALLENGE_INFO));

                HashMap<String, Integer> powerUpMap = mChallegeInfo.getChallengeUserInfo().getPowerUps();
                m2xPowerups = powerUpMap.get(Powerups.XX);
                mNonegsPowerups = powerUpMap.get(Powerups.NO_NEGATIVE);
                mPollPowerups = powerUpMap.get(Powerups.AUDIENCE_POLL);
            }

            NostragamusAnalytics.getInstance().trackPlay(AnalyticsActions.STARTED);
        } else {
            mDummyGame = true;
            if (bundle.containsKey(BundleKeys.FROM_SETTINGS)) {
                mFromSettings = bundle.getBoolean(BundleKeys.FROM_SETTINGS);
            }

            m2xPowerups = 3;
            mNonegsPowerups = 3;
            mPollPowerups = 3;

            NostragamusAnalytics.getInstance().trackDummyGame(AnalyticsActions.STARTED);
        }
    }

    @Override
    public boolean isDummyGame() {
        return mDummyGame;
    }

    @Override
    public void getDummyGameQuestions() {
        mPredictionModelListener.onSuccessQuestions(getDummyQuestionList(), PredictionModelImpl.this);
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
    public Integer getTournamentId() {
        return mMyResult.getTournamentId();
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
            mPredictionModelListener.onApiCallStarted();
            callAllQuestionsApi(mMatchId);
        } else {
            mPredictionModelListener.onNoInternetForQuestions();
        }
    }

    @Override
    public PredictionAdapter getAdapter(Context context, List<Question> questions) {
        mInitialCount = questions.size();

        mPredictionAdapter = new PredictionAdapter(context, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPowerUpAnimation(view);
            }
        });
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

    private void dismissPowerUpAnimation(final View view) {
        mPredictionAdapter.dismissPowerUpAnimation(view, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                removeAppliedPowerUp();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void removeAppliedPowerUp() {
        Question topQuestion = mPredictionAdapter.getTopQuestion();
        increasePowerUpCount(topQuestion.getPowerUpId());
        topQuestion.removeAppliedPowerUp();

        notifyTopQuestion();
    }

    private void increasePowerUpCount(String powerUpId) {
        switch (powerUpId) {
            case Powerups.XX:
                mPredictionModelListener.on2xApplied(++m2xPowerups, true);
                break;
            case Powerups.NO_NEGATIVE:
                mPredictionModelListener.onNonegsApplied(++mNonegsPowerups, true);
                break;
        }
    }

    private void updatePowerUpStatus(String powerUpId) {
        mPredictionModelListener.on2xApplied(m2xPowerups, !(Powerups.XX == powerUpId));
        mPredictionModelListener.onNonegsApplied(mNonegsPowerups, !(Powerups.NO_NEGATIVE == powerUpId));
        mPredictionModelListener.onAudiencePollApplied(mPollPowerups, !(Powerups.AUDIENCE_POLL == powerUpId));
    }

    @Override
    public void apply2xPowerup() {
        if (isNotPowerupApplied()) {
            if (m2xPowerups > 0) {
                mPredictionAdapter.getTopQuestion().apply2xPowerUp();
                notifyTopQuestion();

                m2xPowerups--;
                mPredictionModelListener.on2xApplied(m2xPowerups, false);
            } else {
                mPredictionModelListener.onNoPowerUps();
            }
        }
    }

    @Override
    public void applyNonegsPowerup() {
        if (isNotPowerupApplied()) {
            if (mNonegsPowerups > 0) {
                mPredictionAdapter.getTopQuestion().applyNonegsPowerUp();
                notifyTopQuestion();

                mNonegsPowerups--;
                mPredictionModelListener.onNonegsApplied(mNonegsPowerups, false);
            } else {
                mPredictionModelListener.onNoPowerUps();
            }
        }
    }

    @Override
    public void applyPollPowerup() {
        if (isNotPowerupApplied()) {
            if (mPollPowerups > 0) {
                getAudiencePollPercent();
            } else {
                mPredictionModelListener.onNoPowerUps();
            }
        }
    }

    @Override
    public boolean isFromSettings() {
        return mFromSettings;
    }

    @Override
    public void onSkippingDummyGame() {
        int screenCount = 0;
        if (null != mPredictionAdapter) {
            screenCount = mPredictionAdapter.getTopQuestion().getQuestionNumber();
        }
        NostragamusAnalytics.getInstance().trackDummyGame(AnalyticsActions.SKIPPED, screenCount);
    }

    @Override
    public boolean isAnyQuestionAnswered() {
        return null != mPredictionAdapter
                && (mInitialCount != mPredictionAdapter.getCount() || mPowerUpUpdated);
    }

    @Override
    public Bundle getChallengeInfoBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.CHALLENGE_INFO, Parcels.wrap(mChallegeInfo));
        return bundle;
    }

    @Override
    public void updateChallengeInfoValues(Bundle bundle) {
        if (bundle.containsKey(BundleKeys.UPDATED_CHALLENGE_USER_INFO)) {
            mChallegeInfo.setChallengeUserInfo((ChallengeUserInfo) Parcels.unwrap(bundle.getParcelable(BundleKeys.UPDATED_CHALLENGE_USER_INFO)));

            HashMap<String, Integer> powerUpMap = mChallegeInfo.getChallengeUserInfo().getPowerUps();
            m2xPowerups = powerUpMap.get(Powerups.XX);
            mNonegsPowerups = powerUpMap.get(Powerups.NO_NEGATIVE);
            mPollPowerups = powerUpMap.get(Powerups.AUDIENCE_POLL);

            String powerUpId;
            for (int i = 0; i < mPredictionAdapter.getCount(); i++) {
                // It's for handling the temporary powerups which the user has already applied
                powerUpId = mPredictionAdapter.getItem(i).getPowerUpId();
                if (null != powerUpId) {
                    switch (powerUpId) {
                        case Powerups.XX:
                            m2xPowerups--;
                            break;
                        case Powerups.NO_NEGATIVE:
                            mNonegsPowerups--;
                            break;
                        case Powerups.AUDIENCE_POLL:
                            mPollPowerups--;
                            break;
                    }
                }
            }

            mPowerUpUpdated = true;
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

        if (!mDummyGame) {
            lockAnswerTime();
            NostragamusAnalytics.getInstance().trackPlay(AnalyticsActions.SHUFFLED, AnalyticsLabels.BOTTOM, getTimeSpent());
        }
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == 0) {
            if (mDummyGame) {
                NostragamusAnalytics.getInstance().trackDummyGame(AnalyticsActions.COMPLETED);
                mPredictionModelListener.onDummyGameCompletion();
            }
            return;
        }

        Question topQuestion = mPredictionAdapter.getTopQuestion();
        if (mLastQuestionNumber != topQuestion.getQuestionNumber()) {
            mLastQuestionNumber = topQuestion.getQuestionNumber();
            if (itemsInAdapter == 1) {
                mPredictionModelListener.onShowingLastQuestion();
            }

            mNeitherOptionAvailable = !TextUtils.isEmpty(topQuestion.getQuestionOption3());

            mPredictionModelListener.onQuestionChanged(topQuestion, mInitialCount, mNeitherOptionAvailable);
            updatePowerUpStatus(topQuestion.getPowerUpId());

            if (-1 == mQuestionSeenTimeInMs) {
                mQuestionSeenTimeInMs = Calendar.getInstance().getTimeInMillis();
            }
        }
    }

    @Override
    public boolean needLeftSwipe() {
        return true;
    }

    @Override
    public boolean needRightSwipe() {
        return true;
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

                if (!mPredictionModelListener.onApiCallStopped()) {
                    return;
                }

                if (response.isSuccessful()) {
                    List<Question> questions = response.body().getQuestions();
                    if (null == questions || questions.isEmpty()) {
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
        if (!isDummyGame()) {
            if (Nostragamus.getInstance().hasNetworkConnection()) {
                mPredictionModelListener.onApiCallStarted();
                AudiencePollRequest audiencePollRequest = new AudiencePollRequest();
                audiencePollRequest.setQuestionId(mPredictionAdapter.getTopQuestion().getQuestionId());
                audiencePollRequest.setChallengeId(mChallegeInfo.getChallengeId());

                callAudiencePollApi(audiencePollRequest);
            } else {
                mPredictionModelListener.onNoInternet();
            }
        } else {
            handleAudiencePollResponse(getDummyGameAudiencePoll().getAudiencePoll());
        }
    }

    private AudiencePollResponse getDummyGameAudiencePoll() {
        AudiencePollResponse response = new AudiencePollResponse();

        List<AudiencePoll> audiencePollList = new ArrayList<>();
        response.setAudiencePoll(audiencePollList);

        AudiencePoll audiencePoll = new AudiencePoll();
        audiencePoll.setAnswerId(AnswerIds.LEFT);
        audiencePoll.setAnswerPercentage("30%");
        audiencePollList.add(audiencePoll);

        audiencePoll = new AudiencePoll();
        audiencePoll.setAnswerId(AnswerIds.RIGHT);
        audiencePoll.setAnswerPercentage("70%");
        audiencePollList.add(audiencePoll);

        return response;
    }

    private void callAudiencePollApi(AudiencePollRequest request) {
        MyWebService.getInstance().getAudiencePoll(request).enqueue(new NostragamusCallBack<AudiencePollResponse>() {
            @Override
            public void onResponse(Call<AudiencePollResponse> call, Response<AudiencePollResponse> response) {
                super.onResponse(call, response);

                if (!mPredictionModelListener.onApiCallStopped()) {
                    return;
                }

                if (response.isSuccessful()) {
                    List<AudiencePoll> audiencePoll = response.body().getAudiencePoll();
                    if (null == audiencePoll || audiencePoll.isEmpty()) {
                        mPredictionModelListener.onFailedAudiencePollResponse();
                    } else {
                        handleAudiencePollResponse(audiencePoll);
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

        mPredictionAdapter.getTopQuestion().applyAudiencePollPowerUp(leftAnswerPercent, rightAnswerPercent);
        notifyTopQuestion();

        mPollPowerups--;
        mPredictionModelListener.onAudiencePollApplied(mPollPowerups, false);
    }

    private void saveSinglePrediction(Question question, int answerId) {
        if (!mDummyGame) {
            lockAnswerTime();
            mPredictionModelListener.onApiCallStarted();

            question.setAnswerId(answerId);
            mPostAnswerModel.postAnswer(question, mChallegeInfo.getChallengeId(), mPredictionAdapter.getCount() == 0);
        }
    }

    PostAnswerModelImpl.PostAnswerModelListener postAnsModellistener = new PostAnswerModelImpl.PostAnswerModelListener() {
        @Override
        public void onSuccess(String answerDirection) {
            NostragamusAnalytics.getInstance().trackPlay(AnalyticsActions.ANSWERED, answerDirection, getTimeSpent());

            if (!mPredictionModelListener.onApiCallStopped()) {
                return;
            }

            if (mPredictionAdapter.getCount() == 0) {
                NostragamusAnalytics.getInstance().trackPlay(AnalyticsActions.COMPLETED);

                Bundle bundle = new Bundle();
                bundle.putInt(BundleKeys.CHALLENGE_ID, mMyResult.getChallengeId());
                bundle.putString(BundleKeys.CHALLENGE_NAME, mMyResult.getChallengeName());
                bundle.putString(BundleKeys.CHALLENGE_PHOTO, mMyResult.getChallengeImgUrl());
                mPredictionModelListener.onSuccessCompletion(bundle);
            }
        }

        @Override
        public void onNoInternet() {
            if (!mPredictionModelListener.onApiCallStopped()) {
                return;
            }

            mPredictionModelListener.noInternetOnPostingAnswer();
        }

        @Override
        public void onFailed(String message) {
            if (!mPredictionModelListener.onApiCallStopped()) {
                return;
            }

            mPredictionModelListener.onPostAnswerFailed(message);
        }

        @Override
        public void onMatchAlreadyStarted() {
            if (!mPredictionModelListener.onApiCallStopped()) {
                return;
            }

            mPredictionModelListener.onMatchAlreadyStarted();
        }
    };

    private void lockAnswerTime() {
        mAnswerLockedTimeInMs = Calendar.getInstance().getTimeInMillis();
    }

    private long getTimeSpent() {
        long timeSpent = mAnswerLockedTimeInMs - mQuestionSeenTimeInMs;
        mQuestionSeenTimeInMs = Calendar.getInstance().getTimeInMillis();
        mAnswerLockedTimeInMs = mQuestionSeenTimeInMs;
        return timeSpent;
    }

    private List<Question> getDummyQuestionList() {
        String jsonData = "[\n" +
                "    {\n" +
                "      \"question_id\": 1,\n" +
                "      \"match_id\": 1,\n" +
                "      \"question_text\": \"How many runs will MS Dhoni score in the final?\",\n" +
                "      \"question_context\": \"It is the final of the 2011 World Cup. Indian captain MS Dhoni hasn't had a great WC so far, having scored only 150 runs over the last 8 matches.\",\n" +
                "      \"question_points\": 10,\n" +
                "      \"question_neg_points\": -4,\n" +
                "      \"question_option_1\": \"Less than 50\",\n" +
                "      \"question_image_1\": \"https://cdn-images.spcafe.in/img/es3/scGame/dummygame/dhoni-dummygame1.png\",\n" +
                "      \"question_option_2\": \"50 or more\",\n" +
                "      \"question_image_2\": \"https://cdn-images.spcafe.in/img/es3/scGame/dummygame/dhoni-dummygame2.png\",\n" +
                "      \"question_option_3\": null,\n" +
                "      \"question_answer\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"question_id\": 2,\n" +
                "      \"match_id\": 1,\n" +
                "      \"question_text\": \"Which of these countries will win the World Cup?\",\n" +
                "      \"question_context\": \"The year is 2014 and FIFA World Cup is returning to Latin America. South American teams have always won the WC when held in their backyard. With stars like Neymar and Messi in the region, victory does seem inevitable.\",\n" +
                "      \"question_points\": 10,\n" +
                "      \"question_neg_points\": -4,\n" +
                "      \"question_option_1\": \"Brazil\",\n" +
                "      \"question_image_1\": \"https://cdn-images.spcafe.in/img/es3/scGame/dummygame/brazil.png\",\n" +
                "      \"question_option_2\": \"Argentina\",\n" +
                "      \"question_image_2\": \"https://cdn-images.spcafe.in/img/es3/scGame/dummygame/argentina.png\",\n" +
                "      \"question_option_3\": \"Neither\",\n" +
                "      \"question_answer\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"question_id\": 3,\n" +
                "      \"match_id\": 1,\n" +
                "      \"question_text\": \"Who will win the coveted gold in this thriller?\",\n" +
                "      \"question_context\": \"India has its chance for the first Gold in Rio, with PV Sindhu facing off Carolina Marin in the final's of women's badminton. In the 7 times they have played before, Marin has won 4 times and Sindhu 3 times. \",\n" +
                "      \"question_points\": 10,\n" +
                "      \"question_neg_points\": -4,\n" +
                "      \"question_option_1\": \"PV Sindhu\",\n" +
                "      \"question_image_1\": \"https://cdn-images.spcafe.in/img/es3/scGame/dummygame/sindhu-dummygame.png\",\n" +
                "      \"question_option_2\": \"Carolina Marin\",\n" +
                "      \"question_image_2\": \"https://cdn-images.spcafe.in/img/es3/scGame/dummygame/marin-dummygame.png\",\n" +
                "      \"question_option_3\": null,\n" +
                "      \"question_answer\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"question_id\": 4,\n" +
                "      \"match_id\": 1,\n" +
                "      \"question_text\": \"How many sets will this epic battle last?\",\n" +
                "      \"question_context\": \"2017 Australian Open Final - Federer vs Nadal. The clash of legends. Their previous three face-offs in Australian Open have lasted 5, 4 and 3 sets respectively.\",\n" +
                "      \"question_points\": 10,\n" +
                "      \"question_neg_points\": -4,\n" +
                "      \"question_option_1\": 4,\n" +
                "      \"question_image_1\": \"https://cdn-images.spcafe.in/img/es3/scGame/dummygame/4-dummygame.png\",\n" +
                "      \"question_option_2\": 5,\n" +
                "      \"question_image_2\": \"https://cdn-images.spcafe.in/img/es3/scGame/dummygame/5-dummygame.png\",\n" +
                "      \"question_option_3\": \"Neither\",\n" +
                "      \"question_answer\": null\n" +
                "    }\n" +
                "  ]";
        return MyWebService.getInstance().getObjectFromJson(jsonData,
                new TypeReference<List<Question>>() {
                });
    }

    public interface OnPredictionModelListener {

        void onSuccessCompletion(Bundle bundle);

        void onShowingLastQuestion();

        void onNoInternet();

        void onNoInternetForQuestions();

        void onSuccessQuestions(List<Question> questions, SwipeFlingAdapterView.OnSwipeListener<Question> listener);

        void onFailedQuestions(String message);

        void onNoQuestions();

        void onQuestionChanged(Question question, int initialCount, boolean neitherAvailable);

        void onFailedAudiencePollResponse();

        void notifyTopQuestion();

        void onGetSport(Integer sportId);

        void on2xApplied(int count, boolean reverse);

        void onNonegsApplied(int count, boolean reverse);

        void onAudiencePollApplied(int count, boolean reverse);

        void onPostAnswerFailed(String message);

        void onMatchAlreadyStarted();

        void noInternetOnPostingAnswer();

        void onDummyGameCompletion();

        void onApiCallStarted();

        boolean onApiCallStopped();

        void onNoPowerUps();
    }
}