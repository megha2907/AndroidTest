package in.sportscafe.nostragamus.module.navigation.wallet.doKYC.dto;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

/**
 * Created by deepanshi on 3/30/18.
 */

public class KYCPANImageResponse {


    MultipartBody.Part file;

    public MultipartBody.Part getFile() {
        return file;
    }

    public void setFile(MultipartBody.Part file) {
        this.file = file;
    }

}
