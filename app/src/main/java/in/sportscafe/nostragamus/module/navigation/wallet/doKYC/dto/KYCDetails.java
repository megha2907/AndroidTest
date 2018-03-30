package in.sportscafe.nostragamus.module.navigation.wallet.doKYC.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 3/30/18.
 */

@Parcel
public class KYCDetails {

    @SerializedName("fullName")
    private String userName;

    @SerializedName("panNumber")
    private String panNumber;

    @SerializedName("dateOfBirth")
    private String dateOfBirth;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
