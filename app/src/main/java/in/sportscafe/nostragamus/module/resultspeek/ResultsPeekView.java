package in.sportscafe.nostragamus.module.resultspeek;

import android.support.v7.widget.RecyclerView;

import com.jeeva.android.InAppView;

/**
 * Created by deepanshi on 2/16/17.
 */

public interface ResultsPeekView extends InAppView{

    void setName(String userName, String playerName);

    void setProfileImage(String userImageUrl, String playerImageUrl);

    void setAdapter(RecyclerView.Adapter adapter);

    void setPointsAndMatch(int myPoints, int playerPoints, String matchStage, String challengeName);
}
