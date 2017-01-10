package in.sportscafe.nostragamus.module.user.otheranswers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.dto.Feed;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.Tournament;
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

        }
    }
}