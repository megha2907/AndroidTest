package in.sportscafe.nostragamus.module.contest.helper;

import android.text.TextUtils;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;
import in.sportscafe.nostragamus.module.contest.dto.pool.PoolContestResponse;
import in.sportscafe.nostragamus.module.contest.dto.pool.PoolPayoutMap;

/**
 * Created by sc on 7/12/17.
 */

public class PoolPrizesEstimationHelper {

    private static final String TAG = PoolPrizesEstimationHelper.class.getSimpleName();

    /**
     * @param participants [ >0 ]
     * @param perUserPrize [ >0 ]
     * @return Total Pool prizes
     */
    public double getTotalPrize(int participants, double perUserPrize) {
        double total = 0;

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
     * ------------ ALGORITHM ------------
     * 1. For each payout-level :
     1.1 Find participants-ration (participants * percents)
     1.2 Convert participants-ratio into whole number-of-participants
     (only if float value of participants-ratio)

     1.2.1 if 'step' == 'up' then increase 1 number-of-participants

     1.3 Find prize-ratio (totalPrize * share)
     1.4 Find per-participant-reward (prize-ratio / number-of-participants)

     1.5 For each participants IN number-of-participants :
     1.5.1 create a reward entry (add into rank list)
     *-----------------------------------------
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
                                                 double totalPrize) {
        List<Rewards> rewardsList = null;

        if (payoutMapArrayList != null && !payoutMapArrayList.isEmpty() &&
                participants > 0 && totalPrize > 0) {

            rewardsList = new ArrayList<>();
            int rank = 0;

            /* For every payout-level (iteration) */
            for (PoolPayoutMap poolPayoutMap : payoutMapArrayList) {
                if (poolPayoutMap.getPercent() >= 0 && poolPayoutMap.getShare() >= 0) {

                    int prizeAmount = 0, usersCount = 0;
                    double winningMoney = 0;

                    /* Percentage-of-users out of total participants who should be considered
                    combined for a share in payout-level(this iteration) */
                    float participantsRation = participants * poolPayoutMap.getPercent();

                    /* get countable-users based on percentage-of-users and total participants */
                    usersCount = (int) participantsRation;

                    /* If percentage-of-users number is fraction decimal THEN
                     * based on values of rounding/step from api,
                     * ONLY for UP , increase a countable-user */
                    if (participantsRation % 1 != 0 && !TextUtils.isEmpty(rounding) && rounding.equalsIgnoreCase("up")) {
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
                            rewards.setRank(AppSnippet.ordinal(rank));
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

}
