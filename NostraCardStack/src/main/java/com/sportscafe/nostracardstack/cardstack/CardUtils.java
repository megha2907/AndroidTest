package com.sportscafe.nostracardstack.cardstack;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class CardUtils {
    final static int DIRECTION_TOP_LEFT = 0;
    final static int DIRECTION_TOP_RIGHT = 1;
    final static int DIRECTION_BOTTOM_LEFT = 2;
    final static int DIRECTION_BOTTOM_RIGHT = 3;

    public static void scale(View v, int pixel, int gravity) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.leftMargin -= pixel * 3;
        params.rightMargin -= pixel * 3;
        if (gravity == CardAnimator.TOP) {
            params.topMargin += pixel;
        } else {
            params.topMargin -= pixel;
        }
        params.bottomMargin -= pixel;
        v.setLayoutParams(params);
    }

    /* Allows default move direction at any side */
    public static LayoutParams getMoveParams(View v, int upDown, int leftRight) {
        RelativeLayout.LayoutParams original = (RelativeLayout.LayoutParams) v.getLayoutParams();
        RelativeLayout.LayoutParams params = cloneParams(original);

        params.leftMargin += leftRight;
        params.rightMargin -= leftRight;
        params.topMargin -= upDown;
        params.bottomMargin += upDown;
        return params;
    }

    public static LayoutParams getMoveLeftParams(View v, int left) {
        RelativeLayout.LayoutParams original = (RelativeLayout.LayoutParams) v.getLayoutParams();
        RelativeLayout.LayoutParams params = cloneParams(original);

        params.leftMargin -= left;
        params.rightMargin += left;
        return params;
    }

    public static LayoutParams getMoveRightParams(View v, int right) {
        RelativeLayout.LayoutParams original = (RelativeLayout.LayoutParams) v.getLayoutParams();
        RelativeLayout.LayoutParams params = cloneParams(original);

        params.leftMargin += right;
        params.rightMargin -= right;
        return params;
    }

    public static LayoutParams getMoveTopParams(View v, int top) {
        RelativeLayout.LayoutParams original = (RelativeLayout.LayoutParams) v.getLayoutParams();
        RelativeLayout.LayoutParams params = cloneParams(original);

        params.topMargin -= top;
        params.bottomMargin += top;
        return params;
    }

    public static LayoutParams getMoveBottomParams(View v, int bottom) {
        RelativeLayout.LayoutParams original = (RelativeLayout.LayoutParams) v.getLayoutParams();
        RelativeLayout.LayoutParams params = cloneParams(original);

        params.topMargin += bottom;
        params.bottomMargin -= bottom;
        return params;
    }

    public static void move(View v, int upDown, int leftRight) {
        RelativeLayout.LayoutParams params = getMoveParams(v, upDown, leftRight);
        v.setLayoutParams(params);
    }

    public static LayoutParams scaleFrom(View v, LayoutParams params, int pixel, int gravity) {
        Log.d("pixel", "onScroll: " + pixel);
        params = cloneParams(params);
        params.leftMargin -= pixel;
        params.rightMargin -= pixel;
        if (gravity == CardAnimator.TOP) {
            params.topMargin += pixel;
        } else {
            params.topMargin -= pixel;
        }
        params.bottomMargin -= pixel;
        v.setLayoutParams(params);

        return params;
    }

    public static LayoutParams moveFrom(View v, LayoutParams params, int leftRight, int upDown, int gravity) {
        params = cloneParams(params);
        params.leftMargin += leftRight;
        params.rightMargin -= leftRight;

        if (gravity == CardAnimator.BOTTOM) {
            params.bottomMargin += upDown;
            params.topMargin -= upDown;
        } else {
            params.bottomMargin -= upDown;
            params.topMargin += upDown;
        }
        v.setLayoutParams(params);

        return params;
    }

    //a copy method for RelativeLayout.LayoutParams for backward compartibility
    public static RelativeLayout.LayoutParams cloneParams(RelativeLayout.LayoutParams params) {
        RelativeLayout.LayoutParams copy = new RelativeLayout.LayoutParams(params.width, params.height);
        copy.leftMargin = params.leftMargin;
        copy.topMargin = params.topMargin;
        copy.rightMargin = params.rightMargin;
        copy.bottomMargin = params.bottomMargin;
        int[] rules = params.getRules();
        for (int i = 0; i < rules.length; i++) {
            copy.addRule(i, rules[i]);
        }
        //copy.setMarginStart(params.getMarginStart());
        //copy.setMarginEnd(params.getMarginEnd());

        return copy;
    }

    public static float distance(float x1, float y1, float x2, float y2) {

        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static int direction(float x1, float y1, float x2, float y2) {
        if (x2 > x1) {//RIGHT
            if (y2 > y1) {//DOWN
                return DIRECTION_BOTTOM_RIGHT;
            } else {//UP
                return DIRECTION_TOP_RIGHT;
            }
        } else {//LEFT
            if (y2 > y1) {//DOWN
                return DIRECTION_BOTTOM_LEFT;
            } else {//UP
                return DIRECTION_TOP_LEFT;
            }
        }
    }

    /**
     * Used to identify swipe direction
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static int getSwipeDirection(float x1, float y1, float x2, float y2) {

        double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
        double angle = (rad*180/Math.PI + 180)%360;

        if(inRange(angle, 45, 135)){
            return CardDirection.UP;
        } else if(inRange(angle, 0,45) || inRange(angle, 315, 360)){
            return CardDirection.RIGHT;
        } else if(inRange(angle, 225, 315)){
            return CardDirection.DOWN;
        } else{
            return CardDirection.LEFT;
        }
    }

    private static boolean inRange(double angle, float init, float end){
        return (angle >= init) && (angle < end);
    }



}
