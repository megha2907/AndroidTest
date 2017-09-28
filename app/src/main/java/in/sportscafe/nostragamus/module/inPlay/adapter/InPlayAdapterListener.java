package in.sportscafe.nostragamus.module.inPlay.adapter;

import android.os.Bundle;

import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;

/**
 * Created by deepanshi on 9/6/17.
 */

public interface InPlayAdapterListener {

    /* Challenge */
    void onJoinAnotherContestClicked(Bundle args);

    /* Joined Card Item */
    void onJoinedContestCardClicked(Bundle args);
    void onJoinedContestPrizesClicked(Bundle args);
    void onJoinedContestCurrentRankClicked(Bundle args);

    /* Head Less Contest card Item */
    void onHeadLessContestCardClicked(Bundle args, InPlayContestDto inPlayContestDto);
}
