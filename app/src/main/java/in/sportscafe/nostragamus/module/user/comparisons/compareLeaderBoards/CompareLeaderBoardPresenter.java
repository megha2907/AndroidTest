package in.sportscafe.nostragamus.module.user.comparisons.compareLeaderBoards;

import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.webservice.CompareLeaderBoard;

/**
 * Created by deepanshi on 2/14/17.
 */

public interface CompareLeaderBoardPresenter {

    void onCreateCompareLeaderBoard(Bundle bundle);

    void onCreateCompareLeaderBoardAdapter(List<CompareLeaderBoard> compareLeaderBoardList);
}
