package in.sportscafe.nostragamus.webservice;


import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.module.allchallenges.dto.AllChallengesResponse;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfigsResponse;
import in.sportscafe.nostragamus.module.appupdate.AppUpdateResponse;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.common.TimeResponse;
import in.sportscafe.nostragamus.module.feed.dto.FeedResponse;
import in.sportscafe.nostragamus.module.feed.dto.MatchesResponse;
import in.sportscafe.nostragamus.module.fuzzylbs.FuzzyLbResponse;
import in.sportscafe.nostragamus.module.fuzzyplayers.FuzzyPlayerResponse;
import in.sportscafe.nostragamus.module.othersanswers.PlayerResultPercentageResponse;
import in.sportscafe.nostragamus.module.paytm.AddUserPaymentBankRequest;
import in.sportscafe.nostragamus.module.paytm.AddUserPaymentDetailsResponse;
import in.sportscafe.nostragamus.module.paytm.AddUserPaymentPaytmRequest;
import in.sportscafe.nostragamus.module.paytm.GenerateOrderRequest;
import in.sportscafe.nostragamus.module.paytm.GenerateOrderResponse;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsResponse;
import in.sportscafe.nostragamus.module.play.myresults.dto.ReplayPowerupResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Answer;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollRequest;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.QuestionsResponse;
import in.sportscafe.nostragamus.module.popups.banktransfer.BankTransferRequest;
import in.sportscafe.nostragamus.module.popups.banktransfer.BankTransferResponse;
import in.sportscafe.nostragamus.module.navigation.submitquestion.add.AddQuestionRequest;
import in.sportscafe.nostragamus.module.navigation.submitquestion.tourlist.TourListResponse;
import in.sportscafe.nostragamus.module.settings.app.dto.AppSettingsResponse;
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
import in.sportscafe.nostragamus.module.user.login.dto.JwtToken;
import in.sportscafe.nostragamus.module.user.login.dto.LogInRequest;
import in.sportscafe.nostragamus.module.user.login.dto.LogInResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
import in.sportscafe.nostragamus.module.user.myprofile.dto.TournamentFeedResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.TournamentsResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.nostragamus.module.user.myprofile.edit.UpdateUserRequest;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfoResponse;
import in.sportscafe.nostragamus.module.wallet.WalletTransaction;
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
import retrofit2.http.QueryMap;

/**
 * Created by Jeeva on 14/3/16.
 */
public interface NostragamusService {

    @GET("v1/game/users")
    Call<UserInfoResponse> getUserInfo();

    @GET("v2/game/users/{tournament_id}/timeline")
    Call<FeedResponse> getMatches(@Path("tournament_id") int tourId);

    @GET("v1/game/users/matches")
    Call<MatchesResponse> getMatchResults(@Query("is_attempted") Boolean isAttempted, @Query("without_commentary") Boolean WithoutCommentary);

    @GET("v1/game/users/questions")
    Call<QuestionsResponse> getQuestions(@Query("match_id") int matchId);

    @GET("v1/game/users/tournaments")
    Call<TournamentsResponse> getTournaments(@Query("is_current") boolean isCurrent, @Query("group_by_sport") boolean groupbySport);

    @GET("v1/game/users/tournaments")
    Call<TournamentFeedResponse> getCurrentTournaments(@Query("is_current") boolean isCurrent);

    @POST("/v2/game/login")
    Call<LogInResponse> loginUser(@Body LogInRequest logInRequest);

    @PUT("v1/game/users")
    Call<ApiResponse> updateUser(@Body UpdateUserRequest request);

    @Multipart
    @POST("v1/utility/addFileToS3")
    Call<Result> uploadImage(@Part MultipartBody.Part file, @Part("filePath") RequestBody filepath, @Part("fileName") RequestBody filename);

    @POST("v2/game/users/answer")
    Call<ApiResponse> saveAnswer(
            @Body Answer answer,
            @Query("is_match_complete") boolean isMatchComplete,
            @Query("is_minority_option") boolean isMinorityOption
    );

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

    @GET("v1/game/users/results")
    Call<MyResultsResponse> getMyResults(@Query("match_id") Integer matchId, @Query("player_id") Integer playerId);

    @GET("v1/game/leaderboard/detail")
    Call<LeaderBoardResponse> getLeaderBoardDetail(@Query("group_id") Integer groupId,
                                                   @Query("challenge_id") Integer challengeId, @Query("match_id") Integer matchId);

    @PUT("v2/game/groups/tournaments")
    Call<ApiResponse> updateGroupTournament(@Body GroupTournamentUpdateRequest request);

    @PUT("v2/game/groups")
    Call<ApiResponse> updateGroupName(@Body GroupNameUpdateRequest request);

    @GET("v2/game/users/groups/{group_id}/info")
    Call<GroupSummaryResponse> getGroupSummary(@Path("group_id") Integer groupId);

    @GET("v2/game/users/groups")
    Call<AllGroupsResponse> getAllGroups();

    @GET("v1/setting/getSettingsBody")
    Call<AppSettingsResponse> getAppSettings(@Query("unique_id") String uniqueId);

    @POST("v2/game/users/poll")
    Call<AudiencePollResponse> getAudiencePoll(@Body AudiencePollRequest request);

    @GET("v2/game/refreshToken")
    Call<JwtToken> refreshAccessToken();

    @GET("/v2/game/applyResultsPowerups")
    Call<ReplayPowerupResponse> getReplayPowerup(@Query("powerup_id") String powerupId,
                                                 @Query("match_id") Integer matchId);

    @GET("/v2/game/applyResultsPowerups")
    Call<ReplayPowerupResponse> getFlipPowerup(@Query("powerup_id") String powerupId, @Query("match_id") Integer matchId, @Query("question_id") Integer questionId);

    @GET("/v1/game/users/getPlayerProfile")
    Call<PlayerInfoResponse> getPlayerInfo(@Query("player_user_id") Integer playerId);

    @GET("v1/game/users/matches/timeline")
    Call<MatchesResponse> getTimelines(
            @Query("challenge_id") Integer challengeId,
            @Query("player_id") Integer playerUserId,
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    @GET("v1/game/users/getFuzzyPlayer")
    Call<FuzzyPlayerResponse> fuzzyPlayers(@Query("player_user_name") String key, @Query("match_id") Integer matchId);

    @GET("v1/game/getPlayerResult")
    Call<MyResultsResponse> getPlayerResult(@Query("player_id") Integer playerId, @Query("match_id") Integer matchId);

    @GET("v1/game/getPlayerResultPercentage")
    Call<PlayerResultPercentageResponse> playerResultPercentage(@Query("match_id") Integer matchId);

    @GET("v2/game/users/leaderboard/summary")
    Call<LBLandingResponse> getLBLandingSummary(@QueryMap Map<String, String> queries);

    @GET("v1/game/users/getFuzzySearchLeaderboards")
    Call<FuzzyLbResponse> fuzzyLbs(@Query("search_key") String key);

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

    /*@GET("/v2/game/users/getUserChallenges")
    Call<AllChallengesResponse> getAllChallenges();*/

    @GET("/v2/game/users/getHomeChallenges")
    Call<AllChallengesResponse> getAllChallenges(@Query("filter") String filter);

    @POST("v2/game/users/challenges/transferFromBank")
    Call<BankTransferResponse> bankTransfer(@Body BankTransferRequest bankTransferRequest);

    @POST("v2/game/submitQuestion")
    Call<ApiResponse> submitQuestion(@Body AddQuestionRequest addQuestionRequest);

    @GET("v2/game/getUpcomingMatches")
    Call<TourListResponse> getTourList();

    @GET("/v2/game/challenges/getConfigs")
    Call<ChallengeConfigsResponse> getConfigs(@Query("challenge_id") int challengeId, @Query("config_index") Integer configIndex,@Query("flavor") String appFlavor);

    @PUT("/v2/game/users/joinUserChallenge")
    Call<Challenge> getJoinChallenge(
            @Query("challenge_id") int challengeId,
            @Query("config_index") int configIndex
    );

    @POST("v2/game/generateOrder")
    Call<GenerateOrderResponse> generateOrder(@Body GenerateOrderRequest request);

    @PUT("v2/game/users/paymentInfo")
    Call<AddUserPaymentDetailsResponse> addUserPaymentBankDetails(@Body AddUserPaymentBankRequest request);

    @PUT("v2/game/users/paymentInfo")
    Call<AddUserPaymentDetailsResponse> addUserPaymentPaytmDetails(@Body AddUserPaymentPaytmRequest request);

    @GET("v2/game/getUserTransaction")
    Call<List<WalletTransaction>> getWalletTransactionHistory();

    @GET("v1/utility/getServerTime")
    Call<TimeResponse> getServerTime();

    @GET("/v2/game/users/getHomeChallenges")
    Call<AllChallengesResponse> getCompletedChallengesRequest( @Query("filter") String filter,
                                                               @Query("skip") int skip,
                                                               @Query("limit") int limit);

    @POST("v2/game/users/editAnswer")
    Call<ApiResponse> changeAnswer(@Body ChangeAnswer changeAnswer);

    @GET("v2/game/appUpdate")
    Call<AppUpdateResponse> getAppUpdates(@Query("app_type") String flavor);
}