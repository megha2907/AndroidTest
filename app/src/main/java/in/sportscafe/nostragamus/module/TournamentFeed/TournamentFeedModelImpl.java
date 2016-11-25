package in.sportscafe.nostragamus.module.tournamentFeed;

/**
 * Created by deepanshi on 9/29/16.
 */

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentInfo;
import in.sportscafe.nostragamus.module.home.OnHomeActionListener;


public class TournamentFeedModelImpl implements TournamentFeedModel {

    private TournamentFeedAdapter mTournamentFeedAdapter;

    private OnTournamentFeedModelListener mTournamentFeedModelListener;

    private NostragamusDataHandler mNostragamusDataHandler;

    private TournamentFeedModelImpl(OnTournamentFeedModelListener listener) {
        this.mTournamentFeedModelListener = listener;
        mNostragamusDataHandler = NostragamusDataHandler.getInstance();
    }

    public static TournamentFeedModel newInstance(OnTournamentFeedModelListener listener) {
        return new TournamentFeedModelImpl(listener);
    }

    @Override
    public TournamentFeedAdapter getAdapter(OnHomeActionListener listener) {
        return mTournamentFeedAdapter = new TournamentFeedAdapter(mTournamentFeedModelListener.getContext(), listener);

    }

    @Override
    public void init(Bundle bundle) {
        getTournamentFeed(bundle);
        checkEmpty();
    }

    private void checkEmpty() {
        if(mTournamentFeedAdapter.getItemCount() == 0) {
            mTournamentFeedModelListener.onEmpty();
        }
    }

    @Override
    public void getTournamentFeed(Bundle bundle) {

        TournamentInfo tournamentInfo = (TournamentInfo) bundle.getSerializable(Constants.BundleKeys.TOURNAMENT_LIST);
        for (TournamentFeedInfo tournamentFeedInfo : tournamentInfo.getTournamentFeedInfoList()) {
            mTournamentFeedAdapter.add(tournamentFeedInfo);

        }
        mTournamentFeedAdapter.notifyDataSetChanged();

    }

    public interface OnTournamentFeedModelListener {

        void onEmpty();

        Context getContext();
    }
}