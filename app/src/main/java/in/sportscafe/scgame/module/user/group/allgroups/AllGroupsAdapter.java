package in.sportscafe.scgame.module.user.group.allgroups;;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.List;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.Adapter;

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
        holder.mTvGroupName.setText(allGroups.getgroupName());
    }

    class ViewHolder extends RecyclerView.ViewHolder  {

        View mMainView;

        TextView mTvGroupName;

        TextView mTvGroupMembers;


        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvGroupName = (TextView) V.findViewById(R.id.all_groups_tv_groupName);
            mTvGroupMembers = (TextView) V.findViewById(R.id.all_groups_tv_groupName);


        }

    }

}