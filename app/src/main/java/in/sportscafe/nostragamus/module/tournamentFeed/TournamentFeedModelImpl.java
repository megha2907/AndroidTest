package in.sportscafe.nostragamus.module.tournamentFeed;

import android.content.Context;
import android.os.Bundle;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentInfo;

/**
 * Created by deepanshi on 9/29/16.
 */
public class TournamentFeedModelImpl implements TournamentFeedModel {

    private List<TournamentFeedInfo> mTourFeedInfoList;

    private TournamentFeedAdapter mTournamentFeedAdapter;

    private OnTournamentFeedModelListener mTournamentFeedModelListener;

    private TournamentFeedModelImpl(OnTournamentFeedModelListener listener) {
        this.mTournamentFeedModelListener = listener;
    }

    public static TournamentFeedModel newInstance(OnTournamentFeedModelListener listener) {
        return new TournamentFeedModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        mTourFeedInfoList = Parcels.unwrap(bundle.getParcelable(BundleKeys.TOURNAMENT_LIST));
        checkEmpty();
    }

    @Override
    public TournamentFeedAdapter getAdapter(Context context) {
        mTournamentFeedAdapter = new TournamentFeedAdapter(context);
        mTournamentFeedAdapter.addAll(mTourFeedInfoList);
        return mTournamentFeedAdapter;
    }

    private void checkEmpty() {
        if (mTourFeedInfoList.size() == 0) {
            mTournamentFeedModelListener.onEmpty();
        }
    }

    public interface OnTournamentFeedModelListener {

        void onEmpty();
    }
}