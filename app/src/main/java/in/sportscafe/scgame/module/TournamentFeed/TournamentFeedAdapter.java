package in.sportscafe.scgame.module.TournamentFeed;

/**
 * Created by deepanshi on 9/29/16.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.common.Adapter;
import in.sportscafe.scgame.module.common.Settings;
import in.sportscafe.scgame.module.feed.FeedActivity;
import in.sportscafe.scgame.module.home.OnHomeActionListener;


public class TournamentFeedAdapter extends Adapter<TournamentInfo, TournamentFeedAdapter.ViewHolder> {

    private OnHomeActionListener mOnHomeActionListener;

    private AlertDialog mAlertDialog;
    private Context mcon;

    public TournamentFeedAdapter(Context context, OnHomeActionListener listener) {
        super(context);
        mcon = context;
        this.mOnHomeActionListener = listener;
    }

    @Override
    public TournamentInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public TournamentFeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.inflater_tournament_feed_row, parent, false);
        return new TournamentFeedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TournamentFeedAdapter.ViewHolder holder, int position) {
        TournamentInfo tournamentInfo = getItem(position);
        holder.mPosition = position;

        holder.mTvTournamentName.setText(tournamentInfo.getTournamentName());

        if (tournamentInfo.getCountsUnplayed().equals("0")){

            holder.mBtnTournamentUnplayedCount.setVisibility(View.GONE);
        }
        else
        {
            holder.mBtnTournamentUnplayedCount.setText(tournamentInfo.getCountsUnplayed());
        }


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
        CustomButton mBtnTournamentUnplayedCount;


        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvTournamentName = (TextView) V.findViewById(R.id.tournament_tv_tournamentName);
            mIvTournamentImage = (HmImageView) V.findViewById(R.id.tournament_iv_tournamentImage);
            mBtnTournamentUnplayedCount = (CustomButton) V.findViewById(R.id.tournament_btn_count_unplayed);

            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            TournamentInfo tournamentInfo = getItem(getAdapterPosition());

            Integer tournamentId = tournamentInfo.getTournamentId();
            Log.i("tournamentid", tournamentInfo.getTournamentId().toString());

            Intent intent =  new Intent(mcon, FeedActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putInt(Constants.BundleKeys.TOURNAMENT_ID, tournamentId);
            mBundle.putString(Constants.BundleKeys.TOURNAMENT_NAME, tournamentInfo.getTournamentName());
            intent.putExtras(mBundle);
            mcon.startActivity(intent);

        }

    }



}
