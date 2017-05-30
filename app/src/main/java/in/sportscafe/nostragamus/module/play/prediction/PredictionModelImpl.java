package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;

import com.jeeva.android.ExceptionTracker;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
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
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
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
        //mMyResult = Nostragamus.getInstance().getServerDataManager().getMatchInfo();
        mMyResult = Parcels.unwrap(bundle.getParcelable(BundleKeys.MATCH_LIST));
        mMatchId = mMyResult.getId();

        if (bundle.containsKey(BundleKeys.SPORT_ID)) {
            mPredictionModelListener.onGetSport(bundle.getInt(BundleKeys.SPORT_ID));
        }

        if (bundle.containsKey(BundleKeys.CHALLENGE_INFO)) {
            mChallegeInfo = Parcels.unwrap(bundle.getParcelable(BundleKeys.CHALLENGE_INFO));

            //mChallegeInfo = Nostragamus.getInstance().getServerDataManager().getChallengeInfo();

            HashMap<String, Integer> userPowerUpMap = mChallegeInfo.getChallengeUserInfo().getPowerUps();
            m2xPowerups = userPowerUpMap.get(Powerups.XX);
            mNonegsPowerups = userPowerUpMap.get(Powerups.NO_NEGATIVE);
            mPollPowerups = userPowerUpMap.get(Powerups.AUDIENCE_POLL);

//            HashMap<String, Integer> initialPowerUpMap = mChallegeInfo.getChallengeInfo().getPowerUps();
//
//            if (null != initialPowerUpMap.get(Powerups.XX)) {
//
//                Integer mIl2xPowerups = initialPowerUpMap.get(Powerups.XX);
//                Integer mIlNonegsPowerups = initialPowerUpMap.get(Powerups.NO_NEGATIVE);
//                Integer mIlPollPowerups = initialPowerUpMap.get(Powerups.AUDIENCE_POLL);
//
//                int m2xPowerUpPercent = (m2xPowerups * 100) / mIl2xPowerups;
//                int mNonegsPercent = (mNonegsPowerups * 100) / mIlNonegsPowerups;
//                int mPollPercent = (mPollPowerups * 100) / mIlPollPowerups;
//
//                if (m2xPowerUpPercent < 25 || mNonegsPercent < 25 || mPollPercent < 25){
//                    mPredictionModelListener.showPowerUpCountLessPopUp();
//                }
//
//            }

        }

        NostragamusAnalytics.getInstance().trackPlay(AnalyticsActions.STARTED);
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
    public String getLeftContestName() {
        return String.valueOf(mMyResult.getParties().get(0).getPartyName());
    }

    @Override
    public String getRightContestName() {
        return String.valueOf(mMyResult.getParties().get(1).getPartyName());
    }

    @Override
    public String getLeftContestImageUrl() {
        return String.valueOf(mMyResult.getParties().get(0).getPartyImageUrl());
    }

    @Override
    public String getRightContestImageUrl() {
        return String.valueOf(mMyResult.getParties().get(1).getPartyImageUrl());
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

        /*mPredictionAdapter = new PredictionAdapter(context, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPowerUpAnimation(view);
            }
        });*/

        mPredictionAdapter = new PredictionAdapter(context, new PowerupRemoveListener() {
            @Override
            public void onPwerupRemoved(String powerup) {
                increasePowerUpCount(powerup);
                mPredictionModelListener.notifyTopQuestion();
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

    /*private void dismissPowerUpAnimation(final View view) {
        mPredictionAdapter.dismissPowerUpAnimation(view, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                removeAppliedPowerUp(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }*/

    private void removeAppliedPowerUp(View view) {
        if (view != null) {
            String powerUpOfThisView = (String) view.getTag();
            if (!TextUtils.isEmpty(powerUpOfThisView)) {

                Question topQuestion = mPredictionAdapter.getTopQuestion();
                increasePowerUpCount(powerUpOfThisView);
                topQuestion.removeAppliedPowerUp(powerUpOfThisView);

//                notifyPowerUps();
                /*if (powerUpOfThisView.equals(Powerups.XX)) {
                    mPredictionAdapter.remove2xPowerup();
                } else if (powerUpOfThisView.equals(Powerups.NO_NEGATIVE)) {
                    mPredictionAdapter.removeNonegativePowerup();
                }*/
                mPredictionModelListener.notifyTopQuestion();
            }
        }
    }

    private void increasePowerUpCount(String powerUp) {
        switch (powerUp) {
            case Powerups.XX:
                mPredictionModelListener.on2xApplied(++m2xPowerups, true);
                break;
            case Powerups.NO_NEGATIVE:
                mPredictionModelListener.onNonegsApplied(++mNonegsPowerups, true);
                break;
        }
    }

    private void updatePowerUpStatus(String powerUpId) {
        switch (powerUpId) {
            case Powerups.XX:
                mPredictionModelListener.on2xApplied(m2xPowerups, (Powerups.XX == powerUpId));
                break;
            case Powerups.NO_NEGATIVE:
                mPredictionModelListener.onNonegsApplied(mNonegsPowerups, (Powerups.NO_NEGATIVE == powerUpId));
                break;
            case Powerups.AUDIENCE_POLL:
                mPredictionModelListener.onAudiencePollApplied(mPollPowerups, (Powerups.AUDIENCE_POLL == powerUpId));
                break;
        }
    }

    @Override
    public void apply2xPowerup() {
        if (! isPowerUpApplied(Powerups.XX)) {
            if (m2xPowerups > 0) {
                boolean isApplied = mPredictionAdapter.getTopQuestion().apply2xPowerUp();
                if (isApplied) {
//                    notifyPowerUps();
                    mPredictionAdapter.add2xPowerup();
                    mPredictionModelListener.notifyTopQuestion();

                    m2xPowerups--;
                    mPredictionModelListener.on2xApplied(m2xPowerups, false);
                }
            } else {
                mPredictionModelListener.onNoPowerUps();
            }
        }
    }

    @Override
    public void applyNonegsPowerup() {
        if (! isPowerUpApplied(Powerups.NO_NEGATIVE)) {
            if (mNonegsPowerups > 0) {
                boolean isApplied = mPredictionAdapter.getTopQuestion().applyNonegsPowerUp();
                if (isApplied) {
//                    notifyPowerUps();
                    mPredictionAdapter.addNoNegativePowerup();
                    mPredictionModelListener.notifyTopQuestion();

                    mNonegsPowerups--;
                    mPredictionModelListener.onNonegsApplied(mNonegsPowerups, false);
                }
            } else {
                mPredictionModelListener.onNoPowerUps();
            }
        }
    }

    @Override
    public void applyPollPowerup() {
        if (! isPowerUpApplied(Powerups.AUDIENCE_POLL)) {
            if (mPollPowerups > 0) {
                getAudiencePollPercent();
            } else {
                mPredictionModelListener.onNoPowerUps();
            }
        }
    }

    private boolean isPowerUpApplied(String powerup) {
        if (mPredictionAdapter != null && mPredictionAdapter.getTopQuestion() != null) {
            ArrayList<String> powerupList = mPredictionAdapter.getTopQuestion().getPowerUpArrayList();
            if (powerupList != null) {
                for (String str : powerupList) {
                    if (str.equalsIgnoreCase(powerup)) {
                        return true;  // powerup already applied once
                    }
                }
            }
        }
        return false;
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
                ArrayList<String> powerupsUsed = mPredictionAdapter.getItem(i).getPowerUpArrayList();
                if (powerupsUsed != null) {
                    for (String powerUpStr : powerupsUsed) {
                        if (null != powerUpStr) {
                            switch (powerUpStr) {
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
                }
            }

            mPowerUpUpdated = true;
        }
    }

    @Override
    public boolean isDummyGameShown() {
        return mNostragamusDataHandler.isDummyGameShown()
                || mNostragamusDataHandler.getUserInfo().getTotalMatchesPlayed() > 0;
    }

    @Override
    public void onDummyGameShown() {
        mNostragamusDataHandler.setDummyGameShown(true);
    }

    @Override
    public boolean isQuestionAvailable() {
        return null != mPredictionAdapter && mPredictionAdapter.getCount() > 0;
    }

    @Override
    public void getShareText(Context context) {
        BranchUniversalObject buo = new BranchUniversalObject()
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata(Constants.BundleKeys.USER_REFERRAL_ID, NostragamusDataHandler.getInstance().getUserId());

        LinkProperties linkProperties = new LinkProperties()
                .addTag("question")
                .setFeature("question")
                .setChannel("App")
                .addControlParameter("$android_deeplink_path", "question/share/");

        buo.generateShortUrl(context, linkProperties,
                new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (null == error) {
                            mPredictionModelListener.onGetQuestionShareText(
                                    NostragamusDataHandler.getInstance().getUserInfo().getUserName() + " just need your help to predict the answer for the above question!"
                                    + "\n\nInstead of helping, If you want to predict the same for the cash reward, Click the below link\n" + url
                            );
                        } else {
                            ExceptionTracker.track(error.getMessage());
                        }
                    }
                });
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

        lockAnswerTime();
        NostragamusAnalytics.getInstance().trackPlay(AnalyticsActions.SHUFFLED, AnalyticsLabels.BOTTOM, getTimeSpent());
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == 0) {
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

            /*ArrayList<String> powerupArr = topQuestion.getPowerUpArrayList();
            if (powerupArr != null) {
                for (String powerupStr : powerupArr) {
                    updatePowerUpStatus(powerupStr);
                }
            }*/
            /* Making Powerup buttons full alpha once card is swiped (chosen) */
            updatePowerUpStatus(Powerups.XX);
            updatePowerUpStatus(Powerups.NO_NEGATIVE);
            updatePowerUpStatus(Powerups.AUDIENCE_POLL);

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
        return null == mPredictionAdapter.getTopQuestion().getPowerUpArrayList();
    }

    private void notifyPowerUps() {
        mPredictionAdapter.refreshPowerUps();
        mPredictionModelListener.notifyTopQuestion();
    }

    private void getAudiencePollPercent() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            mPredictionModelListener.onApiCallStarted();
            AudiencePollRequest audiencePollRequest = new AudiencePollRequest();
            audiencePollRequest.setQuestionId(mPredictionAdapter.getTopQuestion().getQuestionId());
            audiencePollRequest.setChallengeId(mChallegeInfo.getChallengeId());

            callAudiencePollApi(audiencePollRequest);
        } else {
            mPredictionModelListener.onNoInternet();
        }
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

        boolean isApplied = mPredictionAdapter.getTopQuestion().applyAudiencePollPowerUp(leftAnswerPercent, rightAnswerPercent);
        if (isApplied) {
//            notifyPowerUps();
            mPredictionAdapter.addAudiencePoll();
            mPredictionModelListener.notifyTopQuestion();

            mPollPowerups--;
            mPredictionModelListener.onAudiencePollApplied(mPollPowerups, false);
        }
    }

    private void saveSinglePrediction(Question question, int answerId) {
        lockAnswerTime();
        mPredictionModelListener.onApiCallStarted();

        question.setAnswerId(answerId);
        mPostAnswerModel.postAnswer(question, mChallegeInfo.getChallengeId(), mPredictionAdapter.getCount() == 0);
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
                bundle.putParcelable(BundleKeys.MATCH_LIST, Parcels.wrap(mMyResult));
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

        void onApiCallStarted();

        boolean onApiCallStopped();

        void onNoPowerUps();

        void showPowerUpCountLessPopUp();

        void onGetQuestionShareText(String shareText);
    }
}