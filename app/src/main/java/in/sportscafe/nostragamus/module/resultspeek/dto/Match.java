package in.sportscafe.nostragamus.module.resultspeek.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.module.othersanswers.MatchAnswerStats;

/**
 * Created by Jeeva on 10/6/16.
 */

@Parcel
public class Match {

    @SerializedName("match_id")
    private int id;

    @SerializedName("tournament_id")
    private Integer tournamentId;

    @SerializedName("tournament_name")
    private String tournamentName;

    @SerializedName("tournament_img_url")
    private String tournamentPhoto;

    @SerializedName("match_stage")
    private String stage;

    @SerializedName("match_venue")
    private String venue;

    @SerializedName("match_parties")
    private List<Parties> parties = new ArrayList<>();

    @SerializedName("match_type")
    private String matchType;

    @SerializedName("match_topic")
    private Topics topics;

    @SerializedName("match_starttime")
    private String startTime;

    @SerializedName("match_endtime")
    private String endTime;

    @SerializedName("match_result")
    private String result;

    @SerializedName("match_result_desc")
    private String resultdesc;

    @SerializedName("count_questions")
    private Integer matchQuestionCount = 0;

    @SerializedName("match_points")
    private Integer matchPoints;

    @SerializedName("count_correct")
    private Integer correctCount = 0;

    @SerializedName("is_attempted")
    private Integer isAttempted;

    @SerializedName("match_winner_id")
    private Integer winnerPartyId;

    @SerializedName("sports_id")
    private Integer sportId;

    @SerializedName("sports_name")
    private String sportName;

    @SerializedName("is_result_published")
    private boolean resultPublished;

    @SerializedName("questions")
    private List<Question> questions = new ArrayList<>();

    @SerializedName("challenge_id")
    private int challengeId=0;

    @SerializedName("challenge_name")
    private String challengeName;

    @SerializedName("contest_name")
    private String contestName;

    @SerializedName("config_name")
    private String configName;

    @SerializedName("challenge_img_url")
    private String challengeImgUrl;

    @SerializedName("avg_match_points")
    private Double avgMatchPoints;

    @SerializedName("highest_match_points")
    private Integer highestMatchPoints;

    @SerializedName("rank")
    private Integer userRank;

    @SerializedName("rank_change")
    private Integer rankChange;

    @SerializedName("count_powerups")
    private Integer countPowerUps;

    @SerializedName("count_match_powerups")
    private Integer countMatchPowerupsUsed;

    @SerializedName("count_room_players")
    private Integer countPlayers;

    @SerializedName("count_match_players")
    private Integer countMatchPlayers;

    @SerializedName("highest_player_id")
    private Integer highestScorerId;

    @SerializedName("highest_player_name")
    private String highestScorerName;

    @SerializedName("highest_player_photo")
    private String highestScorerPhoto;

    @SerializedName("highest_player_room_id")
    private int highestPlayerRoomId;

    private boolean isOnePartyMatch;

    @SerializedName("count_answers")
    private Integer noOfQuestionsAnswered = 0;

    @SerializedName("room_id")
    private int roomId;

    @SerializedName("contest_id")
    private int contestId;

    @SerializedName("show_report")
    private boolean showReportButton = true;


    /**
     *
     * @return
     * The id
     */
    @SerializedName("match_id")
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The match_id
     */
    @SerializedName("match_id")
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The tournamentId
     */
    @SerializedName("tournament_id")
    public Integer getTournamentId() {
        return tournamentId;
    }

    /**
     *
     * @param tournamentId
     * The tournament_id
     */
    @SerializedName("tournament_id")
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    @SerializedName("tournament_name")
    public String getTournamentName() {
        return tournamentName;
    }

    @SerializedName("tournament_name")
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    /**
     *
     * @return
     * The stage
     */
    @SerializedName("match_stage")
    public String getStage() {
        return stage;
    }

    /**
     *
     * @param stage
     * The match_stage
     */
    @SerializedName("match_stage")
    public void setStage(String stage) {
        this.stage = stage;
    }

    /**
     *
     * @return
     * The venue
     */
    @SerializedName("match_venue")
    public String getVenue() {
        return venue;
    }

    /**
     *
     * @param venue
     * The match_venue
     */
    @SerializedName("match_venue")
    public void setVenue(String venue) {
        this.venue = venue;
    }

    @SerializedName("match_parties")
    public List<Parties> getParties() {
        if (parties==null || parties.isEmpty()){
            parties = null;
        }
        return parties;
    }

    @SerializedName("match_parties")
    public void setParties(List<Parties> parties) {
        this.parties = parties;
    }

    /**
     *
     * @return
     * The startTime
     */
    @SerializedName("match_starttime")
    public String getStartTime() {
        return startTime;
    }

    /**
     *
     * @param startTime
     * The match_starttime
     */
    @SerializedName("match_starttime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     *
     * @return
     * The endTime
     */
    @SerializedName("match_endtime")
    public String getEndTime() {
        return endTime;
    }

    /**
     *
     * @param endTime
     * The match_endtime
     */
    @SerializedName("match_endtime")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     *
     * @return
     * The result
     */
    @SerializedName("match_result")
    public String getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The match_result
     */
    @SerializedName("match_result")
    public void setResult(String result) {
        this.result = result;
    }

    /**
     *
     * @return
     * The questionsLive
     */
    @SerializedName("is_attempted")
    public Integer getisAttempted() {
        return isAttempted;
    }

    /**
     *
     * @param isAttempted
     * The match_questions_live
     */
    @SerializedName("is_attempted")
    public void setisAttemoted(Integer isAttempted) {
        this.isAttempted = isAttempted;
    }

    @SerializedName("questions")
    public List<Question> getQuestions() {
        return questions;
    }

    @SerializedName("questions")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @SerializedName("count_questions")
    public Integer getMatchQuestionCount() {
        return matchQuestionCount;
    }

    @SerializedName("count_questions")
    public void setMatchQuestionCount(int matchQuestionCount) {
        this.matchQuestionCount = matchQuestionCount;
    }

    @SerializedName("match_points")
    public Integer getMatchPoints() {
        if(null == matchPoints) {
            return 0;
        }
        return matchPoints;
    }

    @SerializedName("match_points")
    public void setMatchPoints(Integer matchPoints) {
        this.matchPoints = matchPoints;
    }

    @SerializedName("count_correct")
    public Integer getCorrectCount() {
        if(null == correctCount) {
            return 0;
        }
        return correctCount;
    }

    @SerializedName("count_correct")
    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    @SerializedName("tournament_img_url")
    public String getTournamentPhoto() {
        return tournamentPhoto;
    }

    @SerializedName("tournament_img_url")
    public void setTournamentPhoto(String tournamentPhoto) {
        this.tournamentPhoto = tournamentPhoto;
    }

    @SerializedName("match_winner_id")
    public Integer getWinnerPartyId() {
        return winnerPartyId;
    }

    @SerializedName("match_winner_id")
    public void setWinnerPartyId(Integer winnerPartyId) {
        this.winnerPartyId = winnerPartyId;
    }

    @SerializedName("match_result_desc")
    public String getResultdesc() {
        return resultdesc;
    }

    @SerializedName("match_result_desc")
    public void setResultdesc(String resultdesc) {
        this.resultdesc = resultdesc;
    }


    public void updateAnswerPercentage(Map<Integer, MatchAnswerStats> questionAnswersMap) {
        for (Question question : questions) {
            question.updatePollPercentage(questionAnswersMap.get(question.getQuestionId()));
            question.updateTotalPowerUps(questionAnswersMap.get(question.getQuestionId()));
        }
    }

    @SerializedName("sports_id")
    public Integer getSportId() {
        return sportId;
    }

    @SerializedName("sports_id")
    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    @SerializedName("sports_name")
    public String getSportName() {
        return sportName;
    }

    @SerializedName("sports_name")
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    @SerializedName("is_result_published")
    public boolean isResultPublished() {
        return resultPublished;
    }

    @SerializedName("is_result_published")
    public void setResultPublished(boolean resultPublished) {
        this.resultPublished = resultPublished;
    }

    @SerializedName("challenge_id")
    public int getChallengeId() {
        return challengeId;
    }

    @SerializedName("challenge_id")
    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    @SerializedName("challenge_name")
    public String getChallengeName() {
        return challengeName;
    }

    @SerializedName("challenge_name")
    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    @SerializedName("challenge_img_url")
    public String getChallengeImgUrl() {
        return challengeImgUrl;
    }

    @SerializedName("challenge_img_url")
    public void setChallengeImgUrl(String challengeImgUrl) {
        this.challengeImgUrl = challengeImgUrl;
    }

    @SerializedName("avg_match_points")
    public Double getAvgMatchPoints() {
        return avgMatchPoints;
    }

    @SerializedName("avg_match_points")
    public void setAvgMatchPoints(Double avgMatchPoints) {
        this.avgMatchPoints = avgMatchPoints;
    }

    @SerializedName("highest_match_points")
    public Integer getHighestMatchPoints() {
        return highestMatchPoints;
    }

    @SerializedName("highest_match_points")
    public void setHighestMatchPoints(Integer highestMatchPoints) {
        this.highestMatchPoints = highestMatchPoints;
    }

    @SerializedName("rank")
    public Integer getUserRank() {
        return userRank;
    }

    @SerializedName("rank")
    public void setUserRank(Integer userRank) {
        this.userRank = userRank;
    }

    @SerializedName("count_powerups")
    public Integer getCountPowerUps() {
        return countPowerUps;
    }

    @SerializedName("count_powerups")
    public void setCountPowerUps(Integer countPowerUps) {
        this.countPowerUps = countPowerUps;
    }

    @SerializedName("count_room_players")
    public Integer getCountPlayers() {
        return countPlayers;
    }

    @SerializedName("count_room_players")
    public void setCountPlayers(Integer countPlayers) {
        this.countPlayers = countPlayers;
    }

    @SerializedName("count_match_powerups")
    public Integer getCountMatchPowerupsUsed() {
        return countMatchPowerupsUsed;
    }

    @SerializedName("count_match_powerups")
    public void setCountMatchPowerupsUsed(Integer countMatchPowerupsUsed) {
        this.countMatchPowerupsUsed = countMatchPowerupsUsed;
    }

    @SerializedName("count_match_players")
    public Integer getCountMatchPlayers() {
        return countMatchPlayers;
    }

    @SerializedName("count_match_players")
    public void setCountMatchPlayers(Integer countMatchPlayers) {
        this.countMatchPlayers = countMatchPlayers;
    }

    @SerializedName("highest_player_id")
    public Integer getHighestScorerId() {
        return highestScorerId;
    }

    @SerializedName("highest_player_id")
    public void setHighestScorerId(Integer highestScorerId) {
        this.highestScorerId = highestScorerId;
    }

    @SerializedName("highest_player_name")
    public String getHighestScorerName() {
        return highestScorerName;
    }

    @SerializedName("highest_player_name")
    public void setHighestScorerName(String highestScorerName) {
        this.highestScorerName = highestScorerName;
    }

    @SerializedName("highest_player_photo")
    public String getHighestScorerPhoto() {
        return highestScorerPhoto;
    }

    @SerializedName("highest_player_photo")
    public void setHighestScorerPhoto(String highestScorerPhoto) {
        this.highestScorerPhoto = highestScorerPhoto;
    }

    @SerializedName("rank_change")
    public Integer getRankChange() {
        return rankChange;
    }

    @SerializedName("rank_change")
    public void setRankChange(Integer rankChange) {
        this.rankChange = rankChange;
    }


    public boolean isOnePartyMatch() {
        return isOnePartyMatch;
    }

    public void setOnePartyMatch(boolean onePartyMatch) {
        isOnePartyMatch = onePartyMatch;
    }

    @SerializedName("match_type")
    public String getMatchType() {
        return matchType;
    }
    @SerializedName("match_type")
    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    @SerializedName("match_topic")
    public Topics getTopics() {
        if(null == topics) {
            topics = null;
        }
        return topics;
    }

    @SerializedName("match_topic")
    public void setTopics(Topics topics) {
        this.topics = topics;
    }


    @SerializedName("count_answers")
    public Integer getNoOfQuestionsAnswered() {
        return noOfQuestionsAnswered;
    }

    @SerializedName("count_answers")
    public void setNoOfQuestionsAnswered(Integer noOfQuestionsAnswered) {
        this.noOfQuestionsAnswered = noOfQuestionsAnswered;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public int getHighestPlayerRoomId() {
        return highestPlayerRoomId;
    }

    public void setHighestPlayerRoomId(int highestPlayerRoomId) {
        this.highestPlayerRoomId = highestPlayerRoomId;
    }

    public boolean isShowReportButton() {
        return showReportButton;
    }

    public void setShowReportButton(boolean showReportButton) {
        this.showReportButton = showReportButton;
    }
}