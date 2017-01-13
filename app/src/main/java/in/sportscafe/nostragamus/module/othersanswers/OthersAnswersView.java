package in.sportscafe.nostragamus.module.othersanswers;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsAdapter;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface OthersAnswersView extends InAppView {

    void setAdapter(OthersAnswersAdapter adapter);
}