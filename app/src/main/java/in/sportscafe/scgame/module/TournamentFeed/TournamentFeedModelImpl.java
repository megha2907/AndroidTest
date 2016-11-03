package in.sportscafe.scgame.module.TournamentFeed;

/**
 * Created by deepanshi on 9/29/16.
 */

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentsResponse;
import in.sportscafe.scgame.module.home.OnHomeActionListener;
import in.sportscafe.scgame.module.user.group.newgroup.GrpTournamentSelectionAdapter;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;


public class TournamentFeedModelImpl implements TournamentFeedModel {

    private int mClosestDatePosition = 0;

    private TournamentFeedAdapter mTournamentFeedAdapter;

    private TournamentFeedModelImpl.OnTournamentFeedModelListener mTournamentFeedModelListener;

    private ScGameDataHandler mScGameDataHandler;

    private TournamentFeedModelImpl(TournamentFeedModelImpl.OnTournamentFeedModelListener listener) {
        this.mTournamentFeedModelListener = listener;
        mScGameDataHandler = ScGameDataHandler.getInstance();
    }

    public static TournamentFeedModel newInstance(TournamentFeedModelImpl.OnTournamentFeedModelListener listener) {
        return new TournamentFeedModelImpl(listener);
    }

    @Override
    public TournamentFeedAdapter getAdapter(OnHomeActionListener listener) {
        return mTournamentFeedAdapter = new TournamentFeedAdapter(mTournamentFeedModelListener.getContext(), listener);

    }

    @Override
    public void getFeeds() {
        mTournamentFeedAdapter.clear();
        if(ScGame.getInstance().hasNetworkConnection()) {
            getAllTournamentsfromServer();
        } else {
            mTournamentFeedModelListener.onNoInternet();
        }
    }

    private void getAllTournamentsfromServer() {
        if(ScGame.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getTournaments(false).enqueue(
                    new ScGameCallBack<TournamentsResponse>() {
                        @Override
                        public void onResponse(Call<TournamentsResponse> call, Response<TournamentsResponse> response) {
                            if(response.isSuccessful()) {
                                List<TournamentInfo> newTournamentInfo = response.body().getTournamentInfos();

                                if(null != newTournamentInfo && newTournamentInfo.size() > 0) {
                                    List<TournamentInfo> oldTournamentList = mScGameDataHandler.getTournaments();
                                    oldTournamentList.clear();
                                    for (TournamentInfo tournamentInfo : newTournamentInfo) {
                                        if(!oldTournamentList.contains(tournamentInfo)) {
                                            oldTournamentList.add(tournamentInfo);
                                        }
                                    }

                                    mScGameDataHandler.setTournaments(oldTournamentList);
                                    mTournamentFeedAdapter.addAll(ScGameDataHandler.getInstance().getTournaments());

                                    mTournamentFeedModelListener.onSuccessFeeds();
                                }
                                else
                                {
                                    mTournamentFeedModelListener.onFailedFeeds(response.message());
                                }

                            } else {
                                mTournamentFeedModelListener.onFailedFeeds(response.message());
                            }
                        }
                    }
            );
        } else {
            mTournamentFeedModelListener.onNoInternet();
        }
    }

    public interface OnTournamentFeedModelListener {

        void onSuccessFeeds();

        void onFailedFeeds(String message);

        void onNoInternet();

        void onEmpty();

        Context getContext();
    }
}