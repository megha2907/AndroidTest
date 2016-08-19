package in.sportscafe.scgame.module.user.group.newgroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.common.SpacesItemDecoration;

/**
 * Created by Jeeva on 1/7/16.
 */
public class NewGroupActivity extends ScGameActivity implements NewGroupView,
        View.OnClickListener {

    private EditText mEtGroupName;

    private RecyclerView mRvSportSelection;

    private NewGroupPresenter mNewGroupPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        this.mEtGroupName = (EditText) findViewById(R.id.new_group_et_group_name);

        this.mRvSportSelection = (RecyclerView) findViewById(R.id.new_group_rcv);
        this.mRvSportSelection.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_10)));
        this.mRvSportSelection.setLayoutManager(new GridLayoutManager(this, 3));
        this.mRvSportSelection.setHasFixedSize(true);

        this.mNewGroupPresenter = NewGroupPresenterImpl.newInstance(this);
        this.mNewGroupPresenter.onCreateNewGroup();
    }

    @Override
    public void setAdapter(GrpSportSelectionAdapter adapter) {
        this.mRvSportSelection.setAdapter(adapter);
    }

    @Override
    public void setSuccessBack(Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_group_btn_cancel:
                onBackPressed();
                break;
            case R.id.new_group_btn_done:
                mNewGroupPresenter.onClickDone(getTrimmedText(mEtGroupName));
                PayloadBuilder builder = new PayloadBuilder();
                builder.putAttrString("GroupName", getTrimmedText(mEtGroupName))
                        .putAttrString("UserID", ScGameDataHandler.getInstance().getUserId());
                MoEHelper.getInstance(getContext()).trackEvent("CREATE GROUP-ONCLICK", builder.build());
                break;
        }
    }
}