package in.sportscafe.scgame.module.play.myresults;

import android.content.Context;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface MyResultsModel {

    void getMyResultsData(Context context);

    void checkPagination(int firstVisibleItemPosition, int childCount, int itemCount);
}