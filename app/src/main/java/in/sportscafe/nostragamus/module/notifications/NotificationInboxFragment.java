package in.sportscafe.nostragamus.module.notifications;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomButton;
import com.moengage.addon.inbox.InboxFragment;
import com.moengage.addon.inbox.InboxManager;

import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 26/8/16.
 */
public class NotificationInboxFragment extends InboxFragment implements View.OnClickListener {

    private Toolbar mtoolbar;

    private ImageView mLogo;

    private TextView mTitle;

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
        listView.setBackgroundResource(R.color.black);
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Log.v("long clicked","pos: " + pos);
                InboxManager.ViewHolder holder = (InboxManager.ViewHolder) arg1.getTag();
                long notificationId =holder.inboxMessage._id;
                Log.v("long clicked","notification: " + notificationId);
                return true;
            }
        });

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

    public void initToolBar() {
        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf");
        mtoolbar = (Toolbar) getView().findViewById(R.id.tournament_toolbar);
        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);
        mTitle.setTypeface(tftitle);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onClick(View v) {

    }
}