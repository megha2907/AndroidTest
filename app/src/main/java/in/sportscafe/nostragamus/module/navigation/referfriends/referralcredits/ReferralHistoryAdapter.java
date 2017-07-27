package in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.WordUtils;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.walletHistory.WalletHistoryAdapter;
import in.sportscafe.nostragamus.module.navigation.wallet.walletHistory.WalletHistoryTransaction;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.utils.AnimationHelper;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 6/27/17.
 */

public class ReferralHistoryAdapter extends RecyclerView.Adapter<ReferralHistoryAdapter.ReferralHistoryViewHolder> {

    private List<ReferralHistory> mReferralHistoryList;
    private Context mContext;

    public ReferralHistoryAdapter(Context context) {
        mContext = context;
    }

    public List<ReferralHistory> getReferralHistoryList() {
        return mReferralHistoryList;
    }

    /**
     * Add more history items
     *
     * @param referralHistoryList
     */
    public void addReferralHistoryIntoList(List<ReferralHistory> referralHistoryList) {
        if (mReferralHistoryList == null) {
            mReferralHistoryList = new ArrayList<>();
        }
        if (referralHistoryList != null && !referralHistoryList.isEmpty()) {
            mReferralHistoryList.addAll(referralHistoryList);
            notifyDataSetChanged();
        }
    }

    @Override
    public ReferralHistoryAdapter.ReferralHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.referral_history_item, parent, false);

        return new ReferralHistoryAdapter.ReferralHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReferralHistoryAdapter.ReferralHistoryViewHolder holder, int position) {

        if (mReferralHistoryList != null && position < mReferralHistoryList.size()) {    /* position < getItemCount() */
            ReferralHistory referralHistory = mReferralHistoryList.get(position);
            setWinningsAndReferralsText(holder, referralHistory);
            setUserImage(holder, referralHistory);
            showDate(holder, referralHistory);

        }
    }

    private void setUserImage(ReferralHistoryViewHolder holder, ReferralHistory referralHistory) {
        if (referralHistory != null) {
            if (referralHistory.getReferralDetails() != null) {
                holder.mIvUserImage.setImageUrl(referralHistory.getReferralDetails().getUserPhoto());
            }
        }
    }

    private void setWinningsAndReferralsText(ReferralHistoryAdapter.ReferralHistoryViewHolder holder, ReferralHistory referralHistory) {
        if (referralHistory != null) {
            String referralHistoryType = referralHistory.getReferralHistoryType();

            if (!TextUtils.isEmpty(referralHistoryType)) {
                if (referralHistoryType.equalsIgnoreCase(Constants.ReferralHistory.REFERRAL_HISTORY_TYPE_POWERUPS)) {
                    populatePowerUpDetails(holder, referralHistory);

                } else if (referralHistoryType.equalsIgnoreCase(Constants.ReferralHistory.REFERRAL_HISTORY_TYPE_MONEY)) {
                    populateMoneyDetails(holder, referralHistory);

                }
                holder.mTvReferralMoreDetails.setText("Transaction ID - " + referralHistory.getReferralOrderId());
            }
        }
    }

    private void populateMoneyDetails(ReferralHistoryViewHolder holder, ReferralHistory referralHistory) {

        if (referralHistory != null) {
            holder.mTvReferralTitle.setText(WordUtils.capitalize(referralHistory.getReferralDetails().getUserName()) + " made some deposit!");

            holder.mTvReferralWinnings.setVisibility(View.VISIBLE);

            holder.mTvReferralWinnings.setText(" "+WalletHelper.getFormattedStringOfAmount(referralHistory.getTransactionAmount()) +
                    " added to your wallet");

            holder.mTvReferralWinnings.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wallet_credit_small, 0, 0, 0);
        }


    }

    private void showDate(ReferralHistoryViewHolder holder, ReferralHistory referralHistory) {
        try {
            if (!TextUtils.isEmpty(referralHistory.getCreatedAt())) {
                String dateStr = "-";

                String dateTime = referralHistory.getCreatedAt();
                long dateTimeMs = TimeUtils.getMillisecondsFromDateString(
                        dateTime,
                        Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                        Constants.DateFormats.GMT
                );

                dateStr = TimeUtils.getDateStringFromMs(dateTimeMs, "dd") + "/" +
                        TimeUtils.getDateStringFromMs(dateTimeMs, "MM") + "/" +
                        TimeUtils.getDateStringFromMs(dateTimeMs, "yy");

                holder.dateTextView.setText(dateStr);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void populatePowerUpDetails(ReferralHistoryViewHolder holder, ReferralHistory referralHistory) {

        if (referralHistory != null) {
            holder.mTvReferralTitle.setText(WordUtils.capitalize(referralHistory.getReferralDetails().getUserName()) + " joined with your referral Code!");
            holder.mTvReferralWinnings.setVisibility(View.GONE);
            showOrHidePowerUps(holder, referralHistory);
        }

    }

    private void showOrHidePowerUps(ReferralHistoryViewHolder holder, ReferralHistory referralHistory) {

        HashMap<String, Integer> powerUpMap = referralHistory.getPowerUps();

        Integer powerUp2xCount = powerUpMap.get(Constants.Powerups.XX);
        Integer powerUpNonNegsCount = powerUpMap.get(Constants.Powerups.NO_NEGATIVE);
        Integer powerUpPlayerPollCount = powerUpMap.get(Constants.Powerups.AUDIENCE_POLL);

        if (null == powerUp2xCount) {
            powerUp2xCount = 0;
        }

        if (null == powerUpNonNegsCount) {
            powerUpNonNegsCount = 0;
        }

        if (null == powerUpPlayerPollCount) {
            powerUpPlayerPollCount = 0;
        }

        if (powerUp2xCount == 0 && powerUpNonNegsCount == 0 && powerUpPlayerPollCount == 0) {
            holder.powerUpLayout.setVisibility(View.GONE);
        } else {
            holder.powerUpLayout.setVisibility(View.VISIBLE);

            if (powerUp2xCount != 0) {
                holder.powerUp2xImageView.setBackgroundResource(R.drawable.double_powerup_small);
                holder.powerUp2xImageView.setVisibility(View.VISIBLE);
                holder.powerUp2xTextView.setText(String.valueOf(powerUp2xCount));
            } else {
                holder.powerUp2xImageView.setVisibility(View.GONE);
                holder.powerUp2xTextView.setVisibility(View.GONE);
            }

            if (powerUpNonNegsCount != 0) {
                holder.powerUpNoNegativeImageView.setBackgroundResource(R.drawable.no_negative_powerup_small);
                holder.powerUpNoNegativeImageView.setVisibility(View.VISIBLE);
                holder.powerUpNoNegativeTextView.setText(String.valueOf(powerUpNonNegsCount));
            } else {
                holder.powerUpNoNegativeImageView.setVisibility(View.GONE);
                holder.powerUpNoNegativeTextView.setVisibility(View.GONE);
            }

            if (powerUpPlayerPollCount != 0) {
                holder.powerUpAudienceImageView.setBackgroundResource(R.drawable.audience_poll_powerup_small);
                holder.powerUpAudienceImageView.setVisibility(View.VISIBLE);
                holder.powerUpAudienceTextView.setText(String.valueOf(powerUpPlayerPollCount));
            } else {
                holder.powerUpAudienceImageView.setVisibility(View.GONE);
                holder.powerUpAudienceTextView.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return (mReferralHistoryList != null) ? mReferralHistoryList.size() : 0;
    }

    public class ReferralHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        HmImageView mIvUserImage;
        TextView mTvReferralTitle;
        TextView mTvReferralWinnings;
        TextView mTvReferralMoreDetails;
        ImageView moreDetailBtnImageView;
        LinearLayout moreDetailsLayout;
        LinearLayout itemRootLayout;
        LinearLayout moreDetailsButton;
        TextView dateTextView;

        /*PowerUp Layout */
        LinearLayout powerUpLayout;
        ImageView powerUp2xImageView;
        ImageView powerUpNoNegativeImageView;
        ImageView powerUpAudienceImageView;
        TextView powerUp2xTextView;
        TextView powerUpNoNegativeTextView;
        TextView powerUpAudienceTextView;

        public ReferralHistoryViewHolder(View itemView) {
            super(itemView);

            moreDetailsLayout = (LinearLayout) itemView.findViewById(R.id.referral_history_item_detail_layout);
            itemRootLayout = (LinearLayout) itemView.findViewById(R.id.referral_history_item_root_layout);
            mIvUserImage = (HmImageView) itemView.findViewById(R.id.referral_history_user_imageView);
            mTvReferralTitle = (TextView) itemView.findViewById(R.id.referral_history_title_textview);
            dateTextView = (TextView) itemView.findViewById(R.id.referral_history_date_textview);
            mTvReferralWinnings = (TextView) itemView.findViewById(R.id.referral_history_winnings_textview);
            mTvReferralMoreDetails = (TextView) itemView.findViewById(R.id.referral_history_order_id_textview);
            moreDetailBtnImageView = (ImageView) itemView.findViewById(R.id.referral_history_more_details_imgView);
            moreDetailsButton = (LinearLayout) itemView.findViewById(R.id.referral_history_more_details_btn);
            moreDetailsButton.setOnClickListener(this);

            powerUpLayout = (LinearLayout) itemView.findViewById(R.id.powerup_bottom_layout);
            powerUp2xImageView = (ImageView) itemView.findViewById(R.id.referral_history_powerup_won_2x);
            powerUpNoNegativeImageView = (ImageView) itemView.findViewById(R.id.referral_history_powerup_won_noNeg);
            powerUpAudienceImageView = (ImageView) itemView.findViewById(R.id.referral_history_powerup_won_audience);
            powerUp2xTextView = (TextView) itemView.findViewById(R.id.referral_history_powerup_won_2x_count);
            powerUpNoNegativeTextView = (TextView) itemView.findViewById(R.id.referral_history_powerup_won_noNeg_count);
            powerUpAudienceTextView = (TextView) itemView.findViewById(R.id.referral_history_powerup_won_audience_count);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.referral_history_more_details_btn:
                    if (moreDetailsLayout.getVisibility() == View.GONE) {
                        AnimationHelper.expand(moreDetailsLayout);
                        moreDetailBtnImageView.setImageResource(R.drawable.thin_arrow_up_min);
                    } else {
                        AnimationHelper.collapse(moreDetailsLayout);
                        moreDetailBtnImageView.setImageResource(R.drawable.thin_arrow_min);
                    }
                    break;
            }
        }
    }
}