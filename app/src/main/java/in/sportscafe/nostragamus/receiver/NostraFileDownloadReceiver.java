package in.sportscafe.nostragamus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.jeeva.android.Log;

import java.io.File;

import in.sportscafe.nostragamus.service.NostraFileDownloadService;

/**
 * Created by sandip on 09/06/17.
 */

public class NostraFileDownloadReceiver extends BroadcastReceiver {

    private static final String TAG = NostraFileDownloadReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "File Download Broadcast Received...");

        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(NostraFileDownloadService.FILE_DOWNLOAD_BROADCAST_ACTION)) {

                int result = intent.getIntExtra(NostraFileDownloadService.FILE_DOWNLOAD_RESULT_PARAM, -1);
                String fileAbsolutePath = intent.getStringExtra(NostraFileDownloadService.FILE_DOWNLOAD_ABSOLUTE_PATH);

                if (result == NostraFileDownloadService.FILE_DOWNLOAD_SUCCESS && !TextUtils.isEmpty(fileAbsolutePath)) {
                    File file = new File(fileAbsolutePath);
                    if (file.exists()) {
                        updateApp(context, file);
                        return;
                    }
                }
            }
        }

        Log.d(TAG, " Something went wrong, could not install package!");
    }

    /**
     * Update App by launching package-manger action
     * @param context App Context
     * @param file - APK file
     */
    private void updateApp(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }
}
