package in.sportscafe.nostragamus;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroups;
import in.sportscafe.nostragamus.module.user.group.mutualgroups.MutualGroups;
import in.sportscafe.nostragamus.module.user.login.dto.JwtToken;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;

/**
 * Created by Jeeva on 6/4/16.
 */
public class NostragamusDataHandler extends AbstractDataHandler implements Constants {

    private static NostragamusDataHandler sNostragamusDataHandler = new NostragamusDataHandler();

    /**
     * Variable to hold the sharedPreference data
     */
    private SharedPreferences mSharedPreferences;
    private boolean initialFeedbackFormShown;

    private NostragamusDataHandler() {
    }

    public static NostragamusDataHandler getInstance() {
        return sNostragamusDataHandler;
    }

    public void init(Context context) {
        mSharedPreferences = context.getSharedPreferences("in.sportscafe.nostragamus", Context.MODE_PRIVATE);
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public int getPreviousAppVersionCode() {
        return getSharedIntData(SharedKeys.PREVIOUS_APP_VERSION_CODE, 0);
    }

    public void setPreviousAppVersionCode(int versionCode) {
        setSharedIntData(SharedKeys.PREVIOUS_APP_VERSION_CODE, versionCode);
    }

    public boolean isLoggedInUser() {
        return getSharedBooleanData(SharedKeys.LOGGED_USER, false);
    }

    //CHECK IS LOGGED IN USER
    public void setLoggedInUser(boolean loggedInUser) {
        setSharedBooleanData(SharedKeys.LOGGED_USER, loggedInUser);
    }

    public boolean isInitialSportsAvailable() {
        return getSharedBooleanData(SharedKeys.INITIAL_SPORTS_AVAILABLE, false);
    }

    public void setInitialSportsAvailable(boolean initialSportsAvailable) {
        setSharedBooleanData(SharedKeys.INITIAL_SPORTS_AVAILABLE, initialSportsAvailable);
    }


    //ALL SPORTS
    public List<Sport> getAllSports() {
        String allSportsString = getSharedStringData(SharedKeys.ALL_SPORTS);
        Log.i("allsportstring", allSportsString + "");
        if (null == allSportsString || allSportsString.isEmpty()) {
            return new ArrayList<>();
        }
        return MyWebService.getInstance().getObjectFromJson(allSportsString,
                new TypeReference<List<Sport>>() {
                });
    }

    public void setAllSports(String allSports) {
        setSharedStringData(SharedKeys.ALL_SPORTS, allSports);
    }

    public Map<Integer, Sport> getAllSportsMap() {
        Map<Integer, Sport> allSportsMap = new HashMap<>();

        for (Sport sport : getAllSports()) {
            allSportsMap.put(sport.getId(), sport);
        }

        return allSportsMap;
    }

    public void setAllSports(List<Sport> newSports) {
        setAllSports(MyWebService.getInstance().getJsonStringFromObject(newSports));
    }

    //SPORTS FOLLOWING
    public List<Integer> getFavoriteSportsIdList() {
        String favoriteSportsString = getSharedStringData(SharedKeys.FAVORITE_SPORTS);
        if (null == favoriteSportsString || favoriteSportsString.isEmpty()) {
            return new ArrayList<>();
        }
        return MyWebService.getInstance().getObjectFromJson(favoriteSportsString,
                new TypeReference<List<Integer>>() {
                });
    }

    public void setFavoriteSportsIdList(List<Integer> favoriteSportsIdList) {
        setSharedStringData(SharedKeys.FAVORITE_SPORTS,
                MyWebService.getInstance().getJsonStringFromObject(favoriteSportsIdList));
    }

    public String getUserId() {
        return getSharedStringData(SharedKeys.USER_ID);
    }

    //USER ID
    public void setUserId(String userId) {
        setSharedStringData(SharedKeys.USER_ID, userId);
    }


    public String getReferralUserId() {
        return getSharedStringData(BundleKeys.USER_REFERRAL_ID);
    }

    public void setReferralUserId(String userReferralId) {
        setSharedStringData(BundleKeys.USER_REFERRAL_ID, userReferralId);
    }

    public UserInfo getUserInfo() {
        String userInfo = getSharedStringData(SharedKeys.USER_INFO);
        if (null == userInfo || userInfo.isEmpty()) {
            return null;
        }
        return MyWebService.getInstance().getObjectFromJson(userInfo, UserInfo.class);
    }

    //USER INFO
    public void setUserInfo(UserInfo userInfo) {
        Nostragamus.getInstance().setUserEmail(userInfo.getEmail());
        setSharedStringData(SharedKeys.USER_INFO,
                MyWebService.getInstance().getJsonStringFromObject(userInfo));
    }

    public List<Sport> getGlbFollowedSports() {
        List<Integer> favoriteSportsIdList = getFavoriteSportsIdList();

        Map<Integer, Sport> allSportsMap = getAllSportsMap();

        List<Sport> glbFollowedSports = new ArrayList<>();
        for (Integer sportId : favoriteSportsIdList) {
            if (allSportsMap.containsKey(sportId)) {
                glbFollowedSports.add(allSportsMap.get(sportId));
            }
        }

        return glbFollowedSports;
    }


    //GROUPS INFO
    public void setGrpInfoList(List<GroupInfo> grpInfoList) {
        setSharedStringData(SharedKeys.GRP_INFOS, MyWebService.getInstance().getJsonStringFromObject(grpInfoList));
    }

    public Map<Long, GroupInfo> getGrpInfoMap() {
        String grpInfoList = getSharedStringData(SharedKeys.GRP_INFOS);
        if (null == grpInfoList || grpInfoList.isEmpty()) {
            return new HashMap<>();
        }

        List<GroupInfo> grpInfoListObject = MyWebService.getInstance().getObjectFromJson(grpInfoList,
                new TypeReference<List<GroupInfo>>() {
                });
        if (null == grpInfoListObject) {
            grpInfoListObject = new ArrayList<>();

            GroupInfo groupInfo = MyWebService.getInstance().getObjectFromJson(grpInfoList, GroupInfo.class);
            grpInfoListObject.add(groupInfo);
        }

        Map<Long, GroupInfo> groupInfoMap = new HashMap<>();
        for (GroupInfo groupInfo : grpInfoListObject) {
            groupInfoMap.put(groupInfo.getId(), groupInfo);
        }
        return groupInfoMap;
    }

    public void setGrpInfoMap(Map<Long, GroupInfo> grpInfoMap) {

        List<GroupInfo> grpInfoListObject = new ArrayList<>();
        Set<Long> keys = grpInfoMap.keySet();
        for (Long key : keys) {
            grpInfoListObject.add(grpInfoMap.get(key));
        }
        setGrpInfoList(grpInfoListObject);
    }

    public void addNewGroup(GroupInfo newGroupInfo) {
        Map<Long, GroupInfo> grpInfoMap = getGrpInfoMap();
        grpInfoMap.put(newGroupInfo.getId(), newGroupInfo);
        setGrpInfoMap(grpInfoMap);
    }


    //JWT TOKEN
    public void setJwtToken(JwtToken jwtToken) {
        setSharedStringData(SharedKeys.ACCESS_TOKEN, jwtToken.getToken());
        setSharedLongData(SharedKeys.TOKEN_EXPIRY, jwtToken.getExpiry());
    }

    public String getAccessToken() {
        return getSharedStringData(SharedKeys.ACCESS_TOKEN);
    }

    public Long getTokenExpiry() {
        return getSharedLongData(SharedKeys.TOKEN_EXPIRY, 0);
    }


    //POWERUPS
    public List<PowerUp> getPowerUpList() {

        List<PowerUp> powerUpList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : getUserInfo().getPowerUps().entrySet()) {
            powerUpList.add(new PowerUp(entry.getKey(), entry.getValue()));
        }
        return powerUpList;
    }

    public int getNumberof2xPowerups() {
        return getSharedIntData(SharedKeys.NUMBER_OF_POWERUPS, 0);
    }

    public void setNumberof2xPowerups(int numberofPowerups) {
        setSharedIntData(SharedKeys.NUMBER_OF_POWERUPS, numberofPowerups);
    }

    public int getNumberofNonegsPowerups() {
        return getSharedIntData(SharedKeys.NUMBER_OF_NONEGS_POWERUPS, 0);
    }

    public void setNumberofNonegsPowerups(int numberofNonegsPowerups) {
        setSharedIntData(SharedKeys.NUMBER_OF_NONEGS_POWERUPS, numberofNonegsPowerups);
    }

    public int getNumberofAudiencePollPowerups() {
        return getSharedIntData(SharedKeys.NUMBER_OF_AUDIENCE_POLL_POWERUPS, 0);
    }

    public void setNumberofAudiencePollPowerups(int numberofAudiencePollPowerups) {

        setSharedIntData(SharedKeys.NUMBER_OF_AUDIENCE_POLL_POWERUPS, numberofAudiencePollPowerups);
    }

    public int getNumberofReplayPowerups() {
        return getSharedIntData(SharedKeys.NUMBER_OF_REPLAY_POWERUPS, 0);
    }

    public void setNumberofReplayPowerups(int numberofPowerups) {
        setSharedIntData(SharedKeys.NUMBER_OF_REPLAY_POWERUPS, numberofPowerups);
    }

    public int getNumberofFlipPowerups() {
        return getSharedIntData(SharedKeys.NUMBER_OF_FLIP_POWERUPS, 0);
    }

    public void setNumberofFlipPowerups(int numberofPowerups) {
        setSharedIntData(SharedKeys.NUMBER_OF_FLIP_POWERUPS, numberofPowerups);
    }


    //BADGES
    public List<String> getBadgeList() {
        return getUserInfo().getBadges();
    }

    public int getNumberofBadges() {
        return getSharedIntData(SharedKeys.NUMBER_OF_BADGES, 0);
    }

    public void setNumberofBadges(int numberofbadges) {
        setSharedIntData(SharedKeys.NUMBER_OF_BADGES, numberofbadges);
    }


    //TOURNAMENTS
    public List<TournamentFeedInfo> getTournaments() {
        String allTournamentsString = getSharedStringData(SharedKeys.ALL_TOURNAMENTS);
        Log.i("allTournamentsString", allTournamentsString + "");
        if (null == allTournamentsString || allTournamentsString.isEmpty()) {
            return new ArrayList<>();
        }
        return MyWebService.getInstance().getObjectFromJson(allTournamentsString,
                new TypeReference<List<TournamentFeedInfo>>() {
                });
    }

    public void setTournaments(String tournaments) {
        setSharedStringData(SharedKeys.ALL_TOURNAMENTS, tournaments);
    }

    public Map<Integer, TournamentFeedInfo> getTournamentsMap() {
        Map<Integer, TournamentFeedInfo> tournamentMap = new HashMap<>();

        for (TournamentFeedInfo tournamentInfo : getTournaments()) {
            tournamentMap.put(tournamentInfo.getTournamentId(), tournamentInfo);
        }

        return tournamentMap;
    }

    public void setTournaments(List<TournamentFeedInfo> newTournaments) {
        setTournaments(MyWebService.getInstance().getJsonStringFromObject(newTournaments));
    }

    //ALL GROUPS INFO
    public List<AllGroups> getAllGroups() {
        String allGroupssString = getSharedStringData(SharedKeys.ALL_GROUPS);
        Log.i("allGroupssString", allGroupssString + "");
        if (null == allGroupssString || allGroupssString.isEmpty()) {
            return new ArrayList<>();
        }
        return MyWebService.getInstance().getObjectFromJson(allGroupssString,
                new TypeReference<List<AllGroups>>() {
                });
    }

    public void setAllGroups(String allGroups) {
        setSharedStringData(SharedKeys.ALL_GROUPS, allGroups);
    }

    //MUTUAL GROUPS INFO
    public List<MutualGroups> getMutualGroups() {
        String mutualGroups = getSharedStringData(SharedKeys.MUTUAL_GROUPS);
        if (null == mutualGroups || mutualGroups.isEmpty()) {
            return new ArrayList<>();
        }
        return MyWebService.getInstance().getObjectFromJson(mutualGroups,
                new TypeReference<List<MutualGroups>>() {
                });
    }

    public void setMutualGroups(String mutualGroups) {
        setSharedStringData(SharedKeys.MUTUAL_GROUPS, mutualGroups);
    }
    public void setMutualGroups(List<MutualGroups> mutualGroups) {
        setMutualGroups(MyWebService.getInstance().getJsonStringFromObject(mutualGroups));
    }

    public Map<Long, AllGroups> getAllGroupsMap() {
        Map<Long, AllGroups> allGroupsMap = new HashMap<>();

        for (AllGroups allGroups : getAllGroups()) {
            allGroupsMap.put(allGroups.getGroupId(), allGroups);
        }

        return allGroupsMap;
    }

    public void setAllGroupsMap(Map<Long, AllGroups> allGroupsMap) {

        List<AllGroups> allGroupsListObject = new ArrayList<>();
        Set<Long> keys = allGroupsMap.keySet();
        for (Long key : keys) {
            allGroupsListObject.add(allGroupsMap.get(key));
        }
        setAllGroups(allGroupsListObject);
    }

    public void setAllGroups(List<AllGroups> newAllGroups) {
        setAllGroups(MyWebService.getInstance().getJsonStringFromObject(newAllGroups));
    }

    //SELECTED GROUP TOURNAMENTS

    public void setSelectedTournaments(List<TournamentFeedInfo> selectedTournaments) {
        setSelectedTournaments(MyWebService.getInstance().getJsonStringFromObject(selectedTournaments));
    }

    public List<TournamentFeedInfo> getSelectedTournaments() {
        String selectedTournaments = getSharedStringData(SharedKeys.SELECTED_TOURNAMENTS);
        Log.i("selectedTournaments", selectedTournaments);

        if (null == selectedTournaments || selectedTournaments.isEmpty()) {
            return new ArrayList<>();
        }
        return MyWebService.getInstance().getObjectFromJson(selectedTournaments,
                new TypeReference<List<TournamentFeedInfo>>() {
                });
    }

    public void setSelectedTournaments(String selectedTournaments) {
        setSharedStringData(SharedKeys.SELECTED_TOURNAMENTS, selectedTournaments);
    }

    public int getNumberofGroups() {
        return getSharedIntData(SharedKeys.NUMBER_OF_GROUPS, 0);
    }

    public void setNumberofGroups(int numberofgroups) {
        setSharedIntData(SharedKeys.NUMBER_OF_GROUPS, numberofgroups);
    }

    public String getInstallGroupCode() {
        return getSharedStringData(SharedKeys.INSTALL_GROUP_CODE);
    }

    public void setInstallGroupCode(String installGroupCode) {
        setSharedStringData(SharedKeys.INSTALL_GROUP_CODE, installGroupCode);
    }

    public String getInstallGroupName() {
        return getSharedStringData(SharedKeys.INSTALL_GROUP_NAME);
    }

    public void setInstallGroupName(String installGroupName) {
        setSharedStringData(SharedKeys.INSTALL_GROUP_NAME, installGroupName);
    }

    public int getNormalUpdateVersion() {
        return getSharedIntData(SharedKeys.NORMAL_UPDATE_VERSION, -1);
    }

    public void setNormalUpdateVersion(int version) {
        setSharedIntData(SharedKeys.NORMAL_UPDATE_VERSION, version);
    }

    public String getNormalUpdateMessage() {
        return getSharedStringData(SharedKeys.NORMAL_UPDATE_MESSAGE);
    }

    public void setNormalUpdateMessage(String message) {
        setSharedStringData(SharedKeys.NORMAL_UPDATE_MESSAGE, message);
    }

    public Integer getForceUpdateVersion() {
        return getSharedIntData(SharedKeys.FORCE_UPDATE_VERSION, -1);
    }

    public void setForceUpdateVersion(int version) {
        setSharedIntData(SharedKeys.FORCE_UPDATE_VERSION, version);
    }

    public String getForceUpdateMessage() {
        return getSharedStringData(SharedKeys.FORCE_UPDATE_MESSAGE);
    }

    public void setForceUpdateMessage(String message) {
        setSharedStringData(SharedKeys.FORCE_UPDATE_MESSAGE, message);
    }

    public boolean isNormalUpdateEnabled() {
        if (getSharedBooleanData(SharedKeys.NORMAL_UPDATE_ENABLED, true)) {
            long currentTimeMs = Calendar.getInstance().getTimeInMillis();
            long updateShownTimeMs = getNormalUpdateShownTime();
            if (updateShownTimeMs == -1
                    || TimeUtils.getDaysDifference(currentTimeMs - updateShownTimeMs) > 0) {
                setNormalUpdateShownTime(currentTimeMs);
                return true;
            }
        }

        return false;
    }

    public void setNormalUpdateEnabled(boolean enabled) {
        setSharedBooleanData(SharedKeys.NORMAL_UPDATE_ENABLED, enabled);
    }

    private long getNormalUpdateShownTime() {
        return getSharedLongData(SharedKeys.NORMAL_UPDATE_SHOWN_TIME, -1);
    }

    private void setNormalUpdateShownTime(long normalUpdateShownTime) {
        setSharedLongData(SharedKeys.NORMAL_UPDATE_SHOWN_TIME, normalUpdateShownTime);
    }

    @Override
    public void clearAll() {
        boolean formShown = isInitialFeedbackFormShown();
        List<Sport> allSports = getAllSports();
        super.clearAll();

        setInitialFeedbackFormShown(formShown);
        if (!allSports.isEmpty()) {
            setAllSports(allSports);
            setInitialSportsAvailable(true);
        }
    }

    public boolean isInitialFeedbackFormShown() {
        return getSharedBooleanData(SharedKeys.INITIAL_FORM_SHOWN, false);
    }

    public void setInitialFeedbackFormShown(boolean formShown) {
        setSharedBooleanData(SharedKeys.INITIAL_FORM_SHOWN, formShown);
    }
}