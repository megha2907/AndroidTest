package in.sportscafe.nostragamus.module.privateContest.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrizeListItemDto;
import in.sportscafe.nostragamus.utils.CodeSnippet;

/**
 * Created by sc on 23/3/18.
 */

public class PrivateContestPrizeEstimationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = PrivateContestPrizeEstimationRecyclerAdapter.class.getSimpleName();

    private int mNumberOfWinner = 0;
    private double mTotalAmount = 0;
    private List<PrizeListItemDto> mPrizeList;

    public PrivateContestPrizeEstimationRecyclerAdapter(int numberOfWinners, double totalAmt) {
        mNumberOfWinner = numberOfWinners;
        mTotalAmount = totalAmt;
        createList();
    }

    private void createList() {
        mPrizeList = new ArrayList<>();
        PrizeListItemDto prizeListItemDto = null;
        float initSharePercent = (float) (100 / mNumberOfWinner);

        for (int count = 0; count < mNumberOfWinner; count++) {
            double amt = mTotalAmount * (initSharePercent / 100);

            prizeListItemDto = new PrizeListItemDto();
            prizeListItemDto.setWinnerRank(count + 1);
            prizeListItemDto.setSharePercent(initSharePercent);
            prizeListItemDto.setAmount(amt);

            mPrizeList.add(prizeListItemDto);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v1 = inflater.inflate(R.layout.private_contest_advance_estimate_list_item, parent, false);
        return new PrizeEstimationViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindValues(holder, position);
    }

    private void bindValues(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  PrizeEstimationViewHolder && mPrizeList != null && position < mPrizeList.size()) {

            PrizeEstimationViewHolder viewHolder = (PrizeEstimationViewHolder) holder;
            PrizeListItemDto prizeListItemDto = mPrizeList.get(position);

            if (prizeListItemDto != null) {
                viewHolder.rankTextView.setText(AppSnippet.ordinal(prizeListItemDto.getWinnerRank()));
                viewHolder.prizePercentageEditText.setText(CodeSnippet.getFormattedPercentage(prizeListItemDto.getSharePercent()));
                viewHolder.calculatedAmtTextView.setText(Constants.RUPEE_SYMBOL + CodeSnippet.getFormattedAmount(prizeListItemDto.getAmount()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mPrizeList != null) ? mPrizeList.size() : 0;
    }

    public class PrizeEstimationViewHolder extends RecyclerView.ViewHolder implements View.OnFocusChangeListener {

        TextView rankTextView;
        TextView calculatedAmtTextView;
        EditText prizePercentageEditText;

        public PrizeEstimationViewHolder(View itemView) {
            super(itemView);

            rankTextView = (TextView) itemView.findViewById(R.id.rank_textView);
            calculatedAmtTextView = (TextView) itemView.findViewById(R.id.calculated_amt_textView);
            prizePercentageEditText = (EditText) itemView.findViewById(R.id.share_percent_EditText);

            prizePercentageEditText.setOnFocusChangeListener(this);

            /*TextWatcher textWatcher = getTextWatcher();
            prizePercentageEditText.removeTextChangedListener(textWatcher);
            prizePercentageEditText.addTextChangedListener(textWatcher);*/

        }



        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            switch (view.getId()) {
                case R.id.share_percent_EditText:
                    if (!hasFocus) {
                        float percents = getPercentage(prizePercentageEditText.getText().toString());
                        onPercentEdited(percents, getAdapterPosition());
                    }
                    break;
            }
        }
    }

    private void onPercentEdited(float percents, int adapterPosition) {
        if (percents > 0 && mTotalAmount > 0
                && mPrizeList != null && adapterPosition >= 0 && adapterPosition < mPrizeList.size()) {

            PrizeListItemDto prizeListItemDto = mPrizeList.get(adapterPosition);
            if (prizeListItemDto != null) {
                double prize = mTotalAmount * (percents / 100);
                prizeListItemDto.setAmount(prize);
                prizeListItemDto.setSharePercent(percents);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                }, 100);
            }
        }
    }

    private float getPercentage(String str) {
        float percent = 0;

        try {
            if (!TextUtils.isEmpty(str)) {
                percent = Float.valueOf(str);
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        return percent;
    }

    public List<PrizeListItemDto> getAdvancePrizeStructureList() {
        return mPrizeList;
    }
}
