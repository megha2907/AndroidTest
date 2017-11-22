package in.sportscafe.nostragamus.module.challengeCompleted.dataProvider;

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
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedResponse;
import in.sportscafe.nostragamus.module.challengeCompleted.ui.CompletedChallengeHistoryFragment;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompletedChallengeDataProvider {

    private static final String TAG = CompletedChallengeDataProvider.class.getSimpleName();

    public CompletedChallengeDataProvider() {
    }

    public void getCompletedChallenges(Context appContext, int skip, int limit,
                                       CompletedChallengeDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadCompletedChallengeDataFromServer(appContext, skip, limit, listener);
        } else {
            Log.d(TAG, "No Network connection");
            loadCompletedDataFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET, appContext, listener);
        }
    }

    private void loadCompletedChallengeDataFromServer(final Context appContext, final int skip, final int limit,
                                                      final CompletedChallengeDataProviderListener listener) {

        MyWebService.getInstance().getCompletedChallenges(skip, limit).enqueue(new ApiCallBack<List<CompletedResponse>>() {
            @Override
            public void onResponse(Call<List<CompletedResponse>> call, Response<List<CompletedResponse>> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Server response success");

                    /* Save only first page as first page always contains updated and recent history
                     * Also, older history need not to be saved. In case user wants older history,
                      * he can load but caching of those data not required */
                    if (skip == 0) {
                        insertCompletedResponseIntoDatabase(appContext, response.body(), listener);
                    }

                    if (listener != null) {
                        listener.onData(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body());
                    }
                } else {
                    Log.d(TAG, "Server response not successful");
                    loadCompletedDataFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED, appContext, listener);
                }
            }

            @Override
            public void onFailure(Call<List<CompletedResponse>> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");

                /* If no data is loaded then ONLY cached data should be provided
                 * else send error */
                if (skip == 0) {
                    loadCompletedDataFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED, appContext, listener);
                } else {
                    if (listener != null) {
                        listener.onError(Constants.DataStatus.NO_MORE_DATA_WHILE_LOAD_MORE);    // As this case occurs only in pagination, if API fails, just show msg with no more-data
                    }
                }
            }
        });
    }

    private void insertCompletedResponseIntoDatabase(final Context appContext,
                                                     final List<CompletedResponse> completedResponses,
                                                     final CompletedChallengeDataProvider.CompletedChallengeDataProviderListener listener) {

        final ApiCacheDbDto dbDto = new ApiCacheDbDto();
        dbDto.setApiCacheType(ApiCacheType.COMPLETED_CHALLENGE_API);
        dbDto.setTimeStamp(System.currentTimeMillis());
        dbDto.setApiContent(MyWebService.getInstance().getJsonStringFromObject(completedResponses));

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                NostragamusDatabase.getInstance(appContext).insert(DbConstants.TableIds.API_CACHE_TABLE, dbDto);
                return null;
            }

            @Override
            protected void onPostExecute(Void obj) {
                super.onPostExecute(obj);
            }
        }.execute();
    }

    private void loadCompletedDataFromDatabase(final int status, final Context appContext, final CompletedChallengeDataProvider.CompletedChallengeDataProviderListener listener) {
        int apiType = ApiCacheType.COMPLETED_CHALLENGE_API;

        final String whereClause = ApiCacheDbTable.TableFields.API_TYPE + "=?";
        final String[] whereArgs = { String.valueOf(apiType) };

        new AsyncTask<Void, Void, List<CompletedResponse>>() {

            @Override
            protected List<CompletedResponse> doInBackground(Void... params) {
                try {
                    List<ApiCacheDbDto> dbResponseList = (List<ApiCacheDbDto>) NostragamusDatabase.getInstance(appContext).
                            select(DbConstants.TableIds.API_CACHE_TABLE, whereClause, whereArgs);

                    if (dbResponseList != null && dbResponseList.size() > 0 && dbResponseList.get(0) != null) {
                        String apiContentStr = dbResponseList.get(0).getApiContent();

                        return MyWebService.getInstance().getObjectFromJson
                                (apiContentStr, new TypeReference<List<CompletedResponse>>() {});
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<CompletedResponse> completedResponseList) {
                super.onPostExecute(completedResponseList);
                Log.d(TAG, "DB response : " + completedResponseList);

                if (listener != null) {
                    if (completedResponseList != null) {
                        listener.onData(status, completedResponseList);
                    } else {
                        listener.onError(Constants.DataStatus.FROM_DATABASE_ERROR);
                    }
                }
            }
        }.execute();
    }

    public interface CompletedChallengeDataProviderListener {
        void onData(int status, @Nullable List<CompletedResponse> completedResponseList);
        void onError(int status);
    }
}
