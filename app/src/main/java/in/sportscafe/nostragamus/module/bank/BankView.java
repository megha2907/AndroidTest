package in.sportscafe.nostragamus.module.bank;

import com.jeeva.android.InAppView;

import static android.R.attr.x;

/**
 * Created by Jeeva on 01/03/17.
 */

public interface BankView extends InAppView {

    void set2xCount(int count, boolean runningLow);

    void setNonegsCount(int count, boolean runningLow);

    void setPollCount(int count, boolean runningLow);

    void navigateToReferFriend();
}