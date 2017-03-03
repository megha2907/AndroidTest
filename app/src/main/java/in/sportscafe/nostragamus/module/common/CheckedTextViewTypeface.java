package in.sportscafe.nostragamus.module.common;

/**
 * Created by deepanshi on 1/24/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

public class CheckedTextViewTypeface extends CheckedTextView {

    public CheckedTextViewTypeface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    public CheckedTextViewTypeface(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    public CheckedTextViewTypeface(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public void setTypeface(Typeface tf, int style) {
        if(!this.isInEditMode()){
            Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/roboto/RobotoCondensed-Bold.ttf");
            Typeface boldTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/roboto/RobotoCondensed-Bold.ttf");

            if (style == Typeface.BOLD) {
                super.setTypeface(boldTypeface/*, -1*/);
            } else {
                super.setTypeface(normalTypeface/*, -1*/);
            }
        }

    }
}