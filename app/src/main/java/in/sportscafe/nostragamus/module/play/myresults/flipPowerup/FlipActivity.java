package in.sportscafe.nostragamus.module.play.myresults.flipPowerup;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;

/**
 * Created by deepanshi on 12/20/16.
 */

public class FlipActivity extends NostragamusActivity implements FlipView {

    private RecyclerView mRvFlip;

    private FlipPresenter mFlipPresenter;

    private Toolbar mtoolbar;
    private TextView mTitle;
    private boolean goback = false;
    private Bundle mbundle;
    private Match match;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip);

        initToolBar();

        this.mRvFlip = (RecyclerView) findViewById(R.id.flip_rv);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        this.mRvFlip.setLayoutManager(linearLayoutManager);
        this.mRvFlip.setHasFixedSize(true);
        this.mFlipPresenter = FlipPresenterImpl.newInstance(this);
        this.mFlipPresenter.onCreateFlip(getIntent().getExtras());

        mbundle = getIntent().getExtras();
        match = Parcels.unwrap(mbundle.getParcelable(BundleKeys.MATCH_LIST));
    }

    @Override
    public void setAdapter(FlipAdapter flipAdapter) {
        mRvFlip.setAdapter(flipAdapter);
    }

    public void initToolBar() {
        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf");
        mtoolbar = (Toolbar) findViewById(R.id.flip_toolbar);
        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);

        Integer numberofFlipPowerUps = NostragamusDataHandler.getInstance().getNumberofFlipPowerups();

        if(numberofFlipPowerUps==1){
            mTitle.setText(numberofFlipPowerUps + " Flip");
        }else {
            mTitle.setText(numberofFlipPowerUps+ " Flips");
        }

        mTitle.setTypeface(tftitle);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            gotoMyResults(match);
                    }
                }
        );
    }

    private void gotoMyResults(Match match) {
        Intent intent = new Intent(this, MyResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.MATCH_LIST, Parcels.wrap(match));
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void decreasePowerupCount() {

        NostragamusDataHandler.getInstance().setNumberofFlipPowerups(NostragamusDataHandler.getInstance().getNumberofFlipPowerups()-1);
        Integer numberofFlipPowerUps = NostragamusDataHandler.getInstance().getNumberofFlipPowerups();

        if(numberofFlipPowerUps==1){
            mTitle.setText(numberofFlipPowerUps + " Flip Allowed");

        }else if(numberofFlipPowerUps==0){
            mTitle.setText(numberofFlipPowerUps+ "Flip Allowed");
            Toast toast = Toast.makeText(this,Constants.Alerts.FLIP_POWERUP_OVER, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            gotoMyResults(match);
        }else {
            mTitle.setText(numberofFlipPowerUps+ " Flips Allowed");
        }


    }

}