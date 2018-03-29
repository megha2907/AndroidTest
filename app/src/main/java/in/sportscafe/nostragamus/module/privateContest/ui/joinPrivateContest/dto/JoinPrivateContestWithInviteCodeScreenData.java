package in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.privateContest.ui.branchShare.ShareDetailsDto;

/**
 * Created by sc on 29/3/18.
 */
@Parcel
public class JoinPrivateContestWithInviteCodeScreenData {

    private String privateCode;

    /**
     * Dto having details of all shared linked properties from branch link.
     */
    private ShareDetailsDto mShareDetails;

    public String getPrivateCode() {
        return privateCode;
    }

    public void setPrivateCode(String privateCode) {
        this.privateCode = privateCode;
    }

    public ShareDetailsDto getShareDetails() {
        return mShareDetails;
    }

    public void setShareDetails(ShareDetailsDto mShareDetails) {
        this.mShareDetails = mShareDetails;
    }
}
