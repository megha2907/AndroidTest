package in.sportscafe.nostragamus.module.allchallenges;

import android.os.Bundle;
import android.text.TextUtils;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.GameAttemptedStatus;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.allchallenges.dto.AllChallengesResponse;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengesDataResponse;
import in.sportscafe.nostragamus.module.common.TimeResponse;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.settings.app.AppSettingsModel;
import in.sportscafe.nostragamus.module.settings.app.AppSettingsModelImpl;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Query;

/**
 * Created by Jeeva on 14/6/16.
 */
public class AllChallengesApiModelImpl implements AllChallengesApiModel {

    private String CURRENT_CHALLENGES = "current";

    private String COMPLETED_CHALLENGES = "completed";

    private static final int DEFAULT_LIMIT = 10;

    private ChallengesDataResponse mDataResponse;

    private List<Challenge> mAllChallenges = new ArrayList<>();

    private List<Challenge> mCompletedChallenges = new ArrayList<>();

    private List<Challenge> mInPlayChallenges = new ArrayList<>();

    private List<Challenge> mNewChallenges = new ArrayList<>();

    private OnAllChallengesApiModelListener mAllChallengesApiModelListener;

    private Bundle mbundle;

    public AllChallengesApiModelImpl(OnAllChallengesApiModelListener listener) {
        this.mAllChallengesApiModelListener = listener;
    }

    public static AllChallengesApiModelImpl newInstance(OnAllChallengesApiModelListener listener) {
        return new AllChallengesApiModelImpl(listener);
    }

    public AllChallengesApiModelImpl() {
    }

    public static AllChallengesApiModelImpl newInstance() {
        return new AllChallengesApiModelImpl();
    }


    public void getAllChallenges(Bundle bundle) {
        mbundle = bundle;
        if (null != mbundle) {
            if (mbundle.containsKey(Constants.BundleKeys.LOGIN_SCREEN)) {
                callChallengesApi(null);
            }else{
                if (mCompletedChallenges.isEmpty()){
                    callChallengesApi(null);
                }else {
                    callChallengesApi(CURRENT_CHALLENGES);
                }
            }
        } else {
            callChallengesApi(CURRENT_CHALLENGES);
        }
    }

    @Override
    public void getAllCompletedChallenge() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callCompletedChallengesApi(COMPLETED_CHALLENGES, 0, DEFAULT_LIMIT, false);
        }
    }

    /**
     * The challenges which were expired are considered as completed challenges
     *
     * @return the completed challenge list
     */
    public List<Challenge> getCompletedChallenges() {
        mCompletedChallenges = Nostragamus.getInstance().getServerDataManager().getCompletedChallenges();
        return mCompletedChallenges;
    }

    /**
     * The challenges which are currently in-active and that are already started
     * to play by the user are considered as in-play challenges
     *
     * @return the in-play challenge list
     */
    public List<Challenge> getInPlayChallenges() {
        return mInPlayChallenges;
    }

    /**
     * The challenges which are currently in-active, but not yet played by the user
     * are considered as new challenges
     *
     * @return the new challenge list
     */
    public List<Challenge> getNewChallenges() {
        return mNewChallenges;
    }

    private void callChallengesApi(String filter) {
        mAllChallengesApiModelListener.onApiCallStarted();

        MyWebService.getInstance().getAllChallengesRequest(filter).enqueue(
                new NostragamusCallBack<AllChallengesResponse>() {
                    @Override
                    public void onResponse(Call<AllChallengesResponse> call, Response<AllChallengesResponse> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful() && response.body() != null && response.body().getResponse() != null) {
                            mDataResponse = response.body().getResponse();

                            mNewChallenges = mDataResponse.getNewChallenges();
                            mInPlayChallenges = mDataResponse.getInPlayChallenges();
                            if (null != mbundle) {
                                if (mCompletedChallenges.isEmpty()) {
                                    mCompletedChallenges = mDataResponse.getCompletedChallenges();
                                    Nostragamus.getInstance().getServerDataManager().setCompletedChallenges(mCompletedChallenges);
                                }
                            }
                            callServerTimeApi();

                        } else {
                            mAllChallengesApiModelListener.onFailedAllChallengesApi(response.message());
                        }
                    }
                }
        );
    }

    private void callCompletedChallengesApi(String filter, int skip, int limit, final boolean showChallenge) {

        MyWebService.getInstance().getCompletedChallengesRequest(filter, skip, limit).enqueue(
                new NostragamusCallBack<AllChallengesResponse>() {
                    @Override
                    public void onResponse(Call<AllChallengesResponse> call, Response<AllChallengesResponse> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful() && response.body() != null && response.body().getResponse() != null) {
                            ChallengesDataResponse dataResponse = response.body().getResponse();
                            mCompletedChallenges = dataResponse.getCompletedChallenges();
                            Nostragamus.getInstance().getServerDataManager().setCompletedChallenges(mCompletedChallenges);
                            if (showChallenge) {
                                mAllChallengesApiModelListener.onSuccessAllChallengesApi();
                            }
                        }
                    }
                }
        );
    }


    private void callServerTimeApi() {

        MyWebService.getInstance().getServerTime().enqueue(
                new NostragamusCallBack<TimeResponse>() {
                    @Override
                    public void onResponse(Call<TimeResponse> call, Response<TimeResponse> response) {
                        super.onResponse(call, response);

                        if (!mAllChallengesApiModelListener.onApiCallStopped()) {
                            return;
                        }

                        if (response.isSuccessful() && response.body() != null && response.body().getServerTime() != null) {

                            String serverTime = response.body().getServerTime();

                            setServerTimeForGloballyAvailability(serverTime);

                            mAllChallengesApiModelListener.onSuccessAllChallengesApi();

                        } else {
                            mAllChallengesApiModelListener.onSuccessAllChallengesApi();
                        }
                    }
                }
        );
    }

    /**
     * Always set newly received server-time
     *
     * @param serverTime
     */
    private void setServerTimeForGloballyAvailability(String serverTime) {
        if (!TextUtils.isEmpty(serverTime)) {
            try {
                long time = Long.parseLong(serverTime);
                Nostragamus.getInstance().setServerTime(time);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void callAllChallengesApi() {
        mAllChallengesApiModelListener.onApiCallStarted();

        /*MyWebService.getInstance().getAllChallengesRequest().enqueue(
                new NostragamusCallBack<AllChallengesResponse>() {
                    @Override
                    public void onResponse(Call<AllChallengesResponse> call, Response<AllChallengesResponse> response) {
                        super.onResponse(call, response);

                        if (!mAllChallengesApiModelListener.onApiCallStopped()) {
                            return;
                        }

                        if (response.isSuccessful() && response.body() != null) {
                            mAllChallenges = response.body().getChallenges();

                            saveChallengesToServerDataManager(mAllChallenges);

                            if (mAllChallenges.isEmpty()) {
                                mAllChallengesApiModelListener.onEmpty();
                            } else {
                                categorizeChallenges();
                                mAllChallengesApiModelListener.onSuccessAllChallengesApi();
                            }
                        } else {
                            mAllChallengesApiModelListener.onFailedAllChallengesApi(response.message());
                        }
                    }
                }
        );*/
    }

    private void saveChallengesToServerDataManager(List<Challenge> challenges) {
        Nostragamus.getInstance().getServerDataManager().setChallengeList(challenges);
    }

    /*
    * Logic to filter challenges provided by Vignesh
    *
        //assuming c is each row of the response
    if(c.challenge_info.isClosed){ //closed and executed
      return 'Completed';
    }else if(!c.user_challenge_info){ //user not joined
      if(c.count_matches_left == 0 )return 'Completed';
      else return 'New';
    }else { //user joined and challenge not closed
      return 'In Play';
    }
     */
    private void categorizeChallenges() {
        for (Challenge challenge : mAllChallenges) {
            /*if (challenge.getCountMatchesLeft().equals("0")) {
                // If the endtime of the challenge is fell inside the current time, then it is completed challenge
                mCompletedChallenges.add(challenge);
            } else if (isChallengeInitiatedByUser(challenge) || challenge.getChallengeUserInfo().isUserJoined()) {
                mInPlayChallenges.add(challenge);
            } else {
                mNewChallenges.add(challenge);
            }*/


            if (challenge.getChallengeInfo().isClosed()) {
                mCompletedChallenges.add(challenge);        // Completed

            } else if (!challenge.getChallengeUserInfo().isUserJoined()) {

                if (challenge.getCountMatchesLeft().equals("0")) {
                    mCompletedChallenges.add(challenge);        // Completed

                } else {
                    mNewChallenges.add(challenge);      // New
                }
            } else {
                mInPlayChallenges.add(challenge);       // InPlay
            }
        }
    }

    private boolean isChallengeInitiatedByUser(Challenge challenge) {
        if (challenge.getMatchesCategorized() != null) {
            List<Match> matches = challenge.getMatchesCategorized().getAllMatches();
            if (matches != null && !matches.isEmpty()) {
                for (Match match : matches) {
                    if (GameAttemptedStatus.NOT != match.getisAttempted()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public interface OnAllChallengesApiModelListener {

        void onSuccessAllChallengesApi();

        void onEmpty();

        void onFailedAllChallengesApi(String message);

        void onNoInternet();

        void onApiCallStarted();

        boolean onApiCallStopped();
    }
}