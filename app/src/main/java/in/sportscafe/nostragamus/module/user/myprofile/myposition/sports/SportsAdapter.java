package in.sportscafe.nostragamus.module.user.myprofile.myposition.sports;

/**
 * Created by deepanshi on 9/21/16.
 */
        import android.content.Context;
        import android.support.annotation.NonNull;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
        import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

        import java.util.List;

        import in.sportscafe.nostragamus.R;
        import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.BaseSummary;

public class SportsAdapter extends ExpandableRecyclerAdapter<SportsViewParentHolder, SportsViewChildHolder> {

    private LayoutInflater mInflator;

    public SportsAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
    }

    @Override
    public SportsViewParentHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View recipeView = mInflator.inflate(R.layout.inflater_myglobal_sportview, parentViewGroup, false);
        return new SportsViewParentHolder(recipeView);
    }

    @Override
    public SportsViewChildHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View ingredientView = mInflator.inflate(R.layout.inflater_myglobal_tournamentsview, childViewGroup, false);
        return new SportsViewChildHolder(ingredientView);
    }

    @Override
    public void onBindParentViewHolder(SportsViewParentHolder recipeViewHolder, int position, ParentListItem parentListItem) {
        BaseSummary tourSummary = (BaseSummary) parentListItem;
        recipeViewHolder.bind(tourSummary);
    }

    @Override
    public void onBindChildViewHolder(SportsViewChildHolder ingredientViewHolder, int position, Object childListItem) {
        BaseSummary tourSummary = (BaseSummary) childListItem;
        ingredientViewHolder.bind(tourSummary);
    }
}

