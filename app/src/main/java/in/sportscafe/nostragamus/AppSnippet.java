package in.sportscafe.nostragamus;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;


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

    public static void doGeneralImageShare(Context context, Bitmap image, String shareText){
        /*Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
        shareIntent.setType("image/png");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(shareIntent);*/

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "title");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream outstream = contentResolver.openOutputStream(uri);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_TITLE, "Title");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(shareIntent);
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

    public static String ordinal(int i) {
        return i + ordinalOnly(i);
    }

    public static String ordinalOnly(int i) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return "th";
            default:
                return sufixes[i % 10];
        }
    }

    public static Spanned formatHtml(String message) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(message);
        }
    }

    public static AlertDialog getAlertDialog(Context context, String title, String message,
                                             String positiveButton, View.OnClickListener positiveListener,
                                             String negativeButton, View.OnClickListener negativeListener,
                                             CompoundButton.OnCheckedChangeListener dontShowListener) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);

        ((TextView) dialogView.findViewById(R.id.dialog_tv_title)).setText(title);

        ((TextView) dialogView.findViewById(R.id.dialog_tv_message)).setText(formatHtml(message));

        Button button = (Button) dialogView.findViewById(R.id.dialog_btn_positive);
        button.setText(positiveButton);
        button.setOnClickListener(positiveListener);

        button = (Button) dialogView.findViewById(R.id.dialog_btn_negative);
        if(null != positiveButton) {
            button.setText(negativeButton);
        } else {
            button.setVisibility(View.GONE);
        }

        if(null != negativeListener) {
            button.setOnClickListener(negativeListener);
        }

        CheckBox checkBox = (CheckBox) dialogView.findViewById(R.id.dialog_tv_show_again);
        if(null != dontShowListener) {
            checkBox.setOnCheckedChangeListener(dontShowListener);
        } else {
            checkBox.setVisibility(View.GONE);
        }

        return new AlertDialog.Builder(context).setView(dialogView).create();
    }

    public static AlertDialog getAlertDialog(Context context, String title, String message,
                                             String positiveButton, View.OnClickListener positiveListener) {
        return getAlertDialog(context, title, message, positiveButton, positiveListener, null, null, null);
    }

    public static File saveBitmap(Bitmap bitmap, String name) {
        try {
            File mFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            File imgFile = new File(mFolder.getAbsolutePath(), name + ".png");

            if (imgFile.exists()) {
                imgFile.delete();
            }
            imgFile.createNewFile();

            FileOutputStream output = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();

            return imgFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap compressBitmap(Bitmap original, int maxSize) {
        int width = original.getWidth();
        int height = original.getHeight();
        int scale = 1;
        if (height > maxSize || width > maxSize) {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(maxSize /
                    (double) Math.max(height, width)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] bitmapData = out.toByteArray();
        Bitmap compressed = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length, options);

        original.recycle();

        return compressed;
    }

    public static DisplayMetrics getDisplayDimension(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public static File getInternalFolder(Context context, String folderName) {
        File file = context.getDir(folderName, Context.MODE_PRIVATE);
        if(!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static File getInternalFolder(Context context, String mainFolder, String subFolder) {
        File file = new File(getInternalFolder(context, mainFolder), subFolder);
        if(!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static CharSequence noTrailingwhiteLines(CharSequence text) {
        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }

    public static byte[] streamToBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        inputStream.close();
        return buffer.toByteArray();
    }

    public static String formatIfPlural(int number, String label, String pluralTerm) {
        return number + "\n" + label + (number > 1 ? pluralTerm : "");
    }
}