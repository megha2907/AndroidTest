package in.sportscafe.nostragamus.module.user.myprofile.myposition.groups;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.BaseSummary;

/**
 * Created by deepanshi on 10/13/16.
 */

public class GroupsViewParentHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    private final ImageView mArrowExpandImageView;
    private TextView mGroupNameTextView;
    private TextView mGroupsOverallRankTextView;
    private ImageView mGroupsOverallRankStatus;

    public GroupsViewParentHolder(View itemView) {
        super(itemView);
        mGroupNameTextView = (TextView) itemView.findViewById(R.id.group_tv_name);
        mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.arrow_expand_imageview);
        mGroupsOverallRankTextView = (TextView) itemView.findViewById(R.id.group_tv_overall_rank);
        mGroupsOverallRankStatus = (ImageView) itemView.findViewById(R.id.group_iv_rank_status);

    }

    public void bind(BaseSummary tourSummary) {

        mGroupNameTextView.setText(tourSummary.getName());

        if(null == tourSummary.getOverallRankChange()) {
            mGroupsOverallRankStatus.setVisibility(View.INVISIBLE);
        } else if(tourSummary.getRankChange() < 0) {
            mGroupsOverallRankStatus.setImageResource(R.drawable.status_arrow_down);
        } else {
            mGroupsOverallRankStatus.setImageResource(R.drawable.status_arrow_up);
        }

        if(null == tourSummary.getRank()) {
            mGroupsOverallRankTextView.setText("-");
        } else {
            String rank = AppSnippet.ordinal(tourSummary.getRank());
            mGroupsOverallRankTextView.setText(rank);
        }

    }

    @SuppressLint("NewApi")
    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (expanded) {
                mArrowExpandImageView.setRotation(ROTATED_POSITION);
            } else {
                mArrowExpandImageView.setRotation(INITIAL_POSITION);
            }
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            mArrowExpandImageView.startAnimation(rotateAnimation);
        }
    }
}

