package in.sportscafe.scgame.module.user.myprofile.myposition;

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

        import in.sportscafe.scgame.R;
        import in.sportscafe.scgame.module.user.myprofile.myposition.dto.RankSummary;
        import in.sportscafe.scgame.module.user.myprofile.myposition.dto.TourSummary;

public class MyGlobalAdapter extends ExpandableRecyclerAdapter<MyGlobalViewParentHolder, MyGlobalViewChildHolder> {

    private LayoutInflater mInflator;

    public MyGlobalAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
    }

    @Override
    public MyGlobalViewParentHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View recipeView = mInflator.inflate(R.layout.inflater_myglobal_sportview, parentViewGroup, false);
        return new MyGlobalViewParentHolder(recipeView);
    }

    @Override
    public MyGlobalViewChildHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View ingredientView = mInflator.inflate(R.layout.inflater_myglobal_tournamentsview, childViewGroup, false);
        return new MyGlobalViewChildHolder(ingredientView);
    }

    @Override
    public void onBindParentViewHolder(MyGlobalViewParentHolder recipeViewHolder, int position, ParentListItem parentListItem) {
        RankSummary rankSummary = (RankSummary) parentListItem;
        recipeViewHolder.bind(rankSummary);
    }

    @Override
    public void onBindChildViewHolder(MyGlobalViewChildHolder ingredientViewHolder, int position, Object childListItem) {
        RankSummary tourSummary = (RankSummary) childListItem;
        ingredientViewHolder.bind(tourSummary);
    }
}

