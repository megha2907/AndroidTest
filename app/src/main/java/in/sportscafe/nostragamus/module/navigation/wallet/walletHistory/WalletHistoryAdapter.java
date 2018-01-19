package in.sportscafe.nostragamus.module.navigation.wallet.walletHistory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freshchat.consumer.sdk.ConversationOptions;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.popups.submitReport.SubmitReportPopupActivity;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.utils.AnimationHelper;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by sandip on 12/04/17.
 */

public abstract class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.WalletHistoryViewHolder> {

    /* Used for pagination of items loading */
    public abstract void loadMoreHistory();

    private List<WalletHistoryTransaction> mTransactionList;
    private Context mContext;

    public WalletHistoryAdapter(Context context) {
        mContext = context;
    }

    public List<WalletHistoryTransaction> getWalletHistoryList() {
        return mTransactionList;
    }

    /**
     * Add more history items
     *
     * @param transactions
     */
    public void addWalletHistoryIntoList(List<WalletHistoryTransaction> transactions) {
        if (mTransactionList == null) {
            mTransactionList = new ArrayList<>();
        }
        if (transactions != null && !transactions.isEmpty()) {
            mTransactionList.addAll(transactions);
            notifyDataSetChanged();
        }
    }

    @Override
    public WalletHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_history_item, parent, false);

        return new WalletHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WalletHistoryViewHolder holder, int position) {
        if (position >= (getItemCount() - 1)) {     /* Load more data once last position reached */
            loadMoreHistory();
        }

        if (mTransactionList != null && position < mTransactionList.size()) {    /* position < getItemCount() */
            WalletHistoryTransaction transaction = mTransactionList.get(position);

            changeItemBackground(holder, position);

            setTransactionText(holder, transaction);

            showDate(holder, transaction);
        }
    }

    private void setTransactionText(WalletHistoryViewHolder holder, WalletHistoryTransaction transaction) {
        if (transaction != null) {
            String transactionType = transaction.getTransactionType();

            if (!TextUtils.isEmpty(transactionType)) {
                if (transactionType.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_TYPE_DEPOSIT)) {
                    populateDepositDetails(holder, transaction);

                } else if (transactionType.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_TYPE_WITHDRAW)) {
                    populateWithdrawDetails(holder, transaction);

                } else if (transactionType.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_TYPE_JOINING)) {
                    populateJoiningDetails(holder, transaction);

                } else if (transactionType.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_TYPE_WINNING)) {
                    populateWinningDetails(holder, transaction);

                } else if (transactionType.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_TYPE_PROMO)) {
                    populatePromoDetails(holder, transaction);

                } else if (transactionType.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_TYPE_BUY)) {
                    populateBuyDetails(holder, transaction);

                } else if (transactionType.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_TYPE_REFUNDED)) {
                    populateRefundedDetails(holder, transaction);

                }
            }
        }
    }

    private void populateRefundedDetails(WalletHistoryViewHolder holder, WalletHistoryTransaction transaction) {
        if (transaction != null) {
            holder.txnImageView.setImageResource(R.drawable.wallet_join_challenge_icn);
            double amount = transaction.getAmount();
            String title = "Refunded " + WalletHelper.getFormattedStringOfAmount(amount);
            holder.titleTextView.setText(title);

            String detailMsg = "";
            if (!TextUtils.isEmpty(transaction.getMessage())) {
                detailMsg = transaction.getMessage();
            }
            holder.detailsTextView.setText(detailMsg);
        }
    }

    private void populateBuyDetails(WalletHistoryViewHolder holder, WalletHistoryTransaction transaction) {
        if (transaction != null) {
            holder.txnImageView.setImageResource(R.drawable.wallet_join_challenge_icn);
            double amount = transaction.getAmount();
            String title = "Paid " + WalletHelper.getFormattedStringOfAmount(amount) + " from your wallet";
            holder.titleTextView.setText(title);

            String detailMsg = "Bought Powerup";
            if (!TextUtils.isEmpty(transaction.getMessage())) {
                detailMsg = transaction.getMessage();
            }
            holder.detailsTextView.setText(detailMsg);
        }
    }

    private void populatePromoDetails(WalletHistoryViewHolder holder, WalletHistoryTransaction transaction) {
        if (transaction != null) {
            holder.txnImageView.setImageResource(R.drawable.wallet_promo_icn);
            double amount = transaction.getAmount();
            String title = "Added " + WalletHelper.getFormattedStringOfAmount(amount) + " to Promo money";
            holder.titleTextView.setText(title);

            String detailMsg = "";
            if (!TextUtils.isEmpty(transaction.getMessage())) {
                detailMsg = transaction.getMessage();
            }
            holder.detailsTextView.setText(detailMsg);
        }
    }

    private void populateWinningDetails(WalletHistoryViewHolder holder, WalletHistoryTransaction transaction) {
        if (transaction != null) {
            holder.txnImageView.setImageResource(R.drawable.wallet_won_icn);
            double amount = transaction.getAmount();
            String title = "Added " + WalletHelper.getFormattedStringOfAmount(amount) + " to your winnings";
            holder.titleTextView.setText(title);

            String detailMsg = "You won by playing";
            if (!TextUtils.isEmpty(transaction.getMessage())) {
                detailMsg = transaction.getMessage();
            }
            holder.detailsTextView.setText(detailMsg);
        }
    }

    private void populateJoiningDetails(WalletHistoryViewHolder holder, WalletHistoryTransaction transaction) {
        if (transaction != null) {
            holder.txnImageView.setImageResource(R.drawable.wallet_join_challenge_icn);
            double amount = transaction.getAmount();
            String title = "Paid " + WalletHelper.getFormattedStringOfAmount(amount) + " from your wallet";
            holder.titleTextView.setText(title);

            String detailMsg = "Joined challenge ";
            if (!TextUtils.isEmpty(transaction.getMessage())) {
                detailMsg = transaction.getMessage();
            }
            holder.detailsTextView.setText(detailMsg);
        }
    }

    private void populateWithdrawDetails(WalletHistoryViewHolder holder, WalletHistoryTransaction transaction) {
        if (transaction != null) {
            String status = transaction.getTrasactionStatus();
            if (!TextUtils.isEmpty(status)) {
                double amount = transaction.getAmount();

                if (status.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_STATUS_SUCCESS)) {  /* If transaction status Success */
                    holder.txnImageView.setImageResource(R.drawable.wallet_withdraw_success_icn);
                    String account = transaction.getAccount();

                    String msg = "Added " + WalletHelper.getFormattedStringOfAmount(amount);

                    if (!TextUtils.isEmpty(account)) {
                        if (account.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_ACCOUNT_PAYTM)) {
                            msg = msg + " to your paytm wallet";
                        } else if (account.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_ACCOUNT_BANK)) {
                            msg = msg + " to your bank account";
                        }
                    }

                    holder.titleTextView.setText(msg);
                    holder.detailsTextView.setText("Withdrawn from your winnings");

                } else if (status.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_STATUS_FAILED)) {    /* If transaction status FAILED */
                    holder.txnImageView.setImageResource(R.drawable.wallet_withdraw_failed_icn);
                    String msg = WalletHelper.getFormattedStringOfAmount(amount) + " withdrawal failed";
                    holder.titleTextView.setText(msg);
                    holder.detailsTextView.setText("Failed to withdraw from your winnings");

                } else if (status.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_STATUS_INITIATED)) {     /* If transaction status Initiated */
                    holder.txnImageView.setImageResource(R.drawable.wallet_withdraw_initiated_icn);
                    String msg = WalletHelper.getFormattedStringOfAmount(amount) + " withdrawal in progress";
                    holder.titleTextView.setText(msg);
                    holder.detailsTextView.setText("Withdrawn from your winnings");
                }
            }
        }
    }

    private void populateDepositDetails(WalletHistoryViewHolder holder, WalletHistoryTransaction transaction) {
        String status = transaction.getTrasactionStatus();
        if (!TextUtils.isEmpty(status)) {
            if (status.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_STATUS_SUCCESS)) {
                holder.txnImageView.setImageResource(R.drawable.wallet_deposit_icn);

                double amount = transaction.getAmount();
                String msg = "Added " + WalletHelper.getFormattedStringOfAmount(amount) + " to deposit money";
                holder.titleTextView.setText(msg);

                String account = transaction.getAccount();
                if (!TextUtils.isEmpty(account)) {
                    String detailMsg = "";
                    if (account.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_ACCOUNT_PAYTM)) {
                        detailMsg = "Added from paytm wallet";
                    } else if (account.equalsIgnoreCase(Constants.WalletHistory.TRANSACTION_ACCOUNT_BANK)) {
                        detailMsg = "Added from bank account";
                    }
                    holder.detailsTextView.setText(detailMsg);
                }
            }
        }
    }


    private void showDate(WalletHistoryViewHolder holder, WalletHistoryTransaction transaction) {
        try {
            if (!TextUtils.isEmpty(transaction.getCreatedAt())) {
                String dateStr = "-";

                String dateTime = transaction.getCreatedAt();
                long dateTimeMs = TimeUtils.getMillisecondsFromDateString(
                        dateTime,
                        Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                        Constants.DateFormats.GMT
                );

                /*int dayStr = Integer.parseInt(TimeUtils.getDateStringFromMs(dateTimeMs, "d"));
                dateStr = dayStr + AppSnippet.ordinalOnly(dayStr) + " " +
                        TimeUtils.getDateStringFromMs(dateTimeMs, "MMM") + " \'" +
                        TimeUtils.getDateStringFromMs(dateTimeMs, "yy");*/

                dateStr = TimeUtils.getDateStringFromMs(dateTimeMs, "dd") + "/" +
                        TimeUtils.getDateStringFromMs(dateTimeMs, "MM") + "/" +
                        TimeUtils.getDateStringFromMs(dateTimeMs, "yy");

                holder.dateTextView.setText(dateStr);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        holder.txnIdTextView.setText("ID - " + String.valueOf(transaction.getOrderId()));
        holder.historyReportBtn.setVisibility(View.VISIBLE);
//        if (transaction.isShowReportButton()) {
//            holder.historyReportBtn.setVisibility(View.VISIBLE);
//        } else {
//            holder.historyReportBtn.setVisibility(View.GONE);
//        }
    }

    private void changeItemBackground(WalletHistoryViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemRootLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        } else {
            holder.itemRootLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
        }
    }

    private SpannableStringBuilder getSpannedText(boolean isIn, String amount) {

        SpannableStringBuilder builder = new SpannableStringBuilder();
        String txt1 = "", txt3 = "";

        if (isIn) {
            txt1 = "Debited ";
            txt3 = " from your Paytm wallet";
        } else {
            txt1 = "Credited ";
            txt3 = " to your Paytm wallet";
        }

        SpannableString priceTxt1Spannable = new SpannableString(txt1);
        priceTxt1Spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, txt1.length(), 0);
        builder.append(priceTxt1Spannable);

        String txt2 = "₹" + amount;
        SpannableString priceTxt2Spannable = new SpannableString(txt2);
        priceTxt2Spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#22b573")), 0, txt2.length(), 0);
        builder.append(priceTxt2Spannable);

        SpannableString priceTxt3Spannable = new SpannableString(txt3);
        priceTxt3Spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, txt3.length(), 0);
        builder.append(priceTxt3Spannable);

        return builder;
    }

    @Override
    public int getItemCount() {
        return (mTransactionList != null) ? mTransactionList.size() : 0;
    }

    public class WalletHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView txnImageView;
        TextView titleTextView;
        TextView detailsTextView;
        TextView dateTextView;
        TextView txnIdTextView;
        ImageView moreDetailBtnImageView;
        LinearLayout moreDetailsLayout;
        LinearLayout itemRootLayout;
        LinearLayout moreDetailsButton;
        RelativeLayout historyReportBtn;

        public WalletHistoryViewHolder(View itemView) {
            super(itemView);

            moreDetailsLayout = (LinearLayout) itemView.findViewById(R.id.wallet_item_txn_detail_layout);
            itemRootLayout = (LinearLayout) itemView.findViewById(R.id.wallet_history_item_root_layout);
            txnImageView = (ImageView) itemView.findViewById(R.id.wallet_txn_imageView);
            titleTextView = (TextView) itemView.findViewById(R.id.wallet_history_title_textview);
            detailsTextView = (TextView) itemView.findViewById(R.id.wallet_history_detail_textview);
            dateTextView = (TextView) itemView.findViewById(R.id.wallet_item_date_textview);
            txnIdTextView = (TextView) itemView.findViewById(R.id.wallet_item_txn_id_textview);
            moreDetailBtnImageView = (ImageView) itemView.findViewById(R.id.wallet_more_details_imgView);
            moreDetailsButton = (LinearLayout) itemView.findViewById(R.id.wallet_history_more_details_btn);
            historyReportBtn = (RelativeLayout) itemView.findViewById(R.id.wallet_item_rl_report_btn);
            moreDetailsButton.setOnClickListener(this);
            historyReportBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.wallet_history_more_details_btn:
                    if (moreDetailsLayout.getVisibility() == View.GONE) {
                        AnimationHelper.expand(moreDetailsLayout);
                        moreDetailBtnImageView.setImageResource(R.drawable.thin_arrow_up_min);
                    } else {
                        AnimationHelper.collapse(moreDetailsLayout);
                        moreDetailBtnImageView.setImageResource(R.drawable.thin_arrow_min);
                    }
                    break;

                case R.id.wallet_item_rl_report_btn:
                    if (mContext != null) {
                        WalletHistoryTransaction walletHistoryTransaction = mTransactionList.get(getAdapterPosition());
                        if (walletHistoryTransaction != null) {
                           /* Intent intent = new Intent(mContext, SubmitReportPopupActivity.class);
                            intent.putExtra(Constants.BundleKeys.REPORT_TYPE, "wallet");
                            intent.putExtra(Constants.BundleKeys.REPORT_ID, walletHistoryTransaction.getOrderId().toString());
                            intent.putExtra(Constants.BundleKeys.REPORT_HEADING, "Report Transactions");
                            intent.putExtra(Constants.BundleKeys.REPORT_TITLE, "Transaction Id");
                            intent.putExtra(Constants.BundleKeys.REPORT_DESC, walletHistoryTransaction.getOrderId());
                            intent.putExtra(Constants.BundleKeys.REPORT_THANKYOU_TEXT, "You can let us know about any issues with your " +
                                    "transactions. We will review them and make the necessary changes!");
                            mContext.startActivity(intent);*/

                            openWalletQueryChatBox(walletHistoryTransaction);
                        }
                    }
                    break;
            }
        }

        private void openWalletQueryChatBox(WalletHistoryTransaction walletHistoryTransaction) {

            UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
            FreshchatUser user = Freshchat.getInstance(mContext).getUser();
            if (userInfo != null && user != null) {
                Freshchat.resetUser(mContext);
                user.setFirstName(userInfo.getUserName())
                        .setEmail(userInfo.getEmail());
                Freshchat.getInstance(mContext).setUser(user);

            /* Set any custom metadata to give agents more context,
            and for segmentation for marketing or pro-active messaging */
                Map<String, String> userMeta = new HashMap<String, String>();
                userMeta.put("UserId", String.valueOf(userInfo.getId()));
                userMeta.put("Transaction Type", walletHistoryTransaction.getTransactionType());
                userMeta.put("Transaction Order Id", walletHistoryTransaction.getOrderId());
                userMeta.put("Challenge Id", "");
                userMeta.put("MatchId", "");
                userMeta.put("RoomId", "");

                //Call setUserProperties to sync the user properties with Freshchat's servers
                Freshchat.getInstance(mContext).setUserProperties(userMeta);
            }

         /* Open Wallet Related Queries Chat Channel */
            List<String> tags = new ArrayList<>();
            tags.add("Wallet");
            ConversationOptions convOptions = new ConversationOptions()
                    .filterByTags(tags, "Wallet");
            Freshchat.showConversations(mContext, convOptions);

        }
    }
}
