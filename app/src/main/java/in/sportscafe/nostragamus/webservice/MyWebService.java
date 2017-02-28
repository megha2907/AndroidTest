package in.sportscafe.nostragamus.webservice;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import in.sportscafe.nostragamus.Config;
import in.sportscafe.nostragamus.module.allchallenges.dto.AllChallengesResponse;
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
        return mNostragamusService.getUserInfo();
    }

    public Call<PlayerInfoResponse> getPlayerInfoRequest(Integer playerId) {
        return mNostragamusService.getPlayerInfo(playerId);
    }

    public Call<AllSports> getAllSportsRequest() {
        return mNostragamusService.getAllSports();
    }

    public Call<ApiResponse> savePreferenceRequest(PreferenceRequest preferenceRequest) {
        return mNostragamusService.savePreference(preferenceRequest);
    }

    public Call<FeedResponse> getMatches(Integer tourId) {
        return mNostragamusService.getMatches(tourId);
    }

    public Call<MatchesResponse> getMatchResults(Boolean isAttempted, Boolean WithoutCommentary) {
        return mNostragamusService.getMatchResults(isAttempted, WithoutCommentary);
    }

    public Call<QuestionsResponse> getAllQuestions(Integer matchId) {
        return mNostragamusService.getQuestions(matchId);
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

    public Call<MyResultsResponse> getMyResultsRequest(Integer MatchId, Integer playerId) {
        return mNostragamusService.getMyResults(MatchId, playerId);
    }

    public Call<LeaderBoardResponse> getLeaderBoardDetailRequest(Integer groupId,
                                                                 Integer challengeId) {
        return mNostragamusService.getLeaderBoardDetail(groupId, challengeId);
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

    public Call<AppSettingsResponse> getAppSettingsRequest(String uniqueId) {
        return mNostragamusService.getAppSettings(uniqueId);
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

    public Call<MatchesResponse> getTimelinesRequest(Integer playerUserId, int skip, int limit) {
        return mNostragamusService.getTimelines(playerUserId, skip, limit);
    }

    public Call<FuzzyPlayerResponse> getFuzzyPlayersRequest(String key, Integer matchId) {
        return mNostragamusService.fuzzyPlayers(key, matchId);
    }

    public Call<MyResultsResponse> getPlayerResultRequest(Integer playerId, Integer matchId) {
        return mNostragamusService.getPlayerResult(playerId, matchId);
    }

    public Call<PlayerResultPercentageResponse> getPlayerResultPercentageRequest(Integer matchId) {
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

    public Call<ApiResponse> getDeleteGroupRequest(AdminRequest request) {
        return mNostragamusService.deleteGroup(request);
    }

    public Call<CompareLeaderBoardResponse> getLeaderBoardComparisonRequest(Integer playerId) {
        return mNostragamusService.getLeaderBoardComparisonRequest(playerId);
    }

    public Call<AllChallengesResponse> getAllChallengesRequest() {
        return mNostragamusService.getAllChallenges();
    }
}