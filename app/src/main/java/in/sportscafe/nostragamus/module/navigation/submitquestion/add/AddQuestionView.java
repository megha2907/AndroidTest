package in.sportscafe.nostragamus.module.navigation.submitquestion.add;

import com.jeeva.android.View;

/**
 * Created by Jeeva on 24/03/17.
 */
public interface AddQuestionView extends View {

    void goBack();

    void setMatchDate(String matchDate);

    void setPartyAName(String name);

    void setPartyBName(String name);

    void setPartyAPhoto(String imageUrl);

    void setPartyBPhoto(String imageUrl);

    void setMatchStage(String stage);
}