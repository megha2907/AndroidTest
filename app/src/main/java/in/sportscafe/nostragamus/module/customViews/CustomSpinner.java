package in.sportscafe.nostragamus.module.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Spinner;

import java.lang.reflect.Field;

/**
 * Created by sc on 2/4/18.
 */

public class CustomSpinner extends Spinner {

    private final String TAG = CustomSpinner.class.getSimpleName();

    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position, boolean animate) {
        ignoreOldSelectionByReflection();
        super.setSelection(position, animate);
    }

    private void ignoreOldSelectionByReflection() {
        try {
            Class<?> c = this.getClass().getSuperclass().getSuperclass().getSuperclass();
            Field reqField = c.getDeclaredField("mOldSelectedPosition");
            reqField.setAccessible(true);
            reqField.setInt(this, -1);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setSelection(int position) {
        ignoreOldSelectionByReflection();
        super.setSelection(position);
    }

}
