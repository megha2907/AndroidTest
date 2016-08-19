package in.sportscafe.scgame.module.user.group.admin.approve;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.Adapter;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupPerson;

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
        holder.mTvEmail.setText(groupPerson.getEmail());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int mPosition;

        HmImageView mIvPhoto;

        TextView mTvName;

        TextView mTvEmail;

        public ViewHolder(View V) {
            super(V);
            mIvPhoto = (HmImageView) V.findViewById(R.id.approve_iv_photo);
            mTvName = (TextView) V.findViewById(R.id.approve_tv_name);
            mTvEmail = (TextView) V.findViewById(R.id.approve_tv_email);
            V.findViewById(R.id.approve_iv_accept).setOnClickListener(this);
            V.findViewById(R.id.approve_iv_reject).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.approve_iv_accept:
                    mApproveOptionListener.onAccept(mPosition);
                    GroupPerson groupPerson = getItem(getAdapterPosition());
                    PayloadBuilder builder = new PayloadBuilder();
                    builder.putAttrInt("group_personID", groupPerson.getId())
                            .putAttrString("admin_userID", ScGameDataHandler.getInstance().getUserId());
                    MoEHelper.getInstance(context).trackEvent("REQUEST ACCEPTED", builder.build());

                    break;
                case R.id.approve_iv_reject:
                    mApproveOptionListener.onReject(mPosition);
                    GroupPerson groupPerson2 = getItem(getAdapterPosition());
                    PayloadBuilder builder2 = new PayloadBuilder();
                    builder2.putAttrInt("group_personID", groupPerson2.getId())
                            .putAttrString("admin_userID", ScGameDataHandler.getInstance().getUserId());
                    MoEHelper.getInstance(context).trackEvent("REQUEST REJECTED", builder2.build());
                    break;
            }
        }
    }

    public interface OnApproveOptionListener {

        void onAccept(int position);

        void onReject(int position);
    }
}