package in.sportscafe.nostragamus.module.fuzzylbs;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.fuzzysearch.OnFuzzySearchClickListener;
import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;
import in.sportscafe.nostragamus.module.user.login.dto.BasicUserInfo;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FuzzyLbAdapter extends Adapter<LbLanding, FuzzyLbAdapter.ViewHolder> {

    private OnFuzzySearchClickListener mFuzzySearchClickListener;

    public FuzzyLbAdapter(Context context, OnFuzzySearchClickListener listener) {
        super(context);
        this.mFuzzySearchClickListener = listener;
    }

    @Override
    public LbLanding getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_fuzzy_player_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTvPlayerName.setText(getItem(position).getName());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvPlayerName;

        public ViewHolder(View V) {
            super(V);

            mTvPlayerName = (TextView) V.findViewById(R.id.fuzzy_player_tv_name);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            LbLanding lbLanding = getItem(getAdapterPosition());
            mFuzzySearchClickListener.onFuzzyLbClick(lbLanding.getName());

            Intent intent = new Intent(IntentActions.ACTION_FUZZY_LB_CLICK);
            intent.putExtra(BundleKeys.LB_LANDING_DATA, lbLanding);

            LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
        }
    }
}