package in.sportscafe.nostragamus.module.user.group.allgroups;;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity;

/**
 * Created by deepanshi on 23/8/16.
 */
public class AllGroupsAdapter extends Adapter<AllGroups, AllGroupsAdapter.ViewHolder> {

    private Context mcon;
    private static final int CODE_GROUP_INFO = 11;

    public AllGroupsAdapter(Context context, List<AllGroups> AllGroupsList) {
        super(context);
        mcon = context;
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


        if (allGroups.getTournamentsCount()==1){

            holder.mTvGroupTournaments.setText(String.valueOf(allGroups.getTournamentsCount())+" Tournament");
        }
        else {
            holder.mTvGroupTournaments.setText(String.valueOf(allGroups.getTournamentsCount())+" Tournaments");
        }

        if (allGroups.getCountGroupMembers()==1){

            holder.mTvGroupMembers.setText(String.valueOf(allGroups.getCountGroupMembers())+" Member");
        }
        else {
            holder.mTvGroupMembers.setText(String.valueOf(allGroups.getCountGroupMembers())+" Members");
        }


            holder.mIvGroupImage.setImageUrl(allGroups.getGroupPhoto());

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

            AllGroups allGroups= getItem(getAdapterPosition());
            Intent intent =  new Intent(mcon, GroupInfoActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putInt(Constants.BundleKeys.GROUP_ID, allGroups.getGroupId());
            mBundle.putString(Constants.BundleKeys.GROUP_NAME,allGroups.getGroupName());
            intent.putExtras(mBundle);
            ((Activity) mcon).startActivityForResult(intent,CODE_GROUP_INFO);
            ((Activity) mcon).finish();

        }
    }

}