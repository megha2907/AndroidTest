package in.sportscafe.nostragamus.module.privateContest.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrivateContestPrizeTemplateResponse;

/**
 * Created by sc on 23/3/18.
 */

public class PrivateContestPrizeTemplateSpinnerAdapter extends ArrayAdapter<PrivateContestPrizeTemplateResponse> {

    private List<PrivateContestPrizeTemplateResponse> mPrizeTemplateList;
    private PrivateContestPrizeTemplateResponse mSelectedTemplate;

    public PrivateContestPrizeTemplateSpinnerAdapter(@NonNull Context context,
                                                     List<PrivateContestPrizeTemplateResponse> responseList,
                                                     PrivateContestPrizeTemplateResponse selectedTemplate) {
        super(context, R.layout.private_contest_prize_spinner_item, R.id.spinner_item_textView);
        mPrizeTemplateList = responseList;
        mSelectedTemplate = selectedTemplate;
    }

    public List<PrivateContestPrizeTemplateResponse> getPrizeTemplateList() {
        return mPrizeTemplateList;
    }

    public void setSelectedTemplate(PrivateContestPrizeTemplateResponse mSelectedTemplate) {
        this.mSelectedTemplate = mSelectedTemplate;
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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        return setExternalFont(view.findViewById(R.id.spinner_item_textView));
    }

    private View setExternalFont(View spinnerView) {
        ((TextView) spinnerView).setTypeface(Typeface.createFromAsset(
                getContext().getAssets(),
                "fonts/lato/Lato-Regular.ttf"
        ));
        return spinnerView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_contest_prize_spinner_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.textView = (TextView) convertView.findViewById(R.id.spinner_item_textView);
            viewHolder.parent = (LinearLayout) convertView.findViewById(R.id.spinner_item_parent);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bindValues(position, viewHolder);

        return convertView;
    }

    private void bindValues(int position, ViewHolder viewHolder) {
        PrivateContestPrizeTemplateResponse template = mPrizeTemplateList.get(position);
        if (template != null) {

            if (!TextUtils.isEmpty(mPrizeTemplateList.get(position).getName())) {
                viewHolder.textView.setText(mPrizeTemplateList.get(position).getName());
            }

            int bgColor = R.color.grey_303030;
            if (mSelectedTemplate != null && !TextUtils.isEmpty(mSelectedTemplate.getTemplateId()) &&
                    !TextUtils.isEmpty(template.getTemplateId()) &&
                    mSelectedTemplate.getTemplateId().equals(template.getTemplateId())) {
                bgColor = R.color.venice_blue;
            }
            viewHolder.parent.setBackgroundColor(ContextCompat.getColor(viewHolder.parent.getContext(), bgColor));
        }
    }

    public class ViewHolder {
        TextView textView;
        LinearLayout parent;
    }
}
