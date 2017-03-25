package in.sportscafe.nostragamus.module.question.add;

import android.os.Bundle;

/**
 * Created by Jeeva on 24/03/17.
 */
public interface AddQuestionModel {

    void init(Bundle bundle);

    void saveQuestion(String question, String context, String leftOption, String rightOption, String neitherOption);

}