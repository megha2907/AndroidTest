package in.sportscafe.nostragamus.module.user.group.newgroup;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;

/**
 * Created by rb on 30/11/15.
 */
public class GrpTournamentSelectionAdapter extends Adapter<TournamentFeedInfo, RecyclerView.ViewHolder> {

    private List<Integer> mSelectedTournamentsIdList;

    private OnGrpTournamentChangedListener mChangedListener;

    private static final int SELECTED_LABEL_TYPE = 5;

    private static final int UNSELECTED_LABEL_TYPE = 6;

    private static final int TOURNAMENT_TYPE = 7;

    public GrpTournamentSelectionAdapter(Context context, List<Integer> selectedTournamentsIdList,
                                         OnGrpTournamentChangedListener listener) {
        super(context);
        this.mSelectedTournamentsIdList = selectedTournamentsIdList;
        this.mChangedListener = listener;
    }

    @Override
    public TournamentFeedInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
//            case SELECTED_LABEL_TYPE:
//                return new LabelViewHolder(getLayoutInflater().inflate(R.layout.inflater_group_label, parent, false),"SELECTED TOURNAMENTS");
            case UNSELECTED_LABEL_TYPE:
                return new LabelViewHolder(getLayoutInflater().inflate(R.layout.inflater_group_label, parent, false),"UNSELECTED TOURNAMENTS");
            case TOURNAMENT_TYPE:
            default:
                return new TourViewHolder(getLayoutInflater().inflate(R.layout.inflater_group_sport_row, parent, false));

        }

    }




    @Override
    public int getItemViewType(int position) {

        TournamentFeedInfo tournamentInfo = getItem(position);

//        if (tournamentInfo.getTournamentId()== -1) {
//            return SELECTED_LABEL_TYPE;
//        }

        if (tournamentInfo.getTournamentId()== -2){
            return UNSELECTED_LABEL_TYPE;
        }

        return TOURNAMENT_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rcvHolder, int position) {

        if(getItemViewType(position)==TOURNAMENT_TYPE) {
            TourViewHolder holder = (TourViewHolder) rcvHolder;
            TournamentFeedInfo tournamentInfo = getItem(position);


            holder.id = tournamentInfo.getTournamentId();
            holder.mTvTournament.setText(tournamentInfo.getTournamentName());
            holder.mTvSport.setText(tournamentInfo.getSportsName());

            Log.i("tournamentUrl", tournamentInfo.getTournamentPhoto());

            holder.mIvTournament.setImageUrl(tournamentInfo.getTournamentPhoto());


            if (mSelectedTournamentsIdList.contains((tournamentInfo.getTournamentId()))) {
                holder.mIvSelectedIcon.setBackgroundResource(R.drawable.tick_icon);
                holder.mTvTournament.setTextColor(ContextCompat.getColor(holder.mIvTournament.getContext(), R.color.btn_powerup_screen_color));
                holder.mTvTournament.setAlpha(0.9f);

            } else {
                holder.mTvTournament.setTextColor(Color.WHITE);
                holder.mIvSelectedIcon.setBackgroundResource(R.drawable.add_icon);
            }
        }

    }

    class TourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int id;

        View mMainView;

        // CheckBox mCbSport;
        TextView mTvSport;
        HmImageView mIvTournament;
        ImageView mIvSelectedIcon;
        TextView mTvTournament;
        RelativeLayout mRlTournament;

        public TourViewHolder(View V) {
            super(V);
            mMainView = V;
            mIvTournament = (HmImageView) V.findViewById(R.id.group_sport_row_image_tournament);
            mTvSport = (TextView) V.findViewById(R.id.group_sport_row_tv_sport_name);
            mIvSelectedIcon = (ImageView) V.findViewById(R.id.group_sport_row_selected_icon);
            mTvTournament = (TextView) V.findViewById(R.id.group_sport_row_tv_tournament_name);
            mRlTournament = (RelativeLayout)V.findViewById(R.id.group_sport_row_rl);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mChangedListener.onGrpTournamentClicked(position, mSelectedTournamentsIdList.contains(getItem(position).getTournamentId()));
        }
    }

    class LabelViewHolder extends RecyclerView.ViewHolder {

        TextView mTvName;

        public LabelViewHolder(View V, String label) {
            super(V);

            mTvName = (TextView) V.findViewById(R.id.group_label_tv);
            mTvName.setText(label);

        }

    }

    public List<Integer> getSelectedTournamentList() {
        return mSelectedTournamentsIdList;
    }

    public interface OnGrpTournamentChangedListener {

        void onGrpTournamentClicked(int position, boolean selected);
    }

    public void updateSelectionList(TournamentFeedInfo feedInfo) {
        Integer tournamentId = feedInfo.getTournamentId();
        if (mSelectedTournamentsIdList.contains(tournamentId)) {
            mSelectedTournamentsIdList.remove(tournamentId);
        } else {
            mSelectedTournamentsIdList.add(tournamentId);
        }
    }

}