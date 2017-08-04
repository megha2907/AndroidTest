//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package in.sportscafe.nostragamus.module.notifications;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.ForceLoadContentObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.moe.pushlibrary.providers.MoEDataContract.MessageEntity;
import com.moengage.addon.inbox.MoEInboxListAdapter;
import com.moengage.addon.inbox.R.id;
import com.moengage.addon.inbox.R.layout;
import com.moengage.core.Logger;

public class InboxFragment extends Fragment implements LoaderCallbacks<Cursor> {
    private int messageLoader = System.identityHashCode(this);
    private MoEInboxListAdapter mAdapter = null;
    private ListView mListView;
    public static final String BUNDLE_EXTRA_FILTER = "filter";
    private View mEmptyMsg;
    private static final String KEY_CLICK_DISABLED = "CLICK_DISABLED";
    ContentObserver mDataObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        public void onChange(boolean selfChange, Uri uri) {
            if(InboxFragment.this.getActivity() != null) {
                Loader vLoader = InboxFragment.this.getActivity().getSupportLoaderManager().getLoader(InboxFragment.this.messageLoader);
                if(null != vLoader) {
                    Logger.d("Chat Content changed");
                    vLoader.onContentChanged();
                }

            }

        }
    };

    public InboxFragment() {
    }

    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        String filterTag = null;
        if(null != arg1 && arg1.containsKey("filter")) {
            filterTag = arg1.getString("filter");
        }

        return new InboxFragment.CustomCursorLoader(this.getActivity().getApplicationContext(), filterTag);
    }

    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        this.mAdapter.swapCursor(cursor);
        if(cursor == null || cursor.getCount() == 0) {
            this.mListView.setVisibility(View.VISIBLE);
            this.mEmptyMsg.setVisibility(View.GONE);
        }

    }

    public void onStart() {
        super.onStart();
        this.getActivity().getContentResolver().registerContentObserver(MessageEntity.getContentUri(this.getActivity().getApplicationContext()), true, this.mDataObserver);
        this.getActivity().getSupportLoaderManager().initLoader(this.messageLoader, this.getActivity().getIntent().getExtras(), this);
    }

    public void onStop() {
        super.onStop();
        this.getActivity().getContentResolver().unregisterContentObserver(this.mDataObserver);
        this.getActivity().getSupportLoaderManager().destroyLoader(this.messageLoader);
    }

    public void onLoaderReset(Loader<Cursor> arg0) {
        this.mAdapter.swapCursor(null);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(layout.moe_inbox, container, false);
        this.mListView = (ListView)inflatedView.findViewById(id.MOEInboxList);
        this.mAdapter = new MoEInboxListAdapter(this.getActivity(), null);
        this.mListView.setAdapter(this.mAdapter);
        this.mAdapter.setClickable(!this.disableClick());
        this.mEmptyMsg = inflatedView.findViewById(id.emptyBox);
        Bundle args = this.getArguments();
        if(null != args && args.containsKey("filter")) {
            this.getActivity().getSupportLoaderManager().restartLoader(this.messageLoader, args, this);
        }

        return inflatedView;
    }

    public void onQueryTextChanged(String msgTag) {
        Bundle bundle = new Bundle();
        bundle.putString("filter", msgTag);
        this.getActivity().getSupportLoaderManager().restartLoader(this.messageLoader, bundle, this);
    }

    private boolean disableClick() {
        try {
            ComponentName e = new ComponentName(this.getActivity(), this.getActivity().getClass());
            Bundle data = this.getActivity().getPackageManager().getActivityInfo(e,0).metaData;
            if(null != data && data.containsKey("CLICK_DISABLED")) {
                return data.getBoolean("CLICK_DISABLED");
            }
        } catch (NameNotFoundException var3) {
            Logger.e("MoEInboxActivity:disableClick", var3);
        }

        return false;
    }

    static class CustomCursorLoader extends CursorLoader {
        private Context mContext;
        private String filterTag;
       // private final android.support.v4.content.Loader<Cursor>.ForceLoadContentObserver mObserver = new ForceLoadContentObserver(this);

        public CustomCursorLoader(Context context, String filterTag) {
            super(context);
            this.mContext = context;
            this.filterTag = filterTag;
        }

        public Cursor loadInBackground() {
            String selectionClause = null;
            String[] selectionArgs = null;
            if(this.filterTag != null) {
                selectionClause = "msg_tag = ?";
                selectionArgs = new String[]{this.filterTag};
            }

            Cursor cursor = this.mContext.getContentResolver().query(MessageEntity.getContentUri(this.mContext), MessageEntity.PROJECTION, selectionClause, selectionArgs, "gtime DESC");
            if(cursor != null && cursor.getCount() > 0) {
               // cursor.registerContentObserver(this.mObserver);
            }

            return cursor;
        }
    }
}
