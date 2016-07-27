package in.sportscafe.scgame.webservice;

import in.sportscafe.scgame.Config;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ApiResponse;
import in.sportscafe.scgame.module.feed.dto.MatchesResponse;
import in.sportscafe.scgame.module.play.myresults.MyResultsResponse;
import in.sportscafe.scgame.module.play.prediction.dto.Answer;
import in.sportscafe.scgame.module.play.prediction.dto.QuestionsResponse;
import in.sportscafe.scgame.module.user.group.admin.approve.ApproveRequest;
import in.sportscafe.scgame.module.user.group.groupinfo.GroupNameUpdateRequest;
import in.sportscafe.scgame.module.user.group.groupinfo.GroupSportUpdateRequest;
import in.sportscafe.scgame.module.user.group.members.AddGroupRequest;
import in.sportscafe.scgame.module.user.group.members.AdminRequest;
import in.sportscafe.scgame.module.user.group.members.MembersRequest;
import in.sportscafe.scgame.module.user.group.newgroup.NewGroupRequest;
import in.sportscafe.scgame.module.user.group.newgroup.NewGroupResponse;
import in.sportscafe.scgame.module.user.leaderboard.LeaderBoardResponse;
import in.sportscafe.scgame.module.user.login.dto.LogInRequest;
import in.sportscafe.scgame.module.user.login.dto.LogInResponse;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupsDetailResponse;
import in.sportscafe.scgame.module.user.myprofile.edit.UpdateUserRequest;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummaryResponse;
import in.sportscafe.scgame.module.user.sportselection.dto.AllSports;
import in.sportscafe.scgame.module.user.sportselection.dto.PreferenceRequest;
import in.sportscafe.scgame.module.user.sportselection.dto.UserSports;
import retrofit2.Call;

/**
 * Created by Jeeva on 15/3/16.
 */
public class MyWebService extends AbstractWebService<ScGameService> {

    private static MyWebService sMyWebService = new MyWebService();

    public static MyWebService getInstance() {
        return sMyWebService;
    }

    private ScGameService mScGameService;

    public void init() {
        // Initialize SportsCafe Api service using retrofit
        mScGameService = init(Config.API_BASE_URL, ScGameService.class);
    }

    public Call<LogInResponse> getLogInRequest(LogInRequest logInRequest) {
        return mScGameService.loginUser(logInRequest);
    }

    public Call<ApiResponse> getUpdateUserRequest(UpdateUserRequest request) {
        return mScGameService.updateUser(request);
    }

    public Call<AllSports> getAllSportsRequest() {
        return mScGameService.getAllSports();
    }

    public Call<ApiResponse> savePreferenceRequest(PreferenceRequest preferenceRequest) {
        return mScGameService.savePreference(preferenceRequest);
    }

    public Call<UserSports> getUserSportsRequest() {
        return mScGameService.getUserSports(ScGameDataHandler.getInstance().getUserId());
    }

    public Call<MatchesResponse> getMatches() {
        return mScGameService.getMatches(ScGameDataHandler.getInstance().getUserId());
    }

    public Call<QuestionsResponse> getAllQuestions() {
        return mScGameService.getQuestions(ScGameDataHandler.getInstance().getUserId());
    }

    public Call<ApiResponse> getPostAnswerRequest(Answer answer) {
        return mScGameService.saveAnswer(answer);
    }

    public Call<GroupsDetailResponse> getGrpInfoRequest() {
        return mScGameService.getGroupDetails(ScGameDataHandler.getInstance().getUserId());
    }

    public Call<ApiResponse> getJoinGroupRequest(String groupCode) {
        AddGroupRequest memberRequest = new AddGroupRequest();
        memberRequest.setGroupCode(groupCode);
        memberRequest.setUserId(ScGameDataHandler.getInstance().getUserId());
        return mScGameService.joinGroup(memberRequest);
    }

    public Call<NewGroupResponse> getNewGroupRequest(NewGroupRequest request) {
        return mScGameService.createNewGroup(request);
    }

    public Call<ApiResponse> getRemovePersonRequest(AdminRequest request) {
        return mScGameService.removePerson(request);
    }

    public Call<ApiResponse> getMakeAdminRequest(AdminRequest request) {
        return mScGameService.makeAdmin(request);
    }

    public Call<ApiResponse> getApproveMemberRequest(ApproveRequest request) {
        return mScGameService.approveUser(request);
    }

    public Call<ApiResponse> getLeaveGroupRequest(MembersRequest request) {
        return mScGameService.leaveGroup(request);
    }

    public Call<MyResultsResponse> getMyResultsRequest(Integer offset, Integer limit) {
        return mScGameService.getMyResults(ScGameDataHandler.getInstance().getUserId(),
                offset, limit);
    }

    public Call<LbSummaryResponse> getLeaderBoardSummary() {
        return mScGameService.getLeaderBoardSummary(ScGameDataHandler.getInstance().getUserId());
    }

    public Call<LeaderBoardResponse> getLeaderBoardDetailRequest(Long groupId,
                                                                 Integer sportsId,
                                                                 String rankPeriod) {
        return mScGameService.getLeaderBoardDetail(groupId, sportsId, rankPeriod);
    }

    public Call<ApiResponse> getGrpSportUpdateRequest(GroupSportUpdateRequest request) {
        return mScGameService.updateGroupSport(request);
    }

    public Call<ApiResponse> getGrpNameUpdateRequest(GroupNameUpdateRequest request) {
        return mScGameService.updateGroupName(request);
    }

}