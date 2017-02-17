package in.sportscafe.nostragamus.module.resultspeek;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import in.sportscafe.nostragamus.module.resultspeek.dto.ResultsPeek;

/**
 * Created by deepanshi on 2/16/17.
 */

public interface ResultsPeekModel {

    void init(Bundle bundle);

    RecyclerView.Adapter getResultsPeekAdapter(Context context);
}
