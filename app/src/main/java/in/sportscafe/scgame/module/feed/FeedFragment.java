package in.sportscafe.scgame.module.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameFragment;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FeedFragment extends ScGameFragment implements FeedView {

    private RecyclerView mRcvFeed;

    private FeedPresenter mFeedPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRcvFeed = (RecyclerView) findViewById(R.id.feed_rv);
        this.mRcvFeed.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRcvFeed.setHasFixedSize(true);

        this.mFeedPresenter = FeedPresenterImpl.newInstance(this);
        this.mFeedPresenter.onCreateFeed();
    }

    @Override
    public void setAdapter(FeedAdapter feedAdapter, int movePosition) {
        mRcvFeed.setAdapter(feedAdapter);
        mRcvFeed.getLayoutManager().scrollToPosition(movePosition);
    }
}