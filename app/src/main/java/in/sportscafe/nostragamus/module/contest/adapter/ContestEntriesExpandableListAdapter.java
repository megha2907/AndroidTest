package in.sportscafe.nostragamus.module.contest.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.dto.ContestRoomDto;
import in.sportscafe.nostragamus.module.contest.dto.ContestRoomEntryDto;
import in.sportscafe.nostragamus.module.contest.helper.ContestEntriesFilterHelper;

/**
 * Created by sandip on 06/09/17.
 */

public class ContestEntriesExpandableListAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mLayoutInflater;
    private List<ContestRoomDto> mRoomsList;
    private int mFillingRooms = 0;
    private int mFilledRooms = 0;

    public ContestEntriesExpandableListAdapter(Context context, List<ContestRoomDto> rooms) {
        mLayoutInflater = LayoutInflater.from(context);
        mRoomsList = new ArrayList<>();

        filterRooms(rooms);
    }

    private void filterRooms(List<ContestRoomDto> rooms) {
        ContestEntriesFilterHelper filterHelper = new ContestEntriesFilterHelper();
        if (rooms != null) {
            // Filtering and putting into list at top - all filling rooms
            List<ContestRoomDto> tempList = filterHelper.getFillingRoomsFileterredList(rooms);
            if (tempList != null && !tempList.isEmpty()) {
                mRoomsList.addAll(tempList);
                mFillingRooms = tempList.size();
            }

            // Filtering and putting into list - all Filled rooms
            tempList = filterHelper.getFilledRoomsFileterredList(rooms);
            if (tempList != null && !tempList.isEmpty()) {
                mRoomsList.addAll(tempList);
                mFilledRooms = tempList.size();
            }
        }
    }

    @Override
    public int getGroupCount() {
        if (mRoomsList != null) {
            return mRoomsList.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mRoomsList != null && mRoomsList.get(groupPosition) != null && mRoomsList.get(groupPosition).getEntries() != null) {
            return mRoomsList.get(groupPosition).getEntries().size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (mRoomsList != null) {
            return mRoomsList.get(groupPosition);
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (mRoomsList != null && mRoomsList.get(groupPosition) != null && mRoomsList.get(groupPosition).getEntries() != null) {
            return mRoomsList.get(groupPosition).getEntries().get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.contest_entry_list_group_item, null);
            holder = new GroupHolder();
            holder.rootLayout = (LinearLayout) convertView.findViewById(R.id.contest_entry_list_group_root_layout);
            holder.groupTitleTextView = (TextView) convertView.findViewById(R.id.contest_entry_list_group_title_textView);
            holder.groupSizeTextView = (TextView) convertView.findViewById(R.id.contest_entry_list_group_size_textView);
            holder.groupIndicatorImageView = (ImageView) convertView.findViewById(R.id.contest_entry_list_group_indicator_imgView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        if (isExpanded) {
            holder.groupIndicatorImageView.setImageResource(android.R.drawable.arrow_up_float);
        } else {
            holder.groupIndicatorImageView.setImageResource(android.R.drawable.arrow_down_float);
        }

        if (mRoomsList != null && mRoomsList.size() > groupPosition) {
            ContestRoomDto roomDto = mRoomsList.get(groupPosition);

            /*String status = roomDto.getRoomStatus();
            if (!TextUtils.isEmpty(status)) {
                if (status.equalsIgnoreCase("filling")) {
                    holder.groupTitleTextView.setText("Filling");
                } else {
                    holder.groupTitleTextView.setText("Filled");
                }
            }*/

            holder.groupTitleTextView.setText(roomDto.getRoomTitle());
            holder.groupSizeTextView.setText(String.valueOf(roomDto.getEntries().size() + " Entries"));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.contest_entry_list_child_item, null);
            holder = new ChildHolder();
            holder.rootLayout = (LinearLayout) convertView.findViewById(R.id.contest_entry_list_child_root_layout);
            holder.photoImageView = (HmImageView) convertView.findViewById(R.id.contest_entry_list_child_imageView);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.contest_entry_list_child_TextView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        if (childPosition % 2 == 0) {
            holder.rootLayout.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.black_1));
        } else {
            holder.rootLayout.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.black2));
        }

        if (mRoomsList != null && mRoomsList.size() > groupPosition) {
            ContestRoomDto roomDto = mRoomsList.get(groupPosition);
            if (roomDto != null && roomDto.getEntries() != null) {
                ContestRoomEntryDto entry = roomDto.getEntries().get(childPosition);

                if (entry != null) {
                    holder.nameTextView.setText(!TextUtils.isEmpty(entry.getUserName()) ? entry.getUserName() : "");
                    if (!TextUtils.isEmpty(entry.getUserPicUrl())) {
                        holder.photoImageView.setImageUrl(entry.getUserPicUrl());
                    }
                }
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class GroupHolder {
        LinearLayout rootLayout;
        TextView groupTitleTextView;
        TextView groupSizeTextView;
        ImageView groupIndicatorImageView;
    }

    private class ChildHolder {
        LinearLayout rootLayout;
        HmImageView photoImageView;
        TextView nameTextView;
    }
}
