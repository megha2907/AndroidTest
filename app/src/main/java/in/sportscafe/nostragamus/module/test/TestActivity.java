package in.sportscafe.nostragamus.module.test;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 10/6/16.
 */
public class TestActivity extends AppCompatActivity {

    private boolean mBoundDone = true;

    private static final float MAX_ROTATION = 30;

    private float mVisibleHeight;

    private float mHalfVisibleHeight;

    private float mDifference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.test_rcv_timeline);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(ViewUtils.getAnimationAdapter(new TestAdapter(this)));

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                mVisibleHeight = findViewById(R.id.content).getMeasuredHeight();
                mHalfVisibleHeight = getResources().getDimensionPixelSize(R.dimen.dp_150);
                mDifference = mVisibleHeight - mHalfVisibleHeight;
//                recyclerView.smoothScrollToPosition(9);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private boolean mScrollSettled = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if(!mBoundDone && mScrollSettled) {
                            mBoundDone = true;
                            TestActivity.this.startBouncing(recyclerView);
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        mScrollSettled = false;
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        mScrollSettled = true;
                        break;
                }
            }
        });

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                View child = null;
                int[] location = new int[2];
                int yAxis;
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    child = parent.getChildAt(i).findViewById(R.id.test_ll);

                    child.setPivotY(child.getMeasuredHeight());
                    child.getLocationOnScreen(location);
                    child.setRotationX(getRotationByY(location[1]));
                }
                super.onDraw(c, parent, state);
            }
        });
    }

    private void startBouncing(RecyclerView recyclerView) {
        View child = recyclerView.getChildAt(0).findViewById(R.id.test_ll);
        recyclerView.startAnimation(getBounceAnimation());
    }

    private Animation getBounceAnimation() {
        Animation bounceAnim = new TranslateAnimation(0f, 0f, 0f, 100f);
        bounceAnim.setInterpolator(new BounceInterpolator());
        bounceAnim.setDuration(1000);

        return bounceAnim;
    }

    private float getRotationByY(int yAxis) {
        float rotation = MAX_ROTATION * (yAxis - mHalfVisibleHeight) / mDifference;
        if(rotation < 0) {
            return 0;
        }
        return rotation;
    }
}