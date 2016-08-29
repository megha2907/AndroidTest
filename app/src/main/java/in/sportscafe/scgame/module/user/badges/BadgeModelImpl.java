package in.sportscafe.scgame.module.user.badges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.jeeva.android.Log;

import in.sportscafe.scgame.ScGameDataHandler;

public class BadgeModelImpl implements BadgeModel {

    private ScGameDataHandler mScGameDataHandler;

    private BadgeAdapter mBadgeAdapter;

    private BadgeModelListener mBadgeModelListener;

    protected BadgeModelImpl(BadgeModelListener modelListener) {
        this.mBadgeModelListener = modelListener;
        this.mScGameDataHandler = ScGameDataHandler.getInstance();
    }

    public static BadgeModel newInstance(BadgeModelListener modelListener) {
        return new BadgeModelImpl(modelListener);
    }

    @Override
    public RecyclerView.Adapter getBadgeAdapter(Context context) {

        mBadgeAdapter = new BadgeAdapter(context,
                mScGameDataHandler.getBadgeList());
        return mBadgeAdapter;
    }


    public interface BadgeModelListener {

        void onNoInternet();

        void onFailed(String message);
    }
}