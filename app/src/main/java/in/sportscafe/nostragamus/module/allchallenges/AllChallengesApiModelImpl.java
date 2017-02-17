package in.sportscafe.nostragamus.module.allchallenges;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.DateFormats;
import in.sportscafe.nostragamus.Constants.GameAttemptedStatus;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.allchallenges.dto.AllChallengesResponse;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

import static in.sportscafe.nostragamus.utils.timeutils.TimeUtils.getMillisecondsFromDateString;

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
            callAllChallengesApi();
        } else {
            mAllChallengesApiModelListener.onNoInternet();
        }
    }

    /**
     * The challenges which were expired are considered as completed challenges
     * @return the completed challenge list
     */
    public List<Challenge> getCompletedChallenges() {
        return mCompletedChallenges;
    }

    /**
     * The challenges which are currently in-active and that are already started
     * to play by the user are considered as in-play challenges
     * @return the in-play challenge list
     */
    public List<Challenge> getInPlayChallenges() {
        return mInPlayChallenges;
    }

    /**
     * The challenges which are currently in-active, but not yet played by the user
     * are considered as new challenges
     * @return the new challenge list
     */
    public List<Challenge> getNewChallenges() {
        return mNewChallenges;
    }

    private void callAllChallengesApi() {
        MyWebService.getInstance().getAllChallengesRequest().enqueue(
                new NostragamusCallBack<AllChallengesResponse>() {
                    @Override
                    public void onResponse(Call<AllChallengesResponse> call, Response<AllChallengesResponse> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful()) {
                            mAllChallenges = response.body().getChallenges();
                            if(mAllChallenges.isEmpty()) {
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
        );
    }

    private void categorizeChallenges() {
        long currentTimeInMs = Calendar.getInstance().getTimeInMillis();
        long timeInMs;
        for (Challenge challenge : mAllChallenges) {
            timeInMs = TimeUtils.getMillisecondsFromDateString(
                    challenge.getEndTime(),
                    DateFormats.FORMAT_DATE_T_TIME_ZONE,
                    DateFormats.GMT
            );

            if(currentTimeInMs >= timeInMs) {
                // If the endtime of the challenge is fell inside the current time, then it is completed challenge
                mCompletedChallenges.add(challenge);
            } else {
                timeInMs = TimeUtils.getMillisecondsFromDateString(
                        challenge.getStartTime(),
                        DateFormats.FORMAT_DATE_T_TIME_ZONE,
                        DateFormats.GMT
                );

                if(currentTimeInMs < timeInMs) {
                    // If the current time fell inside the starttime of the challenge, then it is upcoming challenge
                    mNewChallenges.add(challenge);
                } else if(isChallengeInitiatedByUser(challenge)) {
                    mInPlayChallenges.add(challenge);
                } else {
                    mNewChallenges.add(challenge);
                }
            }

        }
    }

    private boolean isChallengeInitiatedByUser(Challenge challenge) {
        for (Match match : challenge.getMatches()) {
            if(GameAttemptedStatus.NOT != match.getisAttempted()) {
                return true;
            }
        }
        return false;
    }

    public interface OnAllChallengesApiModelListener {

        void onSuccessAllChallengesApi();

        void onEmpty();

        void onFailedAllChallengesApi(String message);

        void onNoInternet();
    }
}