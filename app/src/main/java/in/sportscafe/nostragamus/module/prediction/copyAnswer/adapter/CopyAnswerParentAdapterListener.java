package in.sportscafe.nostragamus.module.prediction.copyAnswer.adapter;

import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerContest;

/**
 * Created by sc on 24/1/18.
 */

public interface CopyAnswerParentAdapterListener {
    void onUseClicked(CopyAnswerContest copyAnswerContest);
    void scrollToItem(int position);
}
