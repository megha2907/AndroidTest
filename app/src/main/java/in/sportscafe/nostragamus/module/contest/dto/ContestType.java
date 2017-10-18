package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sandip on 01/09/17.
 */
@Parcel
public class ContestType {

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("category_desc")
    private String categoryDesc;

    @SerializedName("priority")
    private int priority;

    private int contestCount;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getContestCount() {
        return contestCount;
    }

    public void setContestCount(int contestCount) {
        this.contestCount = contestCount;
    }
}
