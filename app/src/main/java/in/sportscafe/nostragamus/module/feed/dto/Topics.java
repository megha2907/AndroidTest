package in.sportscafe.nostragamus.module.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 5/26/17.
 */

@Parcel
public class Topics {

    @JsonProperty("topic_name")
    private String topicName;

    @JsonProperty("topic_url")
    private String topicUrl;

    @JsonProperty("topic_name")
    public String getTopicName() {
        return topicName;
    }

    @JsonProperty("topic_name")
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @JsonProperty("topic_url")
    public String getTopicUrl() {
        return topicUrl;
    }

    @JsonProperty("topic_url")
    public void setTopicUrl(String topicUrl) {
        this.topicUrl = topicUrl;
    }
}
