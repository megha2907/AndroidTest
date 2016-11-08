package in.sportscafe.scgame;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.user.group.allgroups.AllGroups;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.powerups.PowerUp;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;
import in.sportscafe.scgame.webservice.MyWebService;

/**
 * Created by Jeeva on 6/4/16.
 */
public class ScGameDataHandler extends AbstractDataHandler implements Constants {

    private static ScGameDataHandler sScGameDataHandler = new ScGameDataHandler();

    /**
     * Variable to hold the sharedPreference data
     */
    private SharedPreferences mSharedPreferences;

    private ScGameDataHandler() {
    }

    public static ScGameDataHandler getInstance() {
        return sScGameDataHandler;
    }

    public void init(Context context) {
        mSharedPreferences = context.getSharedPreferences("in.sportscafe.scgame", Context.MODE_PRIVATE);
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

    //CHECK IS LOGGED IN USER
    public void setLoggedInUser(boolean loggedInUser) {
        setSharedBooleanData(SharedKeys.LOGGED_USER, loggedInUser);
    }

    public boolean isLoggedInUser() {
        return getSharedBooleanData(SharedKeys.LOGGED_USER);
    }

    public boolean isInitialSportsAvailable() {
        return getSharedBooleanData(SharedKeys.INITIAL_SPORTS_AVAILABLE);
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

    public void setAllSports(String allSports) {
        setSharedStringData(SharedKeys.ALL_SPORTS, allSports);
    }



    //SPORTS FOLLOWING
    public List<Integer> getFavoriteSportsIdList() {
        String favoriteSportsString = getSharedStringData(SharedKeys.FAVORITE_SPORTS);
        if (null == favoriteSportsString || favoriteSportsString.isEmpty()) {
            return new ArrayList<>();
        }
        Log.d("ScGameDataHandler", favoriteSportsString);
        return MyWebService.getInstance().getObjectFromJson(favoriteSportsString,
                new TypeReference<List<Integer>>() {
                });
    }

    public void setFavoriteSportsIdList(List<Integer> favoriteSportsIdList) {
        Log.d("ScGameDataHandler", favoriteSportsIdList.toString());
        setSharedStringData(SharedKeys.FAVORITE_SPORTS,
                MyWebService.getInstance().getJsonStringFromObject(favoriteSportsIdList));
    }


    //USER ID
    public void setUserId(String userId) {
        setSharedStringData(SharedKeys.USER_ID, userId);
    }

    public String getUserId() {
        return getSharedStringData(SharedKeys.USER_ID);
    }


    //USER INFO
    public void setUserInfo(UserInfo userInfo) {
        ScGame.getInstance().setUserEmail(userInfo.getEmail());
        setSharedStringData(SharedKeys.USER_INFO,
                MyWebService.getInstance().getJsonStringFromObject(userInfo));
    }

    public UserInfo getUserInfo() {
        String userInfo = getSharedStringData(SharedKeys.USER_INFO);
        if (null == userInfo || userInfo.isEmpty()) {
            return null;
        }
        return MyWebService.getInstance().getObjectFromJson(userInfo, UserInfo.class);
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

    public void setGrpInfoMap(Map<Long, GroupInfo> grpInfoMap) {

        List<GroupInfo> grpInfoListObject = new ArrayList<>();
        Set<Long> keys = grpInfoMap.keySet();
        for (Long key : keys) {
            grpInfoListObject.add(grpInfoMap.get(key));
        }
        setGrpInfoList(grpInfoListObject);
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

    public void addNewGroup(GroupInfo newGroupInfo) {
        Map<Long, GroupInfo> grpInfoMap = getGrpInfoMap();
        grpInfoMap.put(newGroupInfo.getId(), newGroupInfo);
        setGrpInfoMap(grpInfoMap);
    }


    //COOKIE
    public String getCookie() {
        return getSharedStringData(SharedKeys.COOKIE);
    }

    public void setCookie(String cookie) {
        setSharedStringData(SharedKeys.COOKIE, cookie);
    }



    //POWERUPS
    public List<PowerUp> getPowerUpList() {

        List<PowerUp> powerUpList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : getUserInfo().getPowerUps().entrySet())
        {
            powerUpList.add(new PowerUp(entry.getKey(), entry.getValue()));
        }
        return powerUpList;
    }

    public int getNumberofPowerups() {
        return getSharedIntData(SharedKeys.NUMBER_OF_POWERUPS, 0);
    }

    public void setNumberofPowerups(int numberofbadges) {
        setSharedIntData(SharedKeys.NUMBER_OF_POWERUPS, numberofbadges);
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
    public List<TournamentInfo> getTournaments() {
        String allTournamentsString = getSharedStringData(SharedKeys.ALL_TOURNAMENTS);
        Log.i("allTournamentsString", allTournamentsString + "");
        if (null == allTournamentsString || allTournamentsString.isEmpty()) {
            return new ArrayList<>();
        }
        return MyWebService.getInstance().getObjectFromJson(allTournamentsString,
                new TypeReference<List<TournamentInfo>>() {
                });
    }

    public Map<Integer, TournamentInfo> getTournamentsMap() {
        Map<Integer, TournamentInfo> tournamentMap = new HashMap<>();

        for (TournamentInfo tournamentInfo : getTournaments()) {
            tournamentMap.put(tournamentInfo.getTournamentId(), tournamentInfo);
        }

        return tournamentMap;
    }


    public void setTournaments(List<TournamentInfo> newTournaments) {
        setTournaments(MyWebService.getInstance().getJsonStringFromObject(newTournaments));
    }

    public void setTournaments(String tournaments) {
        setSharedStringData(SharedKeys.ALL_TOURNAMENTS, tournaments);
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


    public void setAllGroupsMap(Map<Long, AllGroups> allGroupsMap) {

        List<AllGroups> allGroupsListObject = new ArrayList<>();
        Set<Long> keys = allGroupsMap.keySet();
        for (Long key : keys) {
            allGroupsListObject.add(allGroupsMap.get(key));
        }
        setAllGroups(allGroupsListObject);
    }


    public Map<Long, AllGroups> getAllGroupsMap() {
        Map<Long, AllGroups> allGroupsMap = new HashMap<>();

        for (AllGroups allGroups : getAllGroups()) {
            allGroupsMap.put(allGroups.getGroupId(), allGroups);
        }

        return allGroupsMap;
    }


    public void setAllGroups(List<AllGroups> newAllGroups) {
        setAllGroups(MyWebService.getInstance().getJsonStringFromObject(newAllGroups));
    }

    public void setAllGroups(String allGroups) {
        setSharedStringData(SharedKeys.ALL_GROUPS, allGroups);
    }

    //SELECTED GROUP TOURNAMENTS

    public void setSelectedTournaments(List<TournamentInfo> selectedTournaments) {
        setSelectedTournaments(MyWebService.getInstance().getJsonStringFromObject(selectedTournaments));
    }

    public void setSelectedTournaments(String selectedTournaments) {
        setSharedStringData(SharedKeys.SELECTED_TOURNAMENTS, selectedTournaments);
    }

    public List<TournamentInfo> getSelectedTournaments() {
        String selectedTournaments = getSharedStringData(SharedKeys.SELECTED_TOURNAMENTS);
        Log.i("selectedTournaments", selectedTournaments);

        if (null == selectedTournaments || selectedTournaments.isEmpty()) {
            return new ArrayList<>();
        }
        return MyWebService.getInstance().getObjectFromJson(selectedTournaments,
                new TypeReference<List<TournamentInfo>>() {
                });
    }



    public int getNumberofGroups() {
        return getSharedIntData(SharedKeys.NUMBER_OF_GROUPS, 0);
    }

    public void setNumberofGroups(int numberofgroups) {
        setSharedIntData(SharedKeys.NUMBER_OF_GROUPS, numberofgroups);
    }


    @Override
    public void clearAll() {
        super.clearAll();
        List<Sport> allSports = getAllSports();


        if (!allSports.isEmpty()) {
            setAllSports(allSports);
            setInitialSportsAvailable(true);
        }
    }
}