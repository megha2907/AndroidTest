package com.jeeva.android.widgets.customfont;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.jeeva.android.R;


public class CustomEditText extends AppCompatEditText /*implements TextWatcher*/ {

    private EditTextImeBackListener mOnImeBack;

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        CustomFont.getInstance().setFont(context, this, a.getInt(R.styleable.CustomEditText_typeface, -1));
    }

    public void setOnEditTextImeBackListener(EditTextImeBackListener listener) {
        mOnImeBack = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            if (mOnImeBack != null) {
                mOnImeBack.onImeBack(this, this.getText().toString());
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public interface EditTextImeBackListener {

        void onImeBack(CustomEditText ctrl, String text);
    }
}