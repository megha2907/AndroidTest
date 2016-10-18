package in.sportscafe.scgame.module.play.myresults;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface MyResultsModel {

    void init(Bundle bundle);

    void getMyResultsData(Context context);

    void checkPagination(int firstVisibleItemPosition, int childCount, int itemCount);
}