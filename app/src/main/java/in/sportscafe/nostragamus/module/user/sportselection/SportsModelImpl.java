package in.sportscafe.nostragamus.module.user.sportselection;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.sportselection.dto.AllSports;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

public class SportsModelImpl implements SportsModel {

    private final String TAG = "SportsModelImpl";

    private NostragamusDataHandler mNostragamusDataHandler;

    private SportsModelListener mSportsModelListener;

    protected SportsModelImpl(SportsModelListener modelListener) {
        this.mSportsModelListener = modelListener;
        mNostragamusDataHandler = NostragamusDataHandler.getInstance();

//        fillInitialSports();
    }

    public static SportsModel newInstance(SportsModelListener modelListener) {
        return new SportsModelImpl(modelListener);
    }

    @Override
    public void getAllSportsFromServer() {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getAllSportsRequest().enqueue(
                    new NostragamusCallBack<AllSports>() {
                        @Override
                        public void onResponse(Call<AllSports> call, Response<AllSports> response) {
                            if(response.isSuccessful()) {
                                List<Sport> newSportList = response.body().getSports();

                                if(null != newSportList && newSportList.size() > 0) {
                                    mNostragamusDataHandler.setAllSports(newSportList);

                                    mSportsModelListener.onGetSportsSuccess();
                                }

                            } else {
                                mSportsModelListener.onGetSportsFailed();
                            }
                        }
                    }
            );
        } else {
            mSportsModelListener.onNoNetwork();
        }
    }

    @Override
    public List<Sport> getAllSportsFromLocal() {
        return mNostragamusDataHandler.getAllSports();
    }

    private void fillInitialSports() {
        if(!mNostragamusDataHandler.isInitialSportsAvailable()) {
            mNostragamusDataHandler.setAllSports(getInitialSportsJsonString());
            mNostragamusDataHandler.setInitialSportsAvailable(true);
        }
    }

    private String getInitialSportsJsonString() {
        return "[{\"sports_id\":1,\"sports_name\":\"Cricket\"},{\"sports_id\":2,\"sports_name\":\"Hockey\"},{\"sports_id\":3,\"sports_name\":\"Tennis\"},{\"sports_id\":4,\"sports_name\":\"Football\"},{\"sports_id\":5,\"sports_name\":\"Badminton\"},{\"sports_id\":6,\"sports_name\":\"Kabbadi\"},{\"sports_id\":7,\"sports_name\":\"Basketball\"},{\"sports_id\":8,\"sports_name\":\"Boxing\"}]";
    }

    public interface SportsModelListener {

        void onGetSportsSuccess();

        void onNoNetwork();

        void onGetSportsFailed();
    }
}