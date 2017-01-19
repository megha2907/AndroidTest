package in.sportscafe.nostragamus.module.user.lblanding;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.SportSummary;

/**
 * Created by deepanshi on 14/07/16.
 */
public class LBSportsAdapter extends RecyclerView.Adapter<LBSportsAdapter.MyViewHolder> {

    private List<SportSummary> msportSummaryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public HmImageView lbImage;
        public TextView lbRank;
        public TextView lbName;

        public MyViewHolder(View view) {
            super(view);
            lbImage = (HmImageView) view.findViewById(R.id.lb_summary_item_iv);
            lbRank = (TextView) view.findViewById(R.id.lb_summary_item_rank_tv);
            lbName = (TextView) view.findViewById(R.id.lb_summary_item_name_tv);

        }
    }


    public LBSportsAdapter(List<SportSummary> sportSummaryList) {
        this.msportSummaryList = sportSummaryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflater_lb_summary, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        SportSummary sportSummary = msportSummaryList.get(position);

        //holder.lbImage.setImageUrl(sportSummary.);

        if (null!=sportSummary.getOverallRank()) {
            holder.lbRank.setText(sportSummary.getOverallRank().toString());
        }else {
            holder.lbRank.setText("-");
        }
        holder.lbName.setText(sportSummary.getSportsName());


    }

    @Override
    public int getItemCount() {
        return msportSummaryList.size();
    }
}
