package in.sportscafe.nostragamus.webservice;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Config;
import in.sportscafe.nostragamus.module.allchallenges.dto.AllChallengesResponse;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfigsResponse;
import in.sportscafe.nostragamus.module.allchallenges.join.dto.JoinChallengeRequest;
import in.sportscafe.nostragamus.module.allchallenges.join.dto.JoinChallengeResponse;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardsRequest;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardsResponse;
import in.sportscafe.nostragamus.module.challengeRules.dto.RulesRequest;
import in.sportscafe.nostragamus.module.challengeRules.dto.RulesResponse;
import in.sportscafe.nostragamus.module.contest.dto.ContestEntriesRequest;
import in.sportscafe.nostragamus.module.contest.dto.ContestEntriesResponse;
import in.sportscafe.nostragamus.module.contest.dto.ContestRequest;
import in.sportscafe.nostragamus.module.contest.dto.ContestResponse;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchRequest;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponse;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.module.navigation.appupdate.AppUpdateResponse;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.common.TimeResponse;
import in.sportscafe.nostragamus.module.feed.dto.FeedResponse;
import in.sportscafe.nostragamus.module.feed.dto.MatchesResponse;
import in.sportscafe.nostragamus.module.fuzzylbs.FuzzyLbResponse;
import in.sportscafe.nostragamus.module.fuzzyplayers.FuzzyPlayerResponse;
import in.sportscafe.nostragamus.module.navigation.edituserprofile.UpdateUserProfileRequest;
import in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction.dto.PBTransactionHistoryResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.AddMoneyToWalletRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.dto.WithdrawFromWalletRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.dto.WithdrawFromWalletResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.AddBankDetailsRequest;
import in.sportscafe.nostragamus.module.othersanswers.MatchAnswerStatsResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.AddUserPaymentDetailsResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.AddUserPaymentPaytmRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.GenerateOrderRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.GenerateOrderResponse;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsResponse;
import in.sportscafe.nostragamus.module.play.myresults.dto.ReplayPowerupResponse;
import in.sportscafe.nostragamus.module.play.powerup.dto.PowerupBankStatusRequest;
import in.sportscafe.nostragamus.module.play.powerup.dto.PowerUpBankStatusResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Answer;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollRequest;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.QuestionsResponse;
import in.sportscafe.nostragamus.module.popups.bankpopups.BankTransferRequest;
import in.sportscafe.nostragamus.module.popups.bankpopups.BankTransferResponse;
import in.sportscafe.nostragamus.module.navigation.submitquestion.add.AddQuestionRequest;
import in.sportscafe.nostragamus.module.navigation.submitquestion.tourlist.TourListResponse;
import in.sportscafe.nostragamus.module.prediction.dto.AnswerRequest;
import in.sportscafe.nostragamus.module.prediction.dto.AnswerResponse;
import in.sportscafe.nostragamus.module.prediction.dto.PlayerPollResponse;
import in.sportscafe.nostragamus.module.prediction.dto.PlayersPollRequest;
import in.sportscafe.nostragamus.module.prediction.dto.PredictionAllQuestionResponse;
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
import in.sportscafe.nostragamus.module.user.lblanding.LBLandingResponse;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardResponse;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoardRequest;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoardResponse;
import in.sportscafe.nostragamus.module.user.login.dto.JwtToken;
import in.sportscafe.nostragamus.module.user.login.dto.LogInRequest;
import in.sportscafe.nostragamus.module.user.login.dto.LogInResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
import in.sportscafe.nostragamus.module.user.myprofile.dto.TournamentFeedResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.TournamentsResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.nostragamus.module.user.myprofile.edit.UpdateUserRequest;
import in.sportscafe.nostragamus.module.user.myprofile.edit.VerifyReferralCodeResponse;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfoResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.walletHistory.WalletHistoryTransaction;
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

    public Call<PlayerInfoResponse> getPlayerInfoRequest(Integer playerId) {
        return mNostragamusService.getPlayerInfo(playerId);
    }

    public Call<FeedResponse> getMatches(Integer tourId) {
        return mNostragamusService.getMatches(tourId);
    }

    public Call<MatchesResponse> getMatchResults(Boolean isAttempted, Boolean WithoutCommentary) {
        return mNostragamusService.getMatchResults(isAttempted, WithoutCommentary);
    }

    public Call<QuestionsResponse> getAllQuestions(int matchId) {
        return mNostragamusService.getQuestions(matchId);
    }

    public Call<PredictionAllQuestionResponse> getAllPredictionQuestions(int matchId, int roomId) {
        return mNostragamusService.getAllPredictionQuestions(matchId);
    }

    public Call<TournamentsResponse> getTournaments(boolean isCurrent, boolean groupbySport) {
        return mNostragamusService.getTournaments(isCurrent, groupbySport);
    }

    public Call<TournamentFeedResponse> getCurrentTournaments(boolean isCurrent) {
        return mNostragamusService.getCurrentTournaments(isCurrent);
    }

    public Call<ApiResponse> getPostAnswerRequest(Answer answer, boolean isMatchComplete, boolean isMinorityOption) {
        return mNostragamusService.saveAnswer(answer, isMatchComplete, isMinorityOption);
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

    public Call<MyResultsResponse> getMyResultsRequest(Integer MatchId, Integer playerId) {
        return mNostragamusService.getMyResults(MatchId, playerId);
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

    public Call<AudiencePollResponse> getAudiencePoll(AudiencePollRequest request) {
        return mNostragamusService.getAudiencePoll(request);
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

    public Call<MatchesResponse> getTimelinesRequest(Integer challengeId, Integer playerUserId, int skip, int limit) {
        return mNostragamusService.getTimelines(challengeId, playerUserId, skip, limit);
    }

    public Call<FuzzyPlayerResponse> getFuzzyPlayersRequest(String key, Integer matchId) {
        return mNostragamusService.fuzzyPlayers(key, matchId);
    }

    public Call<MyResultsResponse> getPlayerResultRequest(Integer playerId, Integer matchId) {
        return mNostragamusService.getPlayerResult(playerId, matchId);
    }

    public Call<MatchAnswerStatsResponse> getPlayerResultPercentageRequest(Integer matchId) {
        return mNostragamusService.playerResultPercentage(matchId);
    }

    public Call<LBLandingResponse> getLBLandingSummary(Integer sportsId, Integer groupId, Integer challengeId, Integer tourId) {
        Map<String, String> queries = new HashMap<>();
        if (null != sportsId) {
            queries.put("sports_id", sportsId.toString());
        }
        if (null != groupId) {
            queries.put("group_id", groupId.toString());
        }
        if (null != challengeId) {
            queries.put("challenge_id", challengeId.toString());
        }
        if (null != tourId) {
            queries.put("tournament_id", tourId.toString());
        }
        return mNostragamusService.getLBLandingSummary(queries);
    }

    public Call<FuzzyLbResponse> getFuzzyLbsRequest(String key) {
        return mNostragamusService.fuzzyLbs(key);
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

    public Call<AllChallengesResponse> getAllChallengesRequest(String filter) {
        return mNostragamusService.getAllChallenges(filter);
    }

    public Call<BankTransferResponse> getBankTransferRequest(BankTransferRequest request) {
        return mNostragamusService.bankTransfer(request);
    }

    public Call<ApiResponse> getSubmitQuestionRequest(AddQuestionRequest request) {
        return mNostragamusService.submitQuestion(request);
    }

    public Call<TourListResponse> getTourListRequest() {
        return mNostragamusService.getTourList();
    }

    public Call<ChallengeConfigsResponse> getChallengeConfigsRequest(int challengeId, Integer configIndex, String AppFlavor) {
        return mNostragamusService.getConfigs(challengeId ,configIndex,AppFlavor);
    }

    public Call<Challenge> getJoinChallengeRequest(int challengeId, int configIndex) {
        return mNostragamusService.getJoinChallenge(challengeId, configIndex);
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

    public Call<AllChallengesResponse> getCompletedChallengesRequest(String filter , int skip, int limit) {
        return mNostragamusService.getCompletedChallengesRequest(filter,skip,limit);
    }

    public Call<ApiResponse> getChangeAnswerRequest(ChangeAnswer changeAnswer) {
        return mNostragamusService.changeAnswer(changeAnswer);
    }

    public Call<AppUpdateResponse> getAppUpdatesRequest(String flavor) {
        return mNostragamusService.getAppUpdates(flavor);
    }

    public Call<GenerateOrderResponse> addMoneyToWalletRequest(AddMoneyToWalletRequest request) {
        return mNostragamusService.addMoneyToWallet(request);
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

    public Call<JoinChallengeResponse> joinChallenge(JoinChallengeRequest request) {
        return mNostragamusService.useWalletToJoinChallenge(request);
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
        return mNostragamusService.getNewHomeChallenges();
    }

    public Call<ContestResponse> getContests(int challengeId) {
        return mNostragamusService.getContests(challengeId);
    }

    public Call<List<InPlayResponse>> getInPlayChallenges() {
        return mNostragamusService.getInPlayChallenges();
    }

    public Call<RewardsResponse> getRewardsDetails(int rewardsRequest) {
        return mNostragamusService.getRewardDetails(rewardsRequest);
    }

    public Call<ContestEntriesResponse> getContestEntries(ContestEntriesRequest request) {
        return mNostragamusService.getContestEntries(request);
    }

    public Call<InPlayMatchesResponse> getInPlayMatches(InPlayMatchRequest request) {
        return mNostragamusService.getInPlayMatches(request);
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
}