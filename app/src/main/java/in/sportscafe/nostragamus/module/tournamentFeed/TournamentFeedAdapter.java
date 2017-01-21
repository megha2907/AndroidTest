package in.sportscafe.nostragamus.module.tournamentFeed;

/**
 * Created by deepanshi on 9/29/16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.FeedActivity;
import in.sportscafe.nostragamus.module.home.OnHomeActionListener;


public class TournamentFeedAdapter extends Adapter<TournamentFeedInfo, TournamentFeedAdapter.ViewHolder> {

    private OnHomeActionListener mOnHomeActionListener;

    private  SharedPreferences mNotificationPreferences;

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

        mNotificationPreferences = holder.mMainView.getContext().getSharedPreferences("TOURNAMENTS", 0);

        SharedPreferences.Editor editor = mNotificationPreferences.edit();

        if (NostragamusDataHandler.getInstance().isFirstTimeUser()) {
            editor.putInt("7", 7);
            editor.commit();
        }

        try {
        holder.mTvTournamentName.setText(tournamentInfo.getTournamentName());

        if (!tournamentInfo.getCountsUnplayed().equals("0")){
            holder.mBtnTournamentUnplayedCount.setVisibility(View.VISIBLE);
            holder.mBtnTournamentUnplayedCount.setText(tournamentInfo.getCountsUnplayed());
            holder.mBtnTournamentUnplayed.setVisibility(View.VISIBLE);
        }else {
            holder.mBtnTournamentUnplayedCount.setVisibility(View.GONE);
            holder.mBtnTournamentUnplayed.setVisibility(View.GONE);
        }

        holder.mIvTournamentImage.setImageUrl(
                tournamentInfo.getTournamentPhoto()
        );

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (mNotificationPreferences.contains(String.valueOf(tournamentInfo.getTournamentId())))
        {
            holder.mIvNotificationIcon.setBackgroundResource(R.drawable.tournament_bell_green_icon);
            holder.mTvTournamentNotification.setText("Notifications:ON");
        }else {
            holder.mIvNotificationIcon.setBackgroundResource(R.drawable.tournament_bell_grey_icon);
            holder.mTvTournamentNotification.setText("Notifications:OFF");
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        int mPosition;

        View mMainView;
        TextView mTvTournamentName;
        HmImageView mIvTournamentImage;
        TextView mTvTournamentNotification;
        Button mBtnTournamentUnplayedCount;
        Button mBtnTournamentUnplayed;
        ImageButton mIvNotificationIcon;


        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvTournamentName = (TextView) V.findViewById(R.id.tournament_tv_tournamentName);
            mIvTournamentImage = (HmImageView) V.findViewById(R.id.tournament_iv_tournamentImage);
            mTvTournamentNotification = (TextView) V.findViewById(R.id.tournament_tv_notification);
            mBtnTournamentUnplayedCount = (Button)V.findViewById(R.id.tournament_btn_unplayed_count);
            mBtnTournamentUnplayed = (Button)V.findViewById(R.id.tournament_btn_unplayed_btn);
            mIvNotificationIcon=(ImageButton) V.findViewById(R.id.tournament_iv_notification_icon);

            V.setOnClickListener(this);
            mBtnTournamentUnplayed.setOnClickListener(this);
            mIvNotificationIcon.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {


                   case R.id.tournament_iv_notification_icon:

                       TournamentFeedInfo tournamentInfo1 = getItem(getAdapterPosition());
                       SharedPreferences.Editor editor = mNotificationPreferences.edit();

                       if (mNotificationPreferences.contains(String.valueOf(tournamentInfo1.getTournamentId()))){
                           NostragamusDataHandler.getInstance().setFirstTimeUser(false);
                           editor.remove(String.valueOf(tournamentInfo1.getTournamentId()));
                           editor.commit();
                           notifyItemChanged(getAdapterPosition());
                       }else {
                           editor.putInt(String.valueOf(tournamentInfo1.getTournamentId()),tournamentInfo1.getTournamentId());
                           editor.commit();
                           notifyItemChanged(getAdapterPosition());
                       }

                       break;

                    default:

                        TournamentFeedInfo tournamentInfo = getItem(getAdapterPosition());
                        Integer tournamentId = tournamentInfo.getTournamentId();
                        Bundle bundle = new Bundle();
                        bundle.putInt(BundleKeys.TOURNAMENT_ID, tournamentId);
                        bundle.putString(BundleKeys.TOURNAMENT_NAME, tournamentInfo.getTournamentName());
                        bundle.putString(BundleKeys.SPORT_NAME, tournamentInfo.getSportsName());

                        Intent intent =  new Intent(view.getContext(), FeedActivity.class);
                        intent.putExtras(bundle);
                        view.getContext().startActivity(intent);

                        NostragamusAnalytics.getInstance().trackFeed(AnalyticsActions.TOURNAMENT,
                                tournamentInfo.getTournamentName());

            }

        }

    }
}