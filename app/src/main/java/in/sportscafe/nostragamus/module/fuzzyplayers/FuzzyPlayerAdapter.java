package in.sportscafe.nostragamus.module.fuzzyplayers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.login.dto.BasicUserInfo;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FuzzyPlayerAdapter extends Adapter<BasicUserInfo, FuzzyPlayerAdapter.ViewHolder> {

    public FuzzyPlayerAdapter(Context context) {
        super(context);
    }

    @Override
    public BasicUserInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_fuzzy_player_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTvPlayerName.setText(getItem(position).getUserNickName());
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
            Intent intent = new Intent(IntentActions.ACTION_FUZZY_PLAYER_CLICK);
            intent.putExtra(Constants.BundleKeys.PLAYER_USER_ID, getItem(getAdapterPosition()).getId());

            LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
        }
    }
}