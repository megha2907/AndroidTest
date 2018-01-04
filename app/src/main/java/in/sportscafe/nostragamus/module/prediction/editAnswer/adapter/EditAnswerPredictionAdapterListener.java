package in.sportscafe.nostragamus.module.prediction.editAnswer.adapter;

/**
 * Created by sandip on 15/09/17.
 */

public interface EditAnswerPredictionAdapterListener {
    void onLeftOptionClicked(int pos);
    void onRightOptionClicked(int pos);
    void onWebLinkClicked(String url);
}
