package in.sportscafe.scgame.module.notifications;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomButton;
import com.moengage.addon.inbox.InboxFragment;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.home.OnHomeActionListener;

/**
 * Created by deepanshi on 26/8/16.
 */
public class NotificationInboxFragment extends InboxFragment implements View.OnClickListener {

    private OnHomeActionListener mOnHomeActionListener;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

        }
    }

    @Override
    public void onClick(View view) {
        mOnHomeActionListener.onClickPlay();
    }
}
