package in.sportscafe.nostragamus.module.question.add;

import android.os.Bundle;

/**
 * Created by Jeeva on 24/03/17.
 */
public interface AddQuestionPresenter {

    void onCreateAddQuestion(Bundle bundle);

    void onClickAdd(String question, String context, String leftOption, String rightOption, String neitherOption);
}