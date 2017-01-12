package in.sportscafe.nostragamus.module.fuzzyplayers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

import static com.google.android.gms.analytics.internal.zzy.i;

/**
 * Created by deepanshu on 4/10/16.
 */

public class FuzzyPlayersFragment extends NostragamusFragment implements FuzzyPlayersView {

    private RecyclerView mRecyclerView;

    private EditText mEtSearch;

    private FuzzyPlayersPresenter mOthersAnswersPresenter;

    public static FuzzyPlayersFragment newInstance() {
        return new FuzzyPlayersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fuzzy_players, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) findViewById(R.id.fuzzy_players_rcv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mEtSearch = ((EditText) findViewById(R.id.fuzzy_players_et_search));
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mOthersAnswersPresenter.filterSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        this.mOthersAnswersPresenter = FuzzyPlayersPresenterImpl.newInstance(this);
        mOthersAnswersPresenter.onCreateFuzzyPlayers(getArguments());
    }

    @Override
    public void setAdapter(FuzzyPlayerAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public boolean isListShowing() {
        return mRecyclerView.getAdapter().getItemCount() > 0;
    }

    public boolean clearList() {
        boolean listShowing = isListShowing();
        mEtSearch.setText("");
        return listShowing;
    }
}