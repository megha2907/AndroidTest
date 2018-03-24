package in.sportscafe.nostragamus.module.privateContest.ui.joinWithInviteCode;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinPrivateContestWithInviteCodeFragment extends BaseFragment {

    private static final String TAG = JoinPrivateContestWithInviteCodeFragment.class.getSimpleName();

    public JoinPrivateContestWithInviteCodeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_private_contest_with_invite_code, container, false);
        initViews(view);
        return view;
    }



    private void initViews(View view) {

    }

}
