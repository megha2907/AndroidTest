package in.sportscafe.nostragamus.module.othersanswers;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by Jeeva on 11/01/17.
 */

public class MatchAnswerStats {

    @SerializedName("question_id")
    private Integer questionId;

    @SerializedName("answer3")
    private Integer answer3;

    @SerializedName("answer1")
    private Integer answer1;

    @SerializedName("answer2")
    private Integer answer2;

    @SerializedName("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @SerializedName("question_id")
    public Integer getQuestionId() {
        return questionId;
    }

    @SerializedName("question_id")
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    @SerializedName("answer3")
    public Integer getAnswer3() {
        return answer3;
    }

    @SerializedName("answer3")
    public void setAnswer3(Integer answer3) {
        this.answer3 = answer3;
    }

    @SerializedName("answer1")
    public Integer getAnswer1() {
        return answer1;
    }

    @SerializedName("answer1")
    public void setAnswer1(Integer answer1) {
        this.answer1 = answer1;
    }

    @SerializedName("answer2")
    public Integer getAnswer2() {
        return answer2;
    }

    @SerializedName("answer2")
    public void setAnswer2(Integer answer2) {
        this.answer2 = answer2;
    }

    @SerializedName("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @SerializedName("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

}