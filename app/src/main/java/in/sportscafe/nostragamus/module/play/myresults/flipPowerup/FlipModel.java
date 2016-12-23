package in.sportscafe.nostragamus.module.play.myresults.flipPowerup;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by deepanshi on 12/20/16.
 */

public interface FlipModel{

        FlipAdapter getAdapter();

        void init(Bundle bundle);

        void getMyResultsData(Context context);
}
