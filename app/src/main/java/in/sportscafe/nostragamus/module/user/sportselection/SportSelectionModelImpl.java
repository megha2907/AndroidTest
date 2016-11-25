package in.sportscafe.nostragamus.module.user.sportselection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.preference.PreferenceManager;
import in.sportscafe.nostragamus.module.user.preference.SavePreferenceModelImpl;

public class SportSelectionModelImpl implements SportSelectionModel {

    private NostragamusDataHandler mNostragamusDataHandler;

    private SportSelectionAdapter mSportSelectionAdapter;

    private SportSelectionModelListener mSportSelectionModelListener;

    protected SportSelectionModelImpl(SportSelectionModelListener modelListener) {
        this.mSportSelectionModelListener = modelListener;
        this.mNostragamusDataHandler = NostragamusDataHandler.getInstance();
    }

    public static SportSelectionModel newInstance(SportSelectionModelListener modelListener) {
        return new SportSelectionModelImpl(modelListener);
    }

    @Override
    public RecyclerView.Adapter getSportsSelectionAdapter(Context context) {
        mSportSelectionAdapter = new SportSelectionAdapter(context,
                mNostragamusDataHandler.getFavoriteSportsIdList());
        mSportSelectionAdapter.addAll(mNostragamusDataHandler.getAllSports());
        return mSportSelectionAdapter;
    }

    @Override
    public void saveSelectedSports() {
        final List<Integer> selectedSports = mSportSelectionAdapter.getSelectedSportList();
        if(null != selectedSports && selectedSports.size() > 0) {
            new PreferenceManager().savePreference(selectedSports,
                    new SavePreferenceModelImpl.SavePreferenceModelListener() {
                @Override
                public void onSuccess() {
                    mSportSelectionModelListener.onSelectedSportsSaved();
                }

                @Override
                public void onNoInternet() {
                    mSportSelectionModelListener.onNoInternet();
                }

                @Override
                public void onFailed(String message) {
                    mSportSelectionModelListener.onFailed(message);
                }
            });
        } else {
            mSportSelectionModelListener.onEmptySelection();
        }
    }

    @Override
    public boolean isUserLoggedIn() {
        return mNostragamusDataHandler.isLoggedInUser();
    }

    public interface SportSelectionModelListener {

        void onSelectedSportsSaved();

        void onEmptySelection();

        void onNoInternet();

        void onFailed(String message);
    }
}