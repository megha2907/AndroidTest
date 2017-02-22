package in.sportscafe.nostragamus.module.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by rb on 30/11/15.
 */
public abstract class Adapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> mItems = new ArrayList<>();

    private LayoutInflater mLayoutInflater;

    public Adapter(Context context) {
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public LayoutInflater getLayoutInflater() {
        return this.mLayoutInflater;
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addAll(Collection<T> items) {
        for (T item : items) {
            add(item);
        }
        notifyDataSetChanged();
    }

    public void add(T item) {
        add(item, mItems.size());
    }

    public void add(T item, int position) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void destroy() {
        mItems.clear();
        mLayoutInflater = null;
    }


    public interface OnRecyclerItemClickListener<T> {

        void onRecyclerItemClick(T t);
    }

}