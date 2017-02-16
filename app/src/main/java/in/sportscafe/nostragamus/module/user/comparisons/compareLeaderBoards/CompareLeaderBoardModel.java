package in.sportscafe.nostragamus.module.user.comparisons.compareLeaderBoards;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import in.sportscafe.nostragamus.webservice.CompareLeaderBoard;

/**
 * Created by deepanshi on 2/14/17.
 */

public interface CompareLeaderBoardModel {

    void init(Bundle bundle);

    RecyclerView.Adapter getCompareLeaderBoardAdapter(Context context,List<CompareLeaderBoard> compareLeaderBoardList);
}
