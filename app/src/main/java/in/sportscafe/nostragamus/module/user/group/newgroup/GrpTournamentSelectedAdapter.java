package in.sportscafe.nostragamus.module.user.group.newgroup;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;

/**
 * Created by deepanshi on 1/9/17.
 */

public class GrpTournamentSelectedAdapter extends Adapter<TournamentFeedInfo, GrpTournamentSelectedAdapter.ViewHolder> {

    private Context mcontext;

    private List<Integer> mSelectedTournamentsIdList;

    private GrpTournamentSelectedAdapter.OnGrpTournamentChangedListener mChangedListener;

    public GrpTournamentSelectedAdapter(Context context, List<Integer> SelectedTournamentsIdList) {
        super(context);
        mcontext=context;
        this.mSelectedTournamentsIdList = SelectedTournamentsIdList;
    }

    public GrpTournamentSelectedAdapter(Context context, List<Integer> SelectedTournamentsIdList,
                                        GrpTournamentSelectedAdapter.OnGrpTournamentChangedListener listener) {
        super(context);
        mcontext=context;
        this.mSelectedTournamentsIdList = SelectedTournamentsIdList;
        this.mChangedListener = listener;
    }

    @Override
    public TournamentFeedInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public GrpTournamentSelectedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GrpTournamentSelectedAdapter.ViewHolder(getLayoutInflater().inflate(R.layout.inflater_group_sport_row, parent, false));
    }

    @Override
    public void onBindViewHolder(GrpTournamentSelectedAdapter.ViewHolder holder, int position) {
        TournamentFeedInfo tournamentInfo = getItem(position);

        holder.id = tournamentInfo.getTournamentId();
        holder.mTvTournament.setText(tournamentInfo.getTournamentName());
        holder.mTvSport.setText(tournamentInfo.getSportsName());


        holder.mIvTournament.setImageUrl(tournamentInfo.getTournamentPhoto());


        if (mSelectedTournamentsIdList.contains((tournamentInfo.getTournamentId()))) {
            holder.mIvSelectedIcon.setBackgroundResource(R.drawable.tick_icon);
            holder.mTvTournament.setAlpha((float) 0.9);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int id;

        View mMainView;

        // CheckBox mCbSport;
        TextView mTvSport;
        HmImageView mIvTournament;
        ImageView mIvSelectedIcon;
        TextView mTvTournament;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mIvTournament = (HmImageView) V.findViewById(R.id.group_sport_row_image_tournament);
            mTvSport = (TextView) V.findViewById(R.id.group_sport_row_tv_sport_name);
            mIvSelectedIcon = (ImageView) V.findViewById(R.id.group_sport_row_selected_icon);
            mTvTournament = (TextView) V.findViewById(R.id.group_sport_row_tv_tournament_name);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            boolean removeOldTournament = mSelectedTournamentsIdList.contains(id);
            if(null == mChangedListener
                    || mChangedListener.onGrpTournamentSelected(!removeOldTournament, mSelectedTournamentsIdList.size())) {
               if (removeOldTournament) {
                    mSelectedTournamentsIdList.remove(mSelectedTournamentsIdList.indexOf(id));
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(),mSelectedTournamentsIdList.size());

               }
               else {
                    mSelectedTournamentsIdList.add(id);
                }
                notifyItemChanged(getAdapterPosition());

                if(null != mChangedListener) {
                    mChangedListener.onGrpTournamentChanged(mSelectedTournamentsIdList);
                }
            }
        }
    }

    public List<Integer> getSelectedTournamentList() {
        return mSelectedTournamentsIdList;
    }

    public interface OnGrpTournamentChangedListener {

        boolean onGrpTournamentSelected(boolean addNewTournament, int existingTournamentCount);

        void onGrpTournamentChanged(List<Integer> selectedTournamentsIdList);
    }
}