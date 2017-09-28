package in.sportscafe.nostragamus.module.play.myresults;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.api.Api;
import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.inPlay.ui.ResultsScreenDataDto;
import in.sportscafe.nostragamus.module.play.myresults.dto.ReplayPowerupResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Answer;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.webservice.ChangeAnswer;
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

    private static final int PAGINATION_START_AT = 5;

    private MyResultsAdapter.OnMyResultsActionListener mResultsActionListener;

    private static final int LIMIT = 5;

    private int matchId = 135;

    private MyResultsAdapter mResultAdapter;

    private Match match;

    private Integer mPlayerUserId;

    private OnMyResultsModelListener mResultsModelListener;

    private MyResultsModelImpl(OnMyResultsModelListener listener) {
        this.mResultsModelListener = listener;
    }

    public static MyResultsModel newInstance(OnMyResultsModelListener listener) {
        return new MyResultsModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {

        if (bundle!=null) {

            if (bundle.containsKey(BundleKeys.RESULTS_SCREEN_DATA)) {
                ResultsScreenDataDto resultsScreenData = Parcels.unwrap(bundle.getParcelable(BundleKeys.RESULTS_SCREEN_DATA));
                matchId = resultsScreenData.getMatchId();
            }

            /*
            if (bundle.containsKey(BundleKeys.MATCH_LIST)) {
                match = Parcels.unwrap(bundle.getParcelable(BundleKeys.MATCH_LIST));
               // Match=Nostragamus.getInstance().getServerDataManager().getMatchInfo();
                matchId = match.getId();
                if (null == match.getResult() || match.getResult().isEmpty()) {
                    mResultsModelListener.setToolbarHeading("Awaiting Results");

                    Boolean playedFirstMatch = bundle.getBoolean(BundleKeys.PLAYED_FIRST_MATCH);

                    if (playedFirstMatch){
                        mResultsModelListener.showResultsToBeDeclared(true, match);
                    }else {
                        mResultsModelListener.showResultsToBeDeclared(false, match);
                    }
                }
               if (bundle.containsKey(Constants.ScreenNames.PLAY)){


                    Boolean playedFirstMatch = bundle.getBoolean(BundleKeys.PLAYED_FIRST_MATCH);
                    if (playedFirstMatch){
                       mResultsModelListener.showResultsToBeDeclared(true, Match);
                    }else {
                        mResultsModelListener.showResultsToBeDeclared(false, Match);
                    }
                }

            } else if (bundle.containsKey(BundleKeys.MATCH_ID)) {
                String match_id = bundle.getString(BundleKeys.MATCH_ID);
                matchId = Integer.parseInt(match_id);
            } else {
                mResultsModelListener.onFailedMyResults(Constants.Alerts.RESULTS_INFO_ERROR);
            }

            if (bundle.containsKey(BundleKeys.PLAYER_ID)) {
                mPlayerUserId = bundle.getInt(BundleKeys.PLAYER_ID);
            }

           */


        } else {
            mResultsModelListener.onFailedMyResults(Constants.Alerts.RESULTS_INFO_ERROR);
        }

        //no replay and flip powerup for now
        //UserInfoModelImpl.newInstance(this).getUserInfo();
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
        MyWebService.getInstance().getMyResultsRequest(matchId, mPlayerUserId).enqueue(
                new NostragamusCallBack<MyResultsResponse>() {
                    @Override
                    public void onResponse(Call<MyResultsResponse> call, Response<MyResultsResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            Context context = mResultsModelListener.getContext();
                            if (null != context) {

                                List<Match> myResultList = response.body().getMyResults();

                                if (myResultList.isEmpty() || myResultList == null) {
                                    mResultsModelListener.onFailedMyResults(response.message());
                                } else {
                                    destroyAdapter();
                                    match = myResultList.get(0);
                                    mResultsModelListener.onSuccessMyResults(createAdapter(context));
                                    mResultsModelListener.onsetMatchDetails(myResultList.get(0));
                                    loadAdapterData(myResultList.get(0));

                                    if (myResultList.isEmpty()) {
                                        mResultsModelListener.onEmpty();
                                    }
                                }
                            }
                        } else {
                            mResultsModelListener.onFailedMyResults(response.message());
                        }
                    }
                }
        );
    }

    private MyResultsAdapter createAdapter(Context context) {
        mResultAdapter = new MyResultsAdapter(context, null == mPlayerUserId);
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
    public String getMatchName() {
        return match.getChallengeName();
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
                                            NostragamusDataHandler.getInstance().getUserInfo().getUserName(),
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

    private void callChangeAnswerApi(Integer questionId, Integer answerId) {

        ChangeAnswer changeAnswer = new ChangeAnswer(questionId, answerId);
        mResultsModelListener.StartProgressbar();
        MyWebService.getInstance().getChangeAnswerRequest(changeAnswer).enqueue(new NostragamusCallBack<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
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

    }

    @Override
    public void onClickLeaderBoard(int position) {

    }

    @Override
    public void onClickEditAnswer(int selectedQuestionId, Question question) {
        mResultAdapter.changeToEditAnswers(selectedQuestionId, question);
    }

    @Override
    public void saveUpdatedAnswer(int QuestionId, int AnswerId) {
        callChangeAnswerApi(QuestionId, AnswerId);
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

        void showResultsToBeDeclared(boolean playedFirstMatchm, Match match);

        void onSuccessChangeAnswerResponse(Match match);

        void onFailedChangeAnswerResponse();

        void StartProgressbar();
    }
}