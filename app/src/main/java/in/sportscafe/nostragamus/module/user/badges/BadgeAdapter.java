package in.sportscafe.nostragamus.module.user.badges;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;

/**
 * Created by deepanshi on 23/8/16.
 */
public class BadgeAdapter extends Adapter<Badge, BadgeAdapter.ViewHolder> {

    public BadgeAdapter(Context context, List<Badge> badgeList) {
        super(context);
        addAll(badgeList);
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

        holder.mTvBadgeName.setText(badge.getName());
        holder.mTvBadgeDesc.setText(badge.getDesc());
        holder.mIvBadge.setImageUrl(badge.getPhoto());

        if (null != badge.getIsLocked()) {
            if (badge.getIsLocked()) {
                holder.mIvLocked.setVisibility(View.VISIBLE);
            } else {
                holder.mIvLocked.setVisibility(View.GONE);
            }
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder  {

        View mMainView;

        TextView mTvBadgeName;

        TextView mTvBadgeDesc;

        RelativeLayout mRLBadge;

        HmImageView mIvBadge;

        ImageView mIvLocked;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvBadgeName = (TextView) V.findViewById(R.id.badge_tv_name);
            mTvBadgeDesc = (TextView) V.findViewById(R.id.badge_tv_desc);
            mIvBadge = (HmImageView) V.findViewById(R.id.badge_iv);
            mRLBadge = (RelativeLayout) V.findViewById(R.id.badges_rl);
            mIvLocked= (ImageView) V.findViewById(R.id.badge_locked);
        }
    }

}