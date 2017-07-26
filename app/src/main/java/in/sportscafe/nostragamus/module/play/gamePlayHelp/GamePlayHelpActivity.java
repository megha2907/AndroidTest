package in.sportscafe.nostragamus.module.play.gamePlayHelp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.help.dummygame.DummyGameActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class GamePlayHelpActivity extends NostragamusActivity implements View.OnClickListener, GamePlayHelpFragmentListener {

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play_help);
        setImmersiveFullScreenMode();
        initVisibleScreenLayout();

        loadFragment();
        initViews();
    }

    private void initViews() {
        ImageView closeImageView = (ImageView) findViewById(R.id.game_play_help_close_imgView);
        closeImageView.setOnClickListener(this);
    }

    private void loadFragment() {
        GamePlayHelpFragment fragment = new GamePlayHelpFragment();
        FragmentHelper.replaceFragment(this, R.id.game_play_help_fragment_container, fragment);
    }

    private void initVisibleScreenLayout() {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(dm);

            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) findViewById(R.id.game_play_help_bottom_empty_layout).getLayoutParams();
            rlp.height = (int) (dm.heightPixels * 0.1);

            int total = dm.heightPixels - rlp.height;
            rlp = (RelativeLayout.LayoutParams) findViewById(R.id.game_play_help_top_layout).getLayoutParams();
            rlp.height = total;

        } catch (Exception ex) {}
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.game_play_help_close_imgView:
                onCloseGamePlayInfoClicked();
                break;
        }
    }

    private void onCloseGamePlayInfoClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void onPlayGameClicked() {
        Intent intent = new Intent(this, DummyGameActivity.class);
        startActivity(intent);
    }
}
