package in.sportscafe.scgame;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;

import java.util.HashMap;
import java.util.regex.Pattern;

import in.sportscafe.scgame.utils.timeutils.TimeUtils;
import in.sportscafe.scgame.webservice.MyWebService;


/**
 * Created by Jeeva on 27/5/15.
 */
public class AppSnippet implements Constants {

    public static String getTimeAgo(String serverDate) {
        return TimeUtils.getTimeAgoString(serverDate, DateFormats.FORMAT_DATE_T_TIME_ZONE, DateFormats.GMT);
    }

    public static String ifNullMakeEmpty(String value) {
        return null == value ? "" : value;
    }

    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static Intent getGeneralShareIntent(String shareText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    public static void doGeneralShare(Context context, String shareText) {
        context.startActivity(getGeneralShareIntent(shareText));
    }

    public static void copyToClipBoard(Context context, String copiedText) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Copied Text", copiedText);
        clipboardManager.setPrimaryClip(clipData);
    }

    public static <T> HashMap<String, T> convertStringToHashMap(String text, Class<T> classType) {
        HashMap<String, T> data = new HashMap<>();
        Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
        String[] split = p.split(text);
        for (int i = 1; i + 2 <= split.length; i += 2) {
            data.put(split[i], MyWebService.getInstance().getObjectFromJson(split[i + 1], classType));
        }
        return data;
    }

    public static String getClassName(Class<?> classType) {
        String className = classType.getSimpleName();
        return className.substring(className.lastIndexOf(".") + 1);
    }

    public static void closeApp() {
        try {
            System.exit(1);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}