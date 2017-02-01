package in.sportscafe.nostragamus.module.user.lblanding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;

/**
 * Created by deepanshi on 14/07/16.
 */
public class LBLandingAdapter extends Adapter<LbLanding, LBLandingAdapter.MyViewHolder> {

    private String mLbLandingType;

    private boolean mNeedPadding = true;

    public LBLandingAdapter(Context context, String lbLandingType, boolean needPadding) {
        super(context);
        this.mLbLandingType = lbLandingType;
        this.mNeedPadding = needPadding;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(getLayoutInflater().inflate(R.layout.inflater_lb_summary, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LbLanding lbLandingDto = getItem(position);

        if (mLbLandingType== Constants.LBLandingType.SPORT){
            holder.ivImage.setAlpha(0.1F);
        }

        holder.ivImage.setImageUrl(lbLandingDto.getImgUrl());

        Integer rank = lbLandingDto.getRank();
        if (null != rank) {
            holder.tvRank.setText(AppSnippet.ordinal(rank));
        } else {
            holder.tvRank.setVisibility(View.GONE);
            holder.tvPosTxt.setText("NOT\nPLAYED");
        }

        holder.tvName.setText(lbLandingDto.getName());
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        HmImageView ivImage;

        TextView tvRank;

        TextView tvName;

        TextView tvPosTxt;

        public MyViewHolder(View view) {
            super(view);
            ivImage = (HmImageView) view.findViewById(R.id.lb_summary_item_iv);
            tvRank = (TextView) view.findViewById(R.id.lb_summary_item_rank_tv);
            tvName = (TextView) view.findViewById(R.id.lb_summary_item_name_tv);
            tvPosTxt= (TextView) view.findViewById(R.id.lb_summary_item_pos_tv);

            if (!mNeedPadding) {
                ivImage.setPadding(0, 0, 0, 0);
            }

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString(BundleKeys.LB_LANDING_TYPE, mLbLandingType);
            bundle.putParcelable(BundleKeys.LB_LANDING_DATA, Parcels.wrap(getItem(getAdapterPosition())));

            navigateToPointsActivity(view.getContext(), bundle);
        }

        private void navigateToPointsActivity(Context context, Bundle bundle) {
            Intent intent = new Intent(context, PointsActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

    }
}