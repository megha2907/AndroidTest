package in.sportscafe.scgame.module.user.myprofile.myposition;

/**
 * Created by deepanshi on 9/21/16.
 */

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.RankSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.TourSummary;


public class MyGlobalViewChildHolder extends ChildViewHolder {

    private TextView mIngredientTextView;

    public MyGlobalViewChildHolder(View itemView) {
        super(itemView);
        mIngredientTextView = (TextView) itemView.findViewById(R.id.myglobal_tournaments_tv);
    }

    public void bind(RankSummary tourSummary) {
        mIngredientTextView.setText(tourSummary.getSportName());
    }
}
