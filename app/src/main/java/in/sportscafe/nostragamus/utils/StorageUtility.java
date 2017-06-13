package in.sportscafe.nostragamus.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;


public class StorageUtility {

    private static final String TAG = StorageUtility.class.getSimpleName();
    public final static long SIZE_KB = 1024L;

    /**
     * @return - external storage available/mounted
     */
    public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long getExternalAvailableSpaceInBytes() {
        long availableSpace = -1L;
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return availableSpace;
    }

    public static long getExternalAvailableSpaceInKB(){
        return getExternalAvailableSpaceInBytes() / SIZE_KB;
    }

    private static String getPackageNameDirectory(Context context) {
        return context.getPackageName();
    }

    public static String getFileNameWithSuffix(String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }

    public static String getFileExtension(String fileName) {
        String fileExt = "";

        if (!TextUtils.isEmpty(fileName)) {
            fileExt = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
        }

        return fileExt;
    }

}
