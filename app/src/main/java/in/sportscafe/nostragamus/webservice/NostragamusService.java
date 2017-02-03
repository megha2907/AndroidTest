package in.sportscafe.nostragamus.webservice;


import java.util.Map;

import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.feed.dto.FeedResponse;
import in.sportscafe.nostragamus.module.feed.dto.MatchesResponse;
import in.sportscafe.nostragamus.module.fuzzylbs.FuzzyLbResponse;
import in.sportscafe.nostragamus.module.fuzzyplayers.FuzzyPlayerResponse;
import in.sportscafe.nostragamus.module.othersanswers.PlayerResultPercentageResponse;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsResponse;
import in.sportscafe.nostragamus.module.play.myresults.dto.ReplayPowerupResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Answer;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollRequest;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.QuestionsResponse;
import in.sportscafe.nostragamus.module.settings.app.dto.AppSettingsResponse;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedResponse;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentsResponse;
import in.sportscafe.nostragamus.module.user.group.admin.approve.ApproveRequest;
import in.sportscafe.nostragamus.module.user.group.allgroups.dto.AllGroupsResponse;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupNameUpdateRequest;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupTournamentUpdateRequest;
import in.sportscafe.nostragamus.module.user.group.joingroup.dto.JoinGroupResponse;
import in.sportscafe.nostragamus.module.user.group.members.AddGroupRequest;
import in.sportscafe.nostragamus.module.user.group.members.AdminRequest;
import in.sportscafe.nostragamus.module.user.group.members.MembersRequest;
import in.sportscafe.nostragamus.module.user.group.newgroup.NewGroupRequest;
import in.sportscafe.nostragamus.module.user.group.newgroup.NewGroupResponse;
import in.sportscafe.nostragamus.module.user.lblanding.LBLandingResponse;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardResponse;
import in.sportscafe.nostragamus.module.user.login.dto.JwtToken;
import in.sportscafe.nostragamus.module.user.login.dto.LogInRequest;
import in.sportscafe.nostragamus.module.user.login.dto.LogInResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
import in.sportscafe.nostragamus.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.nostragamus.module.user.myprofile.edit.UpdateUserRequest;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfoResponse;
import in.sportscafe.nostragamus.module.user.sportselection.dto.AllSports;
import in.sportscafe.nostragamus.module.user.sportselection.dto.PreferenceRequest;
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

    @GET("v1/game/sports")
    Call<AllSports> getAllSports();

    @GET("v1/game/users")
    Call<UserInfoResponse> getUserInfo();

    @GET("v2/game/users/{tournament_id}/timeline")
    Call<FeedResponse> getMatches(@Path("tournament_id") int tourId);

    @GET("v1/game/users/matches")
    Call<MatchesResponse> getMatchResults(@Query("is_attempted") Boolean isAttempted ,@Query("without_commentary") Boolean WithoutCommentary);

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
    Call<ApiResponse> saveAnswer(@Body Answer answer,@Query("is_match_complete") Boolean isMatchComplete,@Query("is_minority_option") Boolean isMinorityOption);

    @PUT("v1/game/users/sports/preference")
    Call<ApiResponse> savePreference(@Body PreferenceRequest request);

    @POST("v1/game/groups")
    Call<NewGroupResponse> createNewGroup(@Body NewGroupRequest request);

    @HTTP(method = "DELETE", path = "v1/game/groups/users", hasBody = true)
    Call<ApiResponse> removePerson(@Body AdminRequest request);

    @PUT("v1/game/groups/admins")
    Call<ApiResponse> makeAdmin(@Body AdminRequest request);

    @POST("v1/game/groups/users")
    Call<JoinGroupResponse> joinGroup(@Body AddGroupRequest memberRequest);

    @PUT("v1/game/groups/users")
    Call<ApiResponse> approveUser(@Body ApproveRequest request);

    @HTTP(method = "DELETE", path = "v1/game/users/groups", hasBody = true)
    Call<ApiResponse> leaveGroup(@Body MembersRequest request);

    @GET("v1/game/users/results")
    Call<MyResultsResponse> getMyResults(@Query("match_id") Integer matchId, @Query("player_id") Integer playerId);

    @GET("v1/game/leaderboard/detail")
    Call<LeaderBoardResponse> getLeaderBoardDetail(@Query("sports_id") Integer sportId,
                                                   @Query("group_id") Integer groupId,
                                                   @Query("challenge_id") Integer challengeId);

    @PUT("v1/game/groups/tournaments")
    Call<ApiResponse> updateGroupTournament(@Body GroupTournamentUpdateRequest request);

    @PUT("v1/game/groups")
    Call<ApiResponse> updateGroupName(@Body GroupNameUpdateRequest request);

    @GET("v1/game/users/groups/{group_id}/info")
    Call<GroupSummaryResponse> getGroupSummary(@Path("group_id") Integer groupId);

    @GET("v1/game/users/groups")
    Call<AllGroupsResponse> getAllGroups();

    @GET("v1/setting/getSettingsBody")
    Call<AppSettingsResponse> getAppSettings(@Query("unique_id") String uniqueId);

    @POST("v1/game/users/poll")
    Call<AudiencePollResponse> getAudiencePoll(@Body AudiencePollRequest request);

    @GET("v2/game/refreshToken")
    Call<JwtToken> refreshAccessToken();

    @GET("/v2/game/applyResultsPowerups")
    Call<ReplayPowerupResponse> getReplayPowerup(@Query("powerup_id") String powerupId,
                                                 @Query("match_id") Integer matchId);

    @GET("/v2/game/applyResultsPowerups")
    Call<ReplayPowerupResponse> getFlipPowerup(@Query("powerup_id") String powerupId,@Query("match_id") Integer matchId,@Query("question_id") Integer questionId);

    @GET ("/v1/game/users/getPlayerProfile")
    Call<PlayerInfoResponse> getPlayerInfo(@Query("player_user_id") Integer playerId);

    @GET("v1/game/users/matches/timeline")
    Call<MatchesResponse> getTimelines(
            @Query("player_id") Integer playerUserId,
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    @GET("v1/game/users/getFuzzyPlayer")
    Call<FuzzyPlayerResponse> fuzzyPlayers(@Query("player_user_name") String key,@Query("match_id") Integer matchId);

    @GET("v1/game/getPlayerResult")
    Call<MyResultsResponse> getPlayerResult(@Query("player_id") Integer playerId, @Query("match_id") Integer matchId);

    @GET("v1/game/getPlayerResultPercentage")
    Call<PlayerResultPercentageResponse> playerResultPercentage(@Query("match_id") Integer matchId);

    @GET("v2/game/users/leaderboard/summary")
    Call<LBLandingResponse> getLBLandingSummary(@QueryMap Map<String, String> queries);

    @GET("v1/game/users/getFuzzySearchLeaderboards")
    Call<FuzzyLbResponse> fuzzyLbs(@Query("search_key") String key);

    @GET("/v1/game/users/getPendingUserPopups")
    Call<PopUpResponse> getPopUps(@Query ("screen_name") String screenName);

    @DELETE("v1/game/users/resetGroupLeaderboard")
    Call<ApiResponse> resetLeaderboard(@Query("group_id") Integer groupId);

    @PUT ("/v1/game/users/acknowledgeUserPopup")
    Call<ApiResponse> getAcknowledgePopupRequest(@Query("popup_name") String popUpName);
}