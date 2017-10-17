package in.sportscafe.nostragamus.utils;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

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

    public static void printBundleValues(Bundle bundle, String tag) {
        if (bundle != null) {
            StringBuilder str = new StringBuilder();
                Set<String> keys = bundle.keySet();
                Iterator<String> it = keys.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    str.append(key);
                    str.append(":");
                    str.append(bundle.get(key));
                    str.append("\n\r");
                }
                Log.d(tag, str.toString());
            }
    }

}