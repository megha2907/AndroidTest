package in.sportscafe.scgame.module.user.myprofile.myposition.groups;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.BaseSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.GroupsTourSummary;

/**
 * Created by deepanshi on 10/13/16.
 */

public class GroupsAdapter extends ExpandableRecyclerAdapter<GroupsViewParentHolder, GroupsViewChildHolder> {

    private LayoutInflater mInflator;

    public GroupsAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
    }

    @Override
    public GroupsViewParentHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View recipeView = mInflator.inflate(R.layout.inflater_groups_parent_view, parentViewGroup, false);
        return new GroupsViewParentHolder(recipeView);
    }

    @Override
    public GroupsViewChildHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View ingredientView = mInflator.inflate(R.layout.inflater_groups_child_view, childViewGroup, false);
        return new GroupsViewChildHolder(ingredientView);
    }

    @Override
    public void onBindParentViewHolder(GroupsViewParentHolder recipeViewHolder, int position, ParentListItem parentListItem) {
        BaseSummary tourSummary = (BaseSummary) parentListItem;
        recipeViewHolder.bind(tourSummary);
    }

    @Override
    public void onBindChildViewHolder(GroupsViewChildHolder ingredientViewHolder, int position, Object childListItem) {
        BaseSummary tourSummary = (BaseSummary) childListItem;
        ingredientViewHolder.bind(tourSummary);
    }
}

