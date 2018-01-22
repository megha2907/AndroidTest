package in.sportscafe.nostragamus.module.customViews;

import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;

import java.lang.reflect.Field;

import in.sportscafe.nostragamus.R;

/**
 * Created by sc on 15/1/18.
 *
 * A customised Snackbar
 */
public class CustomSnackBar extends BaseTransientBottomBar<CustomSnackBar> {

    private static final String TAG = CustomSnackBar.class.getSimpleName();

    /**
     * Time duration till the snackbar is visible to user
     */
    public static final int DURATION_LONG = BaseTransientBottomBar.LENGTH_LONG;
    public static final int DURATION_SHORT = BaseTransientBottomBar.LENGTH_SHORT;
    public static final int DURATION_INFINITE = BaseTransientBottomBar.LENGTH_INDEFINITE;

    /**
     * Constructor for the transient bottom bar.
     *
     * @param parent The parent for this transient bottom bar.
     * @param content The content view for this transient bottom bar.
     * @param callback The content view callback for this transient bottom bar.
     */
    private CustomSnackBar(ViewGroup parent, View content, ContentViewCallback callback) {
        super(parent, content, callback);
    }

    public static CustomSnackBar make(@NonNull View parent, @NonNull String message, int duration) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View content = inflater.inflate(R.layout.snackbar_view, (ViewGroup) parent, false);
        final ContentViewCallback viewCallback = new ContentViewCallback(content);

        final CustomSnackBar customSnackbar = new CustomSnackBar((ViewGroup) parent, content, viewCallback);
        customSnackbar.setDuration(duration);
        customSnackbar.setText(message);
        return customSnackbar;
    }

    public CustomSnackBar setImageResource(int resourceId) {
        TextView textView = (TextView) getView().findViewById(R.id.snackbar_text);
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(resourceId, 0, 0, 0);
        textView.setCompoundDrawablePadding(textView.getResources().getDimensionPixelOffset(R.dimen.dim_16));
        return this;
    }

    public CustomSnackBar setText(CharSequence text) {
        TextView textView = (TextView) getView().findViewById(R.id.snackbar_text);
        textView.setText(text);
        return this;
    }

    public CustomSnackBar setAction(CharSequence text, final View.OnClickListener listener) {
        TextView actionView = (TextView) getView().findViewById(R.id.snackbar_action_button);
        actionView.setText(text);
        actionView.setVisibility(View.VISIBLE);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
                dismiss();
            }
        });
        return this;
    }

    public void show() {
//        setAccessibilityDisabled();
        super.show();
    }

    private void setAccessibilityDisabled() {
        try {
            Field mAccessibilityManagerField = BaseTransientBottomBar.class.getDeclaredField("mAccessibilityManager");
            mAccessibilityManagerField.setAccessible(true);
            AccessibilityManager accessibilityManager = (AccessibilityManager) mAccessibilityManagerField.get(this);
            Field mIsEnabledField = AccessibilityManager.class.getDeclaredField("mIsEnabled");
            mIsEnabledField.setAccessible(true);
            mIsEnabledField.setBoolean(accessibilityManager, false);
            mAccessibilityManagerField.set(this, accessibilityManager);
        } catch (Exception e) {
            Log.d(TAG, "Reflection error: " + e.toString());
        }
    }

    private static class ContentViewCallback implements BaseTransientBottomBar.ContentViewCallback {

        private View content;

        public ContentViewCallback(View content) {
            this.content = content;
        }

        @Override
        public void animateContentIn(int delay, int duration) {
            /*ViewCompat.setScaleY(content, 0f);
            ViewCompat.animate(content).scaleY(1f).setDuration(duration).setStartDelay(delay);*/
        }

        @Override
        public void animateContentOut(int delay, int duration) {
            /*ViewCompat.setScaleY(content, 1f);
            ViewCompat.animate(content).scaleY(0f).setDuration(duration).setStartDelay(delay);*/
        }
    }
}
