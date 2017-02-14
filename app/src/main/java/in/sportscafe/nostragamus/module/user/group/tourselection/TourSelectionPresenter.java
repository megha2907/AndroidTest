package in.sportscafe.nostragamus.module.user.group.tourselection;

import android.os.Bundle;

/**
 * Created by deepanshi on 1/6/17.
 */

public interface TourSelectionPresenter {

    void onCreateTourSelection(Bundle bundle, TourSelectionFragment.OnTourSelectionListener tourSelectionListener);
}