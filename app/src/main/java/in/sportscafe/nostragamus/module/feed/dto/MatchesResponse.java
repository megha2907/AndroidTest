package in.sportscafe.nostragamus.module.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 30/6/16.
 */
public class MatchesResponse {

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