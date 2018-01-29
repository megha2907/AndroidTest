package in.sportscafe.nostragamus.module.prediction.copyAnswer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerQuestion;

/**
 * Created by sc on 24/1/18.
 */

public class CopyAnswerChildAdapter extends RecyclerView.Adapter<CopyAnswerChildAdapter.CopyAnswerChildViewHolder> {

    private List<CopyAnswerQuestion> mAnsweredQuestionList;

    public CopyAnswerChildAdapter(List<CopyAnswerQuestion> questions) {
        mAnsweredQuestionList = questions;
    }

    @Override
    public void onViewAttachedToWindow(final CopyAnswerChildViewHolder holder) {
        holder.setIsRecyclable(false);
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public CopyAnswerChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.copy_answer_child_item, parent, false);
        return new CopyAnswerChildViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(CopyAnswerChildViewHolder holder, int position) {
        if (position >= 0 && position < mAnsweredQuestionList.size()) {
            CopyAnswerQuestion answerQuestion = mAnsweredQuestionList.get(position);

            holder.questionTextView.setText(answerQuestion.getQuestionText());
            switch (answerQuestion.getAnswer()) {
                case 1:
                    holder.answertextView.setText(answerQuestion.getQuestionOption1());
                    break;

                case 2:
                    holder.answertextView.setText(answerQuestion.getQuestionOption2());
                    break;

                case 3:
                    holder.answertextView.setText(answerQuestion.getQuestionOption3());
                    break;
            }

            if (answerQuestion.getAppliedPowerUp() != null) {
                if (answerQuestion.getAppliedPowerUp().isDoubler()) {
                    holder.doublerImgView.setVisibility(View.VISIBLE);
                }
                if (answerQuestion.getAppliedPowerUp().isNoNegative()) {
                    holder.noNegImgView.setVisibility(View.VISIBLE);
                }
                if (answerQuestion.getAppliedPowerUp().isPlayerPoll()) {
                    holder.playerPollImgView.setVisibility(View.VISIBLE);
                }
            }

            /* Hide last line in list */
            if (position == mAnsweredQuestionList.size() - 1) {
                holder.lineView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mAnsweredQuestionList != null) ? mAnsweredQuestionList.size() : 0;
    }

    public class CopyAnswerChildViewHolder extends RecyclerView.ViewHolder {

        TextView questionTextView;
        TextView answertextView;
        ImageView doublerImgView;
        ImageView noNegImgView;
        ImageView playerPollImgView;
        View lineView;

        public CopyAnswerChildViewHolder(View itemView) {
            super(itemView);
            questionTextView = (TextView) itemView.findViewById(R.id.copy_answer_item_question_textView);
            answertextView = (TextView) itemView.findViewById(R.id.copy_answer_item_answer_textView);
            doublerImgView = (ImageView) itemView.findViewById(R.id.copy_answer_powerup_2x_right);
            noNegImgView = (ImageView) itemView.findViewById(R.id.copy_answer_powerup_noNeg_right);
            playerPollImgView = (ImageView) itemView.findViewById(R.id.copy_answer_powerup_audience_right);
            lineView = (View) itemView.findViewById(R.id.lineView);
        }
    }
}
