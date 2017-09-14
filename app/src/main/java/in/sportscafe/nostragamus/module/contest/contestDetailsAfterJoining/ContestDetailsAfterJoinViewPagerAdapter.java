package in.sportscafe.nostragamus.module.contest.contestDetailsAfterJoining;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 9/13/17.
 */

public class ContestDetailsAfterJoinViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private View[] mTabs = new View[4];
    private Context mContext;

    public ContestDetailsAfterJoinViewPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public View getTabView(int position) {
        View tabView = mTabs[position];

        if (tabView == null) {
            tabView = LayoutInflater.from(mContext).inflate(R.layout.contest_tab_layout, null);
        }

        TextView tv = (TextView) tabView.findViewById(R.id.text1);
        tv.setText(mFragmentTitleList.get(position));

        return tabView;
    }
}
