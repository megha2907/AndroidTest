package in.sportscafe.nostragamus.module.coachmarker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.sportscafe.nostragamus.R;

import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;

/**
 * Created by deepanshu on 16/11/16.
 */
public class BasicOverlay extends FrameLayout {

    public BasicOverlay(Context context) {
        this(context, null);
    }

    public BasicOverlay(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BasicOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initOverlay();
    }

    private void initOverlay() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.inflater_basic_overlay, this, false));
    }

    public void changeRule(int verb, int basicOverlayGradient) {
        LinearLayout mBasicLayout = (LinearLayout) findViewById(R.id.basic_overlay_ll_labels);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mBasicLayout.getLayoutParams();
        layoutParams.addRule(verb);
        mBasicLayout.setLayoutParams(layoutParams);

        View gradientBg = findViewById(R.id.basic_overlay_v_gradient_bg);
        layoutParams = (RelativeLayout.LayoutParams) gradientBg.getLayoutParams();
        layoutParams.addRule(verb);
        gradientBg.setLayoutParams(layoutParams);

        gradientBg.setBackground(getResources().getDrawable(basicOverlayGradient));
    }

    public void showAtTop() {
        changeRule(ALIGN_PARENT_TOP, R.drawable.basic_overlay_gradient_top);
    }

    public void showAtBottom() {
        changeRule(ALIGN_PARENT_BOTTOM, R.drawable.basic_overlay_gradient_bottom);
    }

    public void setTitle(String name) {
        ((TextView) findViewById(R.id.basic_tv_title)).setText(name);
    }

    public void setDesc(String desc) {
        ((TextView) findViewById(R.id.basic_tv_desc)).setText(desc);
    }

    public void setAlert(String alert) {
        ((TextView) findViewById(R.id.basic_tv_alert)).setText(alert);
    }
}
