package in.sportscafe.nostragamus.module.feed.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 2/2/17.
 */

public class FeedResponse {

    @SerializedName("data")
    private FeedTimeline feedTimeline;

    @SerializedName("data")
    public FeedTimeline getFeedTimeline() {
        return feedTimeline;
    }

    @SerializedName("data")
    public void setFeedTimeline(FeedTimeline feedTimeline) {
        this.feedTimeline = feedTimeline;
    }
}
