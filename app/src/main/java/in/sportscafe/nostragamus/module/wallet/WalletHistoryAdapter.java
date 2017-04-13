package in.sportscafe.nostragamus.module.wallet;

import android.content.Context;
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
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by sandip on 12/04/17.
 */

public class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.WalletHistoryViewHolder> {

    private List<WalletTransaction> mTransactionList;
    private Context mContext;

    public WalletHistoryAdapter(Context context, List<WalletTransaction> transactions) {
        mContext = context;
        mTransactionList = transactions;
    }

    @Override
    public WalletHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_history_item, parent, false);

        return new WalletHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WalletHistoryViewHolder holder, int position) {
        if (mTransactionList != null && mTransactionList.size() > position) {
            WalletTransaction transaction = mTransactionList.get(position);

            // Background color
            if (position % 2 == 0) {
                holder.itemRootLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey1));
            } else {
                holder.itemRootLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
            }

            String txnDetails = transaction.getChallengeName();

            // Debit-credit , set values
            if (transaction.getMoneyFlow().equals(Constants.MoneyFlow.IN)) {
                holder.txnImageView.setImageResource(R.drawable.wallet_debit);
                holder.titleTextView.setText(getSpannedText(true, String.valueOf(transaction.getAmount())));
                txnDetails = "Joined " + transaction.getChallengeName();
            } else {
                holder.txnImageView.setImageResource(R.drawable.wallet_credit);
                holder.titleTextView.setText(getSpannedText(false, String.valueOf(transaction.getAmount())));
                if (!TextUtils.isEmpty(transaction.getRank())) {
                    txnDetails = "Rank " + transaction.getRank() + " in " + transaction.getChallengeName();
                }
            }

            holder.detailsTextView.setText(txnDetails);

            // Date format
            try {
                if (!TextUtils.isEmpty(transaction.getCreatedAt())) {
                    String dateStr = "-";

                    String dateTime = transaction.getCreatedAt();
                    long dateTimeMs = TimeUtils.getMillisecondsFromDateString(
                            dateTime,
                            Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                            Constants.DateFormats.GMT
                    );

                    int dayStr = Integer.parseInt(TimeUtils.getDateStringFromMs(dateTimeMs, "d"));
                    dateStr = dayStr + AppSnippet.ordinalOnly(dayStr) + " " +
                            TimeUtils.getDateStringFromMs(dateTimeMs, "MMM") + " \'" +
                            TimeUtils.getDateStringFromMs(dateTimeMs, "yy");

                    holder.dateTextView.setText(dateStr);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            holder.txnIdTextView.setText("Transaction ID - " + String.valueOf(transaction.getOrderId()));
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

        SpannableString priceTxt1Spannable= new SpannableString(txt1);
        priceTxt1Spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, txt1.length(), 0);
        builder.append(priceTxt1Spannable);

        String txt2 = amount;
        SpannableString priceTxt2Spannable= new SpannableString(txt2);
        priceTxt2Spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#22b573")), 0, txt2.length(), 0);
        builder.append(priceTxt2Spannable);

        SpannableString priceTxt3Spannable= new SpannableString(txt3);
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

        public WalletHistoryViewHolder(View itemView) {
            super(itemView);

            moreDetailsLayout = (LinearLayout) itemView.findViewById(R.id.wallet_item_txn_detail_layout);
            itemRootLayout = (LinearLayout) itemView.findViewById(R.id.wallet_history_item_root_layout);
            txnImageView = (ImageView) itemView.findViewById(R.id.wallet_txn_imageView);
            titleTextView = (TextView) itemView.findViewById(R.id.wallet_history_title_textview);
            detailsTextView = (TextView) itemView.findViewById(R.id.wallet_history_detail_textview);
            dateTextView = (TextView) itemView.findViewById(R.id.wallet_item_date_textview);
            txnIdTextView = (TextView) itemView.findViewById(R.id.wallet_item_txn_id_textview);
            moreDetailBtnImageView = (ImageView) itemView.findViewById(R.id.wallet_more_details_imgBtn);
            moreDetailBtnImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.wallet_more_details_imgBtn:
                    if (moreDetailsLayout.getVisibility() == View.GONE) {
                        moreDetailsLayout.setVisibility(View.VISIBLE);
                        moreDetailBtnImageView.setImageResource(R.drawable.thin_arrow_up_min);
                    } else {
                        moreDetailsLayout.setVisibility(View.GONE);
                        moreDetailBtnImageView.setImageResource(R.drawable.thin_arrow_min);
                    }
                    break;
            }
        }
    }
}
