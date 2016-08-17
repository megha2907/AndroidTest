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

import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
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

    public List<Sport> getAllSports() {
        String allSportsString = getSharedStringData(SharedKeys.ALL_SPORTS);
        Log.i("allsportstring",allSportsString + "");
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

    public void setUserId(String userId) {
        setSharedStringData(SharedKeys.USER_ID, userId);
    }

    public String getUserId() {
//        return "1";
        return getSharedStringData(SharedKeys.USER_ID);
    }

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
            if(allSportsMap.containsKey(sportId)) {
                glbFollowedSports.add(allSportsMap.get(sportId));
            }
        }

        return glbFollowedSports;
    }

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
        Log.i("groupinfo",grpInfoList.toString());
        if (null == grpInfoList || grpInfoList.isEmpty()) {
            return new HashMap<>();
        }

        List<GroupInfo> grpInfoListObject = MyWebService.getInstance().getObjectFromJson(grpInfoList,
                new TypeReference<List<GroupInfo>>() {
                });
        if(null == grpInfoListObject) {
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

    public String getCookie() {
        return getSharedStringData(SharedKeys.COOKIE);
    }

    public void setCookie(String cookie) {
        setSharedStringData(SharedKeys.COOKIE, cookie);
    }

    public int getNumberofPowerups() {
        return getSharedIntData(SharedKeys.NUMBER_OF_POWERUPS,0);
    }

    public void setNumberofPowerups(int numberofpowerups) {
        setSharedIntData(SharedKeys.NUMBER_OF_POWERUPS, numberofpowerups);
    }

    @Override
    public void clearAll() {
        Log.i("clearall","cleared");
        super.clearAll();
        List<Sport> allSports = getAllSports();


        if(!allSports.isEmpty()) {
            setAllSports(allSports);
            setInitialSportsAvailable(true);
        }
    }
}