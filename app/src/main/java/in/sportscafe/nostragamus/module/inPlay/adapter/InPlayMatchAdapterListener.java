package in.sportscafe.nostragamus.module.inPlay.adapter;

import android.os.Bundle;

/**
 * Created by sandip on 09/09/17.
 */

public interface InPlayMatchAdapterListener {
    void onMatchClicked(Bundle args);
    void onMatchActionClicked(int action, Bundle args);
}
