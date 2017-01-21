package in.sportscafe.nostragamus.module.fuzzysearch;

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

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

/**
 * Created by deepanshu on 4/10/16.
 */

public abstract class AbstractFuzzySearchFragment extends NostragamusFragment implements AbstractFuzzySearchView {

    public abstract String getSearchHint();

    private RecyclerView mRecyclerView;

    private EditText mEtSearch;

    private AbstractFuzzySearchPresenter mOthersAnswersPresenter;

    private boolean mUserTyping = true;

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
                if(mUserTyping) {
                    mOthersAnswersPresenter.filterSearch(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        mEtSearch.setHint(getSearchHint());

        this.mOthersAnswersPresenter = AbstractFuzzySearchPresenterImpl.newInstance(this);
        mOthersAnswersPresenter.onCreateFuzzyPlayers(getArguments());
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setSearchItem(String name) {
        mUserTyping = false;
        mEtSearch.setText(name);
        mEtSearch.setSelection(name.length());
        mUserTyping = true;
        AppSnippet.hideSoftKeyBoard(getContext(), mEtSearch);
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