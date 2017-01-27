package in.sportscafe.nostragamus.module.user.badges;

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

/**
 * Created by deepanshi on 23/8/16.
 */
public class BadgeAdapter extends Adapter<Badge, BadgeAdapter.ViewHolder> {

    public BadgeAdapter(Context context, List<Badge> BadgeList) {
        super(context);
        addAll(BadgeList);
    }

    @Override
    public Badge getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_badge_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Badge badge = getItem(position);

//        String badgeId = badge.getName();

        holder.mTvBadgeName.setText(badge.getName());
        holder.mIvBadge.setBackgroundResource(R.drawable.notification_accuracy_badge);
        holder.mTvBadgeDesc.setText(badge.getDesc());

//        switch (badgeId) {
//            case "accuracy_streak":
//                holder.mTvBadgeName.setText("Sharpshooter"+" - ");
//                holder.mIvBadge.setBackgroundResource(R.drawable.notification_accuracy_badge);
//                holder.mTvBadgeDesc.setText(R.string.accuracy_streak);
//                break;
//            case "table_topper":
//                holder.mTvBadgeName.setText("Table Topper"+" - ");
//                holder.mIvBadge.setBackgroundResource(R.drawable.notification_topper_badge);
//                holder.mTvBadgeDesc.setText(R.string.table_topper);
//                break;
//            default:
//                holder.mIvBadge.setBackgroundResource(R.drawable.placeholder_icon);
//                holder.mTvBadgeDesc.setText("You won"+" "+badgeId+" badge");
//                break;
//        }

    }

    class ViewHolder extends RecyclerView.ViewHolder  {

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