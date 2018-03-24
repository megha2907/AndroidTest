package in.sportscafe.nostragamus.module.privateContest.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 21/3/18.
 */

public class CreatePrivateContestResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("config_id")
    private int configId;

    @SerializedName("info")
    private CreatePrivateContestInfo info;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("createdAt")
    private String createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public CreatePrivateContestInfo getInfo() {
        return info;
    }

    public void setInfo(CreatePrivateContestInfo info) {
        this.info = info;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
