package in.sportscafe.nostragamus.module.user.group.admin.approve;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.volley.Volley;

import java.util.HashMap;
import java.util.Map;

import in.sportscafe.nostragamus.Constants.NotificationKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;

/**
 * Created by rb on 30/11/15.
 */
public class ApproveAdapter extends Adapter<GroupPerson, ApproveAdapter.ViewHolder> {

    private OnApproveOptionListener mApproveOptionListener;
    Context context;

    public ApproveAdapter(Context context, OnApproveOptionListener listener) {
        super(context);
        this.mApproveOptionListener = listener;
    }

    @Override
    public GroupPerson getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_approve_row, parent, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GroupPerson groupPerson = getItem(position);

        holder.mPosition = position;

        holder.mIvPhoto.setImageUrl(groupPerson.getPhoto(), Volley.getInstance().getImageLoader(), false);
        holder.mTvName.setText(groupPerson.getUserName());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int mPosition;

        RoundImage mIvPhoto;

        TextView mTvName;


        public ViewHolder(View V) {
            super(V);
            mIvPhoto = (RoundImage) V.findViewById(R.id.approve_iv_photo);
            mTvName = (TextView) V.findViewById(R.id.approve_tv_name);
            V.findViewById(R.id.approve_iv_accept).setOnClickListener(this);
            V.findViewById(R.id.approve_iv_reject).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String category = null;
            switch (view.getId()) {
                case R.id.approve_iv_accept:
                    mApproveOptionListener.onAccept(mPosition);

                    category = NotificationKeys.REQUEST_ACCEPTED;
                    break;
                case R.id.approve_iv_reject:
                    mApproveOptionListener.onReject(mPosition);

                    category = NotificationKeys.REQUEST_REJECTED;
                    break;
            }

            if(null != category) {
                GroupPerson groupPerson = getItem(getAdapterPosition());

                Map<String, String> acceptValues = new HashMap<>();
                acceptValues.put(NotificationKeys.GROUP_PERSONID, groupPerson.getId() + "");
                acceptValues.put(NotificationKeys.ADMIN_USERID, NostragamusDataHandler.getInstance().getUserId());

                NostragamusAnalytics.getInstance().trackOtherEvents(category, acceptValues);
            }
        }
    }

    public interface OnApproveOptionListener {

        void onAccept(int position);

        void onReject(int position);
    }
}