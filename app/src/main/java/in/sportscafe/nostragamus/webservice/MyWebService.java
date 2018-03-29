package in.sportscafe.nostragamus.webservice;

import java.io.File;
import java.util.List;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Config;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedMatchesResponse;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedResponse;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardsResponse;
import in.sportscafe.nostragamus.module.challengeRules.dto.RulesResponse;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrivateContestPrizeTemplateResponse;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.CreatePrivateContestRequest;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.CreatePrivateContestResponse;
import in.sportscafe.nostragamus.module.navigation.help.howtoplay.dto.HowToPlayResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.AddMoneyPaymentCouponRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.AddMoneyPaymentCouponResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.CashFreeGenerateOrderRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.CashFreeGenerateOrderResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.VerifyPaymentCouponRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.VerifyPaymentCouponResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.VerifyPaymentResponse;
import in.sportscafe.nostragamus.module.nostraHome.dto.TimeResponse;
import in.sportscafe.nostragamus.module.common.dto.MatchesResponse;
import in.sportscafe.nostragamus.module.contest.dto.ContestEntriesResponse;
import in.sportscafe.nostragamus.module.contest.dto.ContestResponse;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestQueueRequest;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestQueueResponse;
import in.sportscafe.nostragamus.module.contest.dto.VerifyJoinContestRequest;
import in.sportscafe.nostragamus.module.contest.dto.VerifyJoinContestResponse;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponse;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.module.navigation.appupdate.AppUpdateResponse;
import in.sportscafe.nostragamus.module.navigation.edituserprofile.UpdateUserProfileRequest;
import in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction.dto.PBTransactionHistoryResponse;
import in.sportscafe.nostragamus.module.navigation.submitquestion.add.AddQuestionRequest;
import in.sportscafe.nostragamus.module.navigation.submitquestion.tourlist.TourListResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.AddMoneyToWalletRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.AddBankDetailsRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.AddUserPaymentDetailsResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.AddUserPaymentPaytmRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.GenerateOrderRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.GenerateOrderResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.walletHistory.WalletHistoryTransaction;
import in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.dto.WithdrawFromWalletRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.dto.WithdrawFromWalletResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.JoinPseudoContestRequest;
import in.sportscafe.nostragamus.module.newChallenges.dto.JoinPseudoContestResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengeMatchesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.module.othersanswers.MatchAnswerStatsResponse;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsResponse;
import in.sportscafe.nostragamus.module.play.myresults.dto.ReplayPowerupResponse;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerContestsResponse;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerRequest;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerResponse;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dto.QuestionForEditAnswerResponse;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dto.SaveEditAnswerRequest;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dto.SaveEditAnswerResponse;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.AnswerRequest;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.AnswerResponse;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayerPollResponse;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayersPollRequest;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PredictionAllQuestionResponse;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.PowerUpBankStatusResponse;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.PowerupBankStatusRequest;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.TransferPowerUpFromBankRequest;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.TransferPowerUpFromBankResponse;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.FindPrivateContestResponse;
import in.sportscafe.nostragamus.module.settings.app.dto.AppSettingsResponse;
import in.sportscafe.nostragamus.module.store.buy.BuyRequest;
import in.sportscafe.nostragamus.module.store.buy.BuyResponse;
import in.sportscafe.nostragamus.module.store.dto.StoreApiResponse;
import in.sportscafe.nostragamus.module.upgradeToPro.dto.UpgradeToProResponse;
import in.sportscafe.nostragamus.module.user.group.allgroups.dto.AllGroupsResponse;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupNameUpdateRequest;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupTournamentUpdateRequest;
import in.sportscafe.nostragamus.module.user.group.joingroup.dto.JoinGroupResponse;
import in.sportscafe.nostragamus.module.user.group.members.AddGroupRequest;
import in.sportscafe.nostragamus.module.user.group.members.MembersRequest;
import in.sportscafe.nostragamus.module.user.group.newgroup.NewGroupRequest;
import in.sportscafe.nostragamus.module.user.group.newgroup.NewGroupResponse;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardResponse;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoardResponse;
import in.sportscafe.nostragamus.module.user.login.dto.JwtToken;
import in.sportscafe.nostragamus.module.user.login.dto.LogInRequest;
import in.sportscafe.nostragamus.module.user.login.dto.LogInResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
import in.sportscafe.nostragamus.module.user.myprofile.dto.TournamentFeedResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.nostragamus.module.user.myprofile.edit.UpdateUserRequest;
import in.sportscafe.nostragamus.module.user.myprofile.edit.VerifyReferralCodeResponse;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfoResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Jeeva on 15/3/16.
 */
public class MyWebService extends AbstractWebService<NostragamusService> {

    private static MyWebService sMyWebService = new MyWebService();

    public static synchronized MyWebService getInstance() {
        return sMyWebService;
    }

    private NostragamusService mNostragamusService;

    public void init() {
        // Initialize SportsCafe Api service using retrofit
        mNostragamusService = init(Config.API_BASE_URL, NostragamusService.class);
    }

    public Call<LogInResponse> getLogInRequest(LogInRequest logInRequest) {
        if (BuildConfig.IS_PAID_VERSION) {
            return mNostragamusService.loginUserV3(logInRequest);
        }else {
            return mNostragamusService.loginUserV2(logInRequest);
        }
    }

    public Call<ApiResponse> getUpdateUserRequest(UpdateUserRequest request) {
        return mNostragamusService.updateUser(request);
    }

    public Call<UserInfoResponse> getUserInfoRequest(String userId) {
        return mNostragamusService.getUserInfo();
    }

    public Call<SportsInfoResponse> getSportsInfoRequest() {
        return mNostragamusService.getSportsInfo();
    }

    public Call<PlayerInfoResponse> getPlayerInfoRequest(Integer playerId) {
        return mNostragamusService.getPlayerInfo(playerId);
    }


    public Call<PredictionAllQuestionResponse> getAllPredictionQuestions(int matchId, int roomId) {
        return mNostragamusService.getAllPredictionQuestions(matchId,roomId);
    }

    public Call<TournamentFeedResponse> getCurrentTournaments(boolean isCurrent) {
        return mNostragamusService.getCurrentTournaments(isCurrent);
    }


    public Call<JoinGroupResponse> getJoinGroupRequest(String groupCode) {
        AddGroupRequest memberRequest = new AddGroupRequest();
        memberRequest.setGroupCode(groupCode);
        return mNostragamusService.joinGroup(memberRequest);
    }

    public Call<NewGroupResponse> getNewGroupRequest(NewGroupRequest request) {
        return mNostragamusService.createNewGroup(request);
    }

    public Call<ApiResponse> getRemovePersonRequest(MembersRequest request) {
        return mNostragamusService.removePerson(request);
    }

    public Call<ApiResponse> getMakeAdminRequest(MembersRequest request) {
        return mNostragamusService.makeAdmin(request);
    }

    public Call<ApiResponse> getLeaveGroupRequest(MembersRequest request) {
        return mNostragamusService.leaveGroup(request);
    }

    public Call<MyResultsResponse> getMyResultsRequest(Integer matchId, Integer playerId, int roomId) {
        return mNostragamusService.getMyResults(matchId, playerId,roomId);
    }

    public Call<LeaderBoardResponse> getLeaderBoardDetailRequest(Integer groupId,
                                                                 Integer challengeId, Integer matchId) {
        return mNostragamusService.getLeaderBoardDetail(groupId, challengeId, matchId);
    }

    public Call<ApiResponse> getGrpTournamentUpdateRequest(GroupTournamentUpdateRequest request) {
        return mNostragamusService.updateGroupTournament(request);
    }

    public Call<ApiResponse> getGrpNameUpdateRequest(GroupNameUpdateRequest request) {
        return mNostragamusService.updateGroupName(request);
    }

    public Call<GroupSummaryResponse> getGroupSummaryRequest(Integer groupId) {
        return mNostragamusService.getGroupSummary(groupId);
    }

    public Call<AllGroupsResponse> getAllGroupsRequest() {
        return mNostragamusService.getAllGroups();
    }

    public Call<Result> getUploadPhotoRequest(File file, String filepath, String filename) {
        return mNostragamusService.uploadImage(
                MultipartBody.Part.createFormData("file", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)),
                RequestBody.create(MediaType.parse("multipart/form-data"), filepath),
                RequestBody.create(MediaType.parse("multipart/form-data"), filename));
    }

    public Call<AppSettingsResponse> getAppSettingsRequest(String uniqueId,String flavor) {
        return mNostragamusService.getAppSettings(uniqueId,flavor);
    }

    public Call<JwtToken> getRefreshTokenRequest() {
        return mNostragamusService.refreshAccessToken();
    }

    public Call<ReplayPowerupResponse> getReplayPowerup(String powerupId, Integer matchId) {
        return mNostragamusService.getReplayPowerup(powerupId, matchId);
    }

    public Call<ReplayPowerupResponse> getFlipPowerup(String powerupId, Integer matchId, Integer questionId) {
        return mNostragamusService.getFlipPowerup(powerupId, matchId, questionId);
    }

    public Call<MatchesResponse> getTimelinesRequest(Integer roomId, Integer playerUserId, int skip, int limit) {
        return mNostragamusService.getTimelines(roomId, playerUserId, skip, limit);
    }

    public Call<MyResultsResponse> getPlayerResultRequest(Integer playerId, Integer matchId,int roomId) {
        return mNostragamusService.getPlayerResult(playerId, matchId,roomId);
    }

    public Call<MatchAnswerStatsResponse> getPlayerResultPercentageRequest(Integer matchId) {
        return mNostragamusService.playerResultPercentage(matchId);
    }

    public Call<PopUpResponse> getPopUpsRequest(String screenName) {
        return mNostragamusService.getPopUps(screenName);
    }

    public Call<ApiResponse> getResetLeaderboardRequest(Integer groupId) {
        return mNostragamusService.resetLeaderboard(groupId);
    }

    public Call<ApiResponse> getAcknowledgePopupRequest(String popUpName) {
        return mNostragamusService.getAcknowledgePopupRequest(popUpName);
    }

    public Call<ApiResponse> getDeleteGroupRequest(MembersRequest request) {
        return mNostragamusService.deleteGroup(request);
    }

    public Call<CompareLeaderBoardResponse> getLeaderBoardComparisonRequest(Integer playerId) {
        return mNostragamusService.getLeaderBoardComparisonRequest(playerId);
    }

    public Call<ApiResponse> getSubmitQuestionRequest(AddQuestionRequest request) {
        return mNostragamusService.submitQuestion(request);
    }

    public Call<TourListResponse> getTourListRequest() {
        return mNostragamusService.getTourList();
    }

    public Call<GenerateOrderResponse> getGenerateOrderRequest(GenerateOrderRequest request) {
        return mNostragamusService.generateOrder(request);
    }

    public Call<AddUserPaymentDetailsResponse> addUserPaymentBankDetails(AddBankDetailsRequest request) {
        return mNostragamusService.addUserPaymentBankDetails(request);
    }

    public Call<AddUserPaymentDetailsResponse> addUserPaymentPaytmDetails(AddUserPaymentPaytmRequest request) {
        return mNostragamusService.addUserPaymentPaytmDetails(request);
    }

    public Call<List<WalletHistoryTransaction>> getWalletTransactionHistory(int pageNumber) {
        return mNostragamusService.getWalletTransactionHistory(pageNumber);
    }

    public Call<TimeResponse> getServerTime() {
        return mNostragamusService.getServerTime();
    }

    public Call<SaveEditAnswerResponse> saveEditAnswer(boolean isMinorityOption,
                                                       SaveEditAnswerRequest saveEditAnswerRequest) {
        return mNostragamusService.saveEditAnswer(false, isMinorityOption, saveEditAnswerRequest);
    }

    public Call<QuestionForEditAnswerResponse> questionForEditAnswer(int roomId, int matchId, int questionId) {
        return mNostragamusService.questionForEditAnswer(roomId, matchId, questionId);
    }

    public Call<AppUpdateResponse> getAppUpdatesRequest(String flavor) {
        return mNostragamusService.getAppUpdates(flavor);
    }

    public Call<GenerateOrderResponse> addMoneyToWalletRequest(AddMoneyToWalletRequest request) {
        return mNostragamusService.addMoneyToWallet(request);
    }

    public Call<AddMoneyPaymentCouponResponse> addMoneyPaymentCouponRequest(AddMoneyPaymentCouponRequest request) {
        return mNostragamusService.addMoneyPaymentCouponToWallet(request);
    }

    public Call<VerifyPaymentCouponResponse> verifyPaymentCouponMoneyAdded(VerifyPaymentCouponRequest request) {
        return mNostragamusService.verifyPaymentCouponToWallet(request);
    }

    public Call<UserWalletResponse> getUserWallet() {
        return mNostragamusService.getUserWallet();
    }

    public Call<UserReferralResponse> getUserReferralInfo(String flavor) {
        return mNostragamusService.getUserReferralInfo(flavor);
    }

    public Call<WithdrawFromWalletResponse> withdrawFromWallet(WithdrawFromWalletRequest request) {
        return mNostragamusService.withdrawFromWallet(request);
    }

    public Call<BuyResponse> buyFromStore(BuyRequest request) {
        return mNostragamusService.useWalletToBuyFromStore(request);
    }

    public Call<String> getLatestApk() {
        return mNostragamusService.getLatestApk();
    }

    public Call<UserReferralHistoryResponse> getUserReferralHistory(String flavor,String transactionType) {
        return mNostragamusService.getUserReferralHistory(flavor,transactionType);
    }

    public Call<VerifyReferralCodeResponse> verifyReferralCodeRequest(String referralCode) {
        return mNostragamusService.verifyReferralCode(referralCode);
    }

    public Call<ApiResponse> getWhatsNewShown() {
        return mNostragamusService.getWhatsNewShown();
    }

    public Call<VerifyOTPResponse> getOTPRequest(String phoneNumber) {
        return mNostragamusService.getOTPRequest(phoneNumber);
    }

    public Call<VerifyOTPResponse> verifyOTPRequest(String otp) {
        return mNostragamusService.verifyOTPRequest(otp);
    }

    public Call<StoreApiResponse> getStoreDetails (String category) {
        return mNostragamusService.getStoreDetails(category);
    }

    public Call<PowerUpBankStatusResponse> getPowerupBankStatus(PowerupBankStatusRequest request) {
        return mNostragamusService.powerupTransferStatus(request);
    }

    public Call<PBTransactionHistoryResponse> getPBTransactionHistory(String flavor, String transactionType) {
        return mNostragamusService.getPBTransactionHistory();
    }

    public Call<UpgradeToProResponse> shouldShowUpgradeDialog() {
        return mNostragamusService.shouldShowUpgradeToProDialog();
    }

    public Call<ApiResponse> getUpdateUserProfileRequest(UpdateUserProfileRequest updateUserProfileRequest) {
        return mNostragamusService.updateUserProfile(updateUserProfileRequest);
    }

    public Call<List<NewChallengesResponse>> getNewHomeChallenges() {
        String flavor = "PS";
        if (BuildConfig.IS_ACL_VERSION && BuildConfig.IS_PAID_VERSION){
            flavor = "ACL";
        }else if (BuildConfig.IS_PAID_VERSION){
            flavor = "PRO";
        }

        boolean showOnePartyChallenge = true;

        return mNostragamusService.getNewHomeChallenges(flavor,showOnePartyChallenge);
    }

    public Call<ContestResponse> getContests(int challengeId, boolean needPoolContest) {
        return mNostragamusService.getContests(challengeId, Nostragamus.getInstance().getAppTypeFlavor(), needPoolContest,true);
    }

    public Call<List<InPlayResponse>> getInPlayChallenges() {
        return mNostragamusService.getInPlayChallenges();
    }

    public Call<RewardsResponse> getRewardsDetails(Integer rewardsRequest, Integer configId) {
        return mNostragamusService.getRewardDetails(rewardsRequest,configId);
    }

    public Call<ContestEntriesResponse> getContestEntries(int contestId) {
        return mNostragamusService.getContestEntries(contestId);
    }

    public Call<InPlayMatchesResponse> getInPlayMatches(int roomId, int challengeId) {
        return mNostragamusService.getInPlayMatches(roomId, challengeId);
    }

    public Call<NewChallengeMatchesResponse> getNewChallengeMatches(int challengeId) {
        return mNostragamusService.getNewChallengeMatches(challengeId);
    }

    public Call<UserLeaderBoardResponse> getUserLeaderBoardDetails(Integer userLeaderBoardRequest) {
        return mNostragamusService.getUserLeaderBoardDetails(userLeaderBoardRequest);
    }

    public Call<RulesResponse> getContestRules(int rulesRequest) {
        return mNostragamusService.getContestRules(rulesRequest);
    }

    public Call<PlayerPollResponse> getPlayerPoll(PlayersPollRequest request) {
        return mNostragamusService.getPlayersPoll(request);
    }

    public Call<AnswerResponse> savePredictionAnswer(AnswerRequest request,
                                                     boolean isMatchComplete, boolean isMinorityOption) {
        return mNostragamusService.savePredictionAnswer(request, isMatchComplete, isMinorityOption);
    }

    public Call<TransferPowerUpFromBankResponse> transferPowerUpFromBank(TransferPowerUpFromBankRequest request) {
        return mNostragamusService.transferPowerUpsFromBank(request);
    }

    public Call<JoinPseudoContestResponse> joinPseudoContest(JoinPseudoContestRequest request) {
        return mNostragamusService.joinPseudoContest(request);
    }

    public Call<JoinContestQueueResponse> joinContestQueue(JoinContestQueueRequest request) {
        return mNostragamusService.joinContestQueue(request);
    }

    public Call<VerifyJoinContestResponse> verifyJoinContest(VerifyJoinContestRequest request) {
        return mNostragamusService.verifyJoinContest(request);
    }

    public Call<List<CompletedResponse>> getCompletedChallenges(int skip, int limit) {
        return mNostragamusService.getCompletedChallenges(skip, limit);
    }

    public Call<CompletedMatchesResponse> getCompletedChallengeMatches(int roomId, int challengeId) {
        return mNostragamusService.getCompletedChallengeMatches(roomId, challengeId);
    }

    public Call<ApiResponse> sendErrorReport(SubmitReportRequest request) {
        return mNostragamusService.sendErrorReport(request);
    }

    public Call<BannerResponse> getBannerData() {
        return mNostragamusService.getBannerData();
    }

    public Call<CopyAnswerContestsResponse> getCopyAnswerContestList(int matchId) {
        return mNostragamusService.getCopyAnswerContests(matchId);
    }

    public Call<CopyAnswerResponse> copyAnswer(CopyAnswerRequest request) {
        return mNostragamusService.copyAnswer(request);
    }

    public Call<CashFreeGenerateOrderResponse> generateCashFreeOrderRequest(CashFreeGenerateOrderRequest request) {
        return mNostragamusService.generateCashFreeOrder(request);
    }

    public Call<VerifyPaymentResponse> verifyPayment(String orderId) {
        return mNostragamusService.verifyPayment(orderId);
    }

    public Call<HowToPlayResponse> getHowToPlayData(String slideId) {
        return mNostragamusService.getHowToPlayData(slideId);
    }

    public Call<CreatePrivateContestResponse> createPrivateContest(CreatePrivateContestRequest request) {
        return mNostragamusService.createPrivateContest(request);
    }

    public Call<List<PrivateContestPrizeTemplateResponse>> getPrivateContestPrizeTemplates() {
        return mNostragamusService.getPrivateContestPrizeTemplates();
    }

    public Call<FindPrivateContestResponse> getPrivateContestDetails(String privatCode) {
        return mNostragamusService.getPrivateContestDetails(privatCode);
    }
}