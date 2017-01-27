package in.sportscafe.nostragamus.module.user.playerbadges;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.utils.ViewUtils;

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
        this.mRvBadge.setAdapter(adapter);
    }

    @Override
    public void showBadgesEmpty() {
        TextView noBadges = (TextView) findViewById(R.id.no_badges);
        noBadges.setVisibility(View.VISIBLE);
    }

}
