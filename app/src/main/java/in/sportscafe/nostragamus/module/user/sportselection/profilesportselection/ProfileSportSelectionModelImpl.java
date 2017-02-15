package in.sportscafe.nostragamus.module.user.sportselection.profilesportselection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.preference.PreferenceManager;
import in.sportscafe.nostragamus.module.user.preference.SavePreferenceModelImpl;

/**
 * Created by deepanshi on 12/8/16.
 */

public class ProfileSportSelectionModelImpl implements ProfileSportSelectionModel {

    private ProfileSportSelectionAdapter mSportSelectionAdapter;

    private ProfileSportSelectionModelImpl.ProfileSportSelectionModelListener mSportSelectionModelListener;

    protected ProfileSportSelectionModelImpl(ProfileSportSelectionModelImpl.ProfileSportSelectionModelListener modelListener) {
        this.mSportSelectionModelListener = modelListener;
    }

    public static ProfileSportSelectionModel newInstance(ProfileSportSelectionModelImpl.ProfileSportSelectionModelListener modelListener) {
        return new ProfileSportSelectionModelImpl(modelListener);
    }

    @Override
    public RecyclerView.Adapter getSportsSelectionAdapter(Context context) {
        mSportSelectionAdapter = new ProfileSportSelectionAdapter(
                context,
                NostragamusDataHandler.getInstance().getAllSports(),
                NostragamusDataHandler.getInstance().getFavoriteSportsIdList(),
                new ProfileSportSelectionAdapter.OnSportChangedListener() {
                    @Override
                    public void onOnSportChanged(List<Integer> selectedSportsIdList) {
                        saveSelectedSports();
                    }
                });
        return mSportSelectionAdapter;
    }

    @Override
    public void saveSelectedSports() {
        final List<Integer> selectedSports = mSportSelectionAdapter.getSelectedSportList();
        if (null != selectedSports && selectedSports.size() > 0) {
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

    public interface ProfileSportSelectionModelListener {

        void onSelectedSportsSaved();

        void onEmptySelection();

        void onNoInternet();

        void onFailed(String message);
    }
}