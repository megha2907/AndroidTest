package in.sportscafe.nostragamus.module.allchallenges;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants.GameAttemptedStatus;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.allchallenges.dto.AllChallengesResponse;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengesDataResponse;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class AllChallengesApiModelImpl {

    private List<Challenge> mAllChallenges = new ArrayList<>();

    private List<Challenge> mCompletedChallenges = new ArrayList<>();

    private List<Challenge> mInPlayChallenges = new ArrayList<>();

    private List<Challenge> mNewChallenges = new ArrayList<>();

    private OnAllChallengesApiModelListener mAllChallengesApiModelListener;

    public AllChallengesApiModelImpl(OnAllChallengesApiModelListener listener) {
        this.mAllChallengesApiModelListener = listener;
    }

    public static AllChallengesApiModelImpl newInstance(OnAllChallengesApiModelListener listener) {
        return new AllChallengesApiModelImpl(listener);
    }

    public void getAllChallenges() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callChallengesApi();
        } else {
            mAllChallengesApiModelListener.onNoInternet();
        }
    }

    /**
     * The challenges which were expired are considered as completed challenges
     *
     * @return the completed challenge list
     */
    public List<Challenge> getCompletedChallenges() {
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

    private void callChallengesApi() {
        mAllChallengesApiModelListener.onApiCallStarted();

        MyWebService.getInstance().getAllChallengesRequest().enqueue(
                new NostragamusCallBack<AllChallengesResponse>() {
                    @Override
                    public void onResponse(Call<AllChallengesResponse> call, Response<AllChallengesResponse> response) {
                        super.onResponse(call, response);

                        if (!mAllChallengesApiModelListener.onApiCallStopped()) {
                            return;
                        }

                        if (response.isSuccessful() && response.body() != null && response.body().getResponse() != null) {
                            ChallengesDataResponse dataResponse = response.body().getResponse();

                            mCompletedChallenges = dataResponse.getCompletedChallenges();
                            mNewChallenges = dataResponse.getNewChallenges();
                            mInPlayChallenges = dataResponse.getInPlayChallenges();

                            mAllChallengesApiModelListener.onSuccessAllChallengesApi();

                        } else {
                            mAllChallengesApiModelListener.onFailedAllChallengesApi(response.message());
                        }
                    }
                }
        );
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