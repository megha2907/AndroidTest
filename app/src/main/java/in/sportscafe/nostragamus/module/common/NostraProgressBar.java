package in.sportscafe.nostragamus.module.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import in.sportscafe.nostragamus.R;

/**
 * Created by sandip on 04/09/17.
 */
public class NostraProgressBar extends View {

    private static int DEFAULT_DOT_RADIUS = 6;
    private static int DEFAULT_BOUNCE_DOT_RADIUS = 9;
    private static int DEFAULT_TOTAL_DOTS = 8;
    private static int DEFAULT_PROGRESS_BAR_RADIUS = 24;
    private static String DEFAULT_DOT_COLOR = "#454545";

    //Normal dot radius
    private int dotRadius = DEFAULT_DOT_RADIUS;

    //Expanded Dot Radius
    private int bounceDotRadius = DEFAULT_BOUNCE_DOT_RADIUS;

    //to get identified in which position dot has to expand its radius
    private int dotPosition = 1;

    //specify how many dots you need in a progressbar
    private int dotAmount = DEFAULT_TOTAL_DOTS;

    //specify the circle radius
    private int circleRadius = DEFAULT_PROGRESS_BAR_RADIUS;

    private int dotColor = Color.parseColor(DEFAULT_DOT_COLOR);

    public NostraProgressBar(Context context) {
        super(context);
        initView(context, null);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NostraProgressBar, 0, 0);
            try {
                dotRadius = a.getDimensionPixelSize(R.styleable.NostraProgressBar_dot_radius, DEFAULT_DOT_RADIUS);
                dotAmount = a.getInt(R.styleable.NostraProgressBar_total_dots, DEFAULT_TOTAL_DOTS);
                circleRadius = a.getDimensionPixelSize(R.styleable.NostraProgressBar_progress_circle_radius, DEFAULT_PROGRESS_BAR_RADIUS);
                bounceDotRadius = a.getDimensionPixelSize(R.styleable.NostraProgressBar_bounce_dot_radius, DEFAULT_BOUNCE_DOT_RADIUS);
                dotColor = a.getColor(R.styleable.NostraProgressBar_dot_color, Color.parseColor(DEFAULT_DOT_COLOR));
            } finally {
                a.recycle();
            }
        }
    }

    public NostraProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public NostraProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }



    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //Animation called when attaching to the window, i.e to your screen
        startAnimation();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            //take the point to the center of the screen
            canvas.translate(this.getWidth() / 2, this.getHeight() / 2);

            Paint progressPaint = new Paint();
            progressPaint.setColor(dotColor);

            //call create dot method
            createDotInCircle(canvas, progressPaint);
        } catch (Exception ex) {}
    }

    private void createDotInCircle(Canvas canvas,Paint progressPaint) throws Exception {
        //angle for each dot angle = (360/number of dots)
        int angle = 360 / dotAmount;

        for(int i = 1; i <= dotAmount; i++){
            if(i == dotPosition){
                // angle should be in radians  i.e formula (angle *(Math.PI / 180))
                float x = (float) (circleRadius * (Math.cos((angle * i) * (Math.PI / 180))));
                float y = (float) (circleRadius * (Math.sin((angle * i) * (Math.PI / 180))));

                canvas.drawCircle(x,y, bounceDotRadius, progressPaint);
            } else {
                // angle should be in radians  i.e formula (angle *(Math.PI / 180))
                float x = (float) (circleRadius * (Math.cos((angle * i) * (Math.PI / 180))));
                float y = (float) (circleRadius * (Math.sin((angle * i) * (Math.PI / 180))));

                canvas.drawCircle(x,y, dotRadius, progressPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;

        //Dynamically setting width and height to progressbar 100 is circle radius, dotRadius * 3 to cover the width and height of Progressbar
        width = 100 + (dotRadius*3);
        height = 100 + (dotRadius*3);

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    private void startAnimation() {
        BounceAnimation bounceAnimation = new BounceAnimation();
        bounceAnimation.setDuration(150);
        bounceAnimation.setRepeatCount(Animation.INFINITE);
        bounceAnimation.setInterpolator(new LinearInterpolator());
        bounceAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                dotPosition++;
                //when dotPosition == dotAmount , then start again applying animation from 0th positon , i.e  dotPosition = 0;
                if (dotPosition > dotAmount) {
                    dotPosition = 1;
                }
            }
        });
        startAnimation(bounceAnimation);
    }


    private class BounceAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            //call invalidate to redraw your view againg.
            invalidate();
        }
    }
}
