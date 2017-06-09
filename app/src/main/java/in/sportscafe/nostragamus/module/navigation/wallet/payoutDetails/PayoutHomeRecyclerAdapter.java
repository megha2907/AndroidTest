package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.dto.PayoutAddEditItemDto;

/**
 * Created by sandip on 08/06/17.
 */

public class PayoutHomeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<PayoutAddEditItemDto> mPayoutInfoList;
    private IPayoutAdapterActionListener mPayoutAdapterActionListener;

    public PayoutHomeRecyclerAdapter(Context cxt, @NonNull ArrayList<PayoutAddEditItemDto> list,
                                     @NonNull IPayoutAdapterActionListener listener) {
        mContext = cxt;
        mPayoutInfoList = list;
        mPayoutAdapterActionListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = PayoutViewType.NONE;

        if (mPayoutInfoList != null && !mPayoutInfoList.isEmpty()) {
            viewType = mPayoutInfoList.get(position).getViewType();
        }

        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case PayoutViewType.ADD_PAYTM:
                View v1 = inflater.inflate(R.layout.payout_add_paytm_layout, parent, false);
                viewHolder = new AddPaytmViewHolder(v1, mPayoutAdapterActionListener);
                break;

            case PayoutViewType.ADD_BANK:
                View v2 = inflater.inflate(R.layout.payout_add_bank_layout, parent, false);
                viewHolder = new AddBankViewHolder(v2, mPayoutAdapterActionListener);
                break;

            case PayoutViewType.SHOW_PAYTM:
                View v3 = inflater.inflate(R.layout.payout_show_paytm_layout, parent, false);
                viewHolder = new ShowPaytmViewHolder(v3, mPayoutAdapterActionListener);
                break;

            case PayoutViewType.SHOW_BANK:
                View v4 = inflater.inflate(R.layout.payout_show_bank_layout, parent, false);
                viewHolder = new ShowBankViewHolder(v4, mPayoutAdapterActionListener);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            switch (holder.getItemViewType()) {
                case PayoutViewType.ADD_PAYTM:
                    bindAddPaytmData((AddPaytmViewHolder)holder, position);
                    break;

                case PayoutViewType.ADD_BANK:
                    bindAddBankData((AddBankViewHolder)holder, position);
                    break;

                case PayoutViewType.SHOW_PAYTM:
                    bindEditPaytmData((ShowPaytmViewHolder)holder, position);
                    break;

                case PayoutViewType.SHOW_BANK:
                    bindEditBankData((ShowBankViewHolder)holder, position);
                    break;
            }
        }
    }

    private void bindAddPaytmData(AddPaytmViewHolder holder, int position) {
        holder.iconImageView.setImageResource(R.drawable.info);
    }

    private void bindAddBankData(AddBankViewHolder holder, int position) {
        holder.iconImageView.setImageResource(R.drawable.info);
    }

    private void bindEditPaytmData(ShowPaytmViewHolder holder, int position) {
        holder.iconImageView.setImageResource(R.drawable.edit_icon);

        if (mPayoutInfoList != null && position < mPayoutInfoList.size()) {
            PayoutAddEditItemDto payoutAddEditItemDto = mPayoutInfoList.get(position);

            if (payoutAddEditItemDto.getViewType() == PayoutViewType.SHOW_PAYTM) {
                holder.mAccDetailTextView.setText("XXXXX X1234");
            }
        }
    }

    private void bindEditBankData(ShowBankViewHolder holder, int position) {
        holder.iconImageView.setImageResource(R.drawable.edit_icon);

        if (mPayoutInfoList != null && position < mPayoutInfoList.size()) {
            PayoutAddEditItemDto payoutAddEditItemDto = mPayoutInfoList.get(position);

            if (payoutAddEditItemDto.getViewType() == PayoutViewType.SHOW_BANK) {
                holder.mAccDetailTextView.setText("XXXX-XXXX-XXXX-1234");
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mPayoutInfoList != null) ? mPayoutInfoList.size() : 0;
    }

    private PayoutAddEditItemDto getItem(int position) {
        PayoutAddEditItemDto data = null;

        if (mPayoutInfoList != null && position >= 0 && position < mPayoutInfoList.size()) {
            data = mPayoutInfoList.get(position);
        }

        return data;
    }

    /* ------------- ViewHolders & ViewTypes ------------- */
    public interface PayoutViewType {
        int NONE = -1;
        int ADD_PAYTM = 1;
        int ADD_BANK = 2;
        int SHOW_PAYTM = 3;
        int SHOW_BANK = 4;
    }

    public interface IPayoutAdapterActionListener {
        void onAddPaytmClicked();
        void onAddBankClicked();
        void onEditPaytmClicked(PayoutAddEditItemDto payoutAddEditItemDto, int position);
        void onEditBankClicked(PayoutAddEditItemDto payoutAddEditItemDto, int position);
    }

    private class AddPaytmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iconImageView;
        private IPayoutAdapterActionListener clickListener;

        public AddPaytmViewHolder(View itemView, @NonNull IPayoutAdapterActionListener listener) {
            super(itemView);
            this.clickListener = listener;
            itemView.setOnClickListener(this);
            iconImageView = (ImageView) itemView.findViewById(R.id.payout_list_item_icon_imageView);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onAddPaytmClicked();
            }
        }
    }

    private class AddBankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iconImageView;
        private IPayoutAdapterActionListener clickListener;

        public AddBankViewHolder(View itemView, @NonNull IPayoutAdapterActionListener listener) {
            super(itemView);
            this.clickListener = listener;
            itemView.setOnClickListener(this);
            iconImageView = (ImageView) itemView.findViewById(R.id.payout_list_item_icon_imageView);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onAddBankClicked();
            }
        }
    }

    private class ShowPaytmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iconImageView;
        private TextView mAccDetailTextView;
        private IPayoutAdapterActionListener clickListener;

        public ShowPaytmViewHolder(View itemView, @NonNull IPayoutAdapterActionListener listener) {
            super(itemView);
            this.clickListener = listener;
            itemView.setOnClickListener(this);
            iconImageView = (ImageView) itemView.findViewById(R.id.payout_list_item_icon_imageView);
            mAccDetailTextView = (TextView) itemView.findViewById(R.id.payout_list_item_details_textView);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onEditPaytmClicked(getItem(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

    private class ShowBankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iconImageView;
        private TextView mAccDetailTextView;
        private IPayoutAdapterActionListener clickListener;

        public ShowBankViewHolder(View itemView, @NonNull IPayoutAdapterActionListener listener) {
            super(itemView);
            this.clickListener = listener;
            itemView.setOnClickListener(this);
            iconImageView = (ImageView) itemView.findViewById(R.id.payout_list_item_icon_imageView);
            mAccDetailTextView = (TextView) itemView.findViewById(R.id.payout_list_item_details_textView);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onEditBankClicked(getItem(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

}
