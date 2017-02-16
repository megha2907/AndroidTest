package in.sportscafe.nostragamus.module.user.comparisons.compareLeaderBoards;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.webservice.CompareLeaderBoard;
import in.sportscafe.nostragamus.webservice.CompareLeaderBoardResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 2/14/17.
 */

public class CompareLeaderBoardModelImpl implements CompareLeaderBoardModel {

    private CompareLeaderBoardAdapter mCompareLeaderBoardAdapter;

    private CompareLeaderBoardModelImpl.CompareLeaderBoardModelListener mCompareLeaderBoardModelListener;

    protected CompareLeaderBoardModelImpl(CompareLeaderBoardModelImpl.CompareLeaderBoardModelListener modelListener) {
        this.mCompareLeaderBoardModelListener = modelListener;
    }

    public static CompareLeaderBoardModel newInstance(CompareLeaderBoardModelImpl.CompareLeaderBoardModelListener modelListener) {
        return new CompareLeaderBoardModelImpl(modelListener);
    }

    @Override
    public void init(Bundle bundle) {
        Integer playerId = bundle.getInt(Constants.BundleKeys.PLAYER_ID);
        getLeaderBoardComparison(playerId);
    }

    private void getLeaderBoardComparison(Integer playerId) {
        MyWebService.getInstance().getLeaderBoardComparisonRequest(playerId).enqueue(
                new NostragamusCallBack<CompareLeaderBoardResponse>() {
                    @Override
                    public void onResponse(Call<CompareLeaderBoardResponse> call, Response<CompareLeaderBoardResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {

                            List<CompareLeaderBoard> compareLeaderBoardList = response.body().getCompareLeaderBoards();
                            if (null != compareLeaderBoardList) {
                                mCompareLeaderBoardModelListener.ongetCommonLeaderBoardSuccess(compareLeaderBoardList);
                            }else {
                                mCompareLeaderBoardModelListener.ongetCommonLeaderBoardFailed(response.message());
                            }

                        } else {
                            mCompareLeaderBoardModelListener.ongetCommonLeaderBoardFailed(response.message());
                        }
                    }
                }
        );
    }


    @Override
    public RecyclerView.Adapter getCompareLeaderBoardAdapter(Context context,List<CompareLeaderBoard> compareLeaderBoardList) {

        if (compareLeaderBoardList.isEmpty()) {

            mCompareLeaderBoardModelListener.onNoCommonLeaderBoards();
        }

        mCompareLeaderBoardAdapter = new CompareLeaderBoardAdapter(context,
               compareLeaderBoardList);
        return mCompareLeaderBoardAdapter;
    }


    public interface CompareLeaderBoardModelListener {

        void onNoInternet();

        void onFailed(String message);

        void onNoCommonLeaderBoards();

        void ongetCommonLeaderBoardSuccess(List<CompareLeaderBoard> compareLeaderBoardList);

        void ongetCommonLeaderBoardFailed(String message);
    }
}
