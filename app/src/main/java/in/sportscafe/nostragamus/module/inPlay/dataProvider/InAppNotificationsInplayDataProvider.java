package in.sportscafe.nostragamus.module.inPlay.dataProvider;

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
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;

/**
 * Created by sc on 5/1/18.
 */

public class InAppNotificationsInplayDataProvider {
    private final String TAG = InAppNotificationsInplayDataProvider.class.getSimpleName();

    public void loadInPlayFromDatabase(final Context appContext,
                                        final InPlayDataProvider.InPlayDataProviderListener listener) {
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
