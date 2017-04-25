package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;

/**
 * Created by Jeeva on 17/02/17.
 */

public class ChallengeModelImpl implements ChallengeModel {

    private List<Challenge> mChallenges = new ArrayList<>();

    private int mTagId;

    private String mTabName = "";

    private ChallengeAdapter mSwipeAdapter;

    private ChallengeAdapter mListAdapter;

    private OnChallengeModelListener mChallengeModelListener;

    private ChallengeModelImpl(OnChallengeModelListener listener) {
        mChallengeModelListener = listener;
    }

    public static ChallengeModel newInstance(OnChallengeModelListener listener) {
        return new ChallengeModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle, String tabName) {
        mChallenges = Parcels.unwrap(bundle.getParcelable(BundleKeys.CHALLENGE_LIST));
        mTagId = bundle.getInt(BundleKeys.CHALLENGE_TAG_ID);
        mTabName = tabName;
    }

    @Override
    public RecyclerView.Adapter getSwipeAdapter(Context context) {
        return mSwipeAdapter = new ChallengeAdapter(context, mChallenges, true, mTagId, mTabName);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(Context context) {
        return mListAdapter = new ChallengeAdapter(context, mChallenges, false, mTagId, mTabName);
    }

    public interface OnChallengeModelListener {

    }
}