package in.sportscafe.nostragamus.module.user.group.members;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.module.user.playerprofile.PlayerProfileActivity;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by rb on 30/11/15.
 */
public class MembersAdapter extends Adapter<GroupPerson, MembersAdapter.ViewHolder> {

    private boolean mAdmin;

    private OnMembersOptionListener mMembersOptionListener;

    private List<String> mOptionList = new ArrayList<>();

    public MembersAdapter(Context context, OnMembersOptionListener membersOptionListener, boolean admin, List<GroupPerson> memberList) {
        super(context);
        this.mMembersOptionListener = membersOptionListener;
        this.mOptionList.add("Make Admin");
        this.mOptionList.add("Remove");
        this.mAdmin = admin;
        addAll(memberList);
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

        holder.mIvPhoto.setImageUrl(groupPerson.getPhoto());
        holder.mTvName.setText(groupPerson.getUserName());
        holder.mTvAdminLabel.setVisibility(groupPerson.isAdmin() ? View.VISIBLE : View.GONE);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        RoundImage mIvPhoto;

        TextView mTvName;

        TextView mTvAdminLabel;

        public ViewHolder(View V) {
            super(V);
            mIvPhoto = (RoundImage) V.findViewById(R.id.members_iv_photo);
            mTvName = (TextView) V.findViewById(R.id.members_tv_name);
            mTvAdminLabel = (TextView) V.findViewById(R.id.members_tv_admin_label);
            V.setOnClickListener(this);
            V.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            if (mAdmin) {
                GroupPerson groupPerson = getItem(getAdapterPosition());
                if (!NostragamusDataHandler.getInstance().getUserId().equals(groupPerson.getId().toString())) {
                    ViewUtils.getDialogList(view.getContext(), mOptionList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    mMembersOptionListener.onMakeAdmin(getAdapterPosition());
                                    break;
                                case 1:
                                    mMembersOptionListener.onClickRemove(getAdapterPosition());
                                    break;
                            }
                        }
                    }).show();
                }
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            Integer playerId = getItem(getAdapterPosition()).getId();
            if (NostragamusDataHandler.getInstance().getUserId().equals(playerId.toString())) {
                Intent homeintent = new Intent(v.getContext(), HomeActivity.class);
                homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                homeintent.putExtra(BundleKeys.OPEN_PROFILE, "0");
                v.getContext().startActivity(homeintent);
            } else {
                Bundle mBundle = new Bundle();
                mBundle.putInt(BundleKeys.PLAYER_ID, playerId);
                Intent mintent2 = new Intent(v.getContext(), PlayerProfileActivity.class);
                mintent2.putExtras(mBundle);
                v.getContext().startActivity(mintent2);
            }
        }
    }

    public interface OnMembersOptionListener {

        void onClickRemove(int position);

        void onMakeAdmin(int position);
    }
}