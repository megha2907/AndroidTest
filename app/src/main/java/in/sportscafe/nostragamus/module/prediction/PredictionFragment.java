package in.sportscafe.nostragamus.module.prediction;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sportscafe.nostracardstack.cardstack.CardStack;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.prediction.adapter.PredictionQuestionAdapterListener;
import in.sportscafe.nostragamus.module.prediction.adapter.PredictionQuestionsCardAdapter;
import in.sportscafe.nostragamus.module.prediction.dto.PredictionQuestion;

/**
 * A simple {@link Fragment} subclass.
 */
public class PredictionFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = PredictionFragment.class.getSimpleName();
    private MediaPlayer mp;
    private PredictionFragmentListener mFragmentListener;

    public PredictionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prediction, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PredictionFragmentListener) {
            mFragmentListener = (PredictionFragmentListener) context;
        } else {
            throw new RuntimeException(PredictionActivity.class.getSimpleName() + " must implement "
                    + PredictionFragmentListener.class.getSimpleName());
        }
    }

    private void initView(View rootView) {
        ImageView backButton = (ImageView) rootView.findViewById(R.id.prediction_back_btn);
        RelativeLayout doublerPowerup = (RelativeLayout) rootView.findViewById(R.id.prediction_doubler_Layout);
        RelativeLayout noNegPowerup = (RelativeLayout) rootView.findViewById(R.id.prediction_noNeg_Layout);
        RelativeLayout playerPollPowerup = (RelativeLayout) rootView.findViewById(R.id.prediction_player_poll_Layout);

        doublerPowerup.setOnClickListener(this);
        noNegPowerup.setOnClickListener(this);
        playerPollPowerup.setOnClickListener(this);
        backButton.setOnClickListener(this);

        /* Card stack */
        CardStack cardStack = (CardStack) rootView.findViewById(R.id.prediction_cardStack);
        cardStack.setContentResource(R.layout.prediction_card_layout);
        cardStack.setCanSwipe(true);
        cardStack.setListener(getCardEventsListener());
        cardStack.setAdapter(new PredictionQuestionsCardAdapter(getContext(), getList(), getQuestionsCardAdapterListener(cardStack)));

        mp = MediaPlayer.create(getContext(), R.raw.beep6);
    }

    @NonNull
    private PredictionQuestionAdapterListener getQuestionsCardAdapterListener(final CardStack cardStack) {
        return new PredictionQuestionAdapterListener() {
            @Override
            public void onLeftOptionClicked(int pos) {
                if (cardStack != null) {
                    cardStack.discardTopOnButtonClick(1);   // 1: left, 2: right, 3:top, 4:bottom
                }
            }

            @Override
            public void onRightOptionClicked(int pos) {
                if (cardStack != null) {
                    cardStack.discardTopOnButtonClick(2);
                }
            }
        };
    }

    private List<PredictionQuestion> getList() {
        List<PredictionQuestion> list = new ArrayList<>();

        for (int i = 0 ; i < 5; i++) {
            PredictionQuestion question = new PredictionQuestion();
            question.setQuestionId(i);
            question.setQuestionText("aksdajdajshdjakhdajhaksjdhasjdhasdjad");
            question.setQuestionDescription("ajkjkjdfkjdklfjksl akjsdfkjasdfkl \n jasdf;ksjdfks ajsdfksjdn asdhf");
            question.setOption1("option "+i);
            question.setOption2("option "+i);
            list.add(question);
        }

        return list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initHeading();
        animateTopBottomLayouts();
    }

    private void initHeading() {
        if (getView() != null && getActivity() != null) {
            TextView headingTextView = (TextView) getView().findViewById(R.id.prediction_heading_textView);
            TextView subHeadingTextView = (TextView) getView().findViewById(R.id.prediction_sub_heading_textView);


            headingTextView.setText("Test heading challenge now");
            subHeadingTextView.setText("sub heading of contest challenge");
        }
    }

    private void animateTopBottomLayouts() {
        if (getView() != null && getActivity() != null) {
            final LinearLayout topLayout = (LinearLayout) getView().findViewById(R.id.prediction_top_layout);
            final LinearLayout bottomLayout = (LinearLayout) getView().findViewById(R.id.prediction_bottom_layout);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    topLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.prediction_top_view_anim));
                    bottomLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.prediction_bottom_view_anim));
                    topLayout.setVisibility(View.VISIBLE);
                    bottomLayout.setVisibility(View.VISIBLE);
                }
            }, 500);
        }
    }

    @NonNull
    private CardStack.CardEventListener getCardEventsListener() {
        return new CardStack.CardEventListener() {
            @Override
            public boolean swipeEnd(int direction, float distance) {
                boolean endSwipe = false;
                if (distance > 300) {
                    endSwipe = true;
                }
                return endSwipe;
            }

            @Override
            public boolean swipeStart(int i, float v) {
                return true;
            }

            @Override
            public boolean swipeContinue(int i, float v, float v1) {
                return true;
            }

            @Override
            public void discarded(int index, int direction) {
                if (mp != null && !mp.isPlaying()) {
                    mp.start();
                }

                switch (direction) {
                    case 0:
//                        Toast.makeText(MainActivity.this, "direction 0", Toast.LENGTH_SHORT).show();
                        Log.d("Card", "0");
                        break;

                    case 1:
//                        Toast.makeText(MainActivity.this, "direction 1", Toast.LENGTH_SHORT).show();
                        Log.d("Card", "1");
                        break;

                    case 2:
//                        Toast.makeText(MainActivity.this, "direction 2", Toast.LENGTH_SHORT).show();
                        Log.d("Card", "2");
                        break;

                    case 3:
//                        Toast.makeText(MainActivity.this, "direction 4", Toast.LENGTH_SHORT).show();
                        Log.d("Card", "3");
                        break;
                }
            }

            @Override
            public void topCardTapped() {
                Log.d("Card", "Top card tapped");
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prediction_back_btn:
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
                break;

            case R.id.prediction_doubler_Layout:
                onDoublerPowerUpClicked();
                break;

            case R.id.prediction_noNeg_Layout:
                onNoNegativeClicked();
                break;

            case R.id.prediction_player_poll_Layout:
                onPlayerPollClicked();
                break;
        }
    }

    private void onPlayerPollClicked() {

    }

    private void onNoNegativeClicked() {

    }

    private void onDoublerPowerUpClicked() {

    }
}
