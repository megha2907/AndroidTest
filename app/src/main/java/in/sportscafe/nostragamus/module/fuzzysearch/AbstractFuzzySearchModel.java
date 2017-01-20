package in.sportscafe.nostragamus.module.fuzzysearch;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.module.common.Adapter;

/**
 * Created by deepanshu on 12/10/16.
 */
public interface AbstractFuzzySearchModel {

    void init(Bundle bundle);

    Adapter createAdapter(Context context);

    void filterSearch(String key);
}