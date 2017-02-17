package in.sportscafe.nostragamus.module.play.myresults;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.ExceptionTracker;

import org.parceler.Parcels;

import java.io.File;
import java.util.List;
import java.util.UUID;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresults.dto.ReplayPowerupResponse;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
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

    private static final int LIMIT = 5;

    private int matchId;

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
        if (bundle.containsKey(BundleKeys.MATCH_LIST)) {
            match = Parcels.unwrap(bundle.getParcelable(BundleKeys.MATCH_LIST));
            matchId = match.getId();
        } else if (bundle.containsKey(BundleKeys.MATCH_ID)) {
            String match_id = bundle.getString(BundleKeys.MATCH_ID);
            matchId = Integer.parseInt(match_id);
        } else {
            mResultsModelListener.onFailedMyResults(Constants.Alerts.RESULTS_INFO_ERROR);
            mResultsModelListener.gotoResultsTimeline();
        }

        if (bundle.containsKey(BundleKeys.PLAYER_ID)) {
            mPlayerUserId = bundle.getInt(BundleKeys.PLAYER_ID);
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
        return mResultAdapter = new MyResultsAdapter(context, null == mPlayerUserId);
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
        mResultAdapter.showFlipOptnforQuestion();
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
        return match.getTournamentName();
    }

    @Override
    public void uploadScreenShot(File file) {
        if (checkInternet()) {
            callUploadScreenShotApi(file, "sportscafetest/nostragamus/", UUID.randomUUID().toString() + file.getName());
        }
    }

    private void callUploadScreenShotApi(File file, String filepath, String filename) {
        MyWebService.getInstance().getUploadPhotoRequest(file, filepath, filename)
                .enqueue(new NostragamusCallBack<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful()) {
                            getSharableLink(response.body().getResult());
                        } else {
                            mResultsModelListener.onScreenShotFailed();
                        }
                    }

                });
    }

    private void getSharableLink(String screenShotUrl) {
        Context context = mResultsModelListener.getContext();
        BranchUniversalObject buo = new BranchUniversalObject()
                .setTitle("My Result")
                .setContentDescription(String.format(
                        context.getString(R.string.fb_share_result_text),
                        getMatchResult(),
                        getMatchName(),
                        getMatchPoints()
                ))
                .setContentImageUrl(screenShotUrl)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata(BundleKeys.USER_REFERRAL_ID, NostragamusDataHandler.getInstance().getUserId());


        LinkProperties linkProperties = new LinkProperties()
                .addTag("myResult")
                .setFeature("myResult")
                .addControlParameter("$android_deeplink_path", "myResult/share/");

        buo.generateShortUrl(mResultsModelListener.getContext(), linkProperties,
                new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (null == error) {
                            mResultsModelListener.onScreenShotUploaded(url);
                        } else {
                            mResultsModelListener.onScreenShotFailed();
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

    @Override
    public void onClickLeaderBoard(int position) {

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

        void gotoResultsTimeline();

        void onFailedReplayPowerupResponse();

        void onSuccessReplayPowerupResponse(Match match);

        void onsetMatchDetails(Match match);

        void onScreenShotUploaded(String url);

        void onScreenShotFailed();
    }
}