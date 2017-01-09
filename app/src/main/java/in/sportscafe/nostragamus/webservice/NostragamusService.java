package in.sportscafe.nostragamus.webservice;


import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedResponse;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentsResponse;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.feed.dto.MatchesResponse;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsResponse;
import in.sportscafe.nostragamus.module.play.myresults.dto.ReplayPowerupResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Answer;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollRequest;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.QuestionsResponse;
import in.sportscafe.nostragamus.module.settings.app.dto.AppSettingsResponse;
import in.sportscafe.nostragamus.module.user.group.admin.approve.ApproveRequest;
import in.sportscafe.nostragamus.module.user.group.allgroups.dto.AllGroupsResponse;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupNameUpdateRequest;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupSportUpdateRequest;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupTournamentUpdateRequest;
import in.sportscafe.nostragamus.module.user.group.joingroup.dto.JoinGroupResponse;
import in.sportscafe.nostragamus.module.user.group.members.AddGroupRequest;
import in.sportscafe.nostragamus.module.user.group.members.AdminRequest;
import in.sportscafe.nostragamus.module.user.group.members.MembersRequest;
import in.sportscafe.nostragamus.module.user.group.newgroup.NewGroupRequest;
import in.sportscafe.nostragamus.module.user.group.newgroup.NewGroupResponse;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardResponse;
import in.sportscafe.nostragamus.module.user.login.dto.JwtToken;
import in.sportscafe.nostragamus.module.user.login.dto.LogInRequest;
import in.sportscafe.nostragamus.module.user.login.dto.LogInResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupsDetailResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
import in.sportscafe.nostragamus.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.nostragamus.module.user.myprofile.edit.UpdateUserRequest;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummaryResponse;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfoResponse;
import in.sportscafe.nostragamus.module.user.sportselection.dto.AllSports;
import in.sportscafe.nostragamus.module.user.sportselection.dto.PreferenceRequest;
import in.sportscafe.nostragamus.module.user.sportselection.dto.UserSports;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
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


    @GET("v1/game/sports")
    Call<AllSports> getAllSports();

    @GET("v1/game/users/{userId}/sports")
    Call<UserSports> getUserSports(@Path("userId") String userId);

    @GET("v1/game/users/{userId}")
    Call<UserInfoResponse> getUserInfo(@Path("userId") String userId);

    @GET("v1/game/users/{userId}/matches")
    Call<MatchesResponse> getMatches(@Path("userId") String userId,@Query("tour_id") int tourId);

    @GET("v1/game/users/{userId}/matches")
    Call<MatchesResponse> getMatchResults(@Path("userId") String userId,@Query("is_attempted") Boolean isAttempted ,@Query("without_commentary") Boolean WithoutCommentary);

    @GET("v1/game/users/{userId}/questions")
    Call<QuestionsResponse> getQuestions(@Path("userId") String userId,@Query("match_id") int matchId);

    @GET("v1/game/users/{user_id}/tournaments")
    Call<TournamentsResponse> getTournaments(@Path("user_id") String userId, @Query("is_current") boolean isCurrent, @Query("group_by_sport") boolean groupbySport);

    @GET("v1/game/users/{user_id}/tournaments")
    Call<TournamentFeedResponse> getCurrentTournaments(@Path("user_id") String userId, @Query("is_current") boolean isCurrent);

    @GET("v1/game/users/{userId}/groups/info")
    Call<GroupsDetailResponse> getGroupDetails(@Path("userId") String userId);

    @POST("/v2/game/login")
    Call<LogInResponse> loginUser(@Body LogInRequest logInRequest);

    @PUT("v1/game/users")
    Call<ApiResponse> updateUser(@Body UpdateUserRequest request);

    @Multipart
    @POST("v1/utility/addFileToS3")
    Call<Result> uploadImage(@Part MultipartBody.Part file, @Part("filePath") RequestBody filepath, @Part("fileName") RequestBody filename);

    @POST("v1/game/users/answer")
    Call<ApiResponse> saveAnswer(@Body Answer answer);

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

    @GET("v1/game/users/{userId}/results")
    Call<MyResultsResponse> getMyResults(@Path("userId") String userId,@Query("match_id") Integer matchId);

    @GET("v1/game/users/{userId}/leaderboard/summary")
    Call<LbSummaryResponse> getLeaderBoardSummary(@Path("userId") String userId);

    @GET("v1/game/leaderboard/detail")
    Call<LeaderBoardResponse> getLeaderBoardDetail(@Query("sports_id") Integer sportId,
                                                   @Query("group_id") Integer groupId,
                                                   @Query("challenge_id") Integer challengeId);

    @PUT("v1/game/groups/sports")
    Call<ApiResponse> updateGroupSport(@Body GroupSportUpdateRequest request);

    @PUT("v1/game/groups/tournaments")
    Call<ApiResponse> updateGroupTournament(@Body GroupTournamentUpdateRequest request);

    @PUT("v1/game/groups")
    Call<ApiResponse> updateGroupName(@Body GroupNameUpdateRequest request);

    @GET("v1/game/users/{user_id}/groups/{group_id}/info")
    Call<GroupSummaryResponse> getGroupSummary(@Path("user_id") String userId,@Path("group_id") Integer groupId);

    @GET("v1/game/users/{user_id}/groups")
    Call<AllGroupsResponse> getAllGroups(@Path("user_id") String userId);

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
    Call<PlayerInfoResponse> getPlayerInfo(@Query("player_user_id") String playerId);

    @GET("v1/game/users/matches/timeline")
    Call<MatchesResponse> getTimelines(
            @Query("player_user_id") String playerUserId,
            @Query("skip") int skip,
            @Query("limit") int limit
    );
}