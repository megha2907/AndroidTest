package in.sportscafe.nostragamus.utils;


import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.jeeva.android.ExceptionTracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by Jeeva on 27/5/15.
 */
public class CodeSnippet {

    public static void writeDataInFile(String filePath, String data) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println(data);
            printWriter.close();
        } catch (FileNotFoundException e) {
            ExceptionTracker.track(e);
        } catch (IOException e) {
            ExceptionTracker.track(e);
        }
    }

    public static <T> boolean isServiceRunning(Context context, Class<T> classType) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (classType.getName().equals(service.service.getClassName())) {
                //your service is running
                return true;
            }
        }
        return false;
    }

}