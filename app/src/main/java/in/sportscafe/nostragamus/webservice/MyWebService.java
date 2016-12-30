package in.sportscafe.nostragamus.webservice;

import java.io.File;

import in.sportscafe.nostragamus.Config;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.TournamentFeed.dto.TournamentFeedResponse;
import in.sportscafe.nostragamus.module.TournamentFeed.dto.TournamentsResponse;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Jeeva on 15/3/16.
 */
public class MyWebService extends AbstractWebService<NostragamusService> {

    private static MyWebService sMyWebService = new MyWebService();

    public static MyWebService getInstance() {
        return sMyWebService;
    }

    private NostragamusService mNostragamusService;

    public void init() {
        // Initialize SportsCafe Api service using retrofit
        mNostragamusService = init(Config.API_BASE_URL, NostragamusService.class);
    }

    public Call<LogInResponse> getLogInRequest(LogInRequest logInRequest) {
        return mNostragamusService.loginUser(logInRequest);
    }

    public Call<ApiResponse> getUpdateUserRequest(UpdateUserRequest request) {
        return mNostragamusService.updateUser(request);
    }

    public Call<UserInfoResponse> getUserInfoRequest(String userId) {
        return mNostragamusService.getUserInfo(userId);
    }

    public Call<PlayerInfoResponse> getPlayerInfoRequest(String playerId) {
        return mNostragamusService.getPlayerInfo(playerId);
    }

    public Call<AllSports> getAllSportsRequest() {
        return mNostragamusService.getAllSports();
    }

    public Call<ApiResponse> savePreferenceRequest(PreferenceRequest preferenceRequest) {
        return mNostragamusService.savePreference(preferenceRequest);
    }

    public Call<UserSports> getUserSportsRequest() {
        return mNostragamusService.getUserSports(NostragamusDataHandler.getInstance().getUserId());
    }

    public Call<MatchesResponse> getMatches(Integer tourId) {
        return mNostragamusService.getMatches(NostragamusDataHandler.getInstance().getUserId(),tourId);
    }

    public Call<MatchesResponse> getMatchResults(Boolean isAttempted , Boolean WithoutCommentary) {
        return mNostragamusService.getMatchResults(NostragamusDataHandler.getInstance().getUserId(),isAttempted,WithoutCommentary);
    }

    public Call<QuestionsResponse> getAllQuestions(Integer matchId) {
        return mNostragamusService.getQuestions(NostragamusDataHandler.getInstance().getUserId(),matchId);
    }

    public Call<TournamentsResponse> getTournaments(boolean isCurrent, boolean groupbySport) {
        return mNostragamusService.getTournaments(NostragamusDataHandler.getInstance().getUserId(),isCurrent,groupbySport);
    }

    public Call<TournamentFeedResponse> getCurrentTournaments(boolean isCurrent) {
        return mNostragamusService.getCurrentTournaments(NostragamusDataHandler.getInstance().getUserId(),isCurrent);
    }

    public Call<ApiResponse> getPostAnswerRequest(Answer answer) {
        return mNostragamusService.saveAnswer(answer);
    }

    public Call<GroupsDetailResponse> getGrpInfoRequest() {
        return mNostragamusService.getGroupDetails(NostragamusDataHandler.getInstance().getUserId());
    }

    public Call<JoinGroupResponse> getJoinGroupRequest(String groupCode) {
        AddGroupRequest memberRequest = new AddGroupRequest();
        memberRequest.setGroupCode(groupCode);
        memberRequest.setUserId(NostragamusDataHandler.getInstance().getUserId());
        return mNostragamusService.joinGroup(memberRequest);
    }

    public Call<NewGroupResponse> getNewGroupRequest(NewGroupRequest request) {
        return mNostragamusService.createNewGroup(request);
    }

    public Call<ApiResponse> getRemovePersonRequest(AdminRequest request) {
        return mNostragamusService.removePerson(request);
    }

    public Call<ApiResponse> getMakeAdminRequest(AdminRequest request) {
        return mNostragamusService.makeAdmin(request);
    }

    public Call<ApiResponse> getApproveMemberRequest(ApproveRequest request) {
        return mNostragamusService.approveUser(request);
    }

    public Call<ApiResponse> getLeaveGroupRequest(MembersRequest request) {
        return mNostragamusService.leaveGroup(request);
    }

    public Call<MyResultsResponse> getMyResultsRequest(Integer MatchId) {
        return mNostragamusService.getMyResults(NostragamusDataHandler.getInstance().getUserId(),
                MatchId);
    }

    public Call<LbSummaryResponse> getLeaderBoardSummary() {
        return mNostragamusService.getLeaderBoardSummary(NostragamusDataHandler.getInstance().getUserId());
    }

    public Call<LeaderBoardResponse> getLeaderBoardDetailRequest(Integer sportsId,Integer groupId,
                                                                 Integer challengeId) {
        return mNostragamusService.getLeaderBoardDetail(sportsId, groupId, challengeId);
    }

    public Call<ApiResponse> getGrpSportUpdateRequest(GroupSportUpdateRequest request) {
        return mNostragamusService.updateGroupSport(request);
    }

    public Call<ApiResponse> getGrpTournamentUpdateRequest(GroupTournamentUpdateRequest request) {
        return mNostragamusService.updateGroupTournament(request);
    }

    public Call<ApiResponse> getGrpNameUpdateRequest(GroupNameUpdateRequest request) {
        return mNostragamusService.updateGroupName(request);
    }

    public Call<GroupSummaryResponse> getGroupSummaryRequest(Integer groupId) {
        return mNostragamusService.getGroupSummary(NostragamusDataHandler.getInstance().getUserId(),groupId);
    }

    public Call<AllGroupsResponse> getAllGroupsRequest() {
        return mNostragamusService.getAllGroups(NostragamusDataHandler.getInstance().getUserId());
    }

    public Call<Result> getUploadPhotoRequest(File file, String filepath, String filename) {
        return mNostragamusService.uploadImage(
                MultipartBody.Part.createFormData("file", file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file)),
                RequestBody.create(MediaType.parse("multipart/form-data"), filepath),
                RequestBody.create(MediaType.parse("multipart/form-data"), filename));
    }

    public Call<AppSettingsResponse> getAppSettingsRequest(String uniqueId) {
        return mNostragamusService.getAppSettings(uniqueId);
    }

    public Call<AudiencePollResponse> getAudiencePoll(AudiencePollRequest request) {
        return mNostragamusService.getAudiencePoll(request);
    }

    public Call<JwtToken> getRefreshTokenRequest() {
        return mNostragamusService.refreshAccessToken();
    }

    public Call<ReplayPowerupResponse> getReplayPowerup(String powerupId,Integer matchId) {
        return mNostragamusService.getReplayPowerup(powerupId,matchId);
    }

    public Call<ReplayPowerupResponse> getFlipPowerup(String powerupId, Integer matchId, Integer questionId) {
        return mNostragamusService.getFlipPowerup(powerupId,matchId,questionId);
    }
}