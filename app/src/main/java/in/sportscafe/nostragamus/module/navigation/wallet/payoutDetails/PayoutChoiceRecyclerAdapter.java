package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.dto.PayoutChoiceDto;

/**
 * Created by sandip on 09/06/17.
 */

public class PayoutChoiceRecyclerAdapter extends RecyclerView.Adapter<PayoutChoiceRecyclerAdapter.PayoutChoiceViewHolder> {

    private Context mContext;
    private ArrayList<PayoutChoiceDto> mDataList;
    private IPayoutChoiceAdapterListener mAdapterListener;

    public PayoutChoiceRecyclerAdapter(ArrayList<PayoutChoiceDto> dataList, IPayoutChoiceAdapterListener listener) {
        mDataList = dataList;
        mAdapterListener = listener;
    }

    public ArrayList<PayoutChoiceDto> getDataList() {
        return mDataList;
    }

    @Override
    public PayoutChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.payout_add_paytm_layout, parent, false);
        return new PayoutChoiceViewHolder(itemView, mAdapterListener);
    }

    @Override
    public void onBindViewHolder(PayoutChoiceViewHolder holder, int position) {
        bindDataToViewHolderItem(holder, position);
    }

    @Override
    public int getItemCount() {
        return (mDataList != null) ? mDataList.size() : 0;
    }

    private void bindDataToViewHolderItem(PayoutChoiceViewHolder payoutChoiceViewHolder, int position) {
        if (mDataList != null && position >= 0 && position < mDataList.size()) {
            PayoutChoiceDto data = mDataList.get(position);
            payoutChoiceViewHolder.radioButton.setChecked(data.isSelected());
            payoutChoiceViewHolder.accountNameTextView.setText(data.getAccountName());
            payoutChoiceViewHolder.accountNumberTextView.setText(data.getAccountNumber());
        }
    }

    private PayoutChoiceDto getItem(int position) {
        PayoutChoiceDto data = null;

        if (mDataList != null && position >= 0 && position < mDataList.size()) {
            data = mDataList.get(position);
        }

        return data;
    }

    /**
     * View Holder */
    public class PayoutChoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RadioButton radioButton;
        private TextView accountNameTextView;
        private TextView accountNumberTextView;
        private IPayoutChoiceAdapterListener listener;

        public PayoutChoiceViewHolder(View itemView, IPayoutChoiceAdapterListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.listener = listener;
            radioButton = (RadioButton) itemView.findViewById(R.id.payout_choice_radio_button);
            accountNameTextView = (TextView) itemView.findViewById(R.id.payout_choice_acc_name_textView);
            accountNumberTextView = (TextView) itemView.findViewById(R.id.payout_choice_acc_number_textView);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int pos = getAdapterPosition();
                listener.onItemClicked(getItem(pos), pos);
            }
        }
    }

    public interface IPayoutChoiceAdapterListener {
        void onItemClicked(PayoutChoiceDto payoutChoiceDto, int position);
    }
}
