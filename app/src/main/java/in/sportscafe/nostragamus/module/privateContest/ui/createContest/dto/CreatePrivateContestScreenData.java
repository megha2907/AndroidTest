package in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.dto.ContestType;

/**
 * Created by sc on 21/3/18.
 */
@Parcel
public class CreatePrivateContestScreenData {

    private ContestType contestType;
    private ContestScreenData contestScreenData;

    public ContestType getContestType() {
        return contestType;
    }

    public void setContestType(ContestType contestType) {
        this.contestType = contestType;
    }

    public ContestScreenData getContestScreenData() {
        return contestScreenData;
    }

    public void setContestScreenData(ContestScreenData contestScreenData) {
        this.contestScreenData = contestScreenData;
    }
}
