package in.sportscafe.nostragamus.module.privateContest.adapter;

/**
 * Created by sc on 23/3/18.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrizeListItemDto;

/**
 * NOTE : Used to show prize values as calculated based on templates
 */
public class PrivateContestPrizeListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = PrivateContestPrizeListRecyclerAdapter.class.getSimpleName();

    private List<PrizeListItemDto> mPrizeDtoList;

    public PrivateContestPrizeListRecyclerAdapter(List<PrizeListItemDto> prizeDtoList) {
        mPrizeDtoList = prizeDtoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v1 = inflater.inflate(R.layout.private_contest_prize_list_item, parent, false);
        return new PrizeViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindValues(holder, position);
    }

    private void bindValues(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PrizeViewHolder && mPrizeDtoList != null && position < mPrizeDtoList.size()) {
            PrizeViewHolder viewHolder = (PrizeViewHolder) holder;
            PrizeListItemDto prizeListItemDto = mPrizeDtoList.get(position);

            if (prizeListItemDto != null) {
                viewHolder.rankTextView.setText(AppSnippet.ordinal(prizeListItemDto.getWinnerRank()));
                viewHolder.calculatedAmtTextView.setText(WalletHelper.getFormattedStringOfAmount(prizeListItemDto.getAmount()));
                viewHolder.sharePercentTextView.setText(String.valueOf(prizeListItemDto.getSharePercent()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mPrizeDtoList != null) ? mPrizeDtoList.size() : 0;
    }

    public class PrizeViewHolder extends RecyclerView.ViewHolder {

        TextView rankTextView;
        TextView calculatedAmtTextView;
        TextView sharePercentTextView;

        public PrizeViewHolder(View itemView) {
            super(itemView);

            rankTextView = (TextView) itemView.findViewById(R.id.rank_textView);
            calculatedAmtTextView = (TextView) itemView.findViewById(R.id.calculated_amt_textView);
            sharePercentTextView = (TextView) itemView.findViewById(R.id.share_percent_textView);
        }

    }
}
