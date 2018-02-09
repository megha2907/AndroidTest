package in.sportscafe.nostragamus.module.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import co.mobiwise.materialintro.view.MaterialIntroView;

/**
 * Created by deepanshi on 2/8/18.
 */

public class CustomMaterialIntroView extends MaterialIntroView {

    View rootView;

    public CustomMaterialIntroView(Context context) {
        super(context);
        init(context);
    }

    public CustomMaterialIntroView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomMaterialIntroView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
    }


}
