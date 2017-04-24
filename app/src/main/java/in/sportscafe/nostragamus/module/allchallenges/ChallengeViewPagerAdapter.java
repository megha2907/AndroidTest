package in.sportscafe.nostragamus.module.allchallenges;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;

/**
 * Created by sandip on 19/04/17.
 */

public class ChallengeViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private View[] mTabs = new View[3];
    private Context mContext;

    public ChallengeViewPagerAdapter(FragmentManager manager, Context context) {
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

    public View getTabView(int position, boolean shouldShowMsgCount, int msgCount) {
        View tabView = mTabs[position];

        if (tabView == null) {
            tabView = LayoutInflater.from(mContext).inflate(R.layout.challenge_tab_layout, null);
        }

        TextView tv = (TextView) tabView.findViewById(R.id.text1);
        tv.setText(mFragmentTitleList.get(position));

        /* 'NEW' tab will always be in red irrespective of selection */
        if (tv.getText().toString().equalsIgnoreCase(Constants.ChallengeTabs.NEW)) {
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.radical_red));
        }

        if (shouldShowMsgCount && msgCount > 0) {
            TextView msgTextView = (TextView) tabView.findViewById(R.id.msgCount);
            msgTextView.setText(String.valueOf(msgCount));
            msgTextView.setVisibility(View.VISIBLE);
        }

        return tabView;
    }

}
