package in.sportscafe.nostragamus.module.notifications;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.moengage.addon.inbox.InboxManager;

/**
 * Created by deepanshi on 26/8/16.
 */
public class NotificationInboxHolder extends InboxManager.ViewHolder{

    public TextView title;
    public TextView message;
    public boolean hasCouponCode;
    public String code;
    public TextView timeStamp;
    public TextView couponCode;
    public ImageView notificationIcon;
    public Button copyCode;
    public View couponAction;

}
