package in.sportscafe.nostragamus.module.notifications;

/**
 * Created by deepanshi on 26/8/16.
 */
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moe.pushlibrary.providers.MoEDataContract;
import com.moengage.addon.inbox.InboxManager;
import com.moengage.addon.inbox.InboxUtils;

import org.json.JSONException;
import org.json.JSONObject;

import in.sportscafe.nostragamus.R;


public class NotificationInboxAdapter extends InboxManager.InboxAdapter<NotificationInboxHolder> {


//    private enum NotificationIcons {
//        challenge, cards, accuracy_badge, group,points,topper_badge;
//    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor The cursor from which to get the data. The cursor is already moved to the
     * correct position.
     * @param parent The parent to which the new view is attached to
     * @return the new inflated view which will be used by the adapter
     */


    @Override public View newView(Context context, Cursor cursor, ViewGroup parent,
                                  LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.inflater_notification_inbox, parent, false);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param holder The TourViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param context Interface to application's global information
     * @param cursor The cursor from which to get the data. The cursor is already moved to the
     * correct position.
     * @return The binded View which is ready to be shown
     */
    @Override public void bindData(NotificationInboxHolder holder, Context context, Cursor cursor) {
        String details = cursor.getString(MoEDataContract.MessageEntity.COLUMN_INDEX_MSG_DETAILS);

        try {
            JSONObject mainObject = new JSONObject(details);
            String notification_icon = mainObject.getString("icon_url");

            if (notification_icon.equals("challenge"))
            {
                holder.notificationIcon.setImageResource(R.drawable.notification_challenge);
            }
            else if (notification_icon.equals("cards"))
            {
                holder.notificationIcon.setImageResource(R.drawable.notification_cards);
            }
            else if (notification_icon.equals("accuracy_streak"))
            {
                holder.notificationIcon.setImageResource(R.drawable.notification_accuracy_badge);
            }
            else if(notification_icon.equals("group"))
            {
                holder.notificationIcon.setImageResource(R.drawable.notification_group);
            }
            else if(notification_icon.equals("points"))
            {
                holder.notificationIcon.setImageResource(R.drawable.notification_points);
            }
            else if(notification_icon.equals("table_topper"))
            {
               holder.notificationIcon.setImageResource(R.drawable.notification_topper_badge);
            }
            else {
                holder.notificationIcon.setImageResource(R.drawable.placeholder_icon);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //validity long millis = cursor.getLong(MoEDataContract.MessageEntity.COLUMN_INDEX_GTIME)
        holder.hasCouponCode = InboxUtils.hasCouponCode(details);
        holder.code = InboxUtils.getCouponCode(details);
        holder.couponCode.setText(holder.code);
        holder.title.setText(InboxUtils.getTitle(details));
        holder.message.setText(InboxUtils.getMessage(details));
        holder.copyCode.setTag(holder.code);
        if(holder.hasCouponCode){
            holder.couponAction.setVisibility(View.VISIBLE);
        }else{
            holder.couponAction.setVisibility(View.GONE);
        }

        Typeface tftitle = Typeface.createFromAsset(context.getAssets(), "fonts/lato/Lato-Regular.ttf");
        holder.title.setTypeface(tftitle);
        holder.message.setTypeface(tftitle);

    }

    /**
     * Callback method to be invoked when an item in this AdapterView has been clicked.
     *
     * @param view The view within the AdapterView that was clicked (this will be a view provided
     * by the adapter)
     * @param context Application Context
     * @return true if Click is being overriden, false otherwise
     */
    @Override public boolean onItemClick(View view, Context context) {
        return false;
    }

    /**
     * Return the TourViewHolder from this method which will be used to reduce Hierarchy lookup and also
     * be used to piggy back data required when view is clicked
     *
     * @param convertView The view which is used by the adapter
     * @return The TourViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     */
    @Override public NotificationInboxHolder getViewHolder(View convertView) {
        NotificationInboxHolder holder = (NotificationInboxHolder) convertView.getTag();
        if( null == holder ){
            holder = new NotificationInboxHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.couponCode = (TextView) convertView.findViewById(R.id.code);
            holder.copyCode = (Button) convertView.findViewById(R.id.btnCopy);
            holder.notificationIcon = (ImageView) convertView.findViewById(R.id.notification_icon);
            holder.copyCode.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    String code = (String) v.getTag();
                    copyCouponCodeToClipboard(v.getContext(), code);
                }
            });
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.couponAction = convertView.findViewById(R.id.couponAction);
            convertView.setTag(holder);
                 }
        return holder;
    }

    /**
     * Copies the specified coupon code to the Clipboard.
     *
     * @param mContext
     *            An instance of the application Context
     * @param couponcode
     *            The coupon code to be added to the Clipboard
     */
    public static void copyCouponCodeToClipboard(Context mContext,
                                                 String couponcode) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            copytoClipboardHoneyLess(mContext, couponcode);
        } else {
            copytoClipboardHoney(mContext, couponcode);
        }
        showToast("Coupon code copied to clipboard", mContext);
    }

    /**
     * Shows a Toast message
     *
     * @param message
     *            The message which needs to be shown
     * @param mContext
     *            An instance of the application Context
     */
    public static void showToast(String message, Context mContext) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void copytoClipboardHoney(Context mContext,
                                             String coupon_code) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext
                .getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(
                "coupon code", coupon_code);
        clipboard.setPrimaryClip(clip);
    }

    @SuppressWarnings("deprecation")
    private static void copytoClipboardHoneyLess(Context mContext,
                                                 String coupon_code) {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(coupon_code);
    }
}