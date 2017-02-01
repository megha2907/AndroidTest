package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Config;
import in.sportscafe.nostragamus.Config.Sports;
import in.sportscafe.nostragamus.Constants.AnswerIds;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.prediction.dto.Answer;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePoll;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollRequest;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.prediction.dto.QuestionsResponse;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroups;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;
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

        updateInitialPowerups();
    }

    public static PredictionModel newInstance(OnPredictionModelListener predictionModelListener) {
        return new PredictionModelImpl(predictionModelListener);
    }

    private void updateInitialPowerups() {
        m2xPowerups = mNostragamusDataHandler.getNumberof2xPowerups();
        mNonegsPowerups = mNostragamusDataHandler.getNumberofNonegsPowerups();
        mPollPowerups = mNostragamusDataHandler.getNumberofAudiencePollPowerups();
    }

    @Override
    public void init(Bundle bundle) {

        if (bundle.containsKey(BundleKeys.IS_DUMMY_GAME)) {
            mMyResult = Parcels.unwrap(bundle.getParcelable(BundleKeys.MATCH_LIST));
            mMatchId = mMyResult.getId();

            if (bundle.containsKey(BundleKeys.SPORT_NAME)) {
                mPredictionModelListener.onGetSportName(bundle.getString(BundleKeys.SPORT_NAME));
            }
        } else {
            mDummyGame = true;
            mPredictionModelListener.onGetSportName(Sports.TENNIS);
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
        if (!isDummyGame()) {
            if (Nostragamus.getInstance().hasNetworkConnection()) {
                AudiencePollRequest audiencePollRequest = new AudiencePollRequest();
                audiencePollRequest.setQuestionId(mPredictionAdapter.getTopQuestion().getQuestionId());

                callAudiencePollApi(audiencePollRequest);
            } else {
                mPredictionModelListener.onNoInternet();
            }
        } else {
            handleAudiencePollResponse(getDummyGameAudiencePoll().getAudiencePoll());
            mPredictionModelListener.onSuccessAudiencePollResponse();
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

        if (!mDummyGame) {
            mNostragamusDataHandler.setNumberofAudiencePollPowerups(mPollPowerups);
        }
    }

    private void saveSinglePrediction(Question question, int answerId) {
        if (!mDummyGame) {
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
                    mNostragamusDataHandler.setNumberof2xPowerups(m2xPowerups);
                } else if (Powerups.NO_NEGATIVE.equalsIgnoreCase(powerupId)) {
                    mNostragamusDataHandler.setNumberofNonegsPowerups(mNonegsPowerups);
                }
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