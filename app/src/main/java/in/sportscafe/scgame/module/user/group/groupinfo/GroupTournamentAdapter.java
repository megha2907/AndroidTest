package in.sportscafe.scgame.module.user.group.groupinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.TournamentFeed.TournamentFeedAdapter;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.common.Adapter;
import in.sportscafe.scgame.module.feed.FeedActivity;
import in.sportscafe.scgame.module.home.OnHomeActionListener;

/**
 * Created by deepanshi on 11/1/16.
 */

public class GroupTournamentAdapter extends Adapter<TournamentInfo, GroupTournamentAdapter.ViewHolder> {

    private OnHomeActionListener mOnHomeActionListener;

    private List<TournamentInfo> mSelectedTournamentsIdList;

    private AlertDialog mAlertDialog;
    private Context mcon;

    public GroupTournamentAdapter(Context context, List<TournamentInfo> SelectedTournamentsIdList) {
        super(context);
        mcon = context;
        mSelectedTournamentsIdList=SelectedTournamentsIdList;
        addAll(SelectedTournamentsIdList);
    }

    @Override
    public TournamentInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public GroupTournamentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.inflater_group_sport_row, parent, false);
        return new GroupTournamentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupTournamentAdapter.ViewHolder holder, int position) {
        TournamentInfo tournamentInfo = getItem(position);
        holder.mPosition = position;
        holder.mTvTournamentName.setText(tournamentInfo.getTournamentName());
        Picasso.with(mcon)
                .load(tournamentInfo.getTournamentPhoto())
                .placeholder(R.drawable.placeholder_icon)
                .into(holder.mIvTournamentImage);

        holder.mTvSport.setText(tournamentInfo.getSportsName());

    }


    class ViewHolder extends RecyclerView.ViewHolder  {


        int mPosition;

        View mMainView;
        TextView mTvTournamentName;
        ImageView mIvTournamentImage;
        TextView mTvSport;


        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvTournamentName = (TextView) V.findViewById(R.id.group_sport_row_tv_tournament_name);
            mIvTournamentImage = (ImageView) V.findViewById(R.id.group_sport_row_image_tournament);
            mTvSport = (TextView) V.findViewById(R.id.group_sport_row_tv_sport_name);

        }


    }


}
