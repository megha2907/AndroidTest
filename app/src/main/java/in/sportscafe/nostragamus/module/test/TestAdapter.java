package in.sportscafe.nostragamus.module.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;

/**
 * Created by deepanshi on 14/07/16.
 */
public class TestAdapter extends Adapter<String, TestAdapter.MyViewHolder> {

    public TestAdapter(Context context) {
        super(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(getLayoutInflater().inflate(R.layout.inflater_test, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvName.setText("Item: " + (position + 1));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public MyViewHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.test_tv);
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}