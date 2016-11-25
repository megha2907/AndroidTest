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
              paint.setColor(Color.parseColor("#BAB399"));
              canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.8f,
                           finalBitmap.getHeight() / 2 + 0.6f,
                           finalBitmap.getWidth() / 2 + 0.1f, paint);
              paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
              canvas.drawBitmap(finalBitmap, rect, rect, paint);

              return output;
       }

}


