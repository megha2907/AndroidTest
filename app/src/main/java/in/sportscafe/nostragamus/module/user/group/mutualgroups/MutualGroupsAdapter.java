package in.sportscafe.nostragamus.module.user.group.mutualgroups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity;

/**
 * Created by deepanshi on 1/4/17.
 */

public class MutualGroupsAdapter extends Adapter<MutualGroups, MutualGroupsAdapter.ViewHolder> {

    private Context mcon;
    private static final int CODE_GROUP_INFO = 11;

    public MutualGroupsAdapter(Context context, List<MutualGroups> AllGroupsList) {
        super(context);
        mcon = context;
        addAll(AllGroupsList);
    }

    @Override
    public MutualGroups getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public MutualGroupsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MutualGroupsAdapter.ViewHolder(getLayoutInflater().inflate(R.layout.inflater_all_groups_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MutualGroupsAdapter.ViewHolder holder, int position) {

        MutualGroups mutualGroups = getItem(position);
        holder.mTvGroupName.setText(mutualGroups.getGroupName());

        //holder.mTvGroupTournaments.setText(String.valueOf(allGroups.getTournamentsCount())+" Tournaments");

        if (mutualGroups.getCountGroupMembers()==1){

            holder.mTvGroupTournaments.setText(String.valueOf(mutualGroups.getCountGroupMembers())+" Tournament");
        }
        else {
            holder.mTvGroupTournaments.setText(String.valueOf(mutualGroups.getCountGroupMembers())+" Tournaments");
        }

        if (mutualGroups.getCountGroupMembers()==1){

            holder.mTvGroupMembers.setText(String.valueOf(mutualGroups.getCountGroupMembers())+" Member");
        }
        else {
            holder.mTvGroupMembers.setText(String.valueOf(mutualGroups.getCountGroupMembers())+" Members");
        }

            holder.mIvGroupImage.setImageUrl(mutualGroups.getGroupPhoto());


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
        public void onClick(View v) {

            MutualGroups mutualGroups= getItem(getAdapterPosition());
            Intent intent =  new Intent(mcon, GroupInfoActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putInt(Constants.BundleKeys.GROUP_ID, mutualGroups.getGroupId());
            mBundle.putString(Constants.BundleKeys.GROUP_NAME,mutualGroups.getGroupName());
            intent.putExtras(mBundle);
            ((Activity) mcon).startActivityForResult(intent,CODE_GROUP_INFO);
            ((Activity) mcon).finish();

        }
    }

}