package in.sportscafe.nostragamus.module.paytm;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 7/4/17.
 */

public class GenerateCheckSumResponse {

    @JsonProperty("EMAIL")
    private String eMAIL;

    @JsonProperty("MOBILE_NO")
    private String mOBILENO;

    @JsonProperty("CHECKSUMHASH")
    private String cHECKSUMHASH;

    @JsonProperty("EMAIL")
    public String getEMAIL() {
        return eMAIL;
    }

    @JsonProperty("EMAIL")
    public void setEMAIL(String eMAIL) {
        this.eMAIL = eMAIL;
    }

    @JsonProperty("MOBILE_NO")
    public String getMOBILENO() {
        return mOBILENO;
    }

    @JsonProperty("MOBILE_NO")
    public void setMOBILENO(String mOBILENO) {
        this.mOBILENO = mOBILENO;
    }

    @JsonProperty("CHECKSUMHASH")
    public String getCHECKSUMHASH() {
        return cHECKSUMHASH;
    }

    @JsonProperty("CHECKSUMHASH")
    public void setCHECKSUMHASH(String cHECKSUMHASH) {
        this.cHECKSUMHASH = cHECKSUMHASH;
    }


}
