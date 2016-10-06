package in.sportscafe.scgame.webservice;


import in.sportscafe.scgame.module.common.ApiResponse;
import in.sportscafe.scgame.module.feed.dto.MatchesResponse;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentsResponse;
import in.sportscafe.scgame.module.play.myresults.MyResultsResponse;
import in.sportscafe.scgame.module.play.prediction.dto.Answer;
import in.sportscafe.scgame.module.play.prediction.dto.QuestionsResponse;
import in.sportscafe.scgame.module.user.group.admin.approve.ApproveRequest;
import in.sportscafe.scgame.module.user.group.groupinfo.GroupNameUpdateRequest;
import in.sportscafe.scgame.module.user.group.groupinfo.GroupSportUpdateRequest;
import in.sportscafe.scgame.module.user.group.groupinfo.GroupTournamentUpdateRequest;
import in.sportscafe.scgame.module.user.group.members.AddGroupRequest;
import in.sportscafe.scgame.module.user.group.members.AdminRequest;
import in.sportscafe.scgame.module.user.group.members.MembersRequest;
import in.sportscafe.scgame.module.user.group.newgroup.NewGroupRequest;
import in.sportscafe.scgame.module.user.group.newgroup.NewGroupResponse;
import in.sportscafe.scgame.module.user.leaderboard.LeaderBoardResponse;
import in.sportscafe.scgame.module.user.login.dto.LogInRequest;
import in.sportscafe.scgame.module.user.login.dto.LogInResponse;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupsDetailResponse;
import in.sportscafe.scgame.module.user.myprofile.dto.Result;
import in.sportscafe.scgame.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.scgame.module.user.myprofile.edit.UpdateUserRequest;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummaryResponse;
import in.sportscafe.scgame.module.user.sportselection.dto.AllSports;
import in.sportscafe.scgame.module.user.sportselection.dto.PreferenceRequest;
import in.sportscafe.scgame.module.user.sportselection.dto.UserSports;
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
public interface ScGameService {

    @GET("sports")
    Call<AllSports> getAllSports();

    @GET("users/{userId}/sports")
    Call<UserSports> getUserSports(@Path("userId") String userId);

    @GET("users/{userId}")
    Call<UserInfoResponse> getUserInfo(@Path("userId") String userId);

//    @GET("users/{userId}/matches")
//    Call<MatchesResponse> getMatches(@Path("userId") String userId,@Query("tour_id") int tournamentId);

    @GET("users/{userId}/matches")
    Call<MatchesResponse> getMatches(@Path("userId") String userId,@Query("tour_id") int tourId);

    @GET("users/{userId}/matches")
    Call<MatchesResponse> getMatchResults(@Path("userId") String userId,@Query("is_attempted") Boolean isAttempted ,@Query("without_commentary") Boolean WithoutCommentary);

    @GET("users/{userId}/questions")
    Call<QuestionsResponse> getQuestions(@Path("userId") String userId,@Query("match_id") int matchId);

    @GET("users/{user_id}/tournaments")
    Call<TournamentsResponse> getTournaments(@Path("user_id") String userId, @Query("is_current") boolean isCurrent);

    @GET("users/{userId}/groups/info")
    Call<GroupsDetailResponse> getGroupDetails(@Path("userId") String userId);

    @POST("users/login")
    Call<LogInResponse> loginUser(@Body LogInRequest logInRequest);

    @PUT("users")
    Call<ApiResponse> updateUser(@Body UpdateUserRequest request);

    @Multipart
    @POST("/v1/utility/addFileToS3")
    Call<Result> uploadImage(@Part MultipartBody.Part file, @Part("filePath") RequestBody filepath, @Part("fileName") RequestBody filename);

    @POST("users/answer")
    Call<ApiResponse> saveAnswer(@Body Answer answer);

    @PUT("users/sports/preference")
    Call<ApiResponse> savePreference(@Body PreferenceRequest request);

    @POST("groups")
    Call<NewGroupResponse> createNewGroup(@Body NewGroupRequest request);

    @HTTP(method = "DELETE", path = "groups/users", hasBody = true)
    Call<ApiResponse> removePerson(@Body AdminRequest request);

    @PUT("groups/admins")
    Call<ApiResponse> makeAdmin(@Body AdminRequest request);

    @POST("groups/users")
    Call<ApiResponse> joinGroup(@Body AddGroupRequest memberRequest);

    @PUT("groups/users")
    Call<ApiResponse> approveUser(@Body ApproveRequest request);

    @HTTP(method = "DELETE", path = "users/groups", hasBody = true)
    Call<ApiResponse> leaveGroup(@Body MembersRequest request);

    @GET("users/{userId}/results")
    Call<MyResultsResponse> getMyResults(@Path("userId") String userId,@Query("match_id") Integer matchId);

    @GET("users/{userId}/leaderboard/summary")
    Call<LbSummaryResponse> getLeaderBoardSummary(@Path("userId") String userId);

    @GET("leaderboard/detail")
    Call<LeaderBoardResponse> getLeaderBoardDetail(@Query("group_id") Long groupId,
                                                   @Query("sports_id") Integer sportId,
                                                   @Query("user_rank_period") String rankPeriod);

    @PUT("groups/sports")
    Call<ApiResponse> updateGroupSport(@Body GroupSportUpdateRequest request);

    @PUT("groups/tournaments")
    Call<ApiResponse> updateGroupTournament(@Body GroupTournamentUpdateRequest request);

    @PUT("groups")
    Call<ApiResponse> updateGroupName(@Body GroupNameUpdateRequest request);
}