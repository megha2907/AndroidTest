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
import com.squareup.picasso.Picasso;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;

/**
 * Created by rb on 30/11/15.
 */
public class GrpTournamentSelectionAdapter extends Adapter<TournamentFeedInfo, GrpTournamentSelectionAdapter.ViewHolder> {

    private Context mcontext;

    private List<Integer> mSelectedTournamentsIdList;

    private List<Integer> mUnSelectedTournamentsIdList;

    private OnGrpTournamentChangedListener mChangedListener;

    public GrpTournamentSelectionAdapter(Context context, List<Integer> SelectedTournamentsIdList,List<Integer> UnSelectedTournamentsIdList) {
        super(context);
        mcontext=context;
        this.mSelectedTournamentsIdList = SelectedTournamentsIdList;
        this.mUnSelectedTournamentsIdList = UnSelectedTournamentsIdList;
    }

    public GrpTournamentSelectionAdapter(Context context, List<Integer> SelectedTournamentsIdList,List<Integer> UnSelectedTournamentsIdList,
                                         OnGrpTournamentChangedListener listener) {
        super(context);
        mcontext=context;
        this.mSelectedTournamentsIdList = SelectedTournamentsIdList;
        this.mUnSelectedTournamentsIdList=UnSelectedTournamentsIdList;
        this.mChangedListener = listener;
    }

    @Override
    public TournamentFeedInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_group_sport_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TournamentFeedInfo tournamentInfo = getItem(position);

//        for (Sport sport: ScGameDataHandler.getInstance().getAllSports()){
//
//            if (sport.getName().equals(tournamentInfo.getSportsName())){
//                holder.mIvSport.setBackgroundResource(sport.getImageUrl());
//            }
//        }

        Log.i("isnide","bindviewholder");

        holder.id = tournamentInfo.getTournamentId();
        holder.mTvTournament.setText(tournamentInfo.getTournamentName());
        holder.mTvSport.setText(tournamentInfo.getSportsName());


        Log.i("tournamentUrl",tournamentInfo.getTournamentPhoto());

//        Picasso.with(mcontext)
//                .load(tournamentInfo.getTournamentPhoto())
//                .placeholder(R.drawable.placeholder_icon)
//                .into(holder.mIvTournament);

        holder.mIvTournament.setImageUrl(tournamentInfo.getTournamentPhoto());


        if (mSelectedTournamentsIdList.contains((tournamentInfo.getTournamentId()))) {
             holder.mIvSelectedIcon.setBackgroundResource(R.drawable.tick_icon);
             holder.mTvTournament.setTextColor(mcontext.getResources().getColor(R.color.btn_powerup_screen_color));
             holder.mTvTournament.setAlpha((float) 0.9);

        } else {
            holder.mTvTournament.setTextColor(Color.WHITE);
            holder.mIvSelectedIcon.setBackgroundResource(R.drawable.add_icon);
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

            if (null == mUnSelectedTournamentsIdList) {
                if (null == mChangedListener
                        || mChangedListener.onGrpTournamentSelected(!removeOldTournament, mSelectedTournamentsIdList.size())) {
                    if (removeOldTournament) {
                        mSelectedTournamentsIdList.remove(mSelectedTournamentsIdList.indexOf(id));

                    } else {
                        mSelectedTournamentsIdList.add(id);
                    }
                    notifyItemChanged(getAdapterPosition());

                    if (null != mChangedListener) {
                        mChangedListener.onGrpTournamentChanged(mSelectedTournamentsIdList);
                    }
                }
            }else {

                if (removeOldTournament){
                    mChangedListener.removeSelectedTournament(getAdapterPosition());
                }else {
                    mChangedListener.addSelectedTournament(getAdapterPosition());
                }


            }
        }
    }

    public List<Integer> getSelectedTournamentList() {
        return mSelectedTournamentsIdList;
    }

    public List<Integer> getUnSelectedTournamentList() {
        return mUnSelectedTournamentsIdList;
    }

    public interface OnGrpTournamentChangedListener {

        boolean onGrpTournamentSelected(boolean addNewTournament, int existingTournamentCount);

        void onGrpTournamentChanged(List<Integer> selectedTournamentsIdList);

        void removeSelectedTournament(int adapterPosition);

        void addSelectedTournament(int adapterPosition);
    }

    public void addTournament(int adapterPosition){
        mUnSelectedTournamentsIdList.add(adapterPosition, 0);
        notifyItemInserted(adapterPosition);
    }

    public void removeTournament(int adapterPosition){
        mSelectedTournamentsIdList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }


}