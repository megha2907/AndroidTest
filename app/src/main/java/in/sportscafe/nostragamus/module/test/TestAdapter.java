package in.sportscafe.nostragamus.module.test;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.customfont.CustomTextView;

import java.lang.ref.WeakReference;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;

/**
 * Created by deepanshi on 14/07/16.
 */
public class TestAdapter extends Adapter<String, TestAdapter.ViewHolder> {

    public TestAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_feed_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mLlTourParent.removeAllViews();

        View testView = getLayoutInflater().inflate(R.layout.inflater_test, holder.mLlTourParent, false);
        MyViewHolder myViewHolder = new MyViewHolder(testView);
        myViewHolder.tvName.setText("Item: " + (position + 1));

        holder.mLlTourParent.addView(testView);

        new TimerRunnable(position, myViewHolder.tvName);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        int mPosition;

        private LinearLayout mLlTourParent;

        public ViewHolder(View V) {
            super(V);
            mLlTourParent = (LinearLayout) V.findViewById(R.id.feed_row_ll_tour_parent);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CustomTextView tvName;

        public MyViewHolder(View view) {
            super(view);

            tvName = (CustomTextView) view.findViewById(R.id.test_tv);
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class TimerRunnable implements Runnable {

        private int position;

        private WeakReference<CustomTextView> timerValue;

        private Handler customHandler = new Handler();

        private long updatedTime = 0L;

        private TimerRunnable(int position, CustomTextView timerValue) {
            this.position = position;
            this.timerValue = new WeakReference<>(timerValue);
            customHandler = new Handler();
            customHandler.postDelayed(this, 1000);
        }

        public void run() {
            updatedTime += 1000;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            mins = mins % 60;
            secs = secs % 60;

            CustomTextView tvTemp = timerValue.get();
            if(null == tvTemp || tvTemp.isDestroyed()) {
                customHandler.removeCallbacks(this);
                customHandler = null;
                tvTemp = null;

                Log.d("TimerRunnable", position + " detached");
            } else {
                tvTemp.setText(position + " " +
                        String.format("%02d", hours) + ":"
                        + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs)
                );
                customHandler.postDelayed(this, 1000);

                tvTemp = null;
            }
        }

    };
}