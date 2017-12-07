package in.sportscafe.nostragamus.module.contest.poolContest;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;
import in.sportscafe.nostragamus.module.contest.dto.pool.PoolContestResponse;
import in.sportscafe.nostragamus.module.contest.dto.pool.PoolPayoutMap;

/**
 * Created by sc on 7/12/17.
 */

public class PoolEstimationHelper {

    private static final String TAG = PoolEstimationHelper.class.getSimpleName();

    /**
     * @param participants [ >0 ]
     * @param perUserPrize [ >0 ]
     * @return Total Pool prizes
     */
    public int getTotalPrize(int participants, int perUserPrize) {
        int total = 0;

        if (participants > 0 && perUserPrize > 0) {
            total = participants * perUserPrize;
        }

        return total;
    }

    /**
     * IMPORTANT NOTE:
     * Min-participants (from api response) will ALWAYS be equal/more than number of payoutMap size
     * Total percents in payoutMap (summation of all percents of all items) will ALWAYS be 100% in api response
     * Format of percentage in payoutMap api response will be as per given example here: 0.2/0.20 (20%), 0.05 (5%)
     *
     *
     * @param participants - participants (seekbarProgress + min-participants ) [ >0 ]
     * @param payoutMapArrayList - server response [NonNull/nonEmpty]
     * @param rounding - step - serverResponse [ 'up' or 'down']
     * @param totalPrize - ( (seekbarProgress + minParticipants) * perUserPrize) [ >0 ]
     *
     * @return List of rewards according to winners list
     */
    public List<Rewards> getEstimatedRewardsList(int participants,
                                                 List<PoolPayoutMap> payoutMapArrayList,
                                                 String rounding,
                                                 int totalPrize) {
        List<Rewards> rewardsList = null;

        if (payoutMapArrayList != null && !payoutMapArrayList.isEmpty() &&
                participants > 0 && totalPrize > 0) {

            rewardsList = new ArrayList<>();
            int rank = 0;

            /* For every payout-level (iteration) */
            for (PoolPayoutMap poolPayoutMap : payoutMapArrayList) {
                if (poolPayoutMap.getPercent() >= 0 && poolPayoutMap.getShare() >= 0) {

                    int prizeAmount = 0, usersCount = 0;
                    float winningMoney = 0;

                    /* Percentage-of-users out of total participants who should be considered
                    combined for a share in payout-level(this iteration) */
                    float participantsRation = participants * poolPayoutMap.getPercent();

                    /* get countable-users based on percentage-of-users and total participants */
                    usersCount = (int) participantsRation;

                    /* If percentage-of-users number is fraction decimal THEN
                     * based on values of rounding/step from api,
                     * ONLY for UP , increase a countable-user */
                    if (participantsRation % 1 != 0 && rounding.equalsIgnoreCase("up")) {
                        usersCount++;
                    }

                    /* Percentage-of-prize out of total prize which is to be shared equally for
                     * countable-users in payout-level(this iteration)  */
                    winningMoney = totalPrize * poolPayoutMap.getShare();
                    if (winningMoney > 0 && usersCount > 0) {
                        prizeAmount = (int) (winningMoney / usersCount);

                        /* For every countable-user */
                        for (int count = 1; count <= usersCount; count++) {
                            rank++;

                            Rewards rewards = new Rewards();
                            rewards.setRank(String.valueOf(rank) + "th");
                            rewards.setAmount(prizeAmount);

                            rewardsList.add(rewards);
                        }
                    }

                    Log.d(TAG, "\n ----------- \n Total-participants : " + participants +
                            "\n %-of-user(percent) : " + participantsRation + "("+poolPayoutMap.getPercent()+")" +
                            "\n countable-user (based on %) :" + usersCount +
                            "\n step/rounding : " + rounding +
                            "\n Total-prize (calculated) : " + totalPrize +
                            "\n %-of-prize : " + winningMoney +
                            "\n prizeAmount : " + prizeAmount);
                }
            }
        }

        return rewardsList;
    }

    /* Temporary values -- should be replaced with server response */
    public PoolContestResponse getPoolResponse() {
        PoolContestResponse pool = new PoolContestResponse();
        pool.setPrizePerUser(10);
        pool.setMaxParticipants(10);
        pool.setMinParticipants(2);
        pool.setRoundingLevel("up");

        List<PoolPayoutMap> poolPayoutMapList = new ArrayList<>();

        PoolPayoutMap poolMap = new PoolPayoutMap();
        poolMap.setPercent(0.50f);
        poolMap.setShare(0.5f);

        /*PoolPayoutMap poolMap1 = new PoolPayoutMap();
        poolMap1.setPercent(0.20f);
        poolMap1.setShare(0.3f);

        PoolPayoutMap poolMap2 = new PoolPayoutMap();
        poolMap2.setPercent(0.30f);
        poolMap2.setShare(0.1f);*/

        poolPayoutMapList.add(poolMap);
        /*poolPayoutMapList.add(poolMap1);
        poolPayoutMapList.add(poolMap2);*/

        pool.setPoolPayoutMapList(poolPayoutMapList);

        return pool;
    }
}
