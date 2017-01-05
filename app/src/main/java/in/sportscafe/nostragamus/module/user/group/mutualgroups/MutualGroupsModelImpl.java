package in.sportscafe.nostragamus.module.user.group.mutualgroups;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroups;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsAdapter;
import in.sportscafe.nostragamus.module.user.group.allgroups.dto.AllGroupsResponse;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 1/4/17.
 */

public class MutualGroupsModelImpl implements MutualGroupsModel {

    private NostragamusDataHandler mNostragamusDataHandler;

    private MutualGroupsAdapter mMutualGroupsAdapter;

    private MutualGroupsModelImpl.MutualGroupsModelListener mMutualGroupsModelListener;

    protected MutualGroupsModelImpl(MutualGroupsModelImpl.MutualGroupsModelListener modelListener) {
        this.mMutualGroupsModelListener = modelListener;
        this.mNostragamusDataHandler = NostragamusDataHandler.getInstance();
    }

    public static MutualGroupsModel newInstance(MutualGroupsModelImpl.MutualGroupsModelListener modelListener) {
        return new MutualGroupsModelImpl(modelListener);
    }

    @Override
    public void init(Bundle bundle) {

        PlayerInfo playerInfo = (PlayerInfo) bundle.getSerializable(Constants.BundleKeys.PLAYERINFO);
        getMutualGroups(playerInfo);
    }


    private void getMutualGroups(PlayerInfo playerInfo) {

                            List<MutualGroups> newmutualGroups = playerInfo.getMutualGroups();
                            if (null != newmutualGroups && newmutualGroups.size() > 0) {
                                List<MutualGroups> oldMutualGroupsList = NostragamusDataHandler.getInstance().getMutualGroups();
                                oldMutualGroupsList.clear();
                                for (MutualGroups mutualGroups : newmutualGroups) {
                                    if (!oldMutualGroupsList.contains(mutualGroups)) {
                                        oldMutualGroupsList.add(mutualGroups);
                                    }
                                }
                                NostragamusDataHandler.getInstance().setMutualGroups(oldMutualGroupsList);
                                mMutualGroupsModelListener.ongetMutualGroupsSuccess();

                            }else {
                                mMutualGroupsModelListener.onMutualGroupsEmpty();
                            }
    }


    @Override
    public RecyclerView.Adapter getMutualGroupsAdapter(Context context) {

        if(mNostragamusDataHandler.getMutualGroups().isEmpty() || null == mNostragamusDataHandler.getMutualGroups()){

            mMutualGroupsModelListener.onMutualGroupsEmpty();
        }

        mMutualGroupsAdapter = new MutualGroupsAdapter(context,
                mNostragamusDataHandler.getMutualGroups());
        return mMutualGroupsAdapter;
    }


    public interface MutualGroupsModelListener {

        void onNoInternet();

        void onFailed(String message);

        void onMutualGroupsEmpty();

        void ongetMutualGroupsSuccess();

        void ongetMutualGroupsFailed(String message);
    }
}