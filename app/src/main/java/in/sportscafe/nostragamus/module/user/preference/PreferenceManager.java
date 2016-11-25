package in.sportscafe.nostragamus.module.user.preference;

import com.jeeva.android.Log;

import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class PreferenceManager {

    public void savePreference(List<Integer> selectedSports, SavePreferenceModelImpl.SavePreferenceModelListener listener) {
        Log.i("selectedsports",selectedSports.toString());
        new SavePreferenceModelImpl(listener).savePreference(selectedSports);
    }
}