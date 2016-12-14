package in.sportscafe.nostragamus.module.play.myresults;

import android.view.View;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface MyResultsView extends InAppView {

    void setAdapter(MyResultsAdapter myResultsAdapter);

    void goBack();

    void openDialog();

    void navigatetoPlay(Match match);
}