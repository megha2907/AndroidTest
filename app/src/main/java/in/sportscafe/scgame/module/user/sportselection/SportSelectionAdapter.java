package in.sportscafe.scgame.module.user.sportselection;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.Adapter;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;

import static in.sportscafe.scgame.R.color.dusty_gray;

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
        holder.mIvSport.setImageResource(sport.getImageResource());

        holder.mMainView.setSelected(mSelectedSportsIdList.contains(sport.getId()));

        if (sport.getState() == 1) {
            holder.mMainView.setBackgroundResource(R.drawable.sports_selection_shape);
            holder.mTvSport.setTypeface(null, Typeface.BOLD);
            holder.mIvSport.setImageResource(sport.getSelectedImageResource());
        } else {
            holder.mMainView.setBackgroundColor(Color.TRANSPARENT);
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
            mIvSport.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mSelectedSportsIdList.contains(id)) {
                mSelectedSportsIdList.remove(mSelectedSportsIdList.indexOf(id));
            } else {
                mSelectedSportsIdList.add(id);
                Sport sport = getItem(getAdapterPosition());
                sport.setState(1);

            }
            notifyDataSetChanged();
        }



    }

    public List<Integer> getSelectedSportList() {
        return mSelectedSportsIdList;
    }
}