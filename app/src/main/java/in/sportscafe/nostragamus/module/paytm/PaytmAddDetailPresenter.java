package in.sportscafe.nostragamus.module.paytm;

/**
 * Created by Jeeva on 23/03/17.
 */
public interface PaytmAddDetailPresenter {

    void onCreatePaytmAddDetail();

    void onClickSave(String mobNumber, String confirmMobNumber, String email, String confirmEmail);

    void onClickSkip();
}