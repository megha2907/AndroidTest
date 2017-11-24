package in.sportscafe.nostragamus.webservice;


import java.util.List;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedMatchesResponse;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedResponse;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardsResponse;
import in.sportscafe.nostragamus.module.challengeRules.dto.RulesResponse;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.nostraHome.dto.TimeResponse;
import in.sportscafe.nostragamus.module.common.dto.MatchesResponse;
import in.sportscafe.nostragamus.module.contest.dto.ContestEntriesRequest;
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
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.AnswerRequest;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.AnswerResponse;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayerPollResponse;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayersPollRequest;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PredictionAllQuestionResponse;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.PowerUpBankStatusResponse;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.PowerupBankStatusRequest;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.TransferPowerUpFromBankRequest;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.TransferPowerUpFromBankResponse;
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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jeeva on 14/3/16.
 */
public interface NostragamusService {

    @GET("v1/game/users")
    Call<UserInfoResponse> getUserInfo();

    @GET("v1/game/sports")
    Call<SportsInfoResponse> getSportsInfo();

    @GET("v3/game/users/questions")
    Call<PredictionAllQuestionResponse> getAllPredictionQuestions(@Query("match_id") int matchId,
                                                                  @Query("room_id") int roomId);

    @GET("v1/game/users/tournaments")
    Call<TournamentFeedResponse> getCurrentTournaments(@Query("is_current") boolean isCurrent);

    @POST("/v3/game/login")
    Call<LogInResponse> loginUser(@Body LogInRequest logInRequest);

    @PUT("v1/game/users")
    Call<ApiResponse> updateUser(@Body UpdateUserRequest request);

    @Multipart
    @POST("v1/utility/addFileToS3")
    Call<Result> uploadImage(@Part MultipartBody.Part file, @Part("filePath") RequestBody filepath, @Part("fileName") RequestBody filename);

    @POST("v2/game/groups")
    Call<NewGroupResponse> createNewGroup(@Body NewGroupRequest request);

    @HTTP(method = "DELETE", path = "v2/game/groups/users", hasBody = true)
    Call<ApiResponse> removePerson(@Body MembersRequest request);

    @PUT("v2/game/groups/admins")
    Call<ApiResponse> makeAdmin(@Body MembersRequest request);

    @POST("v2/game/groups/users")
    Call<JoinGroupResponse> joinGroup(@Body AddGroupRequest memberRequest);

    @HTTP(method = "DELETE", path = "v2/game/users/groups", hasBody = true)
    Call<ApiResponse> leaveGroup(@Body MembersRequest request);

    @GET("v3/game/users/results")
    Call<MyResultsResponse> getMyResults(@Query("match_id") Integer matchId,
                                         @Query("player_id") Integer playerId,
                                         @Query("room_id") Integer roomId);

    @GET("v1/game/leaderboard/detail")
    Call<LeaderBoardResponse> getLeaderBoardDetail(@Query("group_id") Integer groupId,
                                                   @Query("challenge_id") Integer challengeId,
                                                   @Query("match_id") Integer matchId);

    @PUT("v2/game/groups/tournaments")
    Call<ApiResponse> updateGroupTournament(@Body GroupTournamentUpdateRequest request);

    @PUT("v2/game/groups")
    Call<ApiResponse> updateGroupName(@Body GroupNameUpdateRequest request);

    @GET("v2/game/users/groups/{group_id}/info")
    Call<GroupSummaryResponse> getGroupSummary(@Path("group_id") Integer groupId);

    @GET("v2/game/users/groups")
    Call<AllGroupsResponse> getAllGroups();

    @GET("v1/setting/getSettingsBody")
    Call<AppSettingsResponse> getAppSettings(@Query("unique_id") String uniqueId,@Query("app_type") String flavor);

    /*@POST("v2/game/users/poll")
    Call<AudiencePollResponse> getAudiencePoll(@Body AudiencePollRequest request);*/

    @GET("v2/game/refreshToken")
    Call<JwtToken> refreshAccessToken();

    @GET("/v2/game/applyResultsPowerups")
    Call<ReplayPowerupResponse> getReplayPowerup(@Query("powerup_id") String powerupId,
                                                 @Query("match_id") Integer matchId);

    @GET("/v2/game/applyResultsPowerups")
    Call<ReplayPowerupResponse> getFlipPowerup(@Query("powerup_id") String powerupId,
                                               @Query("match_id") Integer matchId,
                                               @Query("question_id") Integer questionId);

    @GET("/v1/game/users/getPlayerProfile")
    Call<PlayerInfoResponse> getPlayerInfo(@Query("player_user_id") Integer playerId);

    @GET("v3/game/users/matches/timeline")
    Call<MatchesResponse> getTimelines(
            @Query("room_id") Integer roomId,
            @Query("player_id") Integer playerUserId,
            @Query("skip") int skip,
            @Query("limit") int limit
    );

/*

    @GET("v1/game/users/getFuzzyPlayer")
    Call<FuzzyPlayerResponse> fuzzyPlayers(@Query("player_user_name") String key, @Query("match_id") Integer matchId);
*/

    @GET("v3/game/getPlayerMatchResult")
    Call<MyResultsResponse> getPlayerResult(@Query("player_id") Integer playerId,
                                            @Query("match_id") Integer matchId,
                                            @Query("room_id") int roomId);

    @GET("v3/game/getMatchAnswersStats")
    Call<MatchAnswerStatsResponse> playerResultPercentage(@Query("match_id") Integer matchId);

    /*@GET("v2/game/users/leaderboard/summary")
    Call<LBLandingResponse> getLBLandingSummary(@QueryMap Map<String, String> queries);

    @GET("v1/game/users/getFuzzySearchLeaderboards")
    Call<FuzzyLbResponse> fuzzyLbs(@Query("search_key") String key);*/

    @GET("/v1/game/users/getPendingUserPopups")
    Call<PopUpResponse> getPopUps(@Query("screen_name") String screenName);

    @DELETE("v1/game/users/resetGroupLeaderboard")
    Call<ApiResponse> resetLeaderboard(@Query("group_id") Integer groupId);

    @PUT("/v1/game/users/acknowledgeUserPopup")
    Call<ApiResponse> getAcknowledgePopupRequest(@Query("popup_name") String popUpName);

    @HTTP(method = "DELETE", path = "v2/game/groups", hasBody = true)
    Call<ApiResponse> deleteGroup(@Body MembersRequest request);

    @GET("/v2/game/users/getCommonLeaderboards")
    Call<CompareLeaderBoardResponse> getLeaderBoardComparisonRequest(@Query("player_id") Integer playerId);

    /*@POST("v2/game/users/challenges/transferFromBank")
    Call<BankTransferResponse> bankTransfer(@Body BankTransferRequest bankTransferRequest);*/

    @POST("v2/game/submitQuestion")
    Call<ApiResponse> submitQuestion(@Body AddQuestionRequest addQuestionRequest);

    @GET("v2/game/getUpcomingMatches")
    Call<TourListResponse> getTourList();

    @POST("v2/game/generateOrder")
    Call<GenerateOrderResponse> generateOrder(@Body GenerateOrderRequest request);

    @PUT("v2/game/users/paymentInfo")
    Call<AddUserPaymentDetailsResponse> addUserPaymentBankDetails(@Body AddBankDetailsRequest request);

    @PUT("v2/game/users/paymentInfo")
    Call<AddUserPaymentDetailsResponse> addUserPaymentPaytmDetails(@Body AddUserPaymentPaytmRequest request);

    @GET("/v1/wallet/getUserTransactions")
    Call<List<WalletHistoryTransaction>> getWalletTransactionHistory(@Query("page") int pageNumber);

    @GET("v1/utility/getServerTime")
    Call<TimeResponse> getServerTime();

/*
    @GET("/v2/game/users/getHomeChallenges")
    Call<AllChallengesResponse> getCompletedChallengesRequest( @Query("filter") String filter,
                                                               @Query("skip") int skip,
                                                               @Query("limit") int limit);*/

    @POST("v3/game/users/editAnswer")
    Call<ApiResponse> changeAnswer(@Body ChangeAnswer changeAnswer);

    @GET("v2/game/appUpdate")
    Call<AppUpdateResponse> getAppUpdates(@Query("app_type") String flavor);

    @POST("/v1/wallet/addMoneyToWallet")
    Call<GenerateOrderResponse> addMoneyToWallet(@Body AddMoneyToWalletRequest request);

    @POST("/v1/wallet/getUserWallet")
    Call<UserWalletResponse> getUserWallet();

    @POST("/v1/wallet/withdraw")
    Call<WithdrawFromWalletResponse> withdrawFromWallet(@Body WithdrawFromWalletRequest request);

    /*@POST("/v1/wallet/useWallet")
    Call<JoinChallengeResponse> useWalletToJoinChallenge(@Body JoinChallengeRequest request);*/

    @POST("/v1/wallet/useWallet")
    Call<BuyResponse> useWalletToBuyFromStore(@Body BuyRequest request);

    @GET("/v1/setting/getLatestApk")
    Call<String> getLatestApk();

    @GET("v2/game/getReferralScreen")
    Call<UserReferralResponse> getUserReferralInfo(@Query("app_type") String flavor);

    @GET("v2/game/getReferralTransactions")
    Call<UserReferralHistoryResponse> getUserReferralHistory(@Query("app_type") String flavor,@Query("transaction_type")  String transactionType);

    @GET("v2/game/verifyReferralCode")
    Call<VerifyReferralCodeResponse> verifyReferralCode(@Query("referral_code") String referralCode);

    @GET("v1/game/seenWhatsNew")
    Call<ApiResponse> getWhatsNewShown();

    @GET("/v2/game/resendOTP")
    Call<VerifyOTPResponse> getOTPRequest(@Query("mobile") String phoneNumber);

    @GET("/v2/game/verifyOTP")
    Call<VerifyOTPResponse> verifyOTPRequest(@Query("otp_code") String otp);

    @POST("/v3/game/login")
    Call<LogInResponse> loginUserV3(@Body LogInRequest logInRequest);

    @POST("/v2/game/login")
    Call<LogInResponse> loginUserV2(@Body LogInRequest logInRequest);

    @GET("/v2/game/store/getProductsByCategory")
    Call<StoreApiResponse> getStoreDetails(@Query("category") String category);

    @POST("/v3/game/users/powerupTransferStats")
    Call<PowerUpBankStatusResponse> powerupTransferStatus(@Body PowerupBankStatusRequest request);

    @GET("v2/game/store/getPowerupTransaction")
    Call<PBTransactionHistoryResponse> getPBTransactionHistory();

    @GET("v2/game/showUpgradePopups")
    Call<UpgradeToProResponse> shouldShowUpgradeToProDialog();

    @POST("v1/game/users/updateProfile")
    Call<ApiResponse> updateUserProfile(@Body UpdateUserProfileRequest updateUserProfileRequest);

    @GET("/v3/game/challenges/new")
    Call<List<NewChallengesResponse>> getNewHomeChallenges();

    @GET("v3/game/challenges/getContests")
    Call<ContestResponse> getContests(@Query("challenge_id") int challengeId,
                                      @Query("flavor") String flavor);

    @GET("v3/game/challenges/inPlay")
    Call<List<InPlayResponse>> getInPlayChallenges();

    @GET("v3/game/challenges/rewards")
    Call<RewardsResponse> getRewardDetails(@Query("room_id") Integer roomId,@Query("config_id") Integer configId);

    @GET("v3/game/challenges/contestRooms")
    Call<ContestEntriesResponse> getContestEntries(@Query("config_id") Integer contestId);

    @GET("v3/game/challenges/getUserMatches")
    Call<InPlayMatchesResponse> getInPlayMatches(@Query("room_id") int roomId, @Query("challenge_id") int challengeId);

    @GET("v3/game/challenges/getUserMatches")
    Call<NewChallengeMatchesResponse> getNewChallengeMatches(@Query("challenge_id") int challengeId);

    @GET("v3/game/rooms/leaderboards")
    Call<UserLeaderBoardResponse> getUserLeaderBoardDetails(@Query("room_id") Integer userLeaderBoardRequest);

    @GET("v3/game/contest/rules")
    Call<RulesResponse> getContestRules(@Query("contest_id") int rulesRequest);

    @POST("v3/game/users/poll")
    Call<PlayerPollResponse> getPlayersPoll(@Body PlayersPollRequest request);

    @POST("v3/game/users/answer")
    Call<AnswerResponse> savePredictionAnswer(
            @Body AnswerRequest answer,
            @Query("is_match_complete") boolean isMatchComplete,
            @Query("is_minority_option") boolean isMinorityOption
    );

    @POST("v3/game/users/transferFromBank")
    Call<TransferPowerUpFromBankResponse> transferPowerUpsFromBank(@Body TransferPowerUpFromBankRequest request);

    @POST("/v3/game/challenges/joinPseudoContest")
    Call<JoinPseudoContestResponse> joinPseudoContest(@Body JoinPseudoContestRequest request);

    @POST("/v3/game/challenges/joinContestQueue")
    Call<JoinContestQueueResponse> joinContestQueue(@Body JoinContestQueueRequest request);

    @POST("/v3/game/challenges/verifyJoinContest")
    Call<VerifyJoinContestResponse> verifyJoinContest(@Body VerifyJoinContestRequest request);

    @GET("/v3/game/challenges/completed")
    Call<List<CompletedResponse>> getCompletedChallenges(@Query("skip") int skip, @Query("limit") int limit);

    @GET("v3/game/challenges/getUserMatches")
    Call<CompletedMatchesResponse> getCompletedChallengeMatches(@Query("room_id") int roomId, @Query("challenge_id") int challengeId);

    @POST("v3/game/users/ticket")
    Call<ApiResponse> sendErrorReport(@Body SubmitReportRequest request);
}