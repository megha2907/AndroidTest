package in.sportscafe.scgame.module.user.badges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.List;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.Adapter;

/**
 * Created by deepanshi on 23/8/16.
 */
public class BadgeAdapter extends Adapter<String, BadgeAdapter.ViewHolder> {

    public BadgeAdapter(Context context, List<String> BadgeList) {
        super(context);
        addAll(BadgeList);
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_badge_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String badge = getItem(position);
        String[] parts = badge.split("\\$");

        String badge_id = parts[0];
        String notif_title = parts[1];


        switch (badge_id) {
            case "accuracy_streak":
                holder.mTvBadgeName.setText("Sharpshooter"+" - "+notif_title);
                holder.mIvBadge.setBackgroundResource(R.drawable.notification_accuracy_badge);
                holder.mTvBadgeDesc.setText("More than 80% accuracy in your last 5 matches in a sport");
                break;
            case "table_topper":
                holder.mTvBadgeName.setText("Table Topper"+" - "+notif_title);
                holder.mIvBadge.setBackgroundResource(R.drawable.notification_topper_badge);
                holder.mTvBadgeDesc.setText("Top 10 in a sport leaderboard (min. 25 people)");
                break;
            default:
                holder.mIvBadge.setBackgroundResource(R.drawable.placeholder_icon);
                holder.mTvBadgeDesc.setText("You won"+" "+badge_id+" badge");
                break;
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder  {

        View mMainView;

        TextView mTvBadgeName;

        TextView mTvBadgeDesc;

        CustomButton mIvBadge;



        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvBadgeName = (TextView) V.findViewById(R.id.badge_tv_name);
            mTvBadgeDesc = (TextView) V.findViewById(R.id.badge_tv_desc);
            mIvBadge = (CustomButton) V.findViewById(R.id.badge_iv);

        }

    }

}