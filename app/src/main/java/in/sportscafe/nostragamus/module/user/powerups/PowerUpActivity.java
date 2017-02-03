package in.sportscafe.nostragamus.module.user.powerups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.popups.GetScreenNameListener;

/**
 * Created by Deepanshi on 23/8/16.
 */
public class PowerUpActivity extends NostragamusActivity implements PowerUpView,
        View.OnClickListener{

    private RecyclerView mRvPowerUp;

    private PowerUpPresenter mPowerUpPresenter;

    private Toolbar mtoolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powerup);

        this.mRvPowerUp = (RecyclerView) findViewById(R.id.powerup_rcv);
        this.mRvPowerUp.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        this.mRvPowerUp.setHasFixedSize(true);
        this.mPowerUpPresenter = PowerUpPresenterImpl.newInstance(this);
        this.mPowerUpPresenter.onCreatePowerUpAdapter();
        initToolBar();

    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvPowerUp.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.powerup_btn_earn_more:
                finish();
                break;
            case R.id.sport_selection_btn_back:
                finish();
                break;
        }
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.powerup_toolbar);
        mtoolbar.setTitle("Power Ups");

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

    @Override
    public String getScreenName() {
        return null;
    }
}