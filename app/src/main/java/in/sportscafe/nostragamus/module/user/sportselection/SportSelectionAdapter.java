package in.sportscafe.nostragamus.module.user.sportselection;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;

/**
 * Created by rb on 30/11/15.
 */
public class SportSelectionAdapter extends Adapter<Sport, SportSelectionAdapter.ViewHolder> {

    private List<Integer> mSelectedSportsIdList;
    private Context mContext;

    public SportSelectionAdapter(Context context, List<Integer> selectedSportsIdList) {
        super(context);
        mContext=context;
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

        if (mSelectedSportsIdList.contains((sport.getId())))
        {
            holder.mTvSport.setTextColor(ContextCompat.getColor(mContext, R.color.btn_powerup_screen_color));
            holder.mIvSport.setImageUrl(sport.getSelectedImageUrl());
            holder.mRlSport.setBackgroundResource(R.drawable.sport_colored_card_bg);

        } else {
            holder.mIvSport.setImageUrl(sport.getImageUrl());
            holder.mTvSport.setTextColor(ContextCompat.getColor(mContext, R.color.textcolorlight));
            holder.mRlSport.setBackgroundResource(R.drawable.card_bg);
        }



    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int id;

        View mMainView;

        RelativeLayout mRlSport;

        HmImageView mIvSport;

        TextView mTvSport;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mRlSport=(RelativeLayout) V.findViewById(R.id.sport_row_rl);
            mIvSport = (HmImageView) V.findViewById(R.id.sport_row_iv_sport_image);
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