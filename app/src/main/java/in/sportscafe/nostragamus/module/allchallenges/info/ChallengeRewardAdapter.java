package in.sportscafe.nostragamus.module.allchallenges.info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.allchallenges.dto.ConfigPlayersDetails;
import in.sportscafe.nostragamus.module.allchallenges.dto.RewardBreakUp;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 4/10/17.
 */

public class ChallengeRewardAdapter extends Adapter<ChallengeConfig, ChallengeRewardAdapter.ConfigVH> {

    private ChallengeRewardAdapter.OnConfigAccessListener mAccessListener;

    private String mChallengeEndTime;

    public ChallengeRewardAdapter(Context context, List<ChallengeConfig> configs, String ChallengeEndTime, OnConfigAccessListener accessListener) {
        super(context);
        mAccessListener = accessListener;
        mChallengeEndTime = ChallengeEndTime;
        addAll(configs);
    }

    @Override
    public ChallengeConfig getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ChallengeRewardAdapter.ConfigVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChallengeRewardAdapter.ConfigVH(getLayoutInflater().inflate(R.layout.inflater_config_reward_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ChallengeRewardAdapter.ConfigVH holder, int position) {
        ChallengeConfig config = getItem(position);

        if (config.isFreeEntry()) {
            holder.mTvEntryFeePaid.setText("Free");
        } else {
            holder.mTvEntryFeePaid.setText("₹ " + config.getEntryFee());
        }

        String endTime = mChallengeEndTime;
        long endTimeMs = TimeUtils.getMillisecondsFromDateString(
                endTime,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        int dayOfMonthinEndTime = Integer.parseInt(TimeUtils.getDateStringFromMs(endTimeMs, "d"));

        // Setting end date of the challenge
        holder.mTvChallengeEndTime.setText("Prizes will be announced once challenge ends on the "+
                       dayOfMonthinEndTime + AppSnippet.ordinalOnly(dayOfMonthinEndTime) + " of " +
                        TimeUtils.getDateStringFromMs(endTimeMs, "MMM")
        );

       // holder.mTvDisclaimer.setText("The money you win ultimately will be decided by the number of people who join the challenge");

        int colorRes = R.color.code_gray_17;

        holder.mMainView.setBackgroundColor(ViewUtils.getColor(holder.mMainView.getContext(), colorRes));

        holder.mLlDropDownHolder.removeAllViews();

        try {
            createRewardDropDownList(config.getRewardDetails().getBreakUps(), holder.mLlDropDownHolder);
        }catch (Exception e) {
            holder.mTvChallengeEndTime.setText("Prizes data not Available");
            holder.mRlRewardsLayout.setVisibility(View.GONE);
        }

        mAccessListener.onConfigHeightChanged();
    }


    private void createRewardDropDownList(List<RewardBreakUp> breakUps, ViewGroup parent) {
        for (RewardBreakUp breakUp : breakUps) {
            parent.addView(getDropDownView(breakUp.getRank(), breakUp.getAmount(), breakUp.getWinnerName(), null, parent));
        }
    }

    private View getDropDownView(String key, String value, String winnerName, String memberPhoto, ViewGroup parent) {
        View dropDownView = getLayoutInflater().inflate(R.layout.inflater_reward_row, parent, false);

        TextView tvKey = (TextView) dropDownView.findViewById(R.id.reward_row_tv_name);
        tvKey.setText(key);

        HmImageView memberPic = (HmImageView) dropDownView.findViewById(R.id.reward_row_iv_member_photo);
        if (!TextUtils.isEmpty(memberPhoto)) {
            memberPic.setImageUrl(memberPhoto);
        } else {
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

    class ConfigVH extends RecyclerView.ViewHolder {

        View mMainView;

        RelativeLayout mRlRewardsLayout;
        TextView mTvEntryFeePaid;
        TextView mTvChallengeEndTime;
        LinearLayout mLlDropDownHolder;
        TextView mTvDisclaimerTxt;
        TextView mTvDisclaimer;

        public ConfigVH(View view) {
            super(view);

            mMainView = view;
            mTvEntryFeePaid = (TextView) view.findViewById(R.id.config_reward_tv_amount_paid);
            mRlRewardsLayout = (RelativeLayout) view.findViewById(R.id.config_reward_rl_rewards_layout);
            mTvChallengeEndTime = (TextView) view.findViewById(R.id.config_reward_tv_announcement);
            mLlDropDownHolder = (LinearLayout) view.findViewById(R.id.config_reward_row_ll_rewards);
            mTvDisclaimerTxt = (TextView) view.findViewById(R.id.config_reward_row_tv_disclaimer_txt);
            mTvDisclaimer = (TextView) view.findViewById(R.id.config_reward_row_tv_disclaimer);

        }

    }

    public interface OnConfigAccessListener {

        void onConfigHeightChanged();
    }
}