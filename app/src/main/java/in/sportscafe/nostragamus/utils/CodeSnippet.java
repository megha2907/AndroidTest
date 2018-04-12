package in.sportscafe.nostragamus.utils;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import in.sportscafe.nostragamus.Constants;

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

    public static int convertStringToInt(String str) {
        int result = -1;

        if (!TextUtils.isEmpty(str)) {
            try {
                result = Integer.parseInt(str);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }

    public static String formatUrl(String url) {
        if (null != url) {
            url = url.replaceAll(" ", "%20");
        }
        return url;
    }

    /**
     *
     * @param amount
     * @return Formatted string into Indian amount
     */
    public static String getFormattedAmount(double amount) {
        String amtStr = String.valueOf(amount);
        try {
            DecimalFormat formatter = new DecimalFormat(Constants.AMOUNT_INDIAN_FORMAT_PATTERN);
            amtStr = formatter.format(amount);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return amtStr;
    }

    public static String getFormattedPercentage(float value) {
        String amtStr = String.valueOf(value);
        try {
            DecimalFormat formatter = new DecimalFormat(Constants.PERCENTAGE_PATTERN);
            amtStr = formatter.format(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return amtStr;
    }

}