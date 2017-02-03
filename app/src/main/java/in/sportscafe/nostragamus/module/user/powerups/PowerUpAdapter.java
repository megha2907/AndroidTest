package in.sportscafe.nostragamus.module.user.powerups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;

/**
 * Created by deepanshi on 23/8/16.
 */
public class PowerUpAdapter extends Adapter<PowerUp, PowerUpAdapter.ViewHolder> {

    public PowerUpAdapter(Context context, List<PowerUp> powerUpList) {
        super(context);
        addAll(powerUpList);
    }

    @Override
    public PowerUp getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_powerup_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        PowerUp powerUp = getItem(position);
        holder.mBtnPowerUpCount.setText(String.valueOf(powerUp.getCount()));

        switch (powerUp.getId()) {
            case "2x_global":
                holder.mTvPowerUpName.setText("Bonus 2x");
                holder.mBtnPowerUp.setImageResource(R.drawable.powerup_icon);
                holder.mTvPowerUpDesc.setText("Double your returns when you are confident about a prediction");
                break;
            case "2x":
                holder.mTvPowerUpName.setText("2x");
                holder.mBtnPowerUp.setImageResource(R.drawable.powerup_icon);
                holder.mTvPowerUpDesc.setText("Double your returns when you are confident about a prediction");
                break;
            case "no_negs":
                holder.mTvPowerUpName.setText("No Negative Points");
                holder.mBtnPowerUp.setImageResource(R.drawable.powerup_nonegs);
                holder.mTvPowerUpDesc.setText("Avoid being penalised for an incorrect prediction");
                break;
            case "player_poll":
                holder.mTvPowerUpName.setText("Player Poll");
                holder.mBtnPowerUp.setImageResource(R.drawable.powerup_audience_poll);
                holder.mTvPowerUpDesc.setText("Peak into how other Nostragamus players have predicted");
                break;
            case "answer_flip":
                holder.mTvPowerUpName.setText("Flip Answer");
                holder.mBtnPowerUp.setImageResource(R.drawable.powerup_flip);
                holder.mTvPowerUpDesc.setText("Change your mind about a prediction you made before a match begins");
                break;
            case "match_replay":
                holder.mTvPowerUpName.setText("Replay Match");
                holder.mBtnPowerUp.setImageResource(R.drawable.replay_icon);
                holder.mTvPowerUpDesc.setText("Forget all predictions made for a match so that you can play it again");
                break;

        }

    }

    class ViewHolder extends RecyclerView.ViewHolder  {

        int id;

        View mMainView;

        TextView mTvPowerUpName;

        TextView mTvPowerUpDesc;

        CustomButton mBtnPowerUpCount;

        ImageButton mBtnPowerUp;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvPowerUpName = (TextView) V.findViewById(R.id.powerup_tv_1);
            mTvPowerUpDesc = (TextView) V.findViewById(R.id.powerup_tv_2);
            mBtnPowerUpCount = (CustomButton) V.findViewById(R.id.powerup_btn_powerup_count);
            mBtnPowerUp = (ImageButton) V.findViewById(R.id.powerup_btn);

        }

    }

}