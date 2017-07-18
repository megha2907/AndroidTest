package in.sportscafe.nostragamus.module.allchallenges.info;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
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
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.allchallenges.dto.ConfigPlayersDetails;
import in.sportscafe.nostragamus.module.allchallenges.dto.RewardBreakUp;
import in.sportscafe.nostragamus.module.allchallenges.dto.WinnersRewards;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.FeedWebView;
import in.sportscafe.nostragamus.module.play.prediction.PredictionAdapter;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 4/10/17.
 */

public class ChallengeRewardAdapter extends Adapter<ChallengeConfig, ChallengeRewardAdapter.ConfigVH> {

    private ChallengeRewardAdapter.OnConfigAccessListener mAccessListener;

    private String mChallengeEndTime;

    private Challenge mChallengeInfo;

    private String mTabName;

    public ChallengeRewardAdapter(Context context, List<ChallengeConfig> configs,
                                  String ChallengeEndTime, Challenge challengeInfo,
                                  String tabName,
                                  OnConfigAccessListener accessListener) {
        super(context);
        mAccessListener = accessListener;
        mChallengeEndTime = ChallengeEndTime;
        mChallengeInfo = challengeInfo;
        mTabName = tabName;
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

        String prizesHandOutDate = dayOfMonthinEndTime + AppSnippet.ordinalOnly(dayOfMonthinEndTime) + " of " +
                TimeUtils.getDateStringFromMs(endTimeMs, "MMM");

        // Setting end date of the challenge
        holder.mTvChallengeEndTime.setText("Prizes will be handed out within a day when the challenge ends on " + prizesHandOutDate);

       // holder.mTvDisclaimer.setText("The money you win ultimately will be decided by the number of people who join the challenge");

        int colorRes = R.color.code_gray_17;

        holder.mMainView.setBackgroundColor(ViewUtils.getColor(holder.mMainView.getContext(), colorRes));

        holder.mLlDropDownHolder.removeAllViews();

        holder.mTvDisclaimer.setText(Html.fromHtml(holder.mTvDisclaimer.getResources().getString(R.string.prize_money_disclaimer)));
        holder.mTvDisclaimer.setMovementMethod(LinkMovementMethod.getInstance());
        holder.mTvDisclaimerTxt.setVisibility(View.VISIBLE);
        holder.mTvDisclaimer.setVisibility(View.VISIBLE);

        CharSequence text = holder.mTvDisclaimer.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) holder.mTvDisclaimer.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();//should clear old spans
            for (URLSpan url : urls) {
                LinkSpan click = new LinkSpan(url.getURL());
                style.setSpan(click, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.mTvDisclaimer.setText(style);
        }


        try {
            if (mChallengeInfo.getChallengeInfo().isClosed()) {
                if (!config.getRewardDetails().getWinnersRewardsList().isEmpty()) {
                    createWinnersDropDownList(config.getRewardDetails().getWinnersRewardsList(), holder.mLlDropDownHolder);
                    holder.mTvChallengeEndTime.setVisibility(View.GONE);
                    holder.mTvUserWinningAmount.setText(config.getRewardDetails().getAmountWonByUser());
                }else {
                    holder.mTvChallengeEndTime.setText("Prizes Info not Available");
                    holder.mRlRewardsLayout.setVisibility(View.GONE);
                }
            }else {
                createRewardDropDownList(config.getRewardDetails().getBreakUps(), holder.mLlDropDownHolder);
            }
        }catch (Exception e) {
            holder.mTvChallengeEndTime.setText("Prizes Info not Available");
            holder.mRlRewardsLayout.setVisibility(View.GONE);
        }

        mAccessListener.onConfigHeightChanged();

        /*Only for COMPLETED tab */
        if (mTabName.equalsIgnoreCase(Constants.ChallengeTabs.COMPLETED)) {
            if (mChallengeInfo.getChallengeUserInfo().isUserJoined()) {
                holder.mFreePaidLinerLayout.setVisibility(View.VISIBLE);
            } else {
                holder.mFreePaidLinerLayout.setVisibility(View.GONE);
            }
            holder.mTvChallengeEndTime.setText("Prizes handed out on " + prizesHandOutDate);
            holder.mTvChallengeEndTime.setVisibility(View.VISIBLE);
        }
    }


    private void createRewardDropDownList(List<RewardBreakUp> breakUps, ViewGroup parent) {
        for (RewardBreakUp breakUp : breakUps) {
            parent.addView(getDropDownView(breakUp.getRank(),breakUp.getAmount(),null, null, parent));
        }
    }

    private void createWinnersDropDownList(List<WinnersRewards> winnersRewardsList, ViewGroup parent) {
        for (WinnersRewards winnersRewards : winnersRewardsList) {
            parent.addView(getDropDownView(winnersRewards.getWinnerName(),winnersRewards.getAmountWon(),winnersRewards.getWinnerRank(), null, parent));
        }
    }

    private View getDropDownView(String key, String value,String winnerRank,String memberPhoto, ViewGroup parent) {
        View dropDownView = getLayoutInflater().inflate(R.layout.inflater_reward_row, parent, false);

        TextView tvKey = (TextView) dropDownView.findViewById(R.id.reward_row_tv_name);
        tvKey.setText(key);

        HmImageView memberPic = (HmImageView) dropDownView.findViewById(R.id.reward_row_iv_member_photo);
        if (!TextUtils.isEmpty(memberPhoto)) {
            memberPic.setImageUrl(memberPhoto);
        } else {
            memberPic.setVisibility(View.GONE);
        }

        TextView tvWinnerRank = (TextView) dropDownView.findViewById(R.id.reward_row_tv_winner_rank);
        if (!TextUtils.isEmpty(winnerRank)){
            tvWinnerRank.setVisibility(View.VISIBLE);
            tvWinnerRank.setText(winnerRank);
        }else {
            tvWinnerRank.setVisibility(View.GONE);
        }

        TextView tvValue = (TextView) dropDownView.findViewById(R.id.reward_row_tv_value);

        if (TextUtils.isEmpty(value) || value.equalsIgnoreCase("₹ null")) {
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
        TextView mTvUserWinningAmount;
        LinearLayout mFreePaidLinerLayout;

        public ConfigVH(View view) {
            super(view);

            mMainView = view;
            mTvEntryFeePaid = (TextView) view.findViewById(R.id.config_reward_tv_amount_paid);
            mRlRewardsLayout = (RelativeLayout) view.findViewById(R.id.config_reward_rl_rewards_layout);
            mTvChallengeEndTime = (TextView) view.findViewById(R.id.config_reward_tv_announcement);
            mLlDropDownHolder = (LinearLayout) view.findViewById(R.id.config_reward_row_ll_rewards);
            mTvDisclaimerTxt = (TextView) view.findViewById(R.id.config_reward_row_tv_disclaimer_txt);
            mTvDisclaimer = (TextView) view.findViewById(R.id.config_reward_row_tv_disclaimer);
            mTvUserWinningAmount = (TextView) view.findViewById(R.id.config_reward_tv_winning);
            mFreePaidLinerLayout = (LinearLayout) view.findViewById(R.id.config_reward_ll_paid_layout);

        }

    }

    public interface OnConfigAccessListener {

        void onConfigHeightChanged();
    }

    private class LinkSpan extends URLSpan {

        private LinkSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View view) {
            String url = getURL();
            if (url != null) {
                view.getContext().startActivity(new Intent(view.getContext(), FeedWebView.class).putExtra("url", url));
            }
        }
    }
}