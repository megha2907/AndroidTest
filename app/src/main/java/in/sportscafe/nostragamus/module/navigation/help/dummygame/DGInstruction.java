package in.sportscafe.nostragamus.module.navigation.help.dummygame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.resultspeek.dto.Question;

/**
 * Created by Jeeva on 11/03/17.
 */
@Parcel
public class DGInstruction {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("animation")
    private DGAnimation animation;

    @SerializedName("waitingTime")
    private Long waitingTime;

    @SerializedName("freshStart")
    private boolean freshStart = false;

    @SerializedName("type")
    private String type;

    @SerializedName("textType")
    private String textType;

    @SerializedName("actionType")
    private String actionType;

    @SerializedName("questionType")
    private String questionType;

    @SerializedName("question")
    private Question question;

    @SerializedName("timelineState")
    private int timelineState;

    private Integer scoredPoints;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("name")
    public String getName() {
        return name;
    }

    @SerializedName("name")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("animation")
    public DGAnimation getAnimation() {
        return animation;
    }

    @SerializedName("animation")
    public void setAnimation(DGAnimation animation) {
        this.animation = animation;
    }

    @SerializedName("waitingTime")
    public Long getWaitingTime() {
        return waitingTime;
    }

    @SerializedName("waitingTime")
    public void setWaitingTime(Long waitingTime) {
        this.waitingTime = waitingTime;
    }

    @SerializedName("freshStart")
    public boolean isFreshStart() {
        return freshStart;
    }

    @SerializedName("freshStart")
    public void setFreshStart(boolean freshStart) {
        this.freshStart = freshStart;
    }

    @SerializedName("type")
    public String getType() {
        return type;
    }

    @SerializedName("type")
    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("textType")
    public String getTextType() {
        return textType;
    }

    @SerializedName("textType")
    public void setTextType(String textType) {
        this.textType = textType;
    }

    @SerializedName("actionType")
    public String getActionType() {
        return actionType;
    }

    @SerializedName("actionType")
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @SerializedName("questionType")
    public String getQuestionType() {
        return questionType;
    }

    @SerializedName("questionType")
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    @SerializedName("question")
    public Question getQuestion() {
        return question;
    }

    @SerializedName("question")
    public void setQuestion(Question question) {
        this.question = question;
    }


    public Integer getScoredPoints() {
        return scoredPoints;
    }


    public void setScoredPoints(Integer scoredPoints) {
        this.scoredPoints = scoredPoints;
    }

    public int getTimelineState() {
        return timelineState;
    }

    public void setTimelineState(int timelineState) {
        this.timelineState = timelineState;
    }
}