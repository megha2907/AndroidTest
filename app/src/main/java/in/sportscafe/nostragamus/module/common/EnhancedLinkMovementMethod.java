package in.sportscafe.nostragamus.module.common;

import android.graphics.Rect;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.LeadingMarginSpan;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by deepanshi on 4/22/17.
 */

public class EnhancedLinkMovementMethod extends ArrowKeyMovementMethod {

    private static EnhancedLinkMovementMethod sInstance;

    private static Rect sLineBounds = new Rect();

    public static MovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new EnhancedLinkMovementMethod();
        }
        return sInstance;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {

            int index = getCharIndexAt(widget, event);
            if (index != -1) {
                ClickableSpan[] link = buffer.getSpans(index, index, ClickableSpan.class);
                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    }
                    else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                    }
                    return true;
                }
            }
            /*else {
                Selection.removeSelection(buffer);
            }*/

        }

        return super.onTouchEvent(widget, buffer, event);
    }

    private int getCharIndexAt(TextView textView, MotionEvent event) {
        // get coordinates
        int x = (int) event.getX();
        int y = (int) event.getY();
        x -= textView.getTotalPaddingLeft();
        y -= textView.getTotalPaddingTop();
        x += textView.getScrollX();
        y += textView.getScrollY();

        /*
         * Fail-fast check of the line bound.
         * If we're not within the line bound no character was touched
         */
        Layout layout = textView.getLayout();
        int line = layout.getLineForVertical(y);
        synchronized (sLineBounds) {
            layout.getLineBounds(line, sLineBounds);
            if (! sLineBounds.contains(x, y)) {
                return -1;
            }
        }

        // retrieve line text
        Spanned text = (Spanned) textView.getText();
        int lineStart = layout.getLineStart(line);
        int lineEnd = layout.getLineEnd(line);
        int lineLength = lineEnd - lineStart;
        if (lineLength == 0) {
            return -1;
        }
        Spanned lineText = (Spanned) text.subSequence(lineStart, lineEnd);

        // compute leading margin and subtract it from the x coordinate
        int margin = 0;
        LeadingMarginSpan[] marginSpans = lineText.getSpans(0, lineLength, LeadingMarginSpan.class);
        if (marginSpans != null) {
            for (LeadingMarginSpan span : marginSpans) {
                margin += span.getLeadingMargin(true);
            }
        }
        x -= margin;

        // retrieve text widths
        float[] widths = new float[lineLength];
        TextPaint paint = textView.getPaint();
        paint.getTextWidths(lineText, 0, lineLength, widths);

        // scale text widths by relative font size (absolute size / default size)
        final float defaultSize = textView.getTextSize();
        float scaleFactor = 1f;
        AbsoluteSizeSpan[] absSpans = lineText.getSpans(0, lineLength, AbsoluteSizeSpan.class);
        if (absSpans != null) {
            for (AbsoluteSizeSpan span : absSpans) {
                int spanStart = lineText.getSpanStart(span);
                int spanEnd = lineText.getSpanEnd(span);
                scaleFactor = span.getSize() / defaultSize;
                int start = Math.max(lineStart, spanStart);
                int end = Math.min(lineEnd, spanEnd);
                for (int i = start; i < end; i++) {
                    widths[i] *= scaleFactor;
                }
            }
        }

        // find index of touched character
        float startChar = 0;
        float endChar = 0;
        for (int i = 0; i < lineLength; i++) {
            startChar = endChar;
            endChar += widths[i];
            if (endChar >= x) {
                // which "end" is closer to x, the start or the end of the character?
                int index = lineStart + (x - startChar < endChar - x ? i : i + 1);
                //Logger.e(Logger.LOG_TAG, "Found character: " + (text.length()>index ? text.charAt(index) : ""));
                return index;
            }
        }

        return -1;
    }
}