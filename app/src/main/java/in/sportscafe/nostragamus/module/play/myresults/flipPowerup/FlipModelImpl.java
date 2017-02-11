package in.sportscafe.nostragamus.module.play.myresults.flipPowerup;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresults.dto.ReplayPowerupResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 12/20/16.
 */

public class FlipModelImpl implements FlipModel, FlipAdapter.OnFlipActionListener {

    private static final int PAGINATION_START_AT = 5;

    private static final int LIMIT = 5;

    private int matchId;

    private boolean isLoading = false;

    private boolean hasMoreItems = true;

    private FlipAdapter mFlipAdapter;

    private Match match;

    private FlipModelImpl.OnFlipModelListener mFlipModelListener;


    private FlipModelImpl(FlipModelImpl.OnFlipModelListener listener) {
        this.mFlipModelListener = listener;
    }

    public static FlipModel newInstance(FlipModelImpl.OnFlipModelListener listener) {
        return new FlipModelImpl(listener);
    }

    @Override
    public FlipAdapter getAdapter() {
        mFlipAdapter = new FlipAdapter(mFlipModelListener.getContext());
        mFlipAdapter.setFlipActionListener(this);
        return mFlipAdapter;
    }

    @Override
    public void init(Bundle bundle) {
        if (bundle.containsKey(BundleKeys.MATCH_LIST)) {
            match = Parcels.unwrap(bundle.getParcelable(BundleKeys.MATCH_LIST));
            matchId = match.getId();
        } else {
            mFlipModelListener.onFailedMyResults(Constants.Alerts.RESULTS_INFO_ERROR);
        }
    }

    @Override
    public void getMyResultsData(Context context) {
        addMoreInAdapter(match);
    }

    private void addMoreInAdapter(Match match) {
        mFlipAdapter.add(match);
        mFlipAdapter.notifyDataSetChanged();
    }

    @Override
    public void callFlipQuestionApi(Integer questionId) {
        callFLipPowerupApi(Powerups.ANSWER_FLIP, matchId, questionId);
    }

    private void callFLipPowerupApi(final String powerupId, Integer matchId, Integer questionId) {

        MyWebService.getInstance().getFlipPowerup(powerupId, matchId, questionId).enqueue(new NostragamusCallBack<ReplayPowerupResponse>() {
            @Override
            public void onResponse(Call<ReplayPowerupResponse> call, Response<ReplayPowerupResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful()) {
                    if (response.body().getResponse().equals(null) || response.body().getResponse().equalsIgnoreCase("failure")) {
                        mFlipModelListener.onFailedFlipPowerupResponse();
                    } else {
                        NostragamusAnalytics.getInstance().trackPowerups(AnalyticsActions.APPLIED, powerupId);
                        mFlipModelListener.onSuccessFlipPowerupResponse(match);
                    }

                } else {
                    mFlipModelListener.onFailedFlipPowerupResponse();
                }
            }
        });


    }


    public interface OnFlipModelListener {

        void onSuccessMyResults(FlipAdapter FlipAdapter);

        void onFailedMyResults(String message);

        void onNoInternet();

        void onEmpty();

        Context getContext();

        void onFailedFlipPowerupResponse();

        void onSuccessFlipPowerupResponse(Match match);
    }
}
