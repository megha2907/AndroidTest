package in.sportscafe.nostragamus.module.question.add;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 25/03/17.
 */
public class QuestionSubmitBy {

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_id")
    public int getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JsonProperty("user_name")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }
}