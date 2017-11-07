package in.sportscafe.nostragamus.module.resultspeek;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by deepanshi on 2/16/17.
 */

public class ResultsPeekPresenterImpl implements ResultsPeekPresenter, ResultsPeekModelImpl.ResultsPeekModelListener {

    private ResultsPeekView mResultsPeekBoardView;

    private ResultsPeekModel mResultsPeekModel;

    private boolean mFromProfile = false;

    public ResultsPeekPresenterImpl(ResultsPeekView resultsPeekView) {
        this.mResultsPeekBoardView = resultsPeekView;
        this.mResultsPeekModel = ResultsPeekModelImpl.newInstance(this);
    }

    public static ResultsPeekPresenter newInstance(ResultsPeekView resultsPeekView) {
        return new ResultsPeekPresenterImpl(resultsPeekView);
    }

    @Override
    public void onCreateResultsPeek(Bundle bundle) {
        mResultsPeekBoardView.showProgressbar();
        mResultsPeekModel.init(bundle);
    }

    @Override
    public void onCreateResultsPeekAdapter() {
        Context context = mResultsPeekBoardView.getContext();
        if (null != context) {
            mResultsPeekBoardView.setAdapter(mResultsPeekModel.getResultsPeekAdapter(context));
        }
    }

    @Override
    public void onNoInternet() {
        mResultsPeekBoardView.dismissProgressbar();
        mResultsPeekBoardView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailed(String message) {
        mResultsPeekBoardView.dismissProgressbar();
        mResultsPeekBoardView.showMessage(message);
    }

    @Override
    public boolean isThreadAlive() {
        return null != mResultsPeekBoardView.getContext();
    }

    @Override
    public void onFailedResults() {
        mResultsPeekBoardView.dismissProgressbar();
        mResultsPeekBoardView.showInAppMessage(Constants.Alerts.NO_RESULTS_FOUND);
    }

    @Override
    public void onSuccessBothResults(int myPoints, int playerPoints, String matchStage, String challengeName,String resultPublished) {
        mResultsPeekBoardView.dismissProgressbar();
        mResultsPeekBoardView.setPointsAndMatch(myPoints,playerPoints,matchStage,challengeName,resultPublished);
        onCreateResultsPeekAdapter();
    }

    @Override
    public void setPlayerProfileData(String playerPhoto, String playerName) {
        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        if (userInfo!=null) {
            mResultsPeekBoardView.setName(userInfo.getUserNickName(), playerName);
            mResultsPeekBoardView.setProfileImage(userInfo.getPhoto(), playerPhoto);
        }
    }

}