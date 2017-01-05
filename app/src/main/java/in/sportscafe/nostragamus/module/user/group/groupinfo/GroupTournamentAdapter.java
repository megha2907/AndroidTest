package in.sportscafe.nostragamus.module.user.group.groupinfo;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.home.OnHomeActionListener;

/**
 * Created by deepanshi on 11/1/16.
 */

public class GroupTournamentAdapter extends Adapter<TournamentFeedInfo, GroupTournamentAdapter.ViewHolder> {

    private OnHomeActionListener mOnHomeActionListener;

    private List<TournamentFeedInfo> mSelectedTournamentsIdList;

    private AlertDialog mAlertDialog;
    private Context mcon;

    public GroupTournamentAdapter(Context context, List<TournamentFeedInfo> SelectedTournamentsIdList) {
        super(context);
        mcon = context;
        mSelectedTournamentsIdList=SelectedTournamentsIdList;
        addAll(SelectedTournamentsIdList);
    }

    @Override
    public TournamentFeedInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public GroupTournamentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.inflater_group_sport_row, parent, false);
        return new GroupTournamentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupTournamentAdapter.ViewHolder holder, int position) {
        TournamentFeedInfo tournamentInfo = getItem(position);
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
