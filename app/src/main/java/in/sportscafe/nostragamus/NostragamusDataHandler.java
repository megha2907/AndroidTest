package in.sportscafe.nostragamus;

import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.user.login.dto.JwtToken;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
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

    public String getUserId() {
        return getSharedStringData(SharedKeys.USER_ID);
    }

    //USER ID
    public void setUserId(String userId) {
        NostragamusAnalytics.getInstance().setUserId(userId);
        NostragamusAnalytics.getInstance().setUserProperties();
        setSharedStringData(SharedKeys.USER_ID, userId);
    }

    public String getReferralUserId() {
        return getSharedStringData(BundleKeys.USER_REFERRAL_ID);
    }

    public void setReferralUserId(String userReferralId) {
        setSharedStringData(BundleKeys.USER_REFERRAL_ID, userReferralId);
    }

    public String getUserReferralCode() {
        return getSharedStringData(BundleKeys.USER_REFERRAL_CODE);
    }

    public void setUserReferralCode(String userReferralCode) {
        setSharedStringData(BundleKeys.USER_REFERRAL_CODE, userReferralCode);
    }

    public String getUserReferralName() {
        return getSharedStringData(BundleKeys.USER_REFERRAL_NAME);
    }

    public void setUserReferralName(String userReferralName) {
        setSharedStringData(BundleKeys.USER_REFERRAL_NAME, userReferralName);
    }

    public String getUserReferralPhoto() {
        return getSharedStringData(BundleKeys.USER_REFERRAL_PHOTO);
    }

    public void setUserReferralPhoto(String userReferralPhoto) {
        setSharedStringData(BundleKeys.USER_REFERRAL_PHOTO, userReferralPhoto);
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

    public void setInstallReferralCampaign(String referralCampaign) {
        setSharedStringData(SharedKeys.INSTALL_REFERRAL_CAMPAIGN_KEY, referralCampaign);
    }

    public String getInstallReferralCampaign() {
        return getSharedStringData(SharedKeys.INSTALL_REFERRAL_CAMPAIGN_KEY);
    }

    public void setInstallLinkName(String installLinkName) {
        setSharedStringData(SharedKeys.INSTALL_LINK_NAME, installLinkName);
    }

    public String getInstallLinkName() {
        return getSharedStringData(SharedKeys.INSTALL_LINK_NAME);
    }

    public Integer getWalletInitialAmount() {
        return getSharedIntData(SharedKeys.WALLET_INITIAL_AMOUNT, -1);
    }

    public void setWalletInitialAmount(int walletInitialAmount) {
        setSharedIntData(SharedKeys.WALLET_INITIAL_AMOUNT, walletInitialAmount);
    }

    public String getInstallChannel() {
        return getSharedStringData(SharedKeys.INSTALL_CHANNEL);
    }

    public void removeInstallGroupCode() {
        clearData(SharedKeys.INSTALL_GROUP_CODE);
    }

    public void removeInstallGroupName() {
        clearData(SharedKeys.INSTALL_GROUP_NAME);
    }

    /*public void removeInstallChannel() {
        clearData(SharedKeys.INSTALL_CHANNEL);
    }

    public void removeInstallCampaign() {
        clearData(SharedKeys.INSTALL_REFERRAL_CAMPAIGN_KEY);
    }

    public void removeReferralUserId() {
        clearData(BundleKeys.USER_REFERRAL_ID);
    }*/


    public boolean isInitialFeedbackFormShown() {
        return getSharedBooleanData(SharedKeys.INITIAL_FORM_SHOWN, false);
    }

    public void setInitialFeedbackFormShown(boolean formShown) {
        setSharedBooleanData(SharedKeys.INITIAL_FORM_SHOWN, formShown);
    }

    public boolean isBankInfoFirstTimeChecked() {
        boolean checked = isKeyShared(SharedKeys.BANK_INFO_SHOWN);
        if (!checked) {
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
        boolean isProfileDisclaimerAccepted = isProfileDisclaimerAccepted();
        boolean isFirstMatchPlayed = isPlayedFirstMatch();

        super.clearAll();

        setInitialFeedbackFormShown(formShown);
        setIsProfileDisclaimerAccepted(isProfileDisclaimerAccepted);
        setPlayedFirstMatch(isFirstMatchPlayed);
    }

    public boolean isPlayedFirstMatch() {
        return getSharedBooleanData(SharedKeys.FIRST_MATCH_PLAYED, false);
    }

    public void setPlayedFirstMatch(boolean playedFirstMatch) {
        setSharedBooleanData(SharedKeys.FIRST_MATCH_PLAYED, playedFirstMatch);
    }

    public boolean isPlayedSecondMatchPopUp() {
        return getSharedBooleanData(SharedKeys.SECOND_MATCH_PLAYED, false);
    }

    public void setPlayedSecondMatchPopUp(boolean playedSecondMatchPopUp) {
        setSharedBooleanData(SharedKeys.SECOND_MATCH_PLAYED, playedSecondMatchPopUp);
    }

    public boolean isPlayedFifthMatchPopUp() {
        return getSharedBooleanData(SharedKeys.FIFTH_MATCH_PLAYED, false);
    }

    public void setPlayedFifthMatchPopUp(boolean playedFifthMatchPopUp) {
        setSharedBooleanData(SharedKeys.FIRST_MATCH_PLAYED, playedFifthMatchPopUp);
    }

    public boolean isPlayedSeventhMatchPopUp() {
        return getSharedBooleanData(SharedKeys.SEVENTH_MATCH_PLAYED, false);
    }

    public void setPlayedSeventhMatchPopUp(boolean playedSeventhMatchPopUp) {
        setSharedBooleanData(SharedKeys.SEVENTH_MATCH_PLAYED, playedSeventhMatchPopUp);
    }

    public boolean isVisitedLeaderBoards() {
        return getSharedBooleanData(SharedKeys.VISITED_LEADERBOARDS, false);
    }

    public void setVisitedLeaderBoards(boolean visitedLeaderBoards) {
        setSharedBooleanData(SharedKeys.VISITED_LEADERBOARDS, visitedLeaderBoards);
    }

    public boolean isVisitedSubmitQuestion() {
        return getSharedBooleanData(SharedKeys.VISITED_SUBMIT_QUESTION, false);
    }

    public void setVisitedSubmitQuestion(boolean visitedSubmitQuestion) {
        setSharedBooleanData(SharedKeys.VISITED_SUBMIT_QUESTION, visitedSubmitQuestion);
    }

    public boolean isPowerUpApplied() {
        return getSharedBooleanData(SharedKeys.POWERUP_APPLIED, false);
    }

    public void setPowerUpApplied(boolean powerUpApplied) {
        setSharedBooleanData(SharedKeys.POWERUP_APPLIED, powerUpApplied);
    }

    public int getMatchPlayedCount() {
        return getSharedIntData(SharedKeys.NUMBER_OF_MATCH_PLAYED, 0);
    }

    public void setMatchPlayedCount(Integer count) {
        setSharedIntData(SharedKeys.NUMBER_OF_MATCH_PLAYED, null == count ? 0 : count);
    }

    public int getPowerUpsUsedCount() {
        return getSharedIntData(SharedKeys.NUMBER_OF_POWERUPS_USED, 0);
    }

    public void setPowerUpsUsedCount(Integer count) {
        setSharedIntData(SharedKeys.NUMBER_OF_POWERUPS_USED, null == count ? 0 : count);
    }

    public int getTotalGroupsCount() {
        return getSharedIntData(SharedKeys.NUMBER_OF_TOTAL_GROUPS, 0);
    }

    public void setTotalGroupsCount(Integer count) {
        setSharedIntData(SharedKeys.NUMBER_OF_TOTAL_GROUPS, null == count ? 0 : count);
    }

    public boolean isDummyGameShown() {
        return getSharedBooleanData(SharedKeys.DUMMY_GAME_SHOWN, false);
    }

    public void setDummyGameShown(boolean dummyGameShown) {
        setSharedBooleanData(SharedKeys.DUMMY_GAME_SHOWN, dummyGameShown);
    }

    public String getFeedBack() {
        return getSharedStringData(SharedKeys.FEEDBACK);
    }

    public void setFeedBack(String feedBack) {
        setSharedStringData(SharedKeys.FEEDBACK, feedBack);
    }

    public String getProFeedBack() {
        return getSharedStringData(SharedKeys.PRO_FEEDBACK);
    }

    public void setProFeedBack(String proFeedBack) {
        setSharedStringData(SharedKeys.PRO_FEEDBACK, proFeedBack);
    }

    public String getDownloadPaidApp() {
        return getSharedStringData(SharedKeys.DOWNLOAD_PAID_APP);
    }

    public void setDownloadPaidApp(String downloadPaidApp) {
        setSharedStringData(SharedKeys.DOWNLOAD_PAID_APP, downloadPaidApp);
    }

    public String getAskFriendText() {
        return getSharedStringData(SharedKeys.ASK_FRIEND);
    }

    public void setAskFriendText(String askFriendText) {
        setSharedStringData(SharedKeys.ASK_FRIEND, askFriendText);
    }

    public String getDisclaimerText() {
        return getSharedStringData(SharedKeys.DISCLAIMER_TEXT);
    }

    public void setDisclaimerText(String disclaimerText) {
        setSharedStringData(SharedKeys.DISCLAIMER_TEXT, disclaimerText);
    }

    public String getKYCBenefitsOne() {
        return getSharedStringData(SharedKeys.KYC_BENEFITS_ONE);
    }

    public void setKYCBenefitsOne(String kycBenefitsOne) {
        setSharedStringData(SharedKeys.KYC_BENEFITS_ONE, kycBenefitsOne);
    }

    public String getKYCBenefitsTwo() {
        return getSharedStringData(SharedKeys.KYC_BENEFITS_TWO);
    }

    public void setKYCBenefitsTwo(String kycBenefitsTwo) {
        setSharedStringData(SharedKeys.KYC_BENEFITS_TWO, kycBenefitsTwo);
    }

    public void setShowFAQ(boolean showFAQ) {
        setSharedBooleanData(SharedKeys.SHOW_FAQ, showFAQ);
    }

    public boolean isShowFAQ() {
        return getSharedBooleanData(SharedKeys.SHOW_FAQ, false);
    }


    public void setIsPaymentDetailsShownAtHome(boolean isShownOnHome) {
        setSharedBooleanData(SharedKeys.PAYMENT_DETAILS_SCREEN_SHOWN_ONCE_ON_HOME, isShownOnHome);
    }

    public boolean isPaymentDetailsShownAtHome() {
        return getSharedBooleanData(SharedKeys.PAYMENT_DETAILS_SCREEN_SHOWN_ONCE_ON_HOME, false);
    }

    public void setIsProfileDisclaimerAccepted(boolean disclaimerAccepted) {
        setSharedBooleanData(SharedKeys.IS_PROFILE_DISCLAIMER_ACCEPTED, disclaimerAccepted);
    }

    public boolean isProfileDisclaimerAccepted() {
        return getSharedBooleanData(SharedKeys.IS_PROFILE_DISCLAIMER_ACCEPTED, false);
    }

    public boolean isMarketingCampaign() {
        return getSharedBooleanData(SharedKeys.MARKETING_CAMPAIGN, false);
    }

    public void setMarketingCampaign(boolean marketingCampaign) {
        setSharedBooleanData(SharedKeys.MARKETING_CAMPAIGN, marketingCampaign);
    }

    public boolean isWhatsNewShown() {
        return getSharedBooleanData(SharedKeys.WHATS_NEW_SHOWN, false);
    }

    public void setWhatsNewShown(boolean whatsNewShown) {
        setSharedBooleanData(SharedKeys.WHATS_NEW_SHOWN, whatsNewShown);
    }

    public long getEditProfileShownTime() {
        return getSharedLongData(SharedKeys.EDIT_PROFILE_SHOWN_TIME, -1);
    }

    public void setEditProfileShownTime(long editProfileShownTime) {
        setSharedLongData(SharedKeys.EDIT_PROFILE_SHOWN_TIME, editProfileShownTime);
    }

    public boolean isEditingAnswerFirstTime() {
        return getSharedBooleanData(SharedKeys.EDITING_ANSWER_FIRST_TIME, true);
    }

    public void setEditingAnswerFirstTime() {
        setSharedBooleanData(SharedKeys.EDITING_ANSWER_FIRST_TIME, false);
    }

    public void setPowerUpTransferFromBankAttempted() {
        setSharedBooleanData(SharedKeys.POWERUP_TRANSFER_FROM_BANK_ATTEMPTED, true);
    }

    public boolean isPowerUpTransferFromBankAttempted() {
        return getSharedBooleanData(SharedKeys.POWERUP_TRANSFER_FROM_BANK_ATTEMPTED, false);
    }

    public void setPowerUpTransferFromBankScreenShown() {
        setSharedBooleanData(SharedKeys.POWERUP_TRANSFER_FROM_BANK_SCREEN_SHOWN, true);
    }

    public boolean isPowerUpTransferFromBankScreenShown() {
        return getSharedBooleanData(SharedKeys.POWERUP_TRANSFER_FROM_BANK_SCREEN_SHOWN, false);
    }

    public void setPrivateContestInvitationCode(String privateCode) {
        setSharedStringData(SharedKeys.PRIVATE_CONTEST_INVITATION_CODE, privateCode);
    }

    public String getPrivateContestInvitationCode() {
        return getSharedStringData(SharedKeys.PRIVATE_CONTEST_INVITATION_CODE);
    }
}