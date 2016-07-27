package in.sportscafe.scgame.module.play.myresults;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsActivity extends ScGameActivity implements MyResultsView {

    private RecyclerView mRvMyResults;

    private MyResultsPresenter mResultsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_results);

        this.mRvMyResults = (RecyclerView) findViewById(R.id.my_results_rv);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        // It is to find the scrolling stage to do the pagination
        this.mRvMyResults.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mResultsPresenter.onArticleScroll(linearLayoutManager.findFirstVisibleItemPosition(),
                        linearLayoutManager.getChildCount(), linearLayoutManager.getItemCount());
            }
        });
        this.mRvMyResults.setLayoutManager(linearLayoutManager);
        this.mRvMyResults.setHasFixedSize(true);

        this.mResultsPresenter = MyResultPresenterImpl.newInstance(this);
        this.mResultsPresenter.onCreateMyResults();
    }

    @Override
    public void setAdapter(MyResultsAdapter myResultsAdapter) {
        mRvMyResults.setAdapter(myResultsAdapter);
    }

    public void onBack(View view) {
        onBackPressed();
    }
}