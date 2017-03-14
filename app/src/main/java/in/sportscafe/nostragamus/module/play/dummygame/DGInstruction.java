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

    @JsonProperty("alignment")
    private String alignment;

    @JsonProperty("action1")
    private String action1Text;

    @JsonProperty("action2")
    private String action2Text;

    @JsonProperty("waitingTime")
    private Long waitingTime;

    @JsonProperty("freshStart")
    private boolean freshStart = false;

    @JsonProperty("question")
    private Question question;

    @JsonProperty("type")
    private String type;

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

    @JsonProperty("alignment")
    public String getAlignment() {
        return alignment;
    }

    @JsonProperty("alignment")
    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    @JsonProperty("action1")
    public String getAction1Text() {
        return action1Text;
    }

    @JsonProperty("action1")
    public void setAction1Text(String action1Text) {
        this.action1Text = action1Text;
    }

    @JsonProperty("action2")
    public String getAction2Text() {
        return action2Text;
    }

    @JsonProperty("action2")
    public void setAction2Text(String action2Text) {
        this.action2Text = action2Text;
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

    @JsonProperty("question")
    public Question getQuestion() {
        return question;
    }

    @JsonProperty("question")
    public void setQuestion(Question question) {
        this.question = question;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
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