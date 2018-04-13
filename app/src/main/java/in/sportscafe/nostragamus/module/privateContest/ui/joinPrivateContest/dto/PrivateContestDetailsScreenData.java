package in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.privateContest.ui.branchShare.ShareDetailsDto;

/**
 * Created by sc on 27/3/18.
 */
@Parcel
public class PrivateContestDetailsScreenData {

    private String privateCode;
    private FindPrivateContestResponse privateContestDetailsResponse;
    private ShareDetailsDto shareDetails;

    public String getPrivateCode() {
        return privateCode;
    }

    public void setPrivateCode(String privateCode) {
        this.privateCode = privateCode;
    }

    public FindPrivateContestResponse getPrivateContestDetailsResponse() {
        return privateContestDetailsResponse;
    }

    public void setPrivateContestDetailsResponse(FindPrivateContestResponse privateContestDetailsResponse) {
        this.privateContestDetailsResponse = privateContestDetailsResponse;
    }

    public ShareDetailsDto getShareDetails() {
        return shareDetails;
    }

    public void setShareDetails(ShareDetailsDto shareDetails) {
        this.shareDetails = shareDetails;
    }
}
