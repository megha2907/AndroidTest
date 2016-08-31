package in.sportscafe.scgame.module.user.badges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

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
        holder.mTvBadgeName.setText(badge);
        holder.mTvBadgeDesc.setText("You won"+" "+badge+" badge");
    }

    class ViewHolder extends RecyclerView.ViewHolder  {

        View mMainView;

        TextView mTvBadgeName;

        TextView mTvBadgeDesc;

        HmImageView mImBadge;


        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvBadgeName = (TextView) V.findViewById(R.id.badge_tv_name);
            mTvBadgeDesc = (TextView) V.findViewById(R.id.badge_tv_desc);
            mImBadge = (HmImageView) V.findViewById(R.id.badge_iv);

        }

    }

}