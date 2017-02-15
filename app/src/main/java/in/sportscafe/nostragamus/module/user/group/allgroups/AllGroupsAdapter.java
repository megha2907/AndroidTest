package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity;

/**
 * Created by deepanshi on 23/8/16.
 */
public class AllGroupsAdapter extends Adapter<AllGroups, AllGroupsAdapter.ViewHolder> {

    public AllGroupsAdapter(Context context, List<AllGroups> AllGroupsList) {
        super(context);
        addAll(AllGroupsList);
    }

    @Override
    public AllGroups getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_all_groups_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AllGroups allGroups = getItem(position);
        holder.mTvGroupName.setText(allGroups.getGroupName());
        holder.mIvGroupImage.setImageUrl(allGroups.getGroupPhoto());

        int tourCount = allGroups.getTournamentsCount();
        holder.mTvGroupTournaments.setText(tourCount + " Tournament" + (tourCount > 1 ? "s" : ""));

        tourCount = allGroups.getCountGroupMembers();
        holder.mTvGroupMembers.setText(tourCount + " Member" + (tourCount > 1 ? "s" : ""));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mMainView;

        TextView mTvGroupName;

        TextView mTvGroupMembers;

        TextView mTvGroupTournaments;

        HmImageView mIvGroupImage;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvGroupName = (TextView) V.findViewById(R.id.all_groups_tv_groupName);
            mTvGroupTournaments = (TextView) V.findViewById(R.id.all_groups_tv_GroupTournaments);
            mTvGroupMembers = (TextView) V.findViewById(R.id.all_groups_tv_GroupMembers);
            mIvGroupImage = (HmImageView) V.findViewById(R.id.all_groups_iv_groupImage);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(IntentActions.ACTION_GROUP_CLICK);
            intent.putExtra(BundleKeys.CLICK_POSITION, getAdapterPosition());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

}