package in.sportscafe.nostragamus.module.navigation.submitquestion.add;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeeva on 25/03/17.
 */
public class QuestionSubmitBy {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("user_id")
    public int getUserId() {
        return userId;
    }

    @SerializedName("user_id")
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @SerializedName("user_name")
    public String getUserName() {
        return userName;
    }

    @SerializedName("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }
}