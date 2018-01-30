package in.sportscafe.nostragamus.module.prediction.copyAnswer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerContest;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerQuestion;

/**
 * Created by sc on 29/1/18.
 */

public class CopyAnswerExpandableAdapter extends BaseExpandableListAdapter {

    private List<CopyAnswerContest> mContestList;
    private LayoutInflater mLayoutInflater;
    private CopyAnswerAdapterListener mAdapterListener;
    private HeaderHolder mHeaderHolderViewHolder;

    public CopyAnswerExpandableAdapter(Context context,
                                       List<CopyAnswerContest> copyAnswerContestList,
                                       CopyAnswerAdapterListener listener) {
        mContestList = copyAnswerContestList;
        mLayoutInflater = LayoutInflater.from(context);
        mAdapterListener = listener;
    }

    @Override
    public int getGroupCount() {
        return (mContestList != null) ? mContestList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mContestList != null && mContestList.get(groupPosition) != null &&
                mContestList.get(groupPosition).getAnswers() != null) {
            return mContestList.get(groupPosition).getAnswers().size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (mContestList != null) {
            return mContestList.get(groupPosition);
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (mContestList != null && mContestList.get(groupPosition) != null &&
                mContestList.get(groupPosition).getAnswers() != null) {
            return mContestList.get(groupPosition).getAnswers().get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (groupPosition == 0) {
            convertView = setHeader(parent);
        } else {
            if (groupPosition > 0 && groupPosition < mContestList.size()) {
                final CopyAnswerContest copyAnswerContest = mContestList.get(groupPosition);

                convertView = mLayoutInflater.inflate(R.layout.copy_answer_parent_card_item, parent, false);
                GroupHolder holder = new GroupHolder();
                holder.arrowImgView = (ImageView) convertView.findViewById(R.id.direction_arrow_imgView);
                holder.contestTitleTextView = (TextView) convertView.findViewById(R.id.copy_answer_item_header_textView);
                holder.challengeNameTextView = (TextView) convertView.findViewById(R.id.copy_answer_item_challenge_name_textView);
                holder.answeredTimeTextView = (TextView) convertView.findViewById(R.id.copy_answer_answered_time_textView);
                holder.useButton = (Button) convertView.findViewById(R.id.copy_answer_use_button);
                holder.useButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAdapterListener != null) {
                            mAdapterListener.onUseClicked(copyAnswerContest, mHeaderHolderViewHolder.usePowerUpCheckBox.isChecked());
                        }
                    }
                });

                if (isExpanded) {
                    holder.arrowImgView.setImageResource(R.drawable.challenge_up_arrow);
                } else {
                    holder.arrowImgView.setImageResource(R.drawable.challenge_down_arrow);
                }

                holder.contestTitleTextView.setText(copyAnswerContest.getConfigName());
                holder.challengeNameTextView.setText(copyAnswerContest.getChallengeName());

                String str = "";
                if (copyAnswerContest.getHoursSincePlayed() > 0) {
                    str = "Answered " + copyAnswerContest.getHoursSincePlayed() + " hours ago";
                } else {
                    str = "Answered less than an hour ago";
                }
                holder.answeredTimeTextView.setText(str);
            }
        }

        return convertView;
    }

    @NonNull
    private View setHeader(ViewGroup parent) {
        View convertView;
        convertView = mLayoutInflater.inflate(R.layout.copy_answer_list_header_item, parent, false);
        mHeaderHolderViewHolder = new HeaderHolder();
        mHeaderHolderViewHolder.contestCounterTextView = (TextView) convertView.findViewById(R.id.copy_answer_title_counter_textView);
        mHeaderHolderViewHolder.usePowerUpCheckBox = (CheckBox) convertView.findViewById(R.id.copy_answer_use_powerup_checkbox);

        mHeaderHolderViewHolder.contestCounterTextView.setText("("+ (mContestList.size() - 1) + ")");
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (groupPosition > 0) {
            convertView = mLayoutInflater.inflate(R.layout.copy_answer_child_item, parent, false);

            ChildHolder holder = new ChildHolder();
            holder.questionTextView = (TextView) convertView.findViewById(R.id.copy_answer_item_question_textView);
            holder.answerTextView = (TextView) convertView.findViewById(R.id.copy_answer_item_answer_textView);
            holder.doublerImgView = (ImageView) convertView.findViewById(R.id.copy_answer_powerup_2x_right);
            holder.noNegImgView = (ImageView) convertView.findViewById(R.id.copy_answer_powerup_noNeg_right);
            holder.playerPollImgView = (ImageView) convertView.findViewById(R.id.copy_answer_powerup_audience_right);
            holder.lineView = convertView.findViewById(R.id.lineView);
            holder.parentChildDividerView = convertView.findViewById(R.id.copy_ans_parent_child_divider);

            if (childPosition == 0) {
                holder.parentChildDividerView.setVisibility(View.VISIBLE);
            }

            bindChildValues(groupPosition, childPosition, holder);
        }
        return convertView;
    }

    private void bindChildValues(int groupPosition, int childPosition, ChildHolder holder) {
        if (groupPosition >= 0 && groupPosition < mContestList.size()) {
            List<CopyAnswerQuestion> questionList = mContestList.get(groupPosition).getAnswers();

            if (childPosition >= 0 && childPosition < questionList.size()) {
                CopyAnswerQuestion answerQuestion = questionList.get(childPosition);

                holder.questionTextView.setText(answerQuestion.getQuestionText());
                switch (answerQuestion.getAnswer()) {
                    case 1:
                        holder.answerTextView.setText(answerQuestion.getQuestionOption1());
                        break;

                    case 2:
                        holder.answerTextView.setText(answerQuestion.getQuestionOption2());
                        break;

                    case 3:
                        holder.answerTextView.setText(answerQuestion.getQuestionOption3());
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
                if (childPosition == questionList.size() - 1) {
                    holder.lineView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class HeaderHolder {
        TextView contestCounterTextView;
        CheckBox usePowerUpCheckBox;
    }

    private class GroupHolder {
        ImageView arrowImgView;
        TextView contestTitleTextView;
        TextView challengeNameTextView;
        TextView answeredTimeTextView;
        Button useButton;
    }

    private class ChildHolder {
        TextView questionTextView;
        TextView answerTextView;
        ImageView doublerImgView;
        ImageView noNegImgView;
        ImageView playerPollImgView;
        View lineView;
        View parentChildDividerView;
    }
}
