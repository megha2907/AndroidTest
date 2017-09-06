package in.sportscafe.nostragamus.module.newChallenges.adapter.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;

/**
 * Created by sandip on 23/08/17.
 */

public class NewChallengesItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView challengeNameTextView;
    public TextView challengeTournamentTextView;
    public TextView challengeDateTextView;
    public TextView startTimeTextView;
    public TextView gameLeftTextView;
    public TextView prizeTextView;
    public LinearLayout gameIconLinearLayout;

    private NewChallengeAdapterListener clickListener;

    public NewChallengesItemViewHolder(View itemView, @NonNull NewChallengeAdapterListener listener) {
        super(itemView);
        this.clickListener = listener;

        challengeNameTextView = (TextView) itemView.findViewById(R.id.challenge_name_textView);
        challengeTournamentTextView = (TextView) itemView.findViewById(R.id.challenge_tournaments_textView);
        challengeDateTextView = (TextView) itemView.findViewById(R.id.challenge_date_textView);
        startTimeTextView = (TextView) itemView.findViewById(R.id.challenge_start_time_textView);
        gameLeftTextView = (TextView) itemView.findViewById(R.id.challenge_game_left_textView);
        prizeTextView = (TextView) itemView.findViewById(R.id.challenge_prizes_textView);
        gameIconLinearLayout = (LinearLayout) itemView.findViewById(R.id.challenge_sports_icons_container);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onChallengeClicked(null);
        }
    }
}
