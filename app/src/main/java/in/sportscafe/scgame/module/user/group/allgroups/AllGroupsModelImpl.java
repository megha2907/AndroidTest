package in.sportscafe.scgame.module.user.group.allgroups;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.jeeva.android.Log;

import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.user.group.allgroups.dto.AllGroupsResponse;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.webservice.GroupSummaryResponse;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;


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
    public void init() {
        getAllGroups();
    }


    private void getAllGroups() {
        MyWebService.getInstance().getAllGroupsRequest().enqueue(
                new ScGameCallBack<AllGroupsResponse>() {
                    @Override
                    public void onResponse(Call<AllGroupsResponse> call, Response<AllGroupsResponse> response) {
                        if(response.isSuccessful()) {

                            List<AllGroups> newAllGroups = response.body().getAllGroups();
                            if (null != newAllGroups && newAllGroups.size() > 0) {
                                List<AllGroups> oldAllGroupsList = ScGameDataHandler.getInstance().getAllGroups();
                                oldAllGroupsList.clear();
                                for (AllGroups allGroups : newAllGroups) {
                                    if (!oldAllGroupsList.contains(allGroups)) {
                                        oldAllGroupsList.add(allGroups);
                                    }
                                }
                                ScGameDataHandler.getInstance().setAllGroups(oldAllGroupsList);
                                ScGameDataHandler.getInstance().setNumberofGroups(oldAllGroupsList.size());


                            }

                            mAllGroupsModelListener.ongetAllGroupsSuccess();

                        } else {

                            mAllGroupsModelListener.ongetAllGroupsFailed(response.message());
                        }
                    }
                }
        );
    }


    @Override
    public RecyclerView.Adapter getAllGroupsAdapter(Context context) {

        if(mScGameDataHandler.getAllGroups().isEmpty()){

            mAllGroupsModelListener.onAllGroupsEmpty();
        }

        mAllGroupsAdapter = new AllGroupsAdapter(context,
                mScGameDataHandler.getAllGroups());
        return mAllGroupsAdapter;
    }


    public interface AllGroupsModelListener {

        void onNoInternet();

        void onFailed(String message);

        void onAllGroupsEmpty();

        void ongetAllGroupsSuccess();

        void ongetAllGroupsFailed(String message);
    }
}
