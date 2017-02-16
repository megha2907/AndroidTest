package in.sportscafe.nostragamus.module.user.comparisons.compareLeaderBoards;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.webservice.CompareLeaderBoard;

/**
 * Created by deepanshi on 2/14/17.
 */

public class CompareLeaderBoardPresenterImpl  implements CompareLeaderBoardPresenter, CompareLeaderBoardModelImpl.CompareLeaderBoardModelListener {

    private CompareLeaderBoardView mCompareLeaderBoardView;

    private CompareLeaderBoardModel mCompareLeaderBoardModel;

    private boolean mFromProfile = false;

    public CompareLeaderBoardPresenterImpl(CompareLeaderBoardView compareLeaderBoardView) {
        this.mCompareLeaderBoardView = compareLeaderBoardView;
        this.mCompareLeaderBoardModel = CompareLeaderBoardModelImpl.newInstance(this);
    }

    public static CompareLeaderBoardPresenter newInstance(CompareLeaderBoardView compareLeaderBoardView) {
        return new CompareLeaderBoardPresenterImpl(compareLeaderBoardView);
    }

    @Override
    public void onCreateCompareLeaderBoard(Bundle bundle) {
        mCompareLeaderBoardView.showProgressbar();
        mCompareLeaderBoardModel.init(bundle);
    }

    @Override
    public void onCreateCompareLeaderBoardAdapter(List<CompareLeaderBoard> compareLeaderBoardList) {
        Context context = mCompareLeaderBoardView.getContext();
        if(null != context) {
            mCompareLeaderBoardView.setAdapter(mCompareLeaderBoardModel.getCompareLeaderBoardAdapter(context,compareLeaderBoardList));
        }
    }

    @Override
    public void onNoInternet() {
        mCompareLeaderBoardView.dismissProgressbar();
        mCompareLeaderBoardView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailed(String message) {
        mCompareLeaderBoardView.dismissProgressbar();
        mCompareLeaderBoardView.showMessage(message);
    }

    @Override
    public void onNoCommonLeaderBoards() {
        mCompareLeaderBoardView.noCommonLeaderBoards();
    }

    @Override
    public void ongetCommonLeaderBoardSuccess(List<CompareLeaderBoard> compareLeaderBoardList) {
        mCompareLeaderBoardView.dismissProgressbar();
        onCreateCompareLeaderBoardAdapter(compareLeaderBoardList);
    }

    @Override
    public void ongetCommonLeaderBoardFailed(String message) {
        mCompareLeaderBoardView.dismissProgressbar();
        mCompareLeaderBoardView.showMessage(message);
    }

}
