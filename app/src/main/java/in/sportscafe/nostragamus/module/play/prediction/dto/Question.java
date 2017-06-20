package in.sportscafe.nostragamus.module.play.prediction.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.module.othersanswers.MatchAnswerStats;

/**
 * Created by Jeeva on 27/5/16.
 */
@Parcel
public class Question {

    @JsonProperty("question_id")
    private Integer questionId;

    @JsonProperty("match_id")
    private Integer matchId;

    @JsonProperty("question_text")
    private String questionText;

    @JsonProperty("question_context")
    private String questionContext;

    @JsonProperty("question_option_1")
    private String questionOption1;

    @JsonProperty("question_image_1")
    private String questionImage1;

    @JsonProperty("question_option_2")
    private String questionOption2;

    @JsonProperty("question_image_2")
    private String questionImage2;

    @JsonProperty("question_option_3")
    private String questionOption3;

    @JsonProperty("question_live")
    private boolean questionLive;

    @JsonProperty("submitted_by_name")
    private String questionSubmittedBy;

    @JsonProperty("question_answer")
    private Integer questionAnswer;

    @JsonProperty("answer")
    private Integer answerId;

    @JsonProperty("question_value")
    private String questionValue;

    @JsonProperty("powerup_id")
    private String AnswerPowerUpId;

    @JsonProperty("user_answer_points")
    private Integer answerPoints;

    @JsonProperty("user_answer_points")
    private Integer questionPoints;

    @JsonProperty("question_points")
    private Integer questionPositivePoints;

    @JsonProperty("question_neg_points")
    private Integer questionNegativePoints;

    @JsonProperty("poll")
    private List<AudiencePoll> playerPoll;

    @JsonProperty("answer1")
    private Integer answer1percent;

    @JsonProperty("answer2")
    private Integer answer2percent;

    @JsonProperty("answer3")
    private Integer answer3percent;

    @JsonProperty("powerup_id_arr")
    private ArrayList<String> powerUpArrayList = new ArrayList<>();

    @JsonIgnore
    private int questionTime = 30;

    @JsonIgnore
    private int questionNumber;

    @JsonIgnore
    private int option1AudPollPer = -1;

    @JsonIgnore
    private int option2AudPollPer = -1;

    @JsonIgnore
    private int option3AudPollPer = -1;

    @JsonIgnore
    private int minorityAnswerId = -1;

    @JsonIgnore
    private Integer updatedPositivePoints;

    @JsonIgnore
    private Integer updatedNegativePoints;

    @JsonIgnore
    private Integer editAnswerQuestionId;

    @JsonIgnore
    private Integer total2xPowerupsUsed;

    @JsonIgnore
    private Integer totalNoNegsPowerupsUsed;

    @JsonIgnore
    private Integer totalPlayerPollPowerupsUsed;

    /**
     * @return The questionId
     */
    @JsonProperty("question_id")
    public Integer getQuestionId() {
        return questionId;
    }

    /**
     * @param questionId The question_id
     */
    @JsonProperty("question_id")
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    /**
     * @return The matchId
     */
    @JsonProperty("match_id")
    public Integer getMatchId() {
        return matchId;
    }

    /**
     * @param matchId The match_id
     */
    @JsonProperty("match_id")
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    /**
     * @return The questionText
     */
    @JsonProperty("question_text")
    public String getQuestionText() {
        return questionText;
    }

    /**
     * @param questionText The question_text
     */
    @JsonProperty("question_text")
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @JsonProperty("question_context")
    public String getQuestionContext() {
        return questionContext;
    }

    @JsonProperty("question_context")
    public void setQuestionContext(String questionContext) {
        this.questionContext = questionContext;
    }

    /**
     * @return The questionOption1
     */
    @JsonProperty("question_option_1")
    public String getQuestionOption1() {
        return questionOption1;
    }

    /**
     * @param questionOption1 The question_option_1
     */
    @JsonProperty("question_option_1")
    public void setQuestionOption1(String questionOption1) {
        this.questionOption1 = questionOption1;
    }

    /**
     * @return The questionImage1
     */
    @JsonProperty("question_image_1")
    public String getQuestionImage1() {
        return questionImage1;
    }

    /**
     * @param questionImage1 The question_image_1
     */
    @JsonProperty("question_image_1")
    public void setQuestionImage1(String questionImage1) {
        this.questionImage1 = questionImage1;
    }

    /**
     * @return The questionOption2
     */
    @JsonProperty("question_option_2")
    public String getQuestionOption2() {
        return questionOption2;
    }

    /**
     * @param questionOption2 The question_option_2
     */
    @JsonProperty("question_option_2")
    public void setQuestionOption2(String questionOption2) {
        this.questionOption2 = questionOption2;
    }

    /**
     * @return The questionImage2
     */
    @JsonProperty("question_image_2")
    public String getQuestionImage2() {
        return questionImage2;
    }

    /**
     * @param questionImage2 The question_image_2
     */
    @JsonProperty("question_image_2")
    public void setQuestionImage2(String questionImage2) {
        this.questionImage2 = questionImage2;
    }

    /**
     * @return The questionOption3
     */
    @JsonProperty("question_option_3")
    public String getQuestionOption3() {
        if (null == questionOption3) {
            return "";
        }
        return questionOption3;
    }

    /**
     * @param questionOption3 The question_option_3
     */
    @JsonProperty("question_option_3")
    public void setQuestionOption3(String questionOption3) {
        this.questionOption3 = questionOption3;
    }

    /**
     * @return The questionLive
     */
    @JsonProperty("question_live")
    public boolean getQuestionLive() {
        return questionLive;
    }

    /**
     * @param questionLive The question_live
     */
    @JsonProperty("question_live")
    public void setQuestionLive(boolean questionLive) {
        this.questionLive = questionLive;
    }

    /**
     * @return The questionAnswer
     */
    @JsonProperty("question_answer")
    public Integer getQuestionAnswer() {
        return questionAnswer;
    }

    /**
     * @param questionAnswer The question_answer
     */
    @JsonProperty("question_answer")
    public void setQuestionAnswer(Integer questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    /**
     * @return The answerId
     */
    @JsonProperty("answer")
    public Integer getAnswerId() {
        return answerId;
    }

    /**
     * @param answerId The answer
     */
    @JsonProperty("answer")
    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }


    /**
     * @return The answerPowerUpId
     */
    @JsonProperty("powerup_id")
    public String getAnswerPowerUpId() {
        return AnswerPowerUpId;
    }

    /**
     * @param AnswerPowerUpId The answerPowerUpId
     */
    @JsonProperty("powerup_id")
    public void setAnswerPowerUpId(String AnswerPowerUpId) {
        this.AnswerPowerUpId = AnswerPowerUpId;
    }

    @JsonProperty("questionTime")
    public int getQuestionTime() {
        return questionTime;
    }

    @JsonProperty("questionTime")
    public void setQuestionTime(int questionTime) {
        this.questionTime = questionTime;
    }

    @JsonProperty("questionNumber")
    public int getQuestionNumber() {
        return questionNumber;
    }

    @JsonProperty("questionNumber")
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    @JsonProperty("question_value")
    public String getQuestionValue() {
        return questionValue;
    }

    @JsonProperty("question_value")
    public void setQuestionValue(String questionValue) {
        this.questionValue = questionValue;
    }

    @JsonIgnore
    public ArrayList<String> getPowerUpArrayList() {
        /*if (powerUpArrayList == null) {
            powerUpArrayList = new ArrayList<>();
        }*/
        return powerUpArrayList;
    }

    @JsonIgnore
    public void setPowerUpArrayList(ArrayList<String> powerUpArrayList) {
        this.powerUpArrayList = powerUpArrayList;
    }

    @JsonProperty("user_answer_points")
    public Integer getAnswerPoints() {
        return answerPoints;
    }

    @JsonProperty("user_answer_points")
    public void setAnswerPoints(Integer answerPoints) {
        this.answerPoints = answerPoints;
    }

    @JsonProperty("question_points")
    public Integer getQuestionPositivePoints() {
        return questionPositivePoints;
    }

    @JsonProperty("question_points")
    public void setQuestionPositivePoints(Integer questionPositivePoints) {
        this.questionPositivePoints = questionPositivePoints;
    }

    @JsonProperty("question_neg_points")
    public Integer getQuestionNegativePoints() {
        return questionNegativePoints;
    }

    @JsonProperty("question_neg_points")
    public void setQuestionNegativePoints(Integer questionNegativePoints) {
        this.questionNegativePoints = questionNegativePoints;
    }

    @JsonIgnore
    public int getOption1AudPollPer() {
        return option1AudPollPer;
    }

    @JsonIgnore
    public void setOption1AudPollPer(int option1AudPollPer) {
        this.option1AudPollPer = option1AudPollPer;
    }

    @JsonIgnore
    public int getOption2AudPollPer() {
        return option2AudPollPer;
    }

    @JsonIgnore
    public void setOption2AudPollPer(int option2AudPollPer) {
        this.option2AudPollPer = option2AudPollPer;
    }

    @JsonIgnore
    public int getOption3AudPollPer() {
        return option3AudPollPer;
    }

    @JsonIgnore
    public void setOption3AudPollPer(int option3AudPollPer) {
        this.option3AudPollPer = option3AudPollPer;
    }

    @JsonIgnore
    public int getMinorityAnswerId() {
        return minorityAnswerId;
    }

    @JsonIgnore
    public void setMinorityAnswerId(int minorityAnswerId) {
        this.minorityAnswerId = minorityAnswerId;
    }

    @JsonIgnore
    public void updatePollPercentage(MatchAnswerStats matchAnswerStats) {
        if (null != matchAnswerStats) {
            option1AudPollPer = matchAnswerStats.getAnswer1();
            option2AudPollPer = matchAnswerStats.getAnswer2();
            option3AudPollPer = matchAnswerStats.getAnswer3();
        }
    }

    @JsonIgnore
    public void updateTotalPowerUps(MatchAnswerStats matchAnswerStats) {
        if (null != matchAnswerStats) {
            HashMap<String, Integer> powerUpMap = matchAnswerStats.getPowerUps();
            total2xPowerupsUsed = powerUpMap.get(Constants.Powerups.XX);
            totalNoNegsPowerupsUsed = powerUpMap.get(Constants.Powerups.NO_NEGATIVE);
            totalPlayerPollPowerupsUsed = powerUpMap.get(Constants.Powerups.AUDIENCE_POLL);
        }
    }

    @JsonIgnore
    public boolean isMinorityAnswer() {
        return -1 != minorityAnswerId && answerId == minorityAnswerId;
    }

    @JsonIgnore
    public Integer getUpdatedPositivePoints() {
        if (null == updatedPositivePoints) {
            return getQuestionPositivePoints();
        }
        return updatedPositivePoints;
    }

    @JsonIgnore
    public void setUpdatedPositivePoints(Integer updatedPositivePoints) {
        this.updatedPositivePoints = updatedPositivePoints;
    }

    @JsonIgnore
    public Integer getUpdatedNegativePoints() {
        if (null == updatedNegativePoints) {
            return getQuestionNegativePoints();
        }
        return updatedNegativePoints;
    }

    @JsonIgnore
    public void setUpdatedNegativePoints(Integer updatedNegativePoints) {
        this.updatedNegativePoints = updatedNegativePoints;
    }

    @JsonIgnore
    public boolean apply2xPowerUp() {
        ArrayList<String> powerups = getPowerUpArrayList();
        if (powerups != null) {
            for (String str : powerups) {
                if (str.equalsIgnoreCase(Powerups.XX)) {
                    return false;  // powerup already applied once
                }
            }
        } else {
            powerups = new ArrayList<>();
        }

        // If not applied, apply it
        powerups.add(Powerups.XX);
        setPowerUpArrayList(powerups);
        resetPowerupPoints();
        return true;
    }

    @JsonIgnore
    public boolean applyNonegsPowerUp() {
        ArrayList<String> powerups = getPowerUpArrayList();
        if (powerups != null) {
            for (String str : powerups) {
                if (str.equalsIgnoreCase(Powerups.NO_NEGATIVE)) {
                    return false;  // powerup already applied once
                }
            }
        } else {
            powerups = new ArrayList<>();
        }

        powerups.add(Powerups.NO_NEGATIVE);
        setPowerUpArrayList(powerups);
        resetPowerupPoints();
        return true;
    }

    @JsonIgnore
    public boolean applyAudiencePollPowerUp(int leftAnswerPercent, int rightAnswerPercent) {
//        setPowerUpArrayList(Powerups.AUDIENCE_POLL);
        ArrayList<String> powerups = getPowerUpArrayList();
        if (powerups != null) {
            for (String str : powerups) {
                if (str.equalsIgnoreCase(Powerups.AUDIENCE_POLL)) {
                    return false;  // powerup already applied once
                }
            }
        } else {
            powerups = new ArrayList<>();
        }

        powerups.add(Powerups.AUDIENCE_POLL);
        setPowerUpArrayList(powerups);
        setOption1AudPollPer(leftAnswerPercent);
        setOption2AudPollPer(rightAnswerPercent);

        if (leftAnswerPercent > rightAnswerPercent) {
            setMinorityAnswerId(Constants.AnswerIds.RIGHT);
        } else if (leftAnswerPercent < rightAnswerPercent) {
            setMinorityAnswerId(Constants.AnswerIds.LEFT);
        } else {
            setMinorityAnswerId(-1);
        }

        return true;
    }

    @JsonIgnore
    public void removeAppliedPowerUp(String powerupToRemove) {
        // Once Audiance poll is applied, can not be removed
        if (powerupToRemove.equals(Powerups.AUDIENCE_POLL)) {
            return;
        }

        ArrayList<String> appliedPowerUp = getPowerUpArrayList();
        if (appliedPowerUp != null) {
            // First remove item from applied powerups
            for (int temp = 0; temp < appliedPowerUp.size(); temp++) {
                if (appliedPowerUp.get(temp).equalsIgnoreCase(powerupToRemove)) {
                    appliedPowerUp.remove(temp);
                }
            }
        }

        // Reset points based on remaining applied powerups
        resetPowerupPoints();
    }

    /**
     * This method is used to update points on UI when powerups are either added or removed
     */
    private void resetPowerupPoints() {
        ArrayList<String> appliedPowerUp = getPowerUpArrayList();
        if (appliedPowerUp == null || appliedPowerUp.isEmpty()) {
            setUpdatedPositivePoints(getQuestionPositivePoints());
            setUpdatedNegativePoints(getQuestionNegativePoints());

        } else {
            /* Only one powerup available */
            if (powerUpArrayList.size() == 1) {
                String powerup = powerUpArrayList.get(0);
                switch (powerup) {
                    case Powerups.XX:
                        setUpdatedPositivePoints(2 * getQuestionPositivePoints());
                        setUpdatedNegativePoints(2 * getQuestionNegativePoints());
                        break;

                    case Powerups.NO_NEGATIVE:
                        setUpdatedPositivePoints(getQuestionPositivePoints());
                        setUpdatedNegativePoints(0);
                        break;

                    case Powerups.AUDIENCE_POLL:
                        // No operation for points
                        setUpdatedPositivePoints(getQuestionPositivePoints());
                        setUpdatedNegativePoints(getQuestionNegativePoints());
                        break;
                }

            } else if (powerUpArrayList.size() == 2) {              /* Any 2 powerups */
                String powerup1 = powerUpArrayList.get(0);
                String powerup2 = powerUpArrayList.get(1);

                boolean is2x = false;
                boolean noNegative = false;

                if (powerup1.equals(Powerups.XX) || powerup2.equals(Powerups.XX)) {
                    is2x = true;
                }
                if (powerup1.equals(Powerups.NO_NEGATIVE) || powerup2.equals(Powerups.NO_NEGATIVE)) {
                    noNegative = true;
                }

                if (is2x) {
                    setUpdatedPositivePoints(2 * getQuestionPositivePoints());
                    setUpdatedNegativePoints(2 * getQuestionNegativePoints());
                }
                if (noNegative) {
                    setUpdatedPositivePoints(getQuestionPositivePoints());
                    setUpdatedNegativePoints(0);
                }
                if (is2x && noNegative) {
                    setUpdatedPositivePoints(2 * getQuestionPositivePoints());
                    setUpdatedNegativePoints(0);
                }

            } else if (powerUpArrayList.size() == 3) {      /* All the 3 powerups */
                String powerup1 = powerUpArrayList.get(0);
                String powerup2 = powerUpArrayList.get(1);
                String powerup3 = powerUpArrayList.get(2);

                boolean is2x = false;
                boolean noNegative = false;

                if (powerup1.equals(Powerups.XX) || powerup2.equals(Powerups.XX) || powerup3.equals(Powerups.XX)) {
                    is2x = true;
                }
                if (powerup1.equals(Powerups.NO_NEGATIVE) || powerup2.equals(Powerups.NO_NEGATIVE) || powerup3.equals(Powerups.NO_NEGATIVE)) {
                    noNegative = true;
                }

                if (is2x) {
                    setUpdatedPositivePoints(2 * getQuestionPositivePoints());
                    setUpdatedNegativePoints(2 * getQuestionNegativePoints());
                }
                if (noNegative) {
                    setUpdatedPositivePoints(getQuestionPositivePoints());
                    setUpdatedNegativePoints(0);
                }
                if (is2x && noNegative) {
                    setUpdatedPositivePoints(2 * getQuestionPositivePoints());
                    setUpdatedNegativePoints(0);
                }
            }
        }
    }

    @JsonIgnore
    public void removePollPowerUp() {
        removeAppliedPowerUp(Powerups.AUDIENCE_POLL);
        setOption1AudPollPer(-1);
        setOption2AudPollPer(-1);
        setMinorityAnswerId(-1);
    }

    @JsonProperty("poll")
    public List<AudiencePoll> getAudiencePoll() {
        return playerPoll;
    }

    @JsonProperty("poll")
    public void setAudiencePoll(List<AudiencePoll> playerPoll) {
        this.playerPoll = playerPoll;
    }


    public String getQuestionSubmittedBy() {
        return questionSubmittedBy;
    }

    public void setQuestionSubmittedBy(String questionSubmittedBy) {
        this.questionSubmittedBy = questionSubmittedBy;
    }

    public Integer getAnswer1percent() {
        return answer1percent;
    }

    public void setAnswer1percent(Integer answer1percent) {
        this.answer1percent = answer1percent;
    }

    public Integer getAnswer2percent() {
        return answer2percent;
    }

    public void setAnswer2percent(Integer answer2percent) {
        this.answer2percent = answer2percent;
    }

    public Integer getAnswer3percent() {
        return answer3percent;
    }

    public void setAnswer3percent(Integer answer3percent) {
        this.answer3percent = answer3percent;
    }

    @JsonIgnore
    public Integer getEditAnswerQuestionId() {
        return editAnswerQuestionId;
    }

    @JsonIgnore
    public void setEditAnswerQuestionId(Integer editAnswerQuestionId) {
        this.editAnswerQuestionId = editAnswerQuestionId;
    }

    @JsonIgnore
    public Integer getTotal2xPowerupsUsed() {
        return total2xPowerupsUsed;
    }

    @JsonIgnore
    public void setTotal2xPowerupsUsed(Integer total2xPowerupsUsed) {
        this.total2xPowerupsUsed = total2xPowerupsUsed;
    }

    @JsonIgnore
    public Integer getTotalNoNegsPowerupsUsed() {
        return totalNoNegsPowerupsUsed;
    }

    @JsonIgnore
    public void setTotalNoNegsPowerupsUsed(Integer totalNoNegsPowerupsUsed) {
        this.totalNoNegsPowerupsUsed = totalNoNegsPowerupsUsed;
    }

    @JsonIgnore
    public Integer getTotalPlayerPollPowerupsUsed() {
        return totalPlayerPollPowerupsUsed;
    }

    @JsonIgnore
    public void setTotalPlayerPollPowerupsUsed(Integer totalPlayerPollPowerupsUsed) {
        this.totalPlayerPollPowerupsUsed = totalPlayerPollPowerupsUsed;
    }

}