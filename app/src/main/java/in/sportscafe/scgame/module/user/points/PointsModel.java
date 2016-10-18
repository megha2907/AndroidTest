package in.sportscafe.scgame.module.user.points;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.common.ViewPagerAdapter;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.BaseSummary;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface PointsModel {

    void init(Bundle bundle);

    String getName();

    ViewPagerAdapter getAdapter();

    void refreshLeaderBoard();

    int getSelectedPosition();

}