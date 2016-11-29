package in.sportscafe.nostragamus.module.tournament;

import android.support.v4.app.FragmentManager;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
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

    private NostragamusDataHandler mNostragamusDataHandler;

    private ViewPagerAdapter mViewPagerAdapter;

    private int mSelectedPosition = 0;

    private TournamentModelImpl(TournamentModelImpl.OnTournamentModelListener listener, FragmentManager fm) {
        this.mTournamemtModelListener = listener;
        this.mViewPagerAdapter = new ViewPagerAdapter(fm);
    }

    public static TournamentModel newInstance(TournamentModelImpl.OnTournamentModelListener listener, FragmentManager fm) {
        return new TournamentModelImpl(listener, fm);
    }

    @Override
    public ViewPagerAdapter getAdapter() {
        return mViewPagerAdapter;
    }

    @Override
    public void getTournaments() {
        getAllTournamentsfromServer();
    }

    @Override
    public int getSelectedPosition() {
        return mSelectedPosition;
    }


    private void getAllTournamentsfromServer() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getTournaments(false,true).enqueue(
                    new NostragamusCallBack<TournamentsResponse>() {
                        @Override
                        public void onResponse(Call<TournamentsResponse> call, Response<TournamentsResponse> response) {



                            if (response.isSuccessful()) {
                                List<TournamentInfo> newTournamentInfo = response.body().getTournamentInfos();

                                if (null != newTournamentInfo && newTournamentInfo.size() > 0) {


//                                    List<TournamentFeedInfo> oldTournamentList = mScGameDataHandler.getTournaments();
//                                    oldTournamentList.clear();
//                                    for (TournamentFeedInfo tournamentFeedInfo : newTournamentInfo) {
//                                        if (!oldTournamentList.contains(tournamentFeedInfo)) {
//                                            oldTournamentList.add(tournamentFeedInfo);
//                                        }
//                                    }
//
//                                    mScGameDataHandler.setTournaments(oldTournamentList);

                                    refreshAdapter(newTournamentInfo);

                                    mTournamemtModelListener.onSuccessFeeds();
                                } else {
                                    mTournamemtModelListener.onFailedFeeds(response.message());
                                }

                            } else {
                                mTournamemtModelListener.onFailedFeeds(response.message());
                            }
                        }
                    }
            );
        } else {
            mTournamemtModelListener.onNoInternet();
        }
    }


    private void refreshAdapter(List<TournamentInfo> tournamentInfoList) {

        TournamentInfo tournamentInfo;

        for (int i = 0; i < tournamentInfoList.size(); i++) {
            tournamentInfo = tournamentInfoList.get(i);
            mViewPagerAdapter.addFragment(TournamentFeedFragment.newInstance(tournamentInfo), tournamentInfo.getSportsName());
        }

        mViewPagerAdapter.notifyDataSetChanged();
    }

    public interface OnTournamentModelListener {

        void onEmpty();

        void onNoInternet();

        void onSuccessFeeds();

        void onFailedFeeds(String message);
    }
}