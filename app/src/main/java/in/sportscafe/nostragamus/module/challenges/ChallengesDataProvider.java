package in.sportscafe.nostragamus.module.challenges;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.db.ApiCacheType;
import in.sportscafe.nostragamus.db.DbConstants;
import in.sportscafe.nostragamus.db.NostragamusDatabase;
import in.sportscafe.nostragamus.db.tableDto.ApiCacheDbDto;
import in.sportscafe.nostragamus.db.tables.ApiCacheDbTable;
import in.sportscafe.nostragamus.module.allchallenges.dto.AllChallengesResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 24/08/17.
 */

public class ChallengesDataProvider {

    private static final String TAG = ChallengesDataProvider.class.getSimpleName();

    public interface ChallengeFilter {
        String CURRENT = "current";
        String COMPLETED = "completed";
    }

    public ChallengesDataProvider() {
    }

    public void getCurrentChallenges(Context appContext, ChallengesDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadChallengesFromServer(appContext, ChallengeFilter.CURRENT, listener);
        } else {
            loadChallengesFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET, appContext, ChallengeFilter.CURRENT, listener);
        }
    }

    public void getCompletedChallenges(Context appContext, ChallengesDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadChallengesFromServer(appContext, ChallengeFilter.COMPLETED, listener);
        } else {
            loadChallengesFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET, appContext, ChallengeFilter.COMPLETED, listener);
        }
    }

    private void loadChallengesFromServer(final Context appContext, final String filter, final ChallengesDataProviderListener listener) {
        MyWebService.getInstance().getAllChallengesRequest(filter).enqueue(
                new ApiCallBack<AllChallengesResponse>() {
                    @Override
                    public void onResponse(Call<AllChallengesResponse> call, Response<AllChallengesResponse> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful() && response.body() != null && response.body().getResponse() != null) {
                            Log.d(TAG, "Server response success");
                            insertChallengesIntoDatabase(appContext, filter, response.body(), listener);
                            if (listener != null) {
                                listener.onData(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body());
                            }
                        } else {
                            Log.d(TAG, "Server response null");
                            loadChallengesFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED, appContext, filter, listener);
                        }
                    }

                    @Override
                    public void onFailure(Call<AllChallengesResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        Log.d(TAG, "Server response Failed");
                        loadChallengesFromDatabase(Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED, appContext, filter, listener);
                    }
                }
        );
    }

    private void insertChallengesIntoDatabase(final Context appContext,
                                              String filter,
                                              final AllChallengesResponse challengesResponse,
                                              final ChallengesDataProviderListener listener) {

        final ApiCacheDbDto dbDto = new ApiCacheDbDto();
        if (filter.equals(ChallengeFilter.CURRENT)) {
            dbDto.setApiCacheType(ApiCacheType.CURRENT_CHALLENGE_API);
        } else if (filter.equals(ChallengeFilter.CURRENT)) {
            dbDto.setApiCacheType(ApiCacheType.COMPLETED_CHALLENGE_API);
        }
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

    private void loadChallengesFromDatabase(final int status, final Context appContext, String filter, final ChallengesDataProviderListener listener) {

        final String whereClause = ApiCacheDbTable.TableFields.API_TYPE + "=?";

        int apiType = ApiCacheType.CURRENT_CHALLENGE_API;

        if (filter.equals(ChallengeFilter.CURRENT)) {
            apiType = ApiCacheType.CURRENT_CHALLENGE_API;
        } else if (filter.equals(ChallengeFilter.COMPLETED)) {
            apiType = ApiCacheType.COMPLETED_CHALLENGE_API;
        }

        final String[] whereArgs = { String.valueOf(apiType) };

        new AsyncTask<Void, Void, AllChallengesResponse>() {

            @Override
            protected AllChallengesResponse doInBackground(Void... params) {
                String apiContentStr = (String) NostragamusDatabase.getInstance(appContext).
                        select(DbConstants.TableIds.API_CACHE_TABLE, whereClause, whereArgs);
                return MyWebService.getInstance().getObjectFromJson(apiContentStr, AllChallengesResponse.class);
            }

            @Override
            protected void onPostExecute(AllChallengesResponse challengesResponse) {
                super.onPostExecute(challengesResponse);
                if (listener != null) {
                    listener.onData(status, challengesResponse);
                }
            }
        }.execute();
    }

    public interface ChallengesDataProviderListener {
        void onData(int status, @Nullable AllChallengesResponse challengeData);
        void onError(int status);
    }
}
