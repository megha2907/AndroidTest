package in.sportscafe.scgame.module.user.preference;

import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class PreferenceManager {

    public void savePreference(List<Integer> selectedSports, SavePreferenceModelImpl.SavePreferenceModelListener listener) {
        new SavePreferenceModelImpl(listener).savePreference(selectedSports);
    }

    public void getPreference(GetPreferenceModelImpl.GetPreferenceModelListener listener) {
        new GetPreferenceModelImpl(listener).getPreference();
    }
}