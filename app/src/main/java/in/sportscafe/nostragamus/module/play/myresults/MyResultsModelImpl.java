package in.sportscafe.nostragamus.module.play.myresults;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedContestDto;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.common.ErrorResponse;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.ui.ResultsScreenDataDto;
import in.sportscafe.nostragamus.module.play.myresults.dto.ReplayPowerupResponse;
import in.sportscafe.nostragamus.module.resultspeek.dto.Match;
import in.sportscafe.nostragamus.module.resultspeek.dto.Question;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dto.SaveEditAnswerRequest;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsModelImpl implements MyResultsModel, MyResultsAdapter.OnMyResultsActionListener, UserInfoModelImpl.OnGetUserInfoModelListener {

    private MyResultsAdapter.OnMyResultsActionListener mResultsActionListener;

    private int matchId = 0;

    private int roomId = 0;

    private MyResultsAdapter mResultAdapter;

    private InPlayContestDto mInPlayContest=null;

    private CompletedContestDto mCompletedContestDto =null;

    private Match match;

    private String contestName = "";

    private Integer mPlayerUserId;

    private OnMyResultsModelListener mResultsModelListener;
    private ResultsScreenDataDto mResultsScreenData;

    private MyResultsModelImpl(OnMyResultsModelListener listener) {
        this.mResultsModelListener = listener;
    }

    public static MyResultsModel newInstance(OnMyResultsModelListener listener) {
        return new MyResultsModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {

        if (bundle != null) {

            /* Get MatchId, Room Id and Player Id */
            if (bundle.containsKey(BundleKeys.RESULTS_SCREEN_DATA)) {

                mResultsScreenData = Parcels.unwrap(bundle.getParcelable(BundleKeys.RESULTS_SCREEN_DATA));
                mResultsScreenData.setHeadLess(bundle.getBoolean(BundleKeys.IS_HEADLESS_FLOW, false));

                matchId = mResultsScreenData.getMatchId();
                roomId = mResultsScreenData.getRoomId();
                contestName = mResultsScreenData.getSubTitle();
                String matchStatus = mResultsScreenData.getMatchStatus();
                mInPlayContest = mResultsScreenData.getInPlayContestDto();

                /* Check for Awaiting Results to Change Toolbar Heading */
                if (!TextUtils.isEmpty(matchStatus) && (
                        matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.ANSWER)
                        || matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.CONTINUE)
                        || matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.PLAY))) {

                    mResultsModelListener.setToolbarHeading("Awaiting Results");
                }

                /* Send Player Id to Api to check Other Person Results */
                if (bundle.containsKey(BundleKeys.PLAYER_ID)) {
                    mPlayerUserId = bundle.getInt(BundleKeys.PLAYER_ID);
                }
            }

            if (bundle.containsKey(BundleKeys.INPLAY_CONTEST)){
                 mInPlayContest = Parcels.unwrap(bundle.getParcelable(Constants.BundleKeys.INPLAY_CONTEST));
            }

            if (bundle.containsKey(BundleKeys.COMPLETED_CONTEST)){
                mCompletedContestDto = Parcels.unwrap(bundle.getParcelable(BundleKeys.COMPLETED_CONTEST));
            }

        } else {
            mResultsModelListener.onFailedMyResults(Constants.Alerts.RESULTS_INFO_ERROR);
        }

    }

    @Override
    public void getMyResultsData(Context context) {
        if (checkInternet()) {
            callMyResultsApi();
        }
    }

    private boolean checkInternet() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            return true;
        }
        mResultsModelListener.onNoInternet();
        return false;
    }

    private void callMyResultsApi() {
        MyWebService.getInstance().getMyResultsRequest(matchId, mPlayerUserId, roomId).enqueue(
                new NostragamusCallBack<MyResultsResponse>() {
                    @Override
                    public void onResponse(Call<MyResultsResponse> call, Response<MyResultsResponse> response) {
                        super.onResponse(call, response);
                        Context context = mResultsModelListener.getContext();
                        if (response.isSuccessful() && response.body() != null && response.body() != null) {
                            if (null != context) {
                                match = response.body().getMyResults();
                                if (match != null) {
                                    destroyAdapter();
                                    onResultsDataSuccess(match);
                                } else {
                                    mResultsModelListener.onFailedMyResults(response.message());
                                }
                            }
                        } else {
                            mResultsModelListener.onFailedMyResults(response.message());
                        }
                    }
                }
        );
    }

    private void onResultsDataSuccess(Match match) {
        if (match != null) {
            mResultsModelListener.onSuccessMyResults(createAdapter(mResultsModelListener.getContext()));
            mResultsModelListener.onsetMatchDetails(match);
            loadAdapterData(match);
            //checkIfFirstMatchPlayed();
        }
    }

    private void checkIfFirstMatchPlayed() {
        /* Check if it's the First Match Played */
        if (NostragamusDataHandler.getInstance().getMatchPlayedCount() == 0
                && !NostragamusDataHandler.getInstance().isPlayedFirstMatch()) {
            mResultsModelListener.showResultsToBeDeclared(true);
        } else {
            mResultsModelListener.showResultsToBeDeclared(false);
        }
    }

    private MyResultsAdapter createAdapter(Context context) {
        InPlayContestDto contestDto = mInPlayContest;
        if (contestDto == null) {
            if (mResultsScreenData.getInPlayContestDto() != null) {
                contestDto = mResultsScreenData.getInPlayContestDto();
            }
        }

        mResultAdapter = new MyResultsAdapter(context, null == mPlayerUserId,
                mResultsScreenData,
                contestDto,
                mCompletedContestDto);
        mResultAdapter.setResultsActionListener(this);

        return mResultAdapter;
    }

    private void destroyAdapter() {
        if (null != mResultAdapter) {
            mResultAdapter.destroy();
            mResultAdapter = null;
        }
    }

    private void loadAdapterData(Match match) {
        mResultAdapter.clear();
        mResultAdapter.add(match);
    }

    @Override
    public void callReplayPowerupApplied() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callReplayPowerupAppliedApi(Powerups.MATCH_REPLAY, matchId);
        } else {
            mResultsModelListener.onNoInternet();
        }
    }

    @Override
    public void showFlipQuestion() {
        mResultAdapter.notifyDataSetChanged();
    }

    @Override
    public String getMatchResult() {
        return match.getCorrectCount() + "/" + match.getMatchQuestionCount();
    }

    @Override
    public int getMatchPoints() {
        return match.getMatchPoints();
    }

    @Override
    public InPlayContestDto getInPlayContest() {
        return mInPlayContest;
    }

    @Override
    public String getMatchName() {
        String challengeName = (!TextUtils.isEmpty(match.getChallengeName())) ? match.getChallengeName() : "";
        String contestNameStr = (!TextUtils.isEmpty(contestName)) ? " - "+contestName : "";
        return challengeName + contestNameStr;
    }

    @Override
    public void getShareText() {
        BranchUniversalObject buo = new BranchUniversalObject()
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata(BundleKeys.USER_REFERRAL_ID, NostragamusDataHandler.getInstance().getUserId());

        LinkProperties linkProperties = new LinkProperties()
                .addTag("myResult")
                .setFeature("myResult")
                .setChannel("App")
                .addControlParameter("$android_deeplink_path", "myResult/share/");

        buo.generateShortUrl(mResultsModelListener.getContext(), linkProperties,
                new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (null == error) {
                            mResultsModelListener.onGetShareText(
                                    String.format(
                                            mResultsModelListener.getContext().getString(R.string.fb_share_result_text),
                                            Nostragamus.getInstance().getServerDataManager().getUserInfo().getUserName(),
                                            getMatchResult(),
                                            getMatchName(),
                                            getMatchPoints(),
                                            url
                                    )
                            );
                        } else {
                            mResultsModelListener.onGetShareTextFailed();
                            ExceptionTracker.track(error.getMessage());
                        }
                    }
                });
    }

    @Override
    public String getMatchEndTime() {
        return match.getEndTime();
    }

    @Override
    public Match getMatch() {
        return match;
    }


    private void callReplayPowerupAppliedApi(final String powerupId, Integer matchId) {
        MyWebService.getInstance().getReplayPowerup(powerupId, matchId).enqueue(new NostragamusCallBack<ReplayPowerupResponse>() {
            @Override
            public void onResponse(Call<ReplayPowerupResponse> call, Response<ReplayPowerupResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful()) {
                    if (response.body().getResponse().equals(null) || response.body().getResponse().equalsIgnoreCase("failure")) {
                        mResultsModelListener.onFailedReplayPowerupResponse();
                    } else {
                        NostragamusAnalytics.getInstance().trackPowerups(AnalyticsActions.APPLIED, powerupId);
                        mResultsModelListener.onSuccessReplayPowerupResponse(match);
                    }

                } else {
                    mResultsModelListener.onFailedReplayPowerupResponse();
                }
            }
        });

    }

    /*private void callChangeAnswerApi(Integer matchId,Integer questionId, Integer answerId, int roomId) {

        SaveEditAnswerRequest saveEditAnswerRequest = new SaveEditAnswerRequest(matchId,questionId, answerId, roomId);
        mResultsModelListener.StartProgressbar();
        MyWebService.getInstance().saveEditAnswer(saveEditAnswerRequest).enqueue(new NostragamusCallBack<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                super.onResponse(call, response);
                if (response.code() == 400 && response.errorBody() != null) {

                    ErrorResponse errorResponse = null;
                    try {
                        errorResponse = MyWebService.getInstance().getObjectFromJson(response.errorBody().string(), ErrorResponse.class);
                    } catch (Exception e) {e.printStackTrace();}

                    if (errorResponse != null) {
                        mResultsModelListener.onSaveEditedAnswerServerError(errorResponse.getError());
                    } else {
                        mResultsModelListener.onFailedChangeAnswerResponse();
                    }

                } else if (response.isSuccessful()) {
                    if (response.body().equals(null)) {
                        mResultsModelListener.onFailedChangeAnswerResponse();
                    } else {
                        mResultsModelListener.onSuccessChangeAnswerResponse(match);
                    }

                } else {
                    mResultsModelListener.onFailedReplayPowerupResponse();
                }
            }
        });

    }*/

    @Override
    public void onClickLeaderBoard(int position) {

    }

    @Override
    public void onClickEditAnswer(int selectedQuestionId, Question question) {
        mResultAdapter.changeToEditAnswers(selectedQuestionId, question);
    }

    @Override
    public void saveUpdatedAnswer(int matchId,int QuestionId, int AnswerId, int roomId) {
//        callChangeAnswerApi(matchId,QuestionId, AnswerId, roomId);
    }


    @Override
    public void onSuccessGetUpdatedUserInfo(UserInfo updatedUserInfo) {
    }

    @Override
    public void onFailedGetUpdateUserInfo(String message) {
    }

    @Override
    public void onNoInternet() {

    }

    public interface OnMyResultsModelListener {

        void onSuccessMyResults(MyResultsAdapter myResultsAdapter);

        void onFailedMyResults(String message);

        void onNoInternet();

        void onEmpty();

        Context getContext();

        void onFailedReplayPowerupResponse();

        void onSuccessReplayPowerupResponse(Match match);

        void onsetMatchDetails(Match match);

        void onGetShareText(String shareText);

        void onGetShareTextFailed();

        void setToolbarHeading(String result);

        void showResultsToBeDeclared(boolean playedFirstMatch);

        void onSuccessChangeAnswerResponse(Match match);

        void onFailedChangeAnswerResponse();

        void StartProgressbar();

        void onSaveEditedAnswerServerError(String msg);
    }
}