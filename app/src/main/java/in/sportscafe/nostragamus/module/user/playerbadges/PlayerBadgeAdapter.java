package in.sportscafe.nostragamus.module.user.playerbadges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.badges.BadgeAdapter;

/**
 * Created by deepanshi on 1/5/17.
 */

public class PlayerBadgeAdapter extends Adapter<String, PlayerBadgeAdapter.ViewHolder> {

    public PlayerBadgeAdapter(Context context, List<String> BadgeList) {
        super(context);
        addAll(BadgeList);
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public PlayerBadgeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerBadgeAdapter.ViewHolder(getLayoutInflater().inflate(R.layout.inflater_badge_row, parent, false));
    }

    @Override
    public void onBindViewHolder(PlayerBadgeAdapter.ViewHolder holder, int position) {

        String badge = getItem(position);
        String[] parts = badge.split("\\$");

        String badge_id = parts[0];
        String notif_title = parts[1];

        switch (badge_id) {
            case "accuracy_streak":
                holder.mTvBadgeName.setText("Sharpshooter" + " - " + notif_title);
                holder.mIvBadge.setBackgroundResource(R.drawable.notification_accuracy_badge);
                holder.mTvBadgeDesc.setText(R.string.accuracy_streak);
                break;
            case "table_topper":
                holder.mTvBadgeName.setText("Table Topper" + " - " + notif_title);
                holder.mIvBadge.setBackgroundResource(R.drawable.notification_topper_badge);
                holder.mTvBadgeDesc.setText(R.string.table_topper);
                break;
            default:
                holder.mIvBadge.setBackgroundResource(R.drawable.left_placeholder_icon);
                holder.mTvBadgeDesc.setText("You won" + " " + badge_id + " badge");
                break;
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mMainView;

        TextView mTvBadgeName;

        TextView mTvBadgeDesc;

        ImageButton mIvBadge;


        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvBadgeName = (TextView) V.findViewById(R.id.badge_tv_name);
            mTvBadgeDesc = (TextView) V.findViewById(R.id.badge_tv_desc);
            mIvBadge = (ImageButton) V.findViewById(R.id.badge_iv);

        }

    }

}