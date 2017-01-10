package in.sportscafe.nostragamus.module.user.otheranswers;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by deepanshu on 12/10/16.
 */
public interface OthersAnswersModel {

    void init(Bundle bundle);

    FuzzyPlayerAdapter createAdapter(Context context);

    void filterSearch(String key);
}