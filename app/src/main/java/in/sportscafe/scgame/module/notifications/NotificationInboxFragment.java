package in.sportscafe.scgame.module.notifications;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomButton;
import com.moe.pushlibrary.providers.MoEDataContract;
import com.moengage.addon.inbox.InboxFragment;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.home.OnHomeActionListener;

/**
 * Created by deepanshi on 26/8/16.
 */
public class NotificationInboxFragment extends InboxFragment implements View.OnClickListener {

    private OnHomeActionListener mOnHomeActionListener;

    private Toolbar mtoolbar;

    private ImageView mLogo;

    private TextView mTitle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnHomeActionListener) {
            mOnHomeActionListener = (OnHomeActionListener) context;
        } else {
            throw new IllegalArgumentException("The base class should implement the OnHomeActionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View moView= super.onCreateView(inflater, container, savedInstanceState);
        View toolView = inflater.inflate(R.layout.activity_moe_inbox,container,false);
        ((ViewGroup) toolView.findViewById(R.id.inbox_holder)).addView(moView);
        return toolView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initToolBar();

        ListView listView = (ListView) getView().findViewById(R.id.MOEInboxList);
        listView.setDividerHeight(0);
        listView.setDivider(getResources().getDrawable(android.R.color.transparent));
        listView.setBackgroundResource(R.drawable.gradient_bg);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        super.onLoadFinished(arg0, cursor);

        if(cursor == null || cursor.getCount() == 0) {

            TextView textView =(TextView)getView().findViewById(R.id.emptyBox);
            textView.setVisibility(View.GONE);

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View view = inflater.inflate( R.layout.fragment_no_notifications, null );
            ((ViewGroup) getView()).addView(view);

            CustomButton btnplaynow=(CustomButton)view.findViewById(R.id.play_btn);
            btnplaynow.setOnClickListener(this);

            initToolBar();

        }
    }

    @Override
    public void onClick(View view) {
        mOnHomeActionListener.onClickPlay();
    }


    public void initToolBar() {
        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf");
        mtoolbar = (Toolbar) getView().findViewById(R.id.tournament_toolbar);
        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);
        mTitle.setTypeface(tftitle);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
