package in.sportscafe.nostragamus.module.navigation.submitquestion.add;

import android.os.Bundle;

import in.sportscafe.nostragamus.module.resultspeek.dto.Match;

/**
 * Created by Jeeva on 24/03/17.
 */
public interface AddQuestionModel {

    void init(Bundle bundle);

    Match getMatchDetails();

    void saveQuestion(String question, String context, String leftOption, String rightOption, String neitherOption);
}