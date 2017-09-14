package in.sportscafe.nostragamus.module.customViews;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.dto.Contest;

/**
 * Created by sandip on 11/09/17.
 */

public class TimelineHelper  {



    private static View getLineView(Context context, boolean isCompleted) {
        View view = View.inflate(context, R.layout.match_timeline_line_view, null);
        if (isCompleted) {
            view.setBackgroundResource(R.drawable.games_timeline_yellow_line);
        } else {
            view.setBackgroundResource(R.drawable.games_timeline_grey_line);
        }
        return view;
    }

    private static View getNodeView(Context context, boolean isCompleted, boolean played) {
        View view = View.inflate(context, R.layout.match_timeline_node_view, null);
        if (isCompleted) {
            if (played) {
                view.setBackgroundResource(R.drawable.correct_tick_white);
            } else {
                view.setBackgroundResource(R.drawable.share_cross_icon);
            }
        } else {
            view.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.games_status_timeline_node));
        }
        return view;
    }

    public static void addNode(LinearLayout parent, boolean isMatchCompleted, boolean isPlayed, boolean isNodeWithLine) {
        if (parent != null) {
            Context context = parent.getContext();
            int nodeWidthHeight = (int)context.getResources().getDimension(R.dimen.dim_12);
            int lineWidth = (int)context.getResources().getDimension(R.dimen.dim_60);
            int lineHeight = (int)context.getResources().getDimension(R.dimen.dim_4);

            View view = getNodeView(parent.getContext(), isMatchCompleted, isPlayed);
            if (view != null) {
                parent.addView(view, parent.getChildCount(), new ViewGroup.LayoutParams(nodeWidthHeight, nodeWidthHeight));
            }

            if (isNodeWithLine) {
                view = getLineView(context, isMatchCompleted);
                if (view != null) {
                    parent.addView(view, parent.getChildCount(), new ViewGroup.LayoutParams(lineWidth, lineHeight));
                }
            }
        }
    }

    public static void addTextNode(LinearLayout parent, String title) {
        if (parent != null) {
            Context context = parent.getContext();
            int width = (int)context.getResources().getDimension(R.dimen.dim_80);

            TextView titleView = (TextView) View.inflate(context, R.layout.games_status_timeline_title, null);
            if (title != null) {
                titleView.setText(title);
                parent.addView(titleView, parent.getChildCount(), new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

        }
    }


}
