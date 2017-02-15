package in.sportscafe.nostragamus.module.tournament;

import android.support.v4.app.FragmentManager;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.offline.OfflineDataHandler;
import in.sportscafe.nostragamus.module.tournamentFeed.TournamentFeedFragment;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentInfo;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentsResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 11/14/16.
 */

public class TournamentModelImpl implements TournamentModel {

    private TournamentModelImpl.OnTournamentModelListener mTournamemtModelListener;

    private ViewPagerAdapter mViewPagerAdapter;

    private TournamentModelImpl(TournamentModelImpl.OnTournamentModelListener listener, FragmentManager fm) {
        this.mTournamemtModelListener = listener;
        this.mViewPagerAdapter = new ViewPagerAdapter(fm);
    }

    public static TournamentModel newInstance(TournamentModelImpl.OnTournamentModelListener listener, FragmentManager fm) {
        return new TournamentModelImpl(listener, fm);
    }

    @Override
    public void getTournaments() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getTournaments(false, true).enqueue(
                    new NostragamusCallBack<TournamentsResponse>() {
                        @Override
                        public void onResponse(Call<TournamentsResponse> call, Response<TournamentsResponse> response) {
                            super.onResponse(call, response);

                            if (response.isSuccessful()) {
                                List<TournamentInfo> tournaments = response.body().getTournamentInfos();
                                if(tournaments.isEmpty()) {
                                    mTournamemtModelListener.onEmpty();
                                } else {
                                    refreshAdapter(tournaments);
                                }
                                OfflineDataHandler.getInstance().setAllTournaments(tournaments);
                            } else {
                                mTournamemtModelListener.onFailedFeeds(response.message());
                            }
                        }
                    }
            );
        } else {
            List<TournamentInfo> allTournaments = OfflineDataHandler.getInstance().getAllTournaments();
            if(allTournaments.isEmpty()) {
                mTournamemtModelListener.onEmpty();
            } else {
                refreshAdapter(allTournaments);
            }

            mTournamemtModelListener.onNoInternet();
        }
    }

    private void refreshAdapter(List<TournamentInfo> tournaments) {
        for (TournamentInfo info : tournaments) {
            mViewPagerAdapter.addFragment(TournamentFeedFragment.newInstance(info.getTournamentFeedInfoList()), info.getSportsName());
        }

        mTournamemtModelListener.onSuccessFeeds(mViewPagerAdapter);
        mViewPagerAdapter.notifyDataSetChanged();
    }

    public interface OnTournamentModelListener {

        void onEmpty();

        void onNoInternet();

        void onSuccessFeeds(ViewPagerAdapter adapter);

        void onFailedFeeds(String message);
    }
}