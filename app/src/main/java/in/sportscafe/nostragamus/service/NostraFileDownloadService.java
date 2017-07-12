package in.sportscafe.nostragamus.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.jeeva.android.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import in.sportscafe.nostragamus.R;

/**
 * Created by sandip on 09/06/17.
 */

public class NostraFileDownloadService extends IntentService {

    private static final String TAG = NostraFileDownloadService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 101;

    /**
     * URL of the file to be downloaded should be passed with this key in service-intent.
     */
    public static final String FILE_DOWNLOAD_URL = "fileDownloadUrl";

    /**
     * Pass file-name with extension
     * Only file name should be passed as file will be downloaded to @{Download} dir of device
     * Send extracted filename from URL as best practice
     */
    public static final String FILE_NAME_WITH_EXTENSION = "fileName";

    /**
     * Check to verify that file was successfully downloaded
     */
    public static final int FILE_DOWNLOAD_SUCCESS = 100;

    /**
     * A default error case for any error for downloading file
     */
    public static final int FILE_DOWNLOAD_ERROR = -1;

    /**
     * Use to register broadcast
     *
     * NOTE: Do not change value OR also change in Manifest-receiver
     */
    public static final String FILE_DOWNLOAD_BROADCAST_ACTION = "in.sportscafe.nostragamus.broadcast.action.fileDownloadBroadcast";

    /**
     * Absolute path of file downloaded (if successful), use as key from broadcast intent
     */
    public static final String FILE_DOWNLOAD_ABSOLUTE_PATH = "fileAbsolutPath";

    /**
     * To get download result, use the given key from broadcast intent
     */
    public static final String FILE_DOWNLOAD_RESULT_PARAM = "resultParam";

    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;

    private long mDownloadStartTime;

    public NostraFileDownloadService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, TAG + " created");

        initNotification();
    }

    private void initNotification() {
        mNotificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("App Update")
                .setContentText("Download in progress...")
                .setSmallIcon(R.drawable.white_notification_icon)
        .setProgress(100, 0, false);

        mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        sendNotification();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "OnHandleIntent");

        if (intent != null) {
            int downloadResult = FILE_DOWNLOAD_ERROR;
            String urlPath = intent.getStringExtra(FILE_DOWNLOAD_URL);
            String fileName = intent.getStringExtra(FILE_NAME_WITH_EXTENSION);

            if (!TextUtils.isEmpty(urlPath) && !TextUtils.isEmpty(fileName)) {

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                if (file.exists()) {
                    file.delete();
                }

                downloadResult = downloadFile(urlPath, file);

                /* Send download operation result */
                publishResults(file.getAbsolutePath(), downloadResult);

                cancelNotification();
            } else {
                Log.d(TAG, "URL empty!");
            }
        }
    }

    private synchronized int downloadFile(String serverUrl, File file) {
        int result = FILE_DOWNLOAD_ERROR;

        try {
            /*String encodedUrl = CommonUtilities.getEncodedUrl(serverUrl);
            Log.d(TAG, "Downloading : " + serverUrl + "\n Encoded : " + encodedUrl);*/

            URL url = new URL(serverUrl);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setChunkedStreamingMode(10485760);  // 10MB chunk size
            urlConnection.connect();

            mDownloadStartTime = System.currentTimeMillis();

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(file);

            long fileContentTotalLength = urlConnection.getContentLength();

            byte buffer[] = new byte[1024];
            int bufferLength;
            long downloadLengthCompleted = 0;
            int lastPercent = 0;

            while ((bufferLength = input.read(buffer)) != -1) {
                downloadLengthCompleted += bufferLength;

                lastPercent = updateDownloadStatus(fileContentTotalLength, downloadLengthCompleted, lastPercent);

                output.write(buffer, 0, bufferLength);
            }

            output.flush();
            output.close();
            input.close();
            urlConnection.disconnect();

            Log.d(TAG, "Download Successful : " + file.getPath());
            result = FILE_DOWNLOAD_SUCCESS;
        } catch (Exception ex) {
            Log.e(TAG, "Error while downloading Files");

            if (file.exists()) {
                file.delete();
            }
            ex.printStackTrace();
        }

        return result;
    }

    private int updateDownloadStatus(long fileContentTotalLength, long downloadLengthCompleted, int lastPercent) {
        try {
            int downloadPercent = (int)(downloadLengthCompleted * 100 / fileContentTotalLength);
            if (lastPercent != downloadPercent) {
                lastPercent = downloadPercent;
                long remainingTime = calculateRemainingTime(downloadPercent);
                updateDownloadStatusOnNotification(downloadPercent, remainingTime);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lastPercent;
    }

    private long calculateRemainingTime(int downloadPer) {
        long remainingTime = -1;

        try {
            long elapsedTime = System.currentTimeMillis() - mDownloadStartTime;
            remainingTime = (100 - downloadPer) * elapsedTime / downloadPer;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return remainingTime;
    }

    private void updateDownloadStatusOnNotification(int downloadProgressPercent, long remainingTime) {
        if (mNotificationBuilder != null && mNotificationManager != null) {

            String msg = "Download in progress...";
            if (downloadProgressPercent > 0) {
                msg = downloadProgressPercent + "% downloaded";
            }
            if (remainingTime > 0) {
                try {
                    int minutes = (int) ((remainingTime / 1000) / 60);
                    int seconds = (int) (remainingTime / 1000) % 60;

                    msg = msg + ", " + minutes + ":" + seconds + " min left";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            mNotificationBuilder.setProgress(100, downloadProgressPercent, false);
            mNotificationBuilder.setContentText(msg);
            mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
        }
    }

    /**
     * Publish results once download completed, Broadcast to install package.
     * @param outputPath
     * @param result
     */
    private void publishResults(String outputPath, int result) {
        Intent intent = new Intent(FILE_DOWNLOAD_BROADCAST_ACTION);
        intent.putExtra(FILE_DOWNLOAD_ABSOLUTE_PATH, outputPath);
        intent.putExtra(FILE_DOWNLOAD_RESULT_PARAM, result);
        sendBroadcast(intent);
    }

    /**
     * Cancel downloading notification
     */
    private void cancelNotification() {
        if (getApplicationContext() != null) {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    /**
     * Send notification
     */
    private void sendNotification() {
        if (mNotificationBuilder != null && mNotificationManager != null) {
            mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
        }
    }
}
