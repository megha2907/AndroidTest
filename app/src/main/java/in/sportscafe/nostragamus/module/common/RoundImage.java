package in.sportscafe.nostragamus.module.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.jeeva.android.widgets.HmImageView;

public class RoundImage  extends HmImageView  {

       public RoundImage(Context ctx, AttributeSet attrs) {
              super(ctx, attrs);
       }

       @Override
       protected void onDraw(Canvas canvas) {

              Drawable drawable = getDrawable();
              

              if (drawable == null) {
                     return;
              }

              if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                     return;
              }
              Bitmap b = getBitmap();

              int w = getWidth(), h = getHeight();

              Bitmap roundBitmap = getRoundedCroppedBitmap(b, w);
              //Bitmap shadowBitmap = addShadowToCircularBitmap(roundBitmap, 2, Color.BLACK);
              canvas.drawBitmap(roundBitmap, 0, 0, null);

       }

       public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
              Bitmap finalBitmap;
            /*  Bitmap bmp =(bitmap);
              if(bmp != null) {
              bmp.recycle();
              bmp = null;
              } */

              if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
                     finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
                                  false);

              else
                     finalBitmap = bitmap;

              Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                           finalBitmap.getHeight(), Config.ARGB_8888);
              Canvas canvas = new Canvas(output);

              final Paint paint = new Paint();
              final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                           finalBitmap.getHeight());

              paint.setAntiAlias(true);

              paint.setFilterBitmap(true);
              paint.setDither(true);
              canvas.drawARGB(0, 0, 0, 0);
              canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.8f,
                           finalBitmap.getHeight() / 2 + 0.6f,
                           finalBitmap.getWidth() / 2 + 0.1f, paint);
              paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
              canvas.drawBitmap(finalBitmap, rect, rect, paint);

              return output;
       }


       // Custom method to add a shadow around circular bitmap
       protected Bitmap addShadowToCircularBitmap(Bitmap srcBitmap, int shadowWidth, int shadowColor){
              // Calculate the circular bitmap width with shadow
              int dstBitmapWidth = srcBitmap.getWidth()+shadowWidth*2;
              Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);

              // Initialize a new Canvas instance
              Canvas canvas = new Canvas(dstBitmap);
              canvas.drawBitmap(srcBitmap, shadowWidth, shadowWidth, null);

              // Paint to draw circular bitmap shadow
              Paint paint = new Paint();
              paint.setColor(shadowColor);
              paint.setStyle(Paint.Style.STROKE);
              paint.setStrokeWidth(shadowWidth);
              paint.setAntiAlias(true);

              // Draw the shadow around circular bitmap
              canvas.drawCircle(
                      dstBitmapWidth / 2, // cx
                      dstBitmapWidth / 2, // cy
                      dstBitmapWidth / 2 - shadowWidth / 2, // Radius
                      paint // Paint
              );

        /*
            public void recycle ()
                Free the native object associated with this bitmap, and clear the reference to the
                pixel data. This will not free the pixel data synchronously; it simply allows it to
                be garbage collected if there are no other references. The bitmap is marked as
                "dead", meaning it will throw an exception if getPixels() or setPixels() is called,
                and will draw nothing. This operation cannot be reversed, so it should only be
                called if you are sure there are no further uses for the bitmap. This is an advanced
                call, and normally need not be called, since the normal GC process will free up this
                memory when there are no more references to this bitmap.
        */
              srcBitmap.recycle();

              // Return the circular bitmap with shadow
              return dstBitmap;
       }


}


