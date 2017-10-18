package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 25/09/17.
 */

public class VerifyJoinContestResponse {

    @SerializedName("error")
    private String error;

    @SerializedName("try-again")
    private boolean tryAgain = false;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isTryAgain() {
        return tryAgain;
    }

    public void setTryAgain(boolean tryAgain) {
        this.tryAgain = tryAgain;
    }
}
