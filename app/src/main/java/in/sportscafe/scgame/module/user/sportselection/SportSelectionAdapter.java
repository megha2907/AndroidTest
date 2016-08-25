package in.sportscafe.scgame.module.user.sportselection;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.Adapter;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;

/**
 * Created by rb on 30/11/15.
 */
public class SportSelectionAdapter extends Adapter<Sport, SportSelectionAdapter.ViewHolder> {

    private List<Integer> mSelectedSportsIdList;

    public SportSelectionAdapter(Context context, List<Integer> selectedSportsIdList) {
        super(context);
        this.mSelectedSportsIdList = selectedSportsIdList;
    }

    @Override
    public Sport getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_sport_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sport sport = getItem(position);

        holder.id = sport.getId();
        holder.mTvSport.setText(sport.getName());

        if (mSelectedSportsIdList.contains((sport.getId()))) {
            holder.mMainView.setBackgroundResource(R.drawable.sports_selection_shape);
            holder.mTvSport.setAlpha((float) 0.9);
            holder.mIvSport.setImageResource(sport.getSelectedImageResource());
        } else {
            holder.mMainView.setBackgroundColor(Color.TRANSPARENT);
            holder.mIvSport.setImageResource(sport.getImageResource());
        }



    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int id;

        View mMainView;

        ImageView mIvSport;

        TextView mTvSport;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mIvSport = (ImageView) V.findViewById(R.id.sport_row_iv_sport_image);
            mTvSport = (TextView) V.findViewById(R.id.sport_row_tv_sport_name);

            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mSelectedSportsIdList.contains(id)) {
                mSelectedSportsIdList.remove(mSelectedSportsIdList.indexOf(id));
            } else {
                mSelectedSportsIdList.add(id);
            }
            notifyDataSetChanged();
        }


    }

    public List<Integer> getSelectedSportList() {
        return mSelectedSportsIdList;
    }
}