package in.sportscafe.nostragamus.module.contest.adapter;

import android.os.Bundle;

/**
 * Created by sandip on 01/09/17.
 */

public interface ContestAdapterListener {
    void onContestClicked(Bundle args);
    void onJoinContestClicked(Bundle args);
    void onPrizesClicked(Bundle args);

    void onRulesClicked(Bundle args);

    void onEntriesClicked(Bundle args);

    void onReferAFriendClicked();

    void onEntryFeeClicked(Bundle args);
}
