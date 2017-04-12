package in.sportscafe.nostragamus.module.allchallenges.info;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig.DropDownIds;
import in.sportscafe.nostragamus.module.allchallenges.dto.ConfigPlayersDetails;
import in.sportscafe.nostragamus.module.allchallenges.dto.RewardBreakUp;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 31/03/17.
 */
public class ChallengeConfigAdapter extends Adapter<ChallengeConfig, ChallengeConfigAdapter.ConfigVH> {

    private OnConfigAccessListener mAccessListener;

    public ChallengeConfigAdapter(Context context, List<ChallengeConfig> configs, OnConfigAccessListener accessListener) {
        super(context);
        mAccessListener = accessListener;
        addAll(configs);
    }

    @Override
    public ChallengeConfig getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ConfigVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConfigVH(getLayoutInflater().inflate(R.layout.inflater_pool_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ConfigVH holder, int position) {
        ChallengeConfig config = getItem(position);

        ConfigPlayersDetails memberDetails = config.getPlayersDetails();
        if(config.isFreeEntry()) {
            holder.mTvEntryFee.setText("Free");
            holder.mTvMembersCount.setText(memberDetails.getJoinedCount() + "");
            holder.mTvEntryFee.setTextColor(ContextCompat.getColor(holder.mTvEntryFee.getContext(),R.color.free_entry_tv_color));
            holder.mTvEntryFee.setAllCaps(true);
            holder.mTvMembersCount.setTextColor(ContextCompat.getColor(holder.mTvMembersCount.getContext(),R.color.free_entry_tv_color));
            holder.mTvReward.setTextColor(ContextCompat.getColor(holder.mTvReward.getContext(),R.color.free_entry_tv_color));
            holder.mBtnJoin.setBackground(ContextCompat.getDrawable(holder.mBtnJoin.getContext(), R.drawable.free_join_btn_bg));
        } else {
            holder.mTvEntryFee.setText("₹ " + config.getEntryFee());
            holder.mTvMembersCount.setText(memberDetails.getJoinedCount() + "/" + memberDetails.getMaxCount());
            holder.mTvEntryFee.setTextColor(ContextCompat.getColor(holder.mTvEntryFee.getContext(),R.color.paid_entry_tv_color));
            holder.mTvMembersCount.setTextColor(ContextCompat.getColor(holder.mTvMembersCount.getContext(),R.color.paid_entry_tv_color));
            holder.mTvReward.setTextColor(ContextCompat.getColor(holder.mTvReward.getContext(),R.color.paid_entry_tv_color));
            holder.mBtnJoin.setBackground(ContextCompat.getDrawable(holder.mBtnJoin.getContext(), R.drawable.paid_join_btn_bg));
        }

        holder.mTvPoolName.setText(config.getConfigName());
        holder.mTvReward.setText("Worth "+config.getRewardDetails().getTotalReward());

        int colorRes = R.color.code_gray_17;
        if (position % 2 == 0) {
            colorRes = R.color.code_gray_1d;
        }

        holder.mVDropDown.setVisibility(View.GONE);
        holder.mTvDropDownTitle.setVisibility(View.GONE);
        holder.mTvDisclaimerTxt.setVisibility(View.GONE);
        holder.mTvDisclaimer.setVisibility(View.GONE);
        holder.mVDisclaimerSeparator.setVisibility(View.GONE);

        holder.mMainView.setBackgroundColor(ViewUtils.getColor(holder.mMainView.getContext(), colorRes));

        holder.mLlDropDownHolder.removeAllViews();
        if (DropDownIds.MEMBER == config.getDropDownId()) {
            holder.mVDropDown.setVisibility(View.VISIBLE);
            holder.mTvDropDownTitle.setVisibility(View.VISIBLE);
            holder.mTvDropDownTitle.setText("JOINED");
            holder.mIvDropDownMember.setRotation(180);
            holder.mTvDisclaimerTxt.setVisibility(View.VISIBLE);
            holder.mTvDisclaimer.setVisibility(View.VISIBLE);
            holder.mVDisclaimerSeparator.setVisibility(View.VISIBLE);
            holder.mTvDisclaimer.setText("The money you win ultimately will be decided by the number of people who join the challenge");
            createMemberDropDownList(memberDetails.getPlayers(), holder.mLlDropDownHolder);
        } else if (DropDownIds.REWARD == config.getDropDownId()) {
            holder.mVDropDown.setVisibility(View.VISIBLE);
            holder.mTvDropDownTitle.setVisibility(View.VISIBLE);
            holder.mTvDropDownTitle.setText("REWARDS");
            holder.mIvDropDownReward.setRotation(180);
            holder.mTvDisclaimerTxt.setVisibility(View.VISIBLE);
            holder.mTvDisclaimer.setVisibility(View.VISIBLE);
            holder.mVDisclaimerSeparator.setVisibility(View.VISIBLE);
            holder.mTvDisclaimer.setText("The money you win ultimately will be decided by the number of people who join the challenge");
            createRewardDropDownList(config.getRewardDetails().getBreakUps(), holder.mLlDropDownHolder);
        }
    }

    private void createMemberDropDownList(List<GroupPerson> memberList, ViewGroup parent) {
        for (GroupPerson member : memberList) {
            parent.addView(getDropDownView(member.getUserName(), null, member.getPhoto(), parent));
        }
    }

    private void createRewardDropDownList(List<RewardBreakUp> breakUps, ViewGroup parent) {
        for (RewardBreakUp breakUp : breakUps) {
            parent.addView(getDropDownView(breakUp.getRank(), breakUp.getAmount(),null, parent));
        }
    }

    private View getDropDownView(String key, String value, String memberPhoto ,ViewGroup parent) {
        View dropDownView = getLayoutInflater().inflate(R.layout.inflater_reward_row, parent, false);

        TextView tvKey = (TextView) dropDownView.findViewById(R.id.reward_row_tv_name);
        tvKey.setText(key);

        HmImageView memberPic = (HmImageView) dropDownView.findViewById(R.id.reward_row_iv_member_photo);
        if (!TextUtils.isEmpty(memberPhoto)) {
            memberPic.setImageUrl(memberPhoto);
        }else {
            memberPic.setVisibility(View.GONE);
        }

        TextView tvValue = (TextView) dropDownView.findViewById(R.id.reward_row_tv_value);

        if (TextUtils.isEmpty(value)) {
            tvValue.setVisibility(View.GONE);
        } else {
            tvValue.setVisibility(View.VISIBLE);
            tvValue.setText(value);
            tvKey.setPadding(15,0,0,0);
        }

        return dropDownView;
    }

    class ConfigVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mMainView;

        TextView mTvPoolName;

        Button mBtnJoin;

        TextView mTvEntryFee;

        TextView mTvMembersCount;

        TextView mTvReward;

        TextView mTvDisclaimerTxt;

        TextView mTvDisclaimer;

        View mVDisclaimerSeparator;

        LinearLayout mLlDropDownHolder;

        TextView mTvDropDownTitle;

        ImageView mIvDropDownReward;

        ImageView mIvDropDownMember;

        View mVDropDown;

        public ConfigVH(View view) {
            super(view);

            mMainView = view;
            mTvPoolName = (TextView) view.findViewById(R.id.pool_row_tv_name);
            mTvEntryFee = (TextView) view.findViewById(R.id.pool_row_tv_entry_fee);
            mTvMembersCount = (TextView) view.findViewById(R.id.pool_row_tv_member_count);
            mTvReward = (TextView) view.findViewById(R.id.pool_row_tv_reward);
            mLlDropDownHolder = (LinearLayout) view.findViewById(R.id.pool_row_ll_drop_down);
            mTvDropDownTitle = (TextView) view.findViewById(R.id.pool_row_tv_dropdown_title);
            mVDropDown = (View) view.findViewById(R.id.pool_row_v_separator_drop_down);
            mIvDropDownReward = (ImageView) view.findViewById(R.id.pool_row_iv_reward_arrow);
            mIvDropDownMember = (ImageView) view.findViewById(R.id.pool_row_iv_member_arrow);
            mTvDisclaimerTxt = (TextView) view.findViewById(R.id.pool_row_tv_disclaimer_txt);
            mTvDisclaimer = (TextView) view.findViewById(R.id.pool_row_tv_disclaimer);
            mVDisclaimerSeparator = (View) view.findViewById(R.id.pool_row_v_disclaimer);
            mBtnJoin = (Button) view.findViewById(R.id.pool_row_btn_join);

            mBtnJoin.setOnClickListener(this);
            view.findViewById(R.id.pool_row_ll_member_layout).setOnClickListener(this);
            view.findViewById(R.id.pool_row_ll_reward_layout).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.pool_row_btn_join:
                    mAccessListener.onJoinClick(getAdapterPosition());
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
            ChallengeConfig pool = getItem(position);
            if (DropDownIds.MEMBER == pool.getDropDownId()) {
                pool.setDropDownId(DropDownIds.NONE);
            } else {
                pool.setDropDownId(DropDownIds.MEMBER);
            }
            notifyItemChanged(position);

            mAccessListener.onConfigHeightChanged();
        }

        private void handleRewardDropDown(int position) {
            ChallengeConfig pool = getItem(position);
            if (DropDownIds.REWARD == pool.getDropDownId()) {
                pool.setDropDownId(DropDownIds.NONE);
            } else {
                pool.setDropDownId(DropDownIds.REWARD);
            }
            notifyItemChanged(position);

            mAccessListener.onConfigHeightChanged();
        }
    }

    public interface OnConfigAccessListener {

        void onJoinClick(int position);

        void onConfigHeightChanged();
    }
}