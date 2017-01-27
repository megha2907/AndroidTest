package in.sportscafe.nostragamus.module.user.badges;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;

/**
 * Created by Deepanshi on 23/8/16.
 */
public class BadgeActivity extends NostragamusActivity implements BadgeView{

    private RecyclerView mRvBadge;

    private BadgePresenter mBadgePresenter;

    private Toolbar mtoolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_badges);

        this.mRvBadge = (RecyclerView) findViewById(R.id.badge_rcv);
        this.mRvBadge.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        this.mRvBadge.setHasFixedSize(true);
        this.mBadgePresenter = BadgePresenterImpl.newInstance(this);
        this.mBadgePresenter.onCreateBadgeAdapter();
        initToolBar();

    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvBadge.setAdapter(adapter);
    }

    @Override
    public void showBadgesEmpty() {

        TextView noBadges= (TextView)findViewById(R.id.no_badges);
        noBadges.setVisibility(View.VISIBLE);
    }


    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.badge_toolbar);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoHomeActivity();
                    }
                }

        );
    }

    private void gotoHomeActivity() {
        Intent homeintent = new Intent(this, HomeActivity.class);
        homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        homeintent.putExtra("badges", "openprofile");
        startActivity(homeintent);
        finish();
    }
}