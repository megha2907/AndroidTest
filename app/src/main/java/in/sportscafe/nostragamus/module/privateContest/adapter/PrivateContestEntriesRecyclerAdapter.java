package in.sportscafe.nostragamus.module.privateContest.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.dto.ContestRoomEntryDto;

/**
 * Created by sc on 28/3/18.
 */

public class PrivateContestEntriesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ContestRoomEntryDto> mContestRoomEntryList;

    public PrivateContestEntriesRecyclerAdapter(List<ContestRoomEntryDto> entries) {
        mContestRoomEntryList = entries;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_contest_entries_list_item, parent, false);
        return new PrivateContestEntriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PrivateContestEntriesViewHolder && mContestRoomEntryList != null &&
                position >= 0 && position < mContestRoomEntryList.size()) {

            PrivateContestEntriesViewHolder viewHolder = (PrivateContestEntriesViewHolder) holder;
            ContestRoomEntryDto entryDto = mContestRoomEntryList.get(position);

            viewHolder.photoImageView.setImageUrl(entryDto.getUserPicUrl());
            viewHolder.nameTextView.setText(entryDto.getUserName());

            if (entryDto.isPrivateContestCreator()) {
                viewHolder.contestCreatorTextView.setVisibility(View.VISIBLE);
                viewHolder.parentLayout.setBackgroundColor(ContextCompat.getColor(
                        viewHolder.parentLayout.getContext(), R.color.contest_creator_entry_bg));
            } else {
                viewHolder.contestCreatorTextView.setVisibility(View.GONE);
                viewHolder.parentLayout.setBackgroundColor(ContextCompat.getColor(
                        viewHolder.parentLayout.getContext(), R.color.black));
            }

        }
    }

    @Override
    public int getItemCount() {
        return (mContestRoomEntryList != null) ? mContestRoomEntryList.size() : 0;
    }

    private class PrivateContestEntriesViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout parentLayout;
        HmImageView photoImageView;
        TextView nameTextView;
        TextView contestCreatorTextView;

        public PrivateContestEntriesViewHolder(View itemView) {
            super(itemView);

            parentLayout = (RelativeLayout) itemView.findViewById(R.id.private_contest_list_item_parent);
            photoImageView = (HmImageView) itemView.findViewById(R.id.entry_user_photo);
            nameTextView = (TextView) itemView.findViewById(R.id.entry_user_nick_textView);
            contestCreatorTextView = (TextView) itemView.findViewById(R.id.entry_user_creator_textView);
        }
    }
}
