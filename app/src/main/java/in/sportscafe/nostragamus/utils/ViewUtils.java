package in.sportscafe.nostragamus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.jeeva.android.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.sportscafe.nostragamus.animator.SlideInUpAnimationAdapter;
import in.sportscafe.nostragamus.module.coachmarker.BasicOverlay;
import in.sportscafe.nostragamus.module.coachmarker.TargetView;
import in.sportscafe.nostragamus.module.coachmarker.TourGuide;

/**
 * Created by Jeeva on 17/6/16.
 */
public class ViewUtils {

    static class DialogListAdapter extends ArrayAdapter<String> implements ListAdapter {

        public DialogListAdapter(Context context, List<String> options) {
            super(context, android.R.layout.simple_list_item_1, options);
        }
    }

    public static AlertDialog getDialogList(Context context, List<String> options,
                                            DialogInterface.OnClickListener clickListener) {
        return new AlertDialog.Builder(context)
                .setAdapter(new DialogListAdapter(context, options), clickListener)
                .create();
    }

    public static AlertDialog getAlertDialog(Context context, String message,
                                             String positiveButton,
                                             DialogInterface.OnClickListener positiveListener,
                                             String negativeButton,
                                             DialogInterface.OnClickListener negativeListener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(positiveButton, positiveListener)
                .setNegativeButton(negativeButton, negativeListener)
                .create();
    }

    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap cutPartOfBitmap(Bitmap bitmap, Rect cutRect) {
        /*Bitmap afterCut = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight() - cutRect.bottom - cutRect.top,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(afterCut);
        canvas.drawBitmap(bitmap, 0, 0, null);

        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRect(cutRect, p);*/

        Log.d("Bitmap size", "Width --> " + bitmap.getWidth() + "  Height --> " + bitmap.getHeight());
        Log.d("Cut size", cutRect.toString());

        return Bitmap.createBitmap(
                bitmap,
                cutRect.left,
                cutRect.top,
                cutRect.right - cutRect.left,
                cutRect.bottom - cutRect.top
        );

//        return afterCut;
    }

    public static Bitmap getScreenshotFromRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
//                holder.itemView.setDrawingCacheEnabled(false);
//                holder.itemView.destroyDrawingCache();
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }

        }
        return bigBitmap;
    }

    public static File saveBitmap(Bitmap bitmap) {
        try {
            File mFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            File imgFile = new File(mFolder.getAbsolutePath(), "fb.png");
            if (!imgFile.exists()) {
                imgFile.createNewFile();
            }

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

    public static RecyclerView.Adapter getAnimationAdapter(RecyclerView.Adapter adapter) {
        SlideInUpAnimationAdapter animationAdapter = new SlideInUpAnimationAdapter(adapter);
        animationAdapter.setFirstOnly(true);
        animationAdapter.setDuration(750);
        animationAdapter.setInterpolator(new DecelerateInterpolator(1f));
        return animationAdapter;
//        return adapter;
    }

    public static int getColor(Context context, int resColor) {
        return ContextCompat.getColor(context, resColor);
    }

    public static TourGuide showCoachMarker(Activity activity, ViewGroup mainView, int title, int description,
                                            boolean showAtTop, View.OnClickListener listener, TargetView... targetViews) {
        BasicOverlay basicOverlay = new BasicOverlay(activity);

        basicOverlay.setTitle(activity.getString(title));

        basicOverlay.setDesc(activity.getString(description));

        if(showAtTop) {
            basicOverlay.showAtTop();
        } else {
            basicOverlay.showAtBottom();
        }

//        if(null == mainView) {
            mainView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
//        }

        final ViewGroup finalMainView = mainView;
        final BasicOverlay finalOverlay = basicOverlay;
        final View.OnClickListener finalListener = listener;
        return TourGuide.init(Gravity.CENTER, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalMainView.removeView(finalOverlay);
                if(null != finalListener) {
                    finalListener.onClick(view);
                }
            }
        }).playOn(mainView, basicOverlay, targetViews);
    }
}