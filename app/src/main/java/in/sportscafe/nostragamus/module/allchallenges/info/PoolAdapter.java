package in.sportscafe.nostragamus.module.allchallenges.info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Pool;
import in.sportscafe.nostragamus.module.allchallenges.dto.Pool.DropDownIds;
import in.sportscafe.nostragamus.module.allchallenges.dto.PoolMemberDetails;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 31/03/17.
 */
public class PoolAdapter extends Adapter<Pool, PoolAdapter.PoolVH> {

    public PoolAdapter(Context context, List<Pool> poolList) {
        super(context);
        addAll(poolList);
    }

    @Override
    public Pool getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public PoolVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PoolVH(getLayoutInflater().inflate(R.layout.inflater_pool_row, parent, false));
    }

    @Override
    public void onBindViewHolder(PoolVH holder, int position) {
        Pool pool = getItem(position);

        holder.mTvPoolName.setText(pool.getPoolName());
        holder.mTvEntryFee.setText("Rs " + pool.getEntryFee());
        holder.mTvReward.setText(pool.getRewardDetails().getTotalReward());

        PoolMemberDetails memberDetails = pool.getMemberDetails();
        holder.mTvMembersCount.setText(memberDetails.getJoinedCount() + "/" + memberDetails.getMaxCount());

        int colorRes = R.color.code_gray_17;
        if (position % 2 == 0) {
            colorRes = R.color.code_gray_1d;
        }

        holder.mMainView.setBackgroundColor(ViewUtils.getColor(holder.mMainView.getContext(), colorRes));

        holder.mLlDropDownHolder.removeAllViews();
        if (DropDownIds.MEMBER == pool.getDropDownId()) {
            createMemberDropDownList(memberDetails.getJoinedMembers(), holder.mLlDropDownHolder);
        } else if (DropDownIds.REWARD == pool.getDropDownId()) {
            createRewardDropDownList(pool.getRewardDetails().getRewards(), holder.mLlDropDownHolder);
        }
    }

    private void createMemberDropDownList(List<GroupPerson> memberList, ViewGroup parent) {
        for (GroupPerson member : memberList) {
            parent.addView(getDropDownView(member.getUserName(), null, parent));
        }
    }

    private void createRewardDropDownList(LinkedHashMap<String, String> rewardsMap, ViewGroup parent) {
        for (String key : rewardsMap.keySet()) {
            parent.addView(getDropDownView(key, rewardsMap.get(key), parent));
        }
    }

    private View getDropDownView(String key, String value, ViewGroup parent) {
        View dropDownView = getLayoutInflater().inflate(R.layout.inflater_reward_row, parent, false);

        ((TextView) dropDownView.findViewById(R.id.reward_row_tv_name)).setText(key);

        TextView tvValue = (TextView) dropDownView.findViewById(R.id.reward_row_tv_value);
        if (TextUtils.isEmpty(value)) {
            tvValue.setVisibility(View.GONE);
        } else {
            tvValue.setVisibility(View.VISIBLE);
            tvValue.setText(value);
        }

        return dropDownView;
    }

    class PoolVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mMainView;

        TextView mTvPoolName;

        TextView mTvEntryFee;

        TextView mTvMembersCount;

        TextView mTvReward;

        LinearLayout mLlDropDownHolder;

        public PoolVH(View view) {
            super(view);

            mMainView = view;
            mTvPoolName = (TextView) view.findViewById(R.id.pool_row_tv_name);
            mTvEntryFee = (TextView) view.findViewById(R.id.pool_row_tv_entry_fee);
            mTvMembersCount = (TextView) view.findViewById(R.id.pool_row_tv_member_count);
            mTvReward = (TextView) view.findViewById(R.id.pool_row_tv_reward);
            mLlDropDownHolder = (LinearLayout) view.findViewById(R.id.pool_row_ll_drop_down);

            view.findViewById(R.id.pool_row_btn_join).setOnClickListener(this);
            view.findViewById(R.id.pool_row_ll_member_layout).setOnClickListener(this);
            view.findViewById(R.id.pool_row_ll_reward_layout).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.pool_row_btn_join:
                    break;
                case R.id.pool_row_ll_member_layout:
                    handleMemberDropDown(getAdapterPosition());
                    break;
                case R.id.pool_row_ll_reward_layout:
                    handleRewardDropDown(getAdapterPosition());
                    break;
            }
        }

        private void handleMemberDropDown(int position) {
            Pool pool = getItem(position);
            if (DropDownIds.MEMBER == pool.getDropDownId()) {
                pool.setDropDownId(DropDownIds.NONE);
            } else {
                pool.setDropDownId(DropDownIds.MEMBER);
            }
            notifyItemChanged(position);
        }

        private void handleRewardDropDown(int position) {
            Pool pool = getItem(position);
            if (DropDownIds.REWARD == pool.getDropDownId()) {
                pool.setDropDownId(DropDownIds.NONE);
            } else {
                pool.setDropDownId(DropDownIds.REWARD);
            }
            notifyItemChanged(position);
        }
    }
}