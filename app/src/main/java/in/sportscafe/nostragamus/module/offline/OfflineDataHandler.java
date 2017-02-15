package in.sportscafe.nostragamus.module.offline;

import android.text.TextUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.AbstractDataHandler;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentInfo;
import in.sportscafe.nostragamus.webservice.MyWebService;

/**
 * Created by Jeeva on 6/4/16.
 */
public class OfflineDataHandler extends AbstractDataHandler implements Constants {

    private static OfflineDataHandler sOfflineDataHandler = new OfflineDataHandler();

    private OfflineDataHandler() {
    }

    public static OfflineDataHandler getInstance() {
        return sOfflineDataHandler;
    }

    @Override
    public String getPreferenceFileName() {
        return "in.sportscafe.nostragamus.offline";
    }

    public void setAllTournaments(List<TournamentInfo> tournaments) {
        setSharedStringData(SharedKeys.ALL_TOURNAMENTS, MyWebService.getInstance().getJsonStringFromObject(tournaments));
    }

    public List<TournamentInfo> getAllTournaments() {
        String allTournaments = getSharedStringData(SharedKeys.ALL_TOURNAMENTS);
        if (TextUtils.isEmpty(allTournaments)) {
            return new ArrayList<>();
        }
        return MyWebService.getInstance().getObjectFromJson(
                allTournaments,
                new TypeReference<List<TournamentInfo>>() {
                }
        );
    }
}