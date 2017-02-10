package in.sportscafe.nostragamus.module.offline;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.sportscafe.nostragamus.AbstractDataHandler;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentInfo;
import in.sportscafe.nostragamus.module.user.badges.Badge;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroups;
import in.sportscafe.nostragamus.module.user.group.mutualgroups.MutualGroups;
import in.sportscafe.nostragamus.module.user.login.dto.JwtToken;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;

import static com.google.android.gms.analytics.internal.zzy.n;

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