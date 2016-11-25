package in.sportscafe.nostragamus.module.user.group.members;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.volley.Volley;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by rb on 30/11/15.
 */
public class MembersAdapter extends Adapter<GroupPerson, MembersAdapter.ViewHolder> {

    private boolean mAdmin;

    private OnMembersOptionListener mMembersOptionListener;

    private List<String> mOptionList = new ArrayList<>();

    public MembersAdapter(Context context, OnMembersOptionListener membersOptionListener, boolean admin) {
        super(context);
        this.mMembersOptionListener = membersOptionListener;
        this.mOptionList.add("Make Admin");
        this.mOptionList.add("Remove");
        this.mAdmin = admin;
    }

    @Override
    public GroupPerson getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_members_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GroupPerson groupPerson = getItem(position);

        holder.mPosition = position;

        holder.mIvPhoto.setImageUrl(groupPerson.getPhoto(), Volley.getInstance().getImageLoader(), false);
        holder.mTvName.setText(groupPerson.getUserName());
        holder.mTvAdminLabel.setVisibility(groupPerson.isAdmin() ? View.VISIBLE : View.GONE);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int mPosition;

        RoundImage mIvPhoto;

        TextView mTvName;

        TextView mTvAdminLabel;

        public ViewHolder(View V) {
            super(V);
            mIvPhoto = (RoundImage) V.findViewById(R.id.members_iv_photo);
            mTvName = (TextView) V.findViewById(R.id.members_tv_name);
            mTvAdminLabel = (TextView) V.findViewById(R.id.members_tv_admin_label);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mAdmin) {
                ViewUtils.getDialogList(view.getContext(), mOptionList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mMembersOptionListener.onMakeAdmin(mPosition);
                                break;
                            case 1:
                                mMembersOptionListener.onClickRemove(mPosition);
                                break;
                        }
                    }
                }).show();
            }
        }
    }

    public interface OnMembersOptionListener {

        void onClickRemove(int position);

        void onMakeAdmin(int position);
    }
}