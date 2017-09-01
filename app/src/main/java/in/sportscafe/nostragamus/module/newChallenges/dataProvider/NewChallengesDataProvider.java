package in.sportscafe.nostragamus.module.newChallenges.dataProvider;

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
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesRequest;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 24/08/17.
 */

public class NewChallengesDataProvider {

    private static final String TAG = NewChallengesDataProvider.class.getSimpleName();

    public NewChallengesDataProvider() {
    }

    public void getChallenges(Context appContext, ChallengesDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadChallengesFromServer(appContext, listener);
        } else {
            Log.d(TAG, "No Network connection");
            loadChallengesFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET, appContext, listener);
        }
    }

    private void loadChallengesFromServer(final Context appContext, final ChallengesDataProviderListener listener) {
        NewChallengesRequest request = new NewChallengesRequest();

        MyWebService.getInstance().getNewHomeChallenges(request).enqueue(new ApiCallBack<List<NewChallengesResponse>>() {
            @Override
            public void onResponse(Call<List<NewChallengesResponse>> call, Response<List<NewChallengesResponse>> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    insertChallengesIntoDatabase(appContext, response.body(), listener);
                    if (listener != null) {
                        listener.onData(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body());
                    }
                } else {
                    Log.d(TAG, "Server response null");
                    loadChallengesFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED, appContext, listener);
                }
            }

            @Override
            public void onFailure(Call<List<NewChallengesResponse>> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
                loadChallengesFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED, appContext, listener);
            }
        });
    }

    private void insertChallengesIntoDatabase(final Context appContext,
                                              final List<NewChallengesResponse> challengesResponse,
                                              final ChallengesDataProviderListener listener) {

        final ApiCacheDbDto dbDto = new ApiCacheDbDto();
        dbDto.setApiCacheType(ApiCacheType.NEW_CHALLENGE_API);
        dbDto.setTimeStamp(System.currentTimeMillis());
        dbDto.setApiContent(MyWebService.getInstance().getJsonStringFromObject(challengesResponse));

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

    private void loadChallengesFromDatabase(final int status, final Context appContext, final ChallengesDataProviderListener listener) {
        int apiType = ApiCacheType.NEW_CHALLENGE_API;

        final String whereClause = ApiCacheDbTable.TableFields.API_TYPE + "=?";
        final String[] whereArgs = { String.valueOf(apiType) };

        new AsyncTask<Void, Void, List<NewChallengesResponse>>() {

            @Override
            protected List<NewChallengesResponse> doInBackground(Void... params) {
                try {
                    List<ApiCacheDbDto> dbResponseList = (List<ApiCacheDbDto>) NostragamusDatabase.getInstance(appContext).
                            select(DbConstants.TableIds.API_CACHE_TABLE, whereClause, whereArgs);

                    if (dbResponseList != null && dbResponseList.size() > 0 && dbResponseList.get(0) != null) {
                        String apiContentStr = dbResponseList.get(0).getApiContent();

                        return MyWebService.getInstance().getObjectFromJson
                                (apiContentStr, new TypeReference<List<NewChallengesResponse>>() {});
                    }

                } catch (Exception ex) {}
                return null;
            }

            @Override
            protected void onPostExecute(List<NewChallengesResponse> challengesResponse) {
                super.onPostExecute(challengesResponse);
                Log.d(TAG, "DB response : " + challengesResponse);

                if (listener != null) {
                    if (challengesResponse != null) {
                        listener.onData(status, challengesResponse);
                    } else {
                        listener.onError(Constants.DataStatus.FROM_DATABASE_ERROR);
                    }
                }
            }
        }.execute();
    }

    public interface ChallengesDataProviderListener {
        void onData(int status, @Nullable List<NewChallengesResponse> newChallengesResponseDataList);
        void onError(int status);
    }
}
