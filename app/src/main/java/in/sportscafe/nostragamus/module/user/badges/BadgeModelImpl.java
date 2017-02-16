package in.sportscafe.nostragamus.module.user.badges;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;

import static org.parceler.Parcels.unwrap;

public class BadgeModelImpl implements BadgeModel {

    private BadgeModelListener mBadgeModelListener;

    protected BadgeModelImpl(BadgeModelListener modelListener) {
        this.mBadgeModelListener = modelListener;
    }

    public static BadgeModel newInstance(BadgeModelListener modelListener) {
        return new BadgeModelImpl(modelListener);
    }

    @Override
    public void createAdapter(Context context, Bundle bundle) {
        List<Badge> badgeList = Parcels.unwrap(bundle.getParcelable(BundleKeys.BADGES));
        if(null == badgeList || badgeList.isEmpty()) {
            mBadgeModelListener.onBadgesEmpty();
            return;
        }

        mBadgeModelListener.onAdapterCreated(new BadgeAdapter(context, badgeList));
    }

    public interface BadgeModelListener {

        void onAdapterCreated(BadgeAdapter badgeAdapter);

        void onBadgesEmpty();
    }
}