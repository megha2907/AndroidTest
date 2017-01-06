package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Result {

    @JsonProperty("data")
    private String result;

    /**
     * @return The result
     */
    @JsonProperty("data")
    public String getResult() {
        return result;
    }

    /**
     * @param result The result
     */
    @JsonProperty("data")
    public void setResult(String result) {
        this.result = result;
    }

}