package in.sportscafe.scgame.module.user.badges;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;

/**
 * Created by Deepanshi on 23/8/16.
 */
public class BadgeActivity extends ScGameActivity implements BadgeView{

    private RecyclerView mRvBadge;

    private BadgePresenter mBadgePresenter;

    private Toolbar mtoolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);

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
        mtoolbar.setTitle("Badges");
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }

        );
    }
}