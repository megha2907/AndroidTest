package in.sportscafe.nostragamus.module.feed.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 5/26/17.
 */

@Parcel
public class Topics {

    @SerializedName("topic_name")
    private String topicName;

    @SerializedName("topic_img_url")
    private String topicUrl;

    @SerializedName("topic_name")
    public String getTopicName() {
        return topicName;
    }

    @SerializedName("topic_name")
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @SerializedName("topic_img_url")
    public String getTopicUrl() {
        return topicUrl;
    }

    @SerializedName("topic_img_url")
    public void setTopicUrl(String topicUrl) {
        this.topicUrl = topicUrl;
    }
}
