package in.sportscafe.scgame.module.user.group.allgroups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import in.sportscafe.scgame.ScGameDataHandler;


/**
 * Created by deepanshi on 9/27/16.
 */

public class AllGroupsModelImpl implements AllGroupsModel{

    private ScGameDataHandler mScGameDataHandler;

    private AllGroupsAdapter mAllGroupsAdapter;

    private AllGroupsModelImpl.AllGroupsModelListener mAllGroupsModelListener;

    protected AllGroupsModelImpl(AllGroupsModelImpl.AllGroupsModelListener modelListener) {
        this.mAllGroupsModelListener = modelListener;
        this.mScGameDataHandler = ScGameDataHandler.getInstance();
    }

    public static AllGroupsModel newInstance(AllGroupsModelImpl.AllGroupsModelListener modelListener) {
        return new AllGroupsModelImpl(modelListener);
    }

    @Override
    public RecyclerView.Adapter getAllGroupsAdapter(Context context) {

//        mAllGroupsAdapter = new AllGroupsAdapter(context,
//                mScGameDataHandler.getFavoriteSportsIdList());
        return mAllGroupsAdapter;
    }


    public interface AllGroupsModelListener {

        void onNoInternet();

        void onFailed(String message);
    }
}
