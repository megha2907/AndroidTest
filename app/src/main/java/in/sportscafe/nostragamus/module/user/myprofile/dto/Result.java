package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.google.gson.annotations.SerializedName;


public class Result {

    @SerializedName("data")
    private String result;

    /**
     * @return The result
     */
    @SerializedName("data")
    public String getResult() {
        return result;
    }

    /**
     * @param result The result
     */
    @SerializedName("data")
    public void setResult(String result) {
        this.result = result;
    }

}