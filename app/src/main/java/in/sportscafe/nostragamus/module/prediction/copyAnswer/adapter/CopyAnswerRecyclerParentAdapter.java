package in.sportscafe.nostragamus.module.prediction.copyAnswer.adapter;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerContest;
import in.sportscafe.nostragamus.utils.AnimationHelper;

/**
 * Created by sc on 24/1/18.
 */

public class CopyAnswerRecyclerParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CopyAnswerContest> mContestList;
    private CopyAnswerParentAdapterListener mParentAdapterListener;

    public CopyAnswerRecyclerParentAdapter(List<CopyAnswerContest> copyAnswerContestList,
                                           CopyAnswerParentAdapterListener parentAdapterListener) {
        mContestList = copyAnswerContestList;
        mParentAdapterListener = parentAdapterListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.copy_answer_parent_card_item, parent, false);
        return new CopyAnswerParentViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= 0 && position < mContestList.size()) {
            CopyAnswerParentViewHolder viewHolder = (CopyAnswerParentViewHolder) holder;
            bindChallengeDetails(viewHolder, mContestList.get(position));
            initChildLayout(viewHolder, position);
        }

    }

    private void initChildLayout(CopyAnswerParentViewHolder holder, int position) {
        if (position >= 0 && position < mContestList.size()) {
            CopyAnswerContest contest = mContestList.get(position);

            if (contest != null) {
                CopyAnswerChildAdapter childAdapter = new CopyAnswerChildAdapter(contest.getAnswers());
                holder.nestedRecyclerView.setAdapter(childAdapter);
            }
        }
    }

    private void bindChallengeDetails(CopyAnswerParentViewHolder holder, CopyAnswerContest copyAnswerContest) {
        if (holder != null && copyAnswerContest != null) {
            holder.contestTitleTextView.setText(copyAnswerContest.getConfigName());
            holder.challengeNameTextView.setText(copyAnswerContest.getChallengeName());
            holder.answeredTimeTextView.setText(copyAnswerContest.getHoursSincePlayed() + " hrs");
        }
    }

    @Override
    public int getItemCount() {
        return (mContestList != null) ? mContestList.size() : 0;
    }

    public class CopyAnswerParentViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        ImageView arrowImgView;
        TextView contestTitleTextView;
        TextView challengeNameTextView;
        TextView answeredTimeTextView;
        Button useButton;
        LinearLayout topVisibleLayout;
        LinearLayout contentInvisibleLayout;
        RecyclerView nestedRecyclerView;

        public CopyAnswerParentViewHolder(View itemView) {
            super(itemView);

            arrowImgView = (ImageView) itemView.findViewById(R.id.direction_arrow_imgView);
            contestTitleTextView = (TextView) itemView.findViewById(R.id.copy_answer_item_header_textView);
            challengeNameTextView = (TextView) itemView.findViewById(R.id.copy_answer_item_challenge_name_textView);
            answeredTimeTextView = (TextView) itemView.findViewById(R.id.copy_answer_answered_time_textView);
            useButton = (Button) itemView.findViewById(R.id.copy_answer_use_button);
            nestedRecyclerView = (RecyclerView) itemView.findViewById(R.id.copy_answer_nested_recyclerView);
            topVisibleLayout = (LinearLayout) itemView.findViewById(R.id.copy_answer_item_header_layout);
            contentInvisibleLayout = (LinearLayout) itemView.findViewById(R.id.copy_answer_collapsable_layout);

            nestedRecyclerView.setLayoutManager(new LinearLayoutManager(nestedRecyclerView.getContext()));
            nestedRecyclerView.setHasFixedSize(true);

            useButton.setOnClickListener(this);
            arrowImgView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.copy_answer_use_button:
                    onUseButtonClicked(getAdapterPosition());
                    break;

                case R.id.direction_arrow_imgView:
                    onDetailsClicked(getAdapterPosition(), this);
                    break;
            }
        }

        private void onUseButtonClicked(int adapterPosition) {
            if (mParentAdapterListener != null && adapterPosition < mContestList.size()) {
                mParentAdapterListener.onUseClicked(mContestList.get(adapterPosition));
            }
        }
    }

    private void onDetailsClicked(int adapterPosition, final CopyAnswerParentViewHolder holder) {
        if (holder.contentInvisibleLayout.getVisibility() == View.VISIBLE) {
            AnimationHelper.collapse(holder.contentInvisibleLayout);
            holder.arrowImgView.setImageResource(R.drawable.challenge_down_arrow);
        } else {
            AnimationHelper.expand(holder.contentInvisibleLayout);
            holder.arrowImgView.setImageResource(R.drawable.challenge_up_arrow);

            if (mParentAdapterListener != null) {
                mParentAdapterListener.scrollToItem(adapterPosition);
            }
        }
    }
}
