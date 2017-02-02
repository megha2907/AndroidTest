package in.sportscafe.nostragamus.module.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 2/2/17.
 */

public class FeedResponse {

    @JsonProperty("data")
    private FeedTimeline feedTimeline;

    @JsonProperty("data")
    public FeedTimeline getFeedTimeline() {
        return feedTimeline;
    }

    @JsonProperty("data")
    public void setFeedTimeline(FeedTimeline feedTimeline) {
        this.feedTimeline = feedTimeline;
    }
}
