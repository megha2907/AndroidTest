package in.sportscafe.nostragamus.module.onboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;

import static in.sportscafe.nostragamus.R.layout;

/**
 * Created by deepanshi on 17/2/17.
 */
public class AvatarAdapter extends Adapter<Avatar, AvatarAdapter.AvatarHolder> {

    private float mCardWidth;

    private float mCardHeight;

    public AvatarAdapter(Context context, List<Avatar> avatars, float cardWidth, float cardHeight) {
        super(context);
        mCardWidth = cardWidth;
        mCardHeight = cardHeight;
        addAll(avatars);
    }

    @Override
    public Avatar getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public AvatarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AvatarHolder(getLayoutInflater().inflate(layout.inflater_avatar_row, parent, false));
    }

    @Override
    public void onBindViewHolder(AvatarHolder holder, int position) {
//        holder.mMainView.setBackgroundColor(Color.parseColor("#9" + String.format("%03d", (position * 23)) + "1C"));
        Avatar avatar = getItem(position);
        if (avatar.imageRes != -1) {
            holder.mIvAvatarImage.setImageResource(avatar.imageRes);
        } else {
            holder.mIvAvatarImage.setImageDrawable(null);
        }
    }

    public class AvatarHolder extends RecyclerView.ViewHolder {

        View mMainView;

        ImageView mIvAvatarImage;

        public AvatarHolder(View V) {
            super(V);
            mMainView = V;
            mIvAvatarImage = (ImageView) V.findViewById(R.id.avatar_iv_image);
            ViewGroup.LayoutParams lp = V.getLayoutParams();
            lp.width = (int) mCardWidth;
            lp.height = (int) mCardHeight;
        }
    }
}