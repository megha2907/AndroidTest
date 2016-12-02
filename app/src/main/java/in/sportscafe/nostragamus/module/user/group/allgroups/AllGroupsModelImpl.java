package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.group.allgroups.dto.AllGroupsResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by deepanshi on 9/27/16.
 */

public class AllGroupsModelImpl implements AllGroupsModel{

    private NostragamusDataHandler mNostragamusDataHandler;

    private AllGroupsAdapter mAllGroupsAdapter;

    private AllGroupsModelImpl.AllGroupsModelListener mAllGroupsModelListener;

    protected AllGroupsModelImpl(AllGroupsModelImpl.AllGroupsModelListener modelListener) {
        this.mAllGroupsModelListener = modelListener;
        this.mNostragamusDataHandler = NostragamusDataHandler.getInstance();
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
                new NostragamusCallBack<AllGroupsResponse>() {
                    @Override
                    public void onResponse(Call<AllGroupsResponse> call, Response<AllGroupsResponse> response) {
                        super.onResponse(call, response);
                        if(response.isSuccessful()) {

                            List<AllGroups> newAllGroups = response.body().getAllGroups();
                            if (null != newAllGroups && newAllGroups.size() > 0) {
                                List<AllGroups> oldAllGroupsList = NostragamusDataHandler.getInstance().getAllGroups();
                                oldAllGroupsList.clear();
                                for (AllGroups allGroups : newAllGroups) {
                                    if (!oldAllGroupsList.contains(allGroups)) {
                                        oldAllGroupsList.add(allGroups);
                                    }
                                }
                                NostragamusDataHandler.getInstance().setAllGroups(oldAllGroupsList);
                                NostragamusDataHandler.getInstance().setNumberofGroups(oldAllGroupsList.size());


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

        if(mNostragamusDataHandler.getAllGroups().isEmpty()){

            mAllGroupsModelListener.onAllGroupsEmpty();
        }

        mAllGroupsAdapter = new AllGroupsAdapter(context,
                mNostragamusDataHandler.getAllGroups());
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
