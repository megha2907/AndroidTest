package in.sportscafe.nostragamus.module.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomFont;

import in.sportscafe.nostragamus.R;

public class CustomTabLayout extends TabLayout {

    private Typeface mUnSelTabFont;

    private Typeface mSelTabFont;

    private int mUnSelTextSize;

    private int mSelTextSize;

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTabLayout);
        mUnSelTabFont = CustomFont.getInstance().getTypeFace(context,
                a.getInt(R.styleable.CustomTabLayout_unselected_typeface, -1));
        mSelTabFont = CustomFont.getInstance().getTypeFace(context,
                a.getInt(R.styleable.CustomTabLayout_selected_typeface, -1));
        mUnSelTextSize = a.getDimensionPixelSize(R.styleable.CustomTabLayout_unselected_textSize, 14);
        mSelTextSize = a.getDimensionPixelSize(R.styleable.CustomTabLayout_selected_textSize, 22);
    }

    @Override
    public void addTab(Tab tab, boolean setSelected) {
        super.addTab(tab, setSelected);
        updateUnSelectedTab(tab);
    }

    @Override
    public void setOnTabSelectedListener(final OnTabSelectedListener onTabSelectedListener) {
        super.setOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                updateSelectedTab(tab);
                onTabSelectedListener.onTabSelected(tab);
            }

            @Override
            public void onTabUnselected(Tab tab) {
                updateUnSelectedTab(tab);
                onTabSelectedListener.onTabUnselected(tab);
            }

            @Override
            public void onTabReselected(Tab tab) {
                onTabSelectedListener.onTabReselected(tab);
            }
        });
    }

    private void updateSelectedTab(Tab tab) {
        updateTab(tab, true);
    }

    private void updateUnSelectedTab(Tab tab) {
        updateTab(tab, false);
    }

    private void updateTab(Tab tab, boolean selected) {
        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());

        int tabChildCount = tabView.getChildCount();
        TextView textView;
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                if(selected) {
                    textView = (TextView) tabViewChild;

                    // Todo Have to update when the fonts are choosen
                    textView.setTypeface(mSelTabFont, Typeface.BOLD);
                    textView.setTextSize(mSelTextSize);
                } else{
                    textView = (TextView) tabViewChild;
                    textView.setTypeface(mUnSelTabFont, Typeface.NORMAL);
                    textView.setTextSize(mUnSelTextSize);
                }
                break;
            }
        }
    }
}