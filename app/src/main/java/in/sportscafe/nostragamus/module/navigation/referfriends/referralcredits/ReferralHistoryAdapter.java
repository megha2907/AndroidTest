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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.utils.AnimationHelper;

/**
 * Created by deepanshi on 6/27/17.
 */

public class ReferralHistoryAdapter extends RecyclerView.Adapter<ReferralHistoryAdapter.ReferralHistoryViewHolder> {

    private List<ReferralHistory> mTransactionList;
    private Context mContext;

    public ReferralHistoryAdapter(Context context) {
        mContext = context;
    }

    public List<ReferralHistory> getReferralHistoryList() {
        return mTransactionList;
    }

    /**
     * Add more history items
     *
     * @param transactions
     */
    public void addReferralHistoryIntoList(List<ReferralHistory> transactions) {
        if (mTransactionList == null) {
            mTransactionList = new ArrayList<>();
        }
        if (transactions != null && !transactions.isEmpty()) {
            mTransactionList.addAll(transactions);
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

        if (mTransactionList != null && position < mTransactionList.size()) {    /* position < getItemCount() */
            ReferralHistory referralHistory = mTransactionList.get(position);

            setTransactionText(holder, referralHistory);

        }
    }

    private void setTransactionText(ReferralHistoryAdapter.ReferralHistoryViewHolder holder, ReferralHistory referralHistory) {
        if (referralHistory != null) {
            String referralHistoryType = referralHistory.getReferralHistoryType();

            if (!TextUtils.isEmpty(referralHistoryType)) {
                if (referralHistoryType.equalsIgnoreCase(Constants.ReferralHistory.REFERRAL_HISTORY_TYPE_POWERUPS)) {
                    populatePowerUpDetails(holder, referralHistory);

                } else if (referralHistoryType.equalsIgnoreCase(Constants.ReferralHistory.REFERRAL_HISTORY_TYPE_MONEY)) {
                    populateMoneyDetails(holder, referralHistory);

                }
            }
        }
    }

    private void populateMoneyDetails(ReferralHistoryViewHolder holder, ReferralHistory referralHistory) {

        if (referralHistory != null) {
            holder.mTvReferralTitle.setText(referralHistory.getReferralDetails().getUserName() + " made deposit of " +
                    WalletHelper.getFormattedStringOfAmount(referralHistory.getTransactionAmount()) + "!");

            holder.mTvReferralWinnings.setText(WalletHelper.getFormattedStringOfAmount(referralHistory.getTransactionAmount()) +
                    " added to your wallet");

            holder.mTvReferralWinnings.setCompoundDrawables(ContextCompat.getDrawable(holder.mTvReferralWinnings.getContext(), R.drawable.wallet_credit), null, null, null);
        }


    }

    private void populatePowerUpDetails(ReferralHistoryViewHolder holder, ReferralHistory referralHistory) {

        if (referralHistory != null) {
            holder.mTvReferralTitle.setText(referralHistory.getReferralDetails().getUserName() + " Joined with your referral Code!");

            HashMap<String, Integer> powerUpMap = referralHistory.getPowerUps();
            Integer total2xPowerupsWon = powerUpMap.get(Constants.Powerups.XX);
            Integer totalNoNegsPowerupsWon = powerUpMap.get(Constants.Powerups.NO_NEGATIVE);
            Integer totalPlayerPollPowerupsWon = powerUpMap.get(Constants.Powerups.AUDIENCE_POLL);

            if (null == total2xPowerupsWon) {
                total2xPowerupsWon = 0;
            }

            if (null == totalNoNegsPowerupsWon) {
                totalNoNegsPowerupsWon = 0;
            }

            if (null == totalPlayerPollPowerupsWon) {
                totalPlayerPollPowerupsWon = 0;
            }

            if (total2xPowerupsWon != 0 && totalNoNegsPowerupsWon != 0) {

                SpannableStringBuilder ssb = new SpannableStringBuilder(("Added " + String.valueOf(total2xPowerupsWon) + " and "
                        + String.valueOf(totalNoNegsPowerupsWon) + " to your powerup bank"));

                Bitmap icon2x = BitmapFactory.decodeResource(holder.mTvReferralTitle.getResources(), R.drawable.double_powerup_small);
                Bitmap iconNoNegs = BitmapFactory.decodeResource(holder.mTvReferralTitle.getResources(), R.drawable.no_negative_powerup_small);
                ssb.setSpan(new ImageSpan(icon2x), 9, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.setSpan(new ImageSpan(iconNoNegs), 18, 19, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                holder.mTvReferralWinnings.setText(ssb, TextView.BufferType.SPANNABLE);

            } else if (total2xPowerupsWon != 0 && totalPlayerPollPowerupsWon != 0) {

                SpannableStringBuilder ssb = new SpannableStringBuilder(("Added " + String.valueOf(total2xPowerupsWon) + " and "
                        + String.valueOf(totalPlayerPollPowerupsWon) + " to your powerup bank"));

                Bitmap icon2x = BitmapFactory.decodeResource(holder.mTvReferralTitle.getResources(), R.drawable.double_powerup_small);
                Bitmap iconPLayerPoll = BitmapFactory.decodeResource(holder.mTvReferralTitle.getResources(), R.drawable.audience_poll_powerup_small);
                ssb.setSpan(new ImageSpan(icon2x), 9, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.setSpan(new ImageSpan(iconPLayerPoll), 18, 19, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                holder.mTvReferralWinnings.setText(ssb, TextView.BufferType.SPANNABLE);

            } else if (totalNoNegsPowerupsWon != 0 && totalPlayerPollPowerupsWon != 0) {

                SpannableStringBuilder ssb = new SpannableStringBuilder(("Added " + String.valueOf(totalNoNegsPowerupsWon) + " and "
                        + String.valueOf(totalPlayerPollPowerupsWon) + " to your powerup bank"));

                Bitmap iconNoNegs = BitmapFactory.decodeResource(holder.mTvReferralTitle.getResources(), R.drawable.no_negative_powerup_small);
                Bitmap iconPLayerPoll = BitmapFactory.decodeResource(holder.mTvReferralTitle.getResources(), R.drawable.audience_poll_powerup_small);
                ssb.setSpan(new ImageSpan(iconNoNegs), 9, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.setSpan(new ImageSpan(iconPLayerPoll), 18, 19, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                holder.mTvReferralWinnings.setText(ssb, TextView.BufferType.SPANNABLE);

            }


        }

    }


    @Override
    public int getItemCount() {
        return (mTransactionList != null) ? mTransactionList.size() : 0;
    }

    public class ReferralHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mIvUserImage;
        TextView mTvReferralTitle;
        TextView mTvReferralWinnings;
        TextView mTvReferralMoreDetails;
        ImageView moreDetailBtnImageView;
        LinearLayout moreDetailsLayout;
        LinearLayout itemRootLayout;
        LinearLayout moreDetailsButton;

        public ReferralHistoryViewHolder(View itemView) {
            super(itemView);

            moreDetailsLayout = (LinearLayout) itemView.findViewById(R.id.referral_history_item_detail_layout);
            itemRootLayout = (LinearLayout) itemView.findViewById(R.id.referral_history_item_root_layout);
            mIvUserImage = (ImageView) itemView.findViewById(R.id.referral_history_user_imageView);
            mTvReferralTitle = (TextView) itemView.findViewById(R.id.referral_history_title_textview);
            mTvReferralWinnings = (TextView) itemView.findViewById(R.id.referral_history_winnings_textview);
            mTvReferralMoreDetails = (TextView) itemView.findViewById(R.id.referral_history_item_id_textview);
            moreDetailBtnImageView = (ImageView) itemView.findViewById(R.id.referral_history_more_details_imgView);
            moreDetailsButton = (LinearLayout) itemView.findViewById(R.id.referral_history_more_details_btn);
            moreDetailsButton.setOnClickListener(this);
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