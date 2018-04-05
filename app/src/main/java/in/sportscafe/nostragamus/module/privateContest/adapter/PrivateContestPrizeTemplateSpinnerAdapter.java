package in.sportscafe.nostragamus.module.privateContest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrivateContestPrizeTemplateResponse;

/**
 * Created by sc on 23/3/18.
 */

public class PrivateContestPrizeTemplateSpinnerAdapter extends ArrayAdapter<PrivateContestPrizeTemplateResponse> {

    private List<PrivateContestPrizeTemplateResponse> mPrizeTemplateList;

    public PrivateContestPrizeTemplateSpinnerAdapter(@NonNull Context context,
                                                     List<PrivateContestPrizeTemplateResponse> responseList) {
        super(context, R.layout.private_contest_prize_spinner_item, R.id.spinner_item_textView);
        mPrizeTemplateList = responseList;
    }

    public List<PrivateContestPrizeTemplateResponse> getPrizeTemplateList() {
        return mPrizeTemplateList;
    }

    @Override
    public int getCount() {
        return (mPrizeTemplateList != null) ? mPrizeTemplateList.size() : 0;
    }

    @Nullable
    @Override
    public PrivateContestPrizeTemplateResponse getItem(int position) {
        if (mPrizeTemplateList != null && position >= 0 && position < mPrizeTemplateList.size()) {
            return mPrizeTemplateList.get(position);
        }
        return null;
    }

 /*   @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_contest_prize_spinner_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.spinner_item_textView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mPrizeTemplateList.get(position) != null &&
                !TextUtils.isEmpty(mPrizeTemplateList.get(position).getName())) {
            String name = mPrizeTemplateList.get(position).getName();
            viewHolder.textView.setText(name);
        }
        return convertView;
    }*/

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_contest_prize_spinner_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.spinner_item_textView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mPrizeTemplateList.get(position) != null &&
                !TextUtils.isEmpty(mPrizeTemplateList.get(position).getName())) {
            String name = mPrizeTemplateList.get(position).getName();
            viewHolder.textView.setText(name);
        }

        return convertView;
    }

    public class ViewHolder {
        TextView textView;
    }
}
