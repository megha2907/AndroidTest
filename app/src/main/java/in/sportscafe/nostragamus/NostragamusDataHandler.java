package in.sportscafe.nostragamus;

import android.text.TextUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.user.login.dto.JwtToken;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;

/**
 * Created by Jeeva on 6/4/16.
 */
public class NostragamusDataHandler extends AbstractDataHandler implements Constants {

    private static NostragamusDataHandler sNostragamusDataHandler = new NostragamusDataHandler();

    private NostragamusDataHandler() {
    }

    public static NostragamusDataHandler getInstance() {
        return sNostragamusDataHandler;
    }

    @Override
    public String getPreferenceFileName() {
        return "in.sportscafe.nostragamus";
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

    public boolean isFirstTimeUser() {
        return getSharedBooleanData(SharedKeys.FIRST_TIME_USER, false);
    }

    public void setFirstTimeUser(boolean FirstTimeUser) {
        setSharedBooleanData(SharedKeys.FIRST_TIME_USER, FirstTimeUser);
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
        if (TextUtils.isEmpty(allSportsString)) {
            return new ArrayList<>();
        }
        return MyWebService.getInstance().getObjectFromJson(allSportsString,
                new TypeReference<List<Sport>>() {
                });
    }

    public void setAllSports(String allSports) {
        setSharedStringData(SharedKeys.ALL_SPORTS, allSports);
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
        NostragamusAnalytics.getInstance().setUserId(userId);
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

    public int get2xGlobalPowerupsCount() {
        return getSharedIntData(SharedKeys.NUMBER_OF_2X_GLOBAL_POWERUPS, 0);
    }

    public void set2xGlobalPowerupsCount(Integer count) {
        setSharedIntData(SharedKeys.NUMBER_OF_2X_GLOBAL_POWERUPS, null == count ? 0 : count);
    }

    public int get2xPowerupsCount() {
        return getSharedIntData(SharedKeys.NUMBER_OF_POWERUPS, 0);
    }

    public void set2xPowerupsCount(Integer count) {
        setSharedIntData(SharedKeys.NUMBER_OF_POWERUPS, null == count ? 0 : count);
    }

    public int getNonegsPowerupsCount() {
        return getSharedIntData(SharedKeys.NUMBER_OF_NONEGS_POWERUPS, 0);
    }

    public void setNonegsPowerupsCount(Integer count) {
        setSharedIntData(SharedKeys.NUMBER_OF_NONEGS_POWERUPS, null == count ? 0 : count);
    }

    public int getPollPowerupsCount() {
        return getSharedIntData(SharedKeys.NUMBER_OF_AUDIENCE_POLL_POWERUPS, 0);
    }

    public void setPollPowerupsCount(Integer count) {
        setSharedIntData(SharedKeys.NUMBER_OF_AUDIENCE_POLL_POWERUPS, null == count ? 0 : count);
    }

    public int getReplayPowerupsCount() {
        return getSharedIntData(SharedKeys.NUMBER_OF_REPLAY_POWERUPS, 0);
    }

    public void setReplayPowerupsCount(Integer count) {
        setSharedIntData(SharedKeys.NUMBER_OF_REPLAY_POWERUPS, null == count ? 0 : count);
    }

    public int getFlipPowerupsCount() {
        return getSharedIntData(SharedKeys.NUMBER_OF_FLIP_POWERUPS, 0);
    }

    public void setFlipPowerupsCount(Integer count) {
        setSharedIntData(SharedKeys.NUMBER_OF_FLIP_POWERUPS, null == count ? 0 : count);
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

    public void setInstallChannel(String installChannel) {
        setSharedStringData(SharedKeys.INSTALL_CHANNEL, installChannel);
    }

    public String getInstallChannel() {
        return getSharedStringData(SharedKeys.INSTALL_CHANNEL);
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

    public boolean isInitialFeedbackFormShown() {
        return getSharedBooleanData(SharedKeys.INITIAL_FORM_SHOWN, false);
    }

    public void setInitialFeedbackFormShown(boolean formShown) {
        setSharedBooleanData(SharedKeys.INITIAL_FORM_SHOWN, formShown);
    }

    public boolean isBankInfoFirstTimeChecked() {
        boolean checked = isKeyShared(SharedKeys.BANK_INFO_SHOWN);
        if(!checked) {
            setBankInfoShown(false);
        }
        return checked;
    }

    public boolean isBankInfoShown() {
        return getSharedBooleanData(SharedKeys.BANK_INFO_SHOWN, false);
    }

    public void setBankInfoShown(boolean bankInfoShown) {
        setSharedBooleanData(SharedKeys.BANK_INFO_SHOWN, bankInfoShown);
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
}