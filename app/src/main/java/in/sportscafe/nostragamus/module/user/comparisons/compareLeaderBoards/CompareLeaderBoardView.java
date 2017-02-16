package in.sportscafe.nostragamus.module.user.comparisons.compareLeaderBoards;

import android.support.v7.widget.RecyclerView;

import com.jeeva.android.InAppView;

/**
 * Created by deepanshi on 2/14/17.
 */
public interface CompareLeaderBoardView extends InAppView{

    void setAdapter(RecyclerView.Adapter adapter);

    void noCommonLeaderBoards();
}
