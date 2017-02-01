package in.sportscafe.nostragamus.module.fuzzylbs;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.AppSnippet;
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
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_fuzzy_lb_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LbLanding lbLanding = getItem(position);
        holder.mTvName.setText(lbLanding.getName());
        holder.mTvCategory.setText(AppSnippet.capitalize(lbLanding.getType()));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvName;

        TextView mTvCategory;

        public ViewHolder(View V) {
            super(V);

            mTvName = (TextView) V.findViewById(R.id.fuzzy_lb_tv_name);
            mTvCategory = (TextView) V.findViewById(R.id.fuzzy_lb_tv_category);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            LbLanding lbLanding = getItem(getAdapterPosition());
            mFuzzySearchClickListener.onFuzzyLbClick(lbLanding.getName());

            Intent intent = new Intent(IntentActions.ACTION_FUZZY_LB_CLICK);
            intent.putExtra(BundleKeys.LB_LANDING_DATA, Parcels.wrap(lbLanding));
            intent.putExtra(BundleKeys.LB_LANDING_KEY, lbLanding.getName());

            LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
        }
    }
}