package in.sportscafe.scgame.module.tournamentFeed;

/**
 * Created by deepanshi on 9/29/16.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.scgame.Constants.AnalyticsActions;
import in.sportscafe.scgame.Constants.BundleKeys;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.scgame.module.analytics.ScGameAnalytics;
import in.sportscafe.scgame.module.common.Adapter;
import in.sportscafe.scgame.module.feed.FeedActivity;
import in.sportscafe.scgame.module.home.OnHomeActionListener;


public class TournamentFeedAdapter extends Adapter<TournamentFeedInfo, TournamentFeedAdapter.ViewHolder> {

    private OnHomeActionListener mOnHomeActionListener;

    public TournamentFeedAdapter(Context context, OnHomeActionListener listener) {
        super(context);
        this.mOnHomeActionListener = listener;
    }

    @Override
    public TournamentFeedInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.inflater_tournament_feed_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TournamentFeedInfo tournamentInfo = getItem(position);
        holder.mPosition = position;

        holder.mTvTournamentName.setText(tournamentInfo.getTournamentName());

        holder.mTvTournamentUnplayedCount.setText(tournamentInfo.getTournamentSubtext());

        holder.mIvTournamentImage.setImageUrl(
                tournamentInfo.getTournamentPhoto(),
                Volley.getInstance().getImageLoader(),
                false
        );

    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        int mPosition;

        View mMainView;
        TextView mTvTournamentName;
        HmImageView mIvTournamentImage;
        TextView mTvTournamentUnplayedCount;


        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvTournamentName = (TextView) V.findViewById(R.id.tournament_tv_tournamentName);
            mIvTournamentImage = (HmImageView) V.findViewById(R.id.tournament_iv_tournamentImage);
            mTvTournamentUnplayedCount = (TextView) V.findViewById(R.id.tournament_btn_count_unplayed);

            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            TournamentFeedInfo tournamentInfo = getItem(getAdapterPosition());

            Integer tournamentId = tournamentInfo.getTournamentId();
            Log.i("tournamentId", tournamentInfo.getTournamentId().toString());

            Bundle bundle = new Bundle();
            bundle.putInt(BundleKeys.TOURNAMENT_ID, tournamentId);
            bundle.putString(BundleKeys.TOURNAMENT_NAME, tournamentInfo.getTournamentName());

            Intent intent =  new Intent(view.getContext(), FeedActivity.class);
            intent.putExtras(bundle);
            view.getContext().startActivity(intent);

            ScGameAnalytics.getInstance().trackFeed(AnalyticsActions.TOURNAMENT,
                    tournamentInfo.getTournamentName());
        }
    }
}