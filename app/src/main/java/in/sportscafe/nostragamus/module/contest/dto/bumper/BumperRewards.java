package in.sportscafe.nostragamus.module.contest.dto.bumper;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 2/2/18.
 */
@Parcel
public class BumperRewards {

    @SerializedName("rank")
    private String rank;

    @SerializedName("percentage")
    private double percentage;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

}
