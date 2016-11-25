package in.sportscafe.nostragamus.module.user.badges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import in.sportscafe.nostragamus.NostragamusDataHandler;

public class BadgeModelImpl implements BadgeModel {

    private NostragamusDataHandler mNostragamusDataHandler;

    private BadgeAdapter mBadgeAdapter;

    private BadgeModelListener mBadgeModelListener;

    protected BadgeModelImpl(BadgeModelListener modelListener) {
        this.mBadgeModelListener = modelListener;
        this.mNostragamusDataHandler = NostragamusDataHandler.getInstance();
    }

    public static BadgeModel newInstance(BadgeModelListener modelListener) {
        return new BadgeModelImpl(modelListener);
    }

    @Override
    public RecyclerView.Adapter getBadgeAdapter(Context context) {

        if(mNostragamusDataHandler.getBadgeList().isEmpty()){

            mBadgeModelListener.onBadgesEmpty();
        }


        mBadgeAdapter = new BadgeAdapter(context,
                mNostragamusDataHandler.getBadgeList());
        return mBadgeAdapter;
    }


    public interface BadgeModelListener {

        void onNoInternet();

        void onFailed(String message);

        void onBadgesEmpty();
    }
}