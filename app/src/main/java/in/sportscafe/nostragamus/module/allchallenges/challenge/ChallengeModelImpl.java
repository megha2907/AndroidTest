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

    private ChallengeAdapter mChallengeAdapter;

    private OnChallengeModelListener mChallengeModelListener;

    private ChallengeModelImpl(OnChallengeModelListener listener) {
        mChallengeModelListener = listener;
    }

    public static ChallengeModel newInstance(OnChallengeModelListener listener) {
        return new ChallengeModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        mChallenges = Parcels.unwrap(bundle.getParcelable(BundleKeys.CHALLENGE_LIST));
    }

    @Override
    public RecyclerView.Adapter getAdapter(Context context) {
        return mChallengeAdapter = new ChallengeAdapter(context, mChallenges);
    }

    public interface OnChallengeModelListener {

    }
}