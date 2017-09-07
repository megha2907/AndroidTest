package in.sportscafe.nostragamus.module.contest.ui.viewPager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.dto.ContestType;

/**
 * Created by sandip on 01/09/17.
 */

public class ContestViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<ContestViewPagerFragment> mContestFragmentList;

    public ContestViewPagerAdapter(FragmentManager fm, ArrayList<ContestViewPagerFragment> fragments) {
        super(fm);
        mContestFragmentList = fragments;
    }

    @Override
    public int getCount() {
        return (mContestFragmentList != null) ? mContestFragmentList.size() : 0;
    }

    @Override
    public Fragment getItem(int position) {
        if (mContestFragmentList != null) {
            return mContestFragmentList.get(position);
        }
        return null;
    }

    public View getTabView(Context context, int position) {
        if (mContestFragmentList != null && mContestFragmentList.size() > position) {
            ContestType contestType = mContestFragmentList.get(position).getContestType();
            if (contestType != null) {
                LinearLayout parentLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.contest_tab, null);

                TextView contestCountTextView = (TextView) parentLayout.findViewById(R.id.contest_tab_count_textView);
                contestCountTextView.setText(String.valueOf(contestType.getContestCount()));

                TextView contestTypeTextView = (TextView) parentLayout.findViewById(R.id.contest_type_tab_textView);
                contestTypeTextView.setText(contestType.getName());

                return parentLayout;
            }
        }
        return null;
    }
}
