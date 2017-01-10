package in.sportscafe.nostragamus.module.user.otheranswers;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.module.user.login.dto.BasicUserInfo;
import in.sportscafe.nostragamus.module.user.playerprofile.FuzzyPlayerModelImpl;

/**
 * Created by deepanshu on 5/10/16.
 */
public class OthersAnswersModelImpl implements OthersAnswersModel, FuzzyPlayerModelImpl.OnFuzzyPlayerModelListener {

    private FuzzyPlayerAdapter mFuzzyPlayerAdapter;

    private OnOthersAnswersModelListener onOthersAnswersModelListener;

    private FuzzyPlayerModelImpl mFuzzyPlayerModel;

    private OthersAnswersModelImpl(OnOthersAnswersModelListener listener) {
        this.onOthersAnswersModelListener = listener;
        this.mFuzzyPlayerModel = FuzzyPlayerModelImpl.newInstance(this);
    }

    public static OthersAnswersModel newInstance(OnOthersAnswersModelListener listener) {
        return new OthersAnswersModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        if(null != bundle) {

        }
    }

    @Override
    public FuzzyPlayerAdapter createAdapter(Context context) {
        return mFuzzyPlayerAdapter = new FuzzyPlayerAdapter(context);
    }

    @Override
    public void filterSearch(String key) {
        int length = key.length();
        if (length > 2) {
            mFuzzyPlayerModel.getFuzzyPlayers(key);
        }
    }

    @Override
    public void onGetPlayers(List<BasicUserInfo> players) {
        mFuzzyPlayerAdapter.clear();
        mFuzzyPlayerAdapter.addAll(players);
        mFuzzyPlayerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed() {
        onOthersAnswersModelListener.onFailed();
    }

    @Override
    public void onNoInternet() {
        onOthersAnswersModelListener.onNoInternet();
    }

    public interface OnOthersAnswersModelListener {

        void onNoInternet();

        void onFailed();
    }
}