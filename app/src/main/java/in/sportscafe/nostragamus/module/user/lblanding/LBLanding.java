package in.sportscafe.nostragamus.module.user.lblanding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Jeeva on 19/01/17.
 */

public class LBLanding implements Serializable {

    private Integer id =1;

    private String name = "Cricket";

    private Integer rank ;

    private Integer rankChange ;

    @JsonProperty("img_url")
    private String imgUrl;

    public LBLanding() {
    }

    public LBLanding(Integer id, String name, Integer rank, Integer rankChange, String imgUrl) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.rankChange = rankChange;
        this.imgUrl = imgUrl;
    }

    public Integer getRankChange() {
        return rankChange;
    }

    public void setRankChange(Integer rankChange) {
        this.rankChange = rankChange;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("img_url")
    public String getImgUrl() {
        return imgUrl;
    }

    @JsonProperty("img_url")
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}