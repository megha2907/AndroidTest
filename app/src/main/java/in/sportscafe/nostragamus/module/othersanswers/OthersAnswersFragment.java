package in.sportscafe.nostragamus.module.othersanswers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class OthersAnswersFragment extends NostragamusFragment implements OthersAnswersView {

    private RecyclerView mRvOthersAnswers;

    private OthersAnswersPresenter mOthersAnswersPresenter;

    public static OthersAnswersFragment newInstance(Bundle bundle) {
        OthersAnswersFragment fragment = new OthersAnswersFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_others_answers, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRvOthersAnswers = (RecyclerView) findViewById(R.id.my_results_rv);
        this.mRvOthersAnswers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.mRvOthersAnswers.setHasFixedSize(true);

        this.mOthersAnswersPresenter = OthersAnswersPresenterImpl.newInstance(this);
        this.mOthersAnswersPresenter.onCreateOthersAnswers(getArguments());
    }

    @Override
    public void setAdapter(OthersAnswersAdapter adapter) {
        mRvOthersAnswers.setAdapter(ViewUtils.getAnimationAdapter(adapter));
    }
}