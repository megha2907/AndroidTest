package in.sportscafe.nostragamus.module.prediction.gamePlayHelp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GamePlayHelpFragment extends BaseFragment implements View.OnClickListener {

    private GamePlayHelpFragmentListener mFragmentListener;

    public GamePlayHelpFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GamePlayHelpFragmentListener) {
            mFragmentListener = (GamePlayHelpFragmentListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_play_help, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        Button gamePlayButton = (Button) rootView.findViewById(R.id.powerup_bank_transfer_to_challenge_button);
        gamePlayButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.powerup_bank_transfer_to_challenge_button:
                if (mFragmentListener != null) {
                    mFragmentListener.onPlayGameClicked();
                }
                break;
        }
    }
}
