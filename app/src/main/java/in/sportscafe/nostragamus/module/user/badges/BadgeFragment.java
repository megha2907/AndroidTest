package in.sportscafe.nostragamus.module.user.badges;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

/**
 * Created by deepanshi on 12/8/16.
 */

public class BadgeFragment  extends NostragamusFragment implements BadgeView {

    private RecyclerView mRvBadge;

    private BadgePresenter mBadgePresenter;

    public static BadgeFragment newInstance(List<Badge> badgeList) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.BADGES, Parcels.wrap(badgeList));

        BadgeFragment fragment = new BadgeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_badges, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRvBadge = (RecyclerView) findViewById(R.id.badge_rcv);
        this.mRvBadge.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRvBadge.setHasFixedSize(true);

        this.mBadgePresenter = BadgePresenterImpl.newInstance(this);
        this.mBadgePresenter.onCreateBadges(getArguments());
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvBadge.setAdapter(adapter);
    }

}