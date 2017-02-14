package in.sportscafe.nostragamus.module.user.group.tourselection;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.user.group.newgroup.TourSelectionAdapter;

/**
 * Created by deepanshi on 1/6/17.
 */

public interface TourSelectionModel {

    void init(Bundle bundle);

    void getAllTournaments();

    TourSelectionAdapter getTourSelectionAdapter(Context context, List<TournamentFeedInfo> allTours);
}