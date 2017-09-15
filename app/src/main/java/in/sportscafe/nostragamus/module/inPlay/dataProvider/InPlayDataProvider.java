package in.sportscafe.nostragamus.module.inPlay.dataProvider;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.db.ApiCacheType;
import in.sportscafe.nostragamus.db.DbConstants;
import in.sportscafe.nostragamus.db.NostragamusDatabase;
import in.sportscafe.nostragamus.db.tableDto.ApiCacheDbDto;
import in.sportscafe.nostragamus.db.tables.ApiCacheDbTable;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayRequest;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 9/6/17.
 */

public class InPlayDataProvider {

    private static final String TAG = InPlayDataProvider.class.getSimpleName();

    public InPlayDataProvider() {
    }

    public void getInPlayChallenges(Context appContext, InPlayDataProvider.InPlayDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadInPlayDataFromServer(appContext, listener);
        } else {
            Log.d(TAG, "No Network connection");
            loadInPlayFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET, appContext, listener);
        }
    }

    private void loadInPlayDataFromServer(final Context appContext, final InPlayDataProvider.InPlayDataProviderListener listener) {

        MyWebService.getInstance().getInPlayChallenges().enqueue(new ApiCallBack<List<InPlayResponse>>() {
            @Override
            public void onResponse(Call<List<InPlayResponse>> call, Response<List<InPlayResponse>> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    insertInPlayResponseIntoDatabase(appContext, response.body(), listener);
                    if (listener != null) {
                        listener.onData(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body());
                    }
                } else {
                    Log.d(TAG, "Server response null");
                    loadInPlayFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED, appContext, listener);
                }
            }

            @Override
            public void onFailure(Call<List<InPlayResponse>> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
                loadInPlayFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED, appContext, listener);
            }
        });
    }

    private void insertInPlayResponseIntoDatabase(final Context appContext,
                                              final List<InPlayResponse> inPlayResponses,
                                              final InPlayDataProvider.InPlayDataProviderListener listener) {

        final ApiCacheDbDto dbDto = new ApiCacheDbDto();
        dbDto.setApiCacheType(ApiCacheType.IN_PLAY_API);
        dbDto.setTimeStamp(System.currentTimeMillis());
        dbDto.setApiContent(MyWebService.getInstance().getJsonStringFromObject(inPlayResponses));

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                long insertCount = NostragamusDatabase.getInstance(appContext).insert(DbConstants.TableIds.API_CACHE_TABLE, dbDto);
                Log.d(TAG, "Records inserted : " + insertCount);
                return null;
            }

            @Override
            protected void onPostExecute(Void obj) {
                super.onPostExecute(obj);
            }
        }.execute();
    }

    private void loadInPlayFromDatabase(final int status, final Context appContext, final InPlayDataProvider.InPlayDataProviderListener listener) {
        int apiType = ApiCacheType.IN_PLAY_API;

        final String whereClause = ApiCacheDbTable.TableFields.API_TYPE + "=?";
        final String[] whereArgs = { String.valueOf(apiType) };

        new AsyncTask<Void, Void, List<InPlayResponse>>() {

            @Override
            protected List<InPlayResponse> doInBackground(Void... params) {
                try {
                    List<ApiCacheDbDto> dbResponseList = (List<ApiCacheDbDto>) NostragamusDatabase.getInstance(appContext).
                            select(DbConstants.TableIds.API_CACHE_TABLE, whereClause, whereArgs);

                    if (dbResponseList != null && dbResponseList.size() > 0 && dbResponseList.get(0) != null) {
                        String apiContentStr = dbResponseList.get(0).getApiContent();

                        return MyWebService.getInstance().getObjectFromJson
                                (apiContentStr, new TypeReference<List<InPlayResponse>>() {});
                    }

                } catch (Exception ex) {}
                return null;
            }

            @Override
            protected void onPostExecute(List<InPlayResponse> inPlayResponses) {
                super.onPostExecute(inPlayResponses);
                Log.d(TAG, "DB response : " + inPlayResponses);

                if (listener != null) {
                    if (inPlayResponses != null) {
                        listener.onData(status, inPlayResponses);
                    } else {
                        listener.onError(Constants.DataStatus.FROM_DATABASE_ERROR);
                    }
                }
            }
        }.execute();
    }

    public interface InPlayDataProviderListener {
        void onData(int status, @Nullable List<InPlayResponse> inPlayResponseList);
        void onError(int status);
    }
}
