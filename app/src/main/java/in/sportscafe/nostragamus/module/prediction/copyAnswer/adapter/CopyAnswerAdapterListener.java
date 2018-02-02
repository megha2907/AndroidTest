package in.sportscafe.nostragamus.module.prediction.copyAnswer.adapter;

import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerContest;

/**
 * Created by sc on 24/1/18.
 */

public interface CopyAnswerAdapterListener {
    void onUseClicked(CopyAnswerContest copyAnswerContest, boolean shouldApplyPowerUp);
}
