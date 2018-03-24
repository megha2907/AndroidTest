package in.sportscafe.nostragamus.module.privateContest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.R;

/**
 * Created by sc on 23/3/18.
 */

public class PrivateContestPrizeEstimationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = PrivateContestPrizeEstimationRecyclerAdapter.class.getSimpleName();

    private int mNumberOfWinner = 0;

    public PrivateContestPrizeEstimationRecyclerAdapter(int numberOfWinners) {
        mNumberOfWinner = numberOfWinners;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v1 = inflater.inflate(R.layout.private_contest_prize_estimate_list_item, parent, false);
        return new PrizeEstimationViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindValues(holder, position);
    }

    private void bindValues(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  PrizeEstimationViewHolder) {
            PrizeEstimationViewHolder viewHolder = (PrizeEstimationViewHolder) holder;

            viewHolder.rankTextView.setText(AppSnippet.ordinal(position + 1));
        }
    }

    @Override
    public int getItemCount() {
        return mNumberOfWinner;
    }

    public class PrizeEstimationViewHolder extends RecyclerView.ViewHolder implements View.OnFocusChangeListener {

        TextView rankTextView;
        TextView calculatedAmtTextView;
        EditText prizePercentageEditText;

        public PrizeEstimationViewHolder(View itemView) {
            super(itemView);

            rankTextView = (TextView) itemView.findViewById(R.id.rank_textView);
            calculatedAmtTextView = (TextView) itemView.findViewById(R.id.calculated_amt_textView);
            prizePercentageEditText = (EditText) itemView.findViewById(R.id.prizes_editView);

            prizePercentageEditText.setOnFocusChangeListener(this);
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            switch (view.getId()) {
                case R.id.prizes_editView:
                    if (!hasFocus) {
                        onFocusLost(getAdapterPosition());
                    }
                    break;
            }
        }

        private void onFocusLost(int adapterPosition) {

        }
    }


}
