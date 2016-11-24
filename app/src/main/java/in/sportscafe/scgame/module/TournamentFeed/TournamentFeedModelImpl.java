package in.sportscafe.scgame.module.tournamentFeed;

/**
 * Created by deepanshi on 9/29/16.
 */

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.scgame.module.tournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.tournamentFeed.dto.TournamentsResponse;
import in.sportscafe.scgame.module.home.OnHomeActionListener;
import in.sportscafe.scgame.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;


public class TournamentFeedModelImpl implements TournamentFeedModel {

    private TournamentFeedAdapter mTournamentFeedAdapter;

    private OnTournamentFeedModelListener mTournamentFeedModelListener;

    private ScGameDataHandler mScGameDataHandler;

    private TournamentFeedModelImpl(OnTournamentFeedModelListener listener) {
        this.mTournamentFeedModelListener = listener;
        mScGameDataHandler = ScGameDataHandler.getInstance();
    }

    public static TournamentFeedModel newInstance(OnTournamentFeedModelListener listener) {
        return new TournamentFeedModelImpl(listener);
    }

    @Override
    public TournamentFeedAdapter getAdapter(OnHomeActionListener listener) {
        return mTournamentFeedAdapter = new TournamentFeedAdapter(mTournamentFeedModelListener.getContext(), listener);

    }

    @Override
    public void init(Bundle bundle) {
        getTournamentFeed(bundle);
        checkEmpty();
    }

    private void checkEmpty() {
        if(mTournamentFeedAdapter.getItemCount() == 0) {
            mTournamentFeedModelListener.onEmpty();
        }
    }

    @Override
    public void getTournamentFeed(Bundle bundle) {

        TournamentInfo tournamentInfo = (TournamentInfo) bundle.getSerializable(Constants.BundleKeys.TOURNAMENT_LIST);
        for (TournamentFeedInfo tournamentFeedInfo : tournamentInfo.getTournamentFeedInfoList()) {
            mTournamentFeedAdapter.add(tournamentFeedInfo);

        }
        mTournamentFeedAdapter.notifyDataSetChanged();

    }

    public interface OnTournamentFeedModelListener {

        void onEmpty();

        Context getContext();
    }
}