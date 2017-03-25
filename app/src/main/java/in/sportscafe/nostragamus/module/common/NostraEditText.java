package in.sportscafe.nostragamus.module.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomEditText;
import com.jeeva.android.widgets.customfont.CustomTextView;

import in.sportscafe.nostragamus.R;

public class NostraEditText extends LinearLayout {

    private TextView mTvHint;

    private CustomEditText mEtTextBox;

    private CustomTextView mTvError;

    public NostraEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public NostraEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        openAttribute(context, attrs);
    }

    private void createHint(Context context, int style, String text) {
        mTvHint = new TextView(new ContextThemeWrapper(context, style), null, style);
        mTvHint.setText(text);
        addView(mTvHint);
    }

    private void createTextBox(Context context, int style) {
        mEtTextBox = new CustomEditText(context, null, style);
        addView(mEtTextBox);
    }

    private void createError(Context context, int style, String text) {
        mTvError = new CustomTextView(context, null, style);
        mTvError.setText(text);
        addView(mTvError);
    }

    private void openAttribute(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NostraEditText);

        createHint(
                context,
                a.getResourceId(R.styleable.NostraEditText_hintStyle, -1),
//                R.style.EditTextHintStyle,
                a.getString(R.styleable.NostraEditText_hintText)
        );

        /*createTextBox(
                context,
                a.getResourceId(R.styleable.NostraEditText_boxStyle, -1)
        );

        createError(
                context,
                a.getResourceId(R.styleable.NostraEditText_errorStyle, -1),
                a.getString(R.styleable.NostraEditText_errorText)
        );*/

//        a.recycle();
    }
}