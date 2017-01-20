package in.sportscafe.nostragamus.module.user.lblanding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.TourSummary;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;

/**
 * Created by deepanshi on 14/07/16.
 */
public class LBLandingAdapter extends Adapter<LBLanding, LBLandingAdapter.MyViewHolder> {

    private boolean mNeedPadding = true;

    public LBLandingAdapter(Context context, boolean needPadding) {
        super(context);
        this.mNeedPadding = needPadding;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(getLayoutInflater().inflate(R.layout.inflater_lb_summary, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LBLanding lbLandingDto = getItem(position);

        holder.ivImage.setImageUrl(lbLandingDto.getImgUrl());

        Integer rank = lbLandingDto.getRank();
        if (null != rank) {
            holder.tvRank.setText(AppSnippet.ordinal(rank));
        } else {
            holder.tvRank.setText("-");
        }

        holder.tvName.setText(lbLandingDto.getName());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        HmImageView ivImage;

        TextView tvRank;

        TextView tvName;

        public MyViewHolder(View view) {
            super(view);
            ivImage = (HmImageView) view.findViewById(R.id.lb_summary_item_iv);
            tvRank = (TextView) view.findViewById(R.id.lb_summary_item_rank_tv);
            tvName = (TextView) view.findViewById(R.id.lb_summary_item_name_tv);

            if(!mNeedPadding) {
                ivImage.setPadding(0, 0, 0, 0);
            }
        }

    }
}