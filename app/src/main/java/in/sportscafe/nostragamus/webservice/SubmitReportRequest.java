package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 11/20/17.
 */

public class SubmitReportRequest {

    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private String id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
