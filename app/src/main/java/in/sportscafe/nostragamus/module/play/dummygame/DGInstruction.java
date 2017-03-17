package in.sportscafe.nostragamus.module.play.dummygame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.play.prediction.dto.Question;

/**
 * Created by Jeeva on 11/03/17.
 */
@Parcel
public class DGInstruction {

    @JsonProperty("name")
    private String name;

    @JsonProperty("animation")
    private DGAnimation animation;

    @JsonProperty("waitingTime")
    private Long waitingTime;

    @JsonProperty("freshStart")
    private boolean freshStart = false;

    @JsonProperty("type")
    private String type;

    @JsonProperty("textType")
    private String textType;

    @JsonProperty("actionType")
    private String actionType;

    @JsonProperty("questionType")
    private String questionType;

    @JsonProperty("question")
    private Question question;

    @JsonIgnore
    private Integer scoredPoints;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("animation")
    public DGAnimation getAnimation() {
        return animation;
    }

    @JsonProperty("animation")
    public void setAnimation(DGAnimation animation) {
        this.animation = animation;
    }

    @JsonProperty("waitingTime")
    public Long getWaitingTime() {
        return waitingTime;
    }

    @JsonProperty("waitingTime")
    public void setWaitingTime(Long waitingTime) {
        this.waitingTime = waitingTime;
    }

    @JsonProperty("freshStart")
    public boolean isFreshStart() {
        return freshStart;
    }

    @JsonProperty("freshStart")
    public void setFreshStart(boolean freshStart) {
        this.freshStart = freshStart;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("textType")
    public String getTextType() {
        return textType;
    }

    @JsonProperty("textType")
    public void setTextType(String textType) {
        this.textType = textType;
    }

    @JsonProperty("actionType")
    public String getActionType() {
        return actionType;
    }

    @JsonProperty("actionType")
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @JsonProperty("questionType")
    public String getQuestionType() {
        return questionType;
    }

    @JsonProperty("questionType")
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    @JsonProperty("question")
    public Question getQuestion() {
        return question;
    }

    @JsonProperty("question")
    public void setQuestion(Question question) {
        this.question = question;
    }

    @JsonIgnore
    public Integer getScoredPoints() {
        return scoredPoints;
    }

    @JsonIgnore
    public void setScoredPoints(Integer scoredPoints) {
        this.scoredPoints = scoredPoints;
    }
}