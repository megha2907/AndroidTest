package in.sportscafe.nostragamus.module.othersanswers;

import android.content.Context;
import android.os.Bundle;

import java.io.File;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface OthersAnswersModel {

    void init(Bundle bundle);

    void getOthersAnswers();

    OthersAnswersAdapter getAdapter(Context context);
}