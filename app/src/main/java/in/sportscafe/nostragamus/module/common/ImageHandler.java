package in.sportscafe.nostragamus.module.common;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;

import com.jeeva.android.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by DevaBalaji on 17-Oct-15.
 */
public class ImageHandler {

    private final String TAG = this.getClass().getSimpleName();

    private static final int ORIENTATION_0 = 0;

    private static final int ORIENTATION_90 = 90;

    private static final int ORIENTATION_180 = 180;

    private static final int ORIENTATION_270 = 270;

    public interface ProcessImageListener {
        void onProcessSuccess(String imagePath, Bitmap processedImage);

        void onProcessFailed(Exception exception);
    }

    public static ImageHandler newInstance() {
        return new ImageHandler();
    }

    public void processAndSaveImageFromUri(final Context context, final Uri imageUri, final String saveFilePath,
                                           final int maxSize, final ProcessImageListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                processImage(context, null, imageUri, saveFilePath, maxSize, listener);
            }
        }).start();

    }

    private void processImage(Context context, Bitmap image, Uri imageUri, String saveFilePath,
                              int maxSize, ProcessImageListener listener) {
        try {
            Bitmap comBitmap = null;
            if(null == image) {
                comBitmap = compressBitmap(getPath(context, imageUri), maxSize);
            } else {
                comBitmap = compressBitmap(image, maxSize);
            }

            Bitmap rotBitmap = rotateBitmap(comBitmap, getRotateAngle(context, imageUri));
            saveBitmap(rotBitmap, saveFilePath); // compressed bitmap and stored into respective path
            listener.onProcessSuccess(saveFilePath, rotBitmap);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            listener.onProcessFailed(new Exception(e));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            listener.onProcessFailed(new Exception(e));
        } catch (IOException e) {
            e.printStackTrace();
            listener.onProcessFailed(new Exception(e));
        }
    }

    public Bitmap compressBitmap(String bitmapPath, int maxSize) throws IOException {

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(bitmapPath);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int width = o.outWidth;
        int height = o.outHeight;

        if(maxSize == -1) {
            maxSize = Math.max(width, height);
        }

        int scale = 1;
        if (height > maxSize || width > maxSize) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(maxSize /
                    (double) Math.max(height, width)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(bitmapPath);
        Bitmap b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();

        return b;
    }

    public Bitmap compressBitmap(Bitmap original, int maxSize) throws IOException {
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

    public Bitmap rotateBitmap(Bitmap bitmap, int rotateAngle) throws IOException {
        if (rotateAngle > -1) {
            Matrix mat = new Matrix();
            mat.setRotate(rotateAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        }
        return bitmap;
    }

    public int getRotateAngle(Context context, Uri imageUri) {
        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cur = context.getContentResolver().query(imageUri, orientationColumn, null, null, null);

        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }

        switch (orientation) {
            case ORIENTATION_0:
                return 0;
            case ORIENTATION_90:
                return 90;
            case ORIENTATION_180:
                return 180;
            case ORIENTATION_270:
                return 270;
        }
        return -1;
    }

    public void saveBitmap(Bitmap bitmap, String savePath) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(savePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Uri createImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "camera", null);
        return Uri.parse(path);
    }

    public Bitmap getCompressedBitmap(Bitmap bitmap, int maxSizeInBytes) {

        try {
            int scale = 1;
            while ((bitmap.getWidth() * bitmap.getHeight()) * (1 / Math.pow(scale, 2)) >
                    maxSizeInBytes) {
                scale++;
            }

            //Log.d(TAG, "scale = " + scale + ", orig-width: " + o.outWidth + ",orig-height: " + o.outHeight);

            Bitmap b = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            //InputStream in = mActivity.getContentResolver().openInputStream(bs);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(bs, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d(TAG, "1th scale operation dimenions - width: " + width + ",height: " + height);

                double y = Math.sqrt(maxSizeInBytes / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
                return b;
                /*Matrix matrix = new Matrix();

                matrix.postRotate(90);

                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);*/

            } else {
                b = BitmapFactory.decodeStream(bs);
            }
            bs.close();

            Log.d(TAG, "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public byte[] convertBitmapAsByteArray(Context context, Uri uri) {
        Bitmap bitmap = getBitmapFromUri(context, uri);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public byte[] convertBitmapAsByteArray(Context context, Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public String convertBitmapByteArrayAsString(byte[] bitmapByteArray) {
        return Base64.encodeToString(bitmapByteArray, Base64.DEFAULT);
    }

    public Bitmap getBitmapFromUri(Context context, Uri imageUri) {
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getBitmapFromFile(Context context, File file) {
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public String getPath(Context context, Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}