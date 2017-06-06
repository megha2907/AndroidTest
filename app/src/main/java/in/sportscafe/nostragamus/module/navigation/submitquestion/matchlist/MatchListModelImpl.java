package in.sportscafe.nostragamus.module.navigation.submitquestion.matchlist;

import android.content.Context;
import android.os.Bundle;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by deepanshi on 10/5/16.
 */
public class MatchListModelImpl implements MatchListModel {

    private MatchListAdapter mMatchListAdapter;

    private OnMatchListModelListener mMatchListModelListener;

    private MatchListModelImpl(OnMatchListModelListener listener) {
        this.mMatchListModelListener = listener;
    }

    public static MatchListModel newInstance(OnMatchListModelListener listener) {
        return new MatchListModelImpl(listener);
    }

    @Override
    public void init(Context context, Bundle bundle) {
        if (null != bundle && bundle.containsKey(BundleKeys.MATCH_LIST)) {
            List<Match> matchList = Parcels.unwrap(bundle.getParcelable(BundleKeys.MATCH_LIST));
            if(null == matchList || matchList.isEmpty()) {
                mMatchListModelListener.onEmpty();
                return;
            }

            mMatchListAdapter = new MatchListAdapter(context);
            mMatchListAdapter.addAll(matchList);

            mMatchListModelListener.onAdapterCreated(mMatchListAdapter);
        }
    }

    public interface OnMatchListModelListener {

        void onEmpty();

        void onAdapterCreated(MatchListAdapter adapter);
    }
}