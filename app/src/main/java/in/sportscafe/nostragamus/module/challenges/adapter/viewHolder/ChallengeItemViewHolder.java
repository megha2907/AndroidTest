package in.sportscafe.nostragamus.module.challenges.adapter.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.PayoutHomeRecyclerAdapter;

/**
 * Created by sandip on 23/08/17.
 */

public class ChallengeItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView iconImageView;
    private PayoutHomeRecyclerAdapter.IPayoutAdapterActionListener clickListener;

    public ChallengeItemViewHolder(View itemView, @NonNull PayoutHomeRecyclerAdapter.IPayoutAdapterActionListener listener) {
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
