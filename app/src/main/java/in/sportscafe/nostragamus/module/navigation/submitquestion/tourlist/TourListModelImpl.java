package in.sportscafe.nostragamus.module.navigation.submitquestion.tourlist;

import android.support.v4.app.FragmentManager;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.navigation.submitquestion.matchlist.MatchListFragment;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 11/14/16.
 */
public class TourListModelImpl implements TourListModel {

    private TourListModelImpl.OnTournamentModelListener mTournamemtModelListener;

    private ViewPagerAdapter mViewPagerAdapter;

    private TourListModelImpl(TourListModelImpl.OnTournamentModelListener listener, FragmentManager fm) {
        this.mTournamemtModelListener = listener;
        this.mViewPagerAdapter = new ViewPagerAdapter(fm);
    }

    public static TourListModel newInstance(TourListModelImpl.OnTournamentModelListener listener, FragmentManager fm) {
        return new TourListModelImpl(listener, fm);
    }

    @Override
    public void getTourList() {
        callTourListApi();
    }

    private void callTourListApi() {
        if (!Nostragamus.getInstance().hasNetworkConnection()) {
            mTournamemtModelListener.onNoInternet();
            return;
        }

        mTournamemtModelListener.onApiCallStarted();

        MyWebService.getInstance().getTourListRequest()
                .enqueue(new NostragamusCallBack<TourListResponse>() {
                    @Override
                    public void onResponse(Call<TourListResponse> call, Response<TourListResponse> response) {
                        super.onResponse(call, response);

                        if (!mTournamemtModelListener.onApiCallStopped()) {
                            return;
                        }

                        if (response.isSuccessful()) {
                            TourListResponse tourListResponse = response.body();
                            if(null != tourListResponse) {
                                handleTourListResponse(tourListResponse.getTourList());
                                return;
                            }
                        }
                        mTournamemtModelListener.onFailedTourList();
                    }
                });
    }

    private void handleTourListResponse(List<Tour> tourList) {
        for (Tour tour : tourList) {
            mViewPagerAdapter.addFragment(MatchListFragment.newInstance(tour.getMatchList()), tour.getName());
        }

        mTournamemtModelListener.onSuccessTourList(mViewPagerAdapter);
        mViewPagerAdapter.notifyDataSetChanged();
    }

    public interface OnTournamentModelListener {

        boolean onApiCallStopped();

        void onApiCallStarted();

        void onNoInternet();

        void onEmpty();

        void onSuccessTourList(ViewPagerAdapter adapter);

        void onFailedTourList();
    }
}