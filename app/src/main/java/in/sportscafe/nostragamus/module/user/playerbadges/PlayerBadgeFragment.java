package in.sportscafe.nostragamus.module.user.playerbadges;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.animator.AnimationAdapter;
import in.sportscafe.nostragamus.animator.SlideInUpAnimationAdapter;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.user.badges.BadgeFragment;
import in.sportscafe.nostragamus.module.user.badges.BadgePresenter;
import in.sportscafe.nostragamus.module.user.badges.BadgePresenterImpl;
import in.sportscafe.nostragamus.module.user.badges.BadgeView;
import in.sportscafe.nostragamus.module.user.group.mutualgroups.MutualGroupsFragment;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 1/5/17.
 */

public class PlayerBadgeFragment extends NostragamusFragment implements PlayerBadgeView {

    private RecyclerView mRvBadge;

    private PlayerBadgePresenter mBadgePresenter;

    public static PlayerBadgeFragment newInstance(PlayerInfo playerInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BundleKeys.PLAYERINFO, playerInfo);

        PlayerBadgeFragment fragment = new PlayerBadgeFragment();
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
        this.mBadgePresenter = PlayerBadgePresenterImpl.newInstance(this);
        this.mBadgePresenter.onCreatePlayerBadgeAdapter(getArguments());

    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvBadge.setAdapter(getAnimationAdapter(adapter));
    }

    private AnimationAdapter getAnimationAdapter(RecyclerView.Adapter adapter) {
        SlideInUpAnimationAdapter animationAdapter = new SlideInUpAnimationAdapter(adapter);
        animationAdapter.setFirstOnly(true);
        animationAdapter.setDuration(750);
        animationAdapter.setInterpolator(new DecelerateInterpolator(1f));
        return animationAdapter;
    }

    @Override
    public void showBadgesEmpty() {
        TextView noBadges = (TextView) findViewById(R.id.no_badges);
        noBadges.setVisibility(View.VISIBLE);
    }

}
