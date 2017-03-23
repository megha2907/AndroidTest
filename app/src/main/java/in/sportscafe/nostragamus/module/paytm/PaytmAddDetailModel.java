package in.sportscafe.nostragamus.module.paytm;

/**
 * Created by Jeeva on 23/03/17.
 */
public interface PaytmAddDetailModel {

    void savePaytmDetails(String mobNumber, String confirmMobNumber, String email, String confirmEmail);
}