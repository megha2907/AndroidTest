package in.sportscafe.nostragamus.cache;

import android.content.Context;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.db.ApiCacheType;
import in.sportscafe.nostragamus.db.DbConstants;
import in.sportscafe.nostragamus.db.NostragamusDatabase;
import in.sportscafe.nostragamus.db.tableDto.ApiCacheDbDto;
import in.sportscafe.nostragamus.db.tables.ApiCacheDbTable;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sc on 10/1/18.
 *
 * Used for cache mgt
 */

public class CacheManagementHelper {

    private static final String TAG = CacheManagementHelper.class.getSimpleName();

    /**
     * Fetch Inplay data from server ONCE, Game played ; and store into DB so that data accuracy mostly remains same
     *
     * The DB cached/saved data is used for:
     * 1. InApp Notifications
     * 2. Counter displayed at bottom-navigation-bar (Inplay tab)
     *
     * @param appContext
     */
    public void fetchInplayDataAndSaveIntoDb(final Context appContext) {
        Log.d(TAG, "Fetching InPlay data");

        MyWebService.getInstance().getInPlayChallenges().enqueue(new ApiCallBack<List<InPlayResponse>>() {
            @Override
            public void onResponse(Call<List<InPlayResponse>> call, Response<List<InPlayResponse>> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Inplay - Server response success");
                    insertInPlayResponseIntoDatabase(appContext, response.body());

                } else {
                    Log.d(TAG, "Inplay - Server response null");
                }
            }

            @Override
            public void onFailure(Call<List<InPlayResponse>> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Inplay - Server response Failed");
            }
        });
    }

    private void insertInPlayResponseIntoDatabase(final Context appContext,
                                                  final List<InPlayResponse> inPlayResponses) {

        final ApiCacheDbDto dbDto = new ApiCacheDbDto();
        dbDto.setApiCacheType(ApiCacheType.IN_PLAY_API);
        dbDto.setTimeStamp(System.currentTimeMillis());
        dbDto.setApiContent(new Gson().toJson(inPlayResponses));

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

    /**
     *
     * @param appContext
     * @param listener
     * Returns Inplay cached/saved Data
     */
    public void loadInPlayFromDatabase(final Context appContext, final InPlayDataProvider.InPlayDataProviderListener listener) {
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

                        return new Gson().fromJson(apiContentStr, new TypeReference<List<InPlayResponse>>() {}.getType());
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
                        listener.onData(Constants.DataStatus.FROM_DATABASE_CACHED_DATA, inPlayResponses);
                    } else {
                        listener.onData(Constants.DataStatus.FROM_DATABASE_ERROR, null);
                    }
                }
            }
        }.execute();
    }
}
