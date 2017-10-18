package in.sportscafe.nostragamus.module.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.EnhancedLinkMovementMethod;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;


/**
 * Created by sandip on 11/09/17.
 */

public class TimelineHelper {

    public enum MatchTimelineTypeEnum {
        IN_PLAY_HEADLESS, IN_PLAY_JOINED, IN_PLAY_MATCHES_SCREEN
    }


    private static View getLineView(Context context, String matchStatus, boolean played, MatchTimelineTypeEnum typeEnum) {
        View view = View.inflate(context, R.layout.match_timeline_line_view, null);

        switch (typeEnum) {
            case IN_PLAY_HEADLESS:
                if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.COMPLETED)) {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.games_timeline_brown_line));
                } else {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.games_timeline_brown_line));
                }
                break;

            case IN_PLAY_JOINED:
                if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.COMPLETED)) {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.games_timeline_yellow_line));
                } else if (matchStatus.equals(Constants.InPlayMatchStatus.LIVE)) {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.games_timeline_grey_line));
                } else {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.games_timeline_grey_line));

                }
                break;
            case IN_PLAY_MATCHES_SCREEN:
                if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.COMPLETED)) {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.games_timeline_yellow_line));
                } else if (matchStatus.equals(Constants.InPlayMatchStatus.LIVE)) {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.games_timeline_grey_line));
                } else {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.games_timeline_grey_line));

                }
                break;
        }

        return view;
    }

    private static View getNodeView(Context context, String matchStatus, int matchAttemptedStatus, boolean played, MatchTimelineTypeEnum typeEnum) {
        View view = View.inflate(context, R.layout.match_timeline_node_view, null);
        switch (typeEnum) {
            case IN_PLAY_HEADLESS:
                if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.COMPLETED)) {

                    if (Constants.GameAttemptedStatus.COMPLETELY == matchAttemptedStatus) {
                        view.setBackgroundResource(R.drawable.timeline_grey_tick_dot);
                    } else if (Constants.GameAttemptedStatus.PARTIALLY == matchAttemptedStatus) {
                        view.setBackgroundResource(R.drawable.timeline_brown_dot);
                    } else {
                        view.setBackgroundResource(R.drawable.timeline_lock_grey);
                    }

                } else if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.ONGOING)) {

                    if (Constants.GameAttemptedStatus.COMPLETELY == matchAttemptedStatus) {
                        view.setBackgroundResource(R.drawable.timeline_grey_tick_dot);
                    } else if (Constants.GameAttemptedStatus.PARTIALLY == matchAttemptedStatus) {
                        view.setBackgroundResource(R.drawable.timeline_brown_dot);
                    } else {
                        view.setBackgroundResource(R.drawable.timeline_brown_dot);
                    }

                } else if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.LIVE)) {

                    if (Constants.GameAttemptedStatus.COMPLETELY == matchAttemptedStatus) {
                        view.setBackgroundResource(R.drawable.timeline_grey_tick_dot);
                    } else if (Constants.GameAttemptedStatus.PARTIALLY == matchAttemptedStatus) {
                        view.setBackgroundResource(R.drawable.timeline_brown_dot);
                    } else {
                        view.setBackgroundResource(R.drawable.timeline_lock_grey);
                    }

                } else if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.UPCOMING)) {

                    view.setBackgroundResource(R.drawable.timeline_lock_grey);

                } else {
                    view.setBackgroundResource(R.drawable.timeline_lock_grey);
                }

                break;

            case IN_PLAY_JOINED:

                if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.COMPLETED)) {

                    if (Constants.GameAttemptedStatus.COMPLETELY == matchAttemptedStatus) {
                        view.setBackgroundResource(R.drawable.timeline_tick_yellow_dot);
                    } else if (Constants.GameAttemptedStatus.PARTIALLY == matchAttemptedStatus) {
                        view.setBackgroundResource(R.drawable.timeline_tick_yellow_dot);
                    } else {
                        view.setBackgroundResource(R.drawable.timeline_yellow_cross_ring);
                    }

                } else if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.ONGOING)) {
                    if (played) {
                        view.setBackgroundResource(R.drawable.timeline_blue_tick);
                    } else {
                        view.setBackgroundResource(R.drawable.timeline_blue_dot);
                    }

                } else if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.LIVE)) {
                    if (played) {
                        view.setBackgroundResource(R.drawable.timeline_blue_tick);
                    } else {
                        view.setBackgroundResource(R.drawable.timeline_yellow_cross_ring);
                    }

                } else if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.UPCOMING)) {
                    view.setBackgroundResource(R.drawable.timeline_grey_dot);

                } else {
                    view.setBackgroundResource(R.drawable.timeline_grey_dot);

                }

            case IN_PLAY_MATCHES_SCREEN:

                if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.COMPLETED)) {

                    if (Constants.GameAttemptedStatus.COMPLETELY == matchAttemptedStatus) {
                        view.setBackgroundResource(R.drawable.timeline_tick_yellow_dot);
                    } else if (Constants.GameAttemptedStatus.PARTIALLY == matchAttemptedStatus) {
                        view.setBackgroundResource(R.drawable.timeline_tick_yellow_dot);
                    } else {
                        view.setBackgroundResource(R.drawable.timeline_yellow_cross_ring);
                    }

                } else if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.LIVE)) {

                    if (played) {
                        view.setBackgroundResource(R.drawable.timeline_blue_tick);
                    } else {
                        view.setBackgroundResource(R.drawable.timeline_yellow_cross_ring);
                    }

                } else if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.ONGOING)) {

                    if (played) {
                        view.setBackgroundResource(R.drawable.timeline_blue_tick);
                    } else {
                        view.setBackgroundResource(R.drawable.timeline_blue_dot);
                    }

                } else if (matchStatus.equalsIgnoreCase(Constants.InPlayMatchStatus.UPCOMING)) {

                    view.setBackgroundResource(R.drawable.timeline_grey_dot);

                } else {

                    view.setBackgroundResource(R.drawable.timeline_grey_dot);

                }

                break;
        }

        return view;
    }

    public static void addNode(LinearLayout parent, String matchStatus,
                               int matchAttemptedStatus, boolean isPlayed, boolean isNodeWithLine,
                               MatchTimelineTypeEnum typeEnum, int matchSize) {
        if (parent != null) {
            Context context = parent.getContext();
            int nodeWidthHeight = (int) context.getResources().getDimension(R.dimen.dim_12);

            int maxLineWidth;
            int matchSizeNew;

            if (matchSize == 2) {
                matchSizeNew = matchSize - 1;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_260);
            } else if (matchSize == 3) {
                matchSizeNew = matchSize - 1;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_246);
            } else if (matchSize == 4) {
                matchSizeNew = matchSize - 1;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_235);
            } else if (matchSize == 5) {
                matchSizeNew = matchSize - 1;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_220);
            } else {
                matchSizeNew = matchSize;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_230);
            }

            int lineWidth = maxLineWidth / matchSizeNew;
            int lineHeight = (int) context.getResources().getDimension(R.dimen.dim_4);


            if (isNodeWithLine) {
                View view = getLineView(context, matchStatus, isPlayed, typeEnum);
                if (view != null) {
                    parent.addView(view, parent.getChildCount(), new ViewGroup.LayoutParams(lineWidth, lineHeight));
                }
            }

            View view = getNodeView(parent.getContext(), matchStatus, matchAttemptedStatus, isPlayed, typeEnum);
            parent.addView(view, parent.getChildCount(), new ViewGroup.LayoutParams(nodeWidthHeight, nodeWidthHeight));

        }
    }

    public static void addTextNode(LinearLayout parent, String title, int matchSize, String status,
                                   MatchTimelineTypeEnum typeEnum, boolean isPlayed, int matchAttemptedStatus) {
        if (parent != null) {
            Context context = parent.getContext();

            int matchSizeNew;
            int maxLineWidth;
            /*= (int) context.getResources().getDimension(R.dimen.dim_310); */

            if (matchSize == 2) {
                matchSizeNew = matchSize - 1;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_270);
            } else if (matchSize == 3) {
                matchSizeNew = matchSize - 1;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_270);
            } else if (matchSize == 4) {
                matchSizeNew = matchSize - 1;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_270);
            } else if (matchSize == 5) {
                matchSizeNew = matchSize - 1;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_265);
            } else {
                matchSizeNew = matchSize;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_270);
            }

            int width = maxLineWidth / matchSizeNew;
            //int width = (int)context.getResources().getDimension(R.dimen.dim_80);

            TextView titleView = (TextView) View.inflate(context, R.layout.games_status_timeline_title, null);
            if (title != null) {
                titleView.setText(title);
                Typeface faceBold = Typeface.createFromAsset(context.getAssets(), "fonts/lato/Lato-Bold.ttf");
                Typeface faceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/lato/Lato-Regular.ttf");

                switch (typeEnum) {
                    case IN_PLAY_HEADLESS:

                        if (Constants.GameAttemptedStatus.COMPLETELY == matchAttemptedStatus ||
                                Constants.GameAttemptedStatus.PARTIALLY == matchAttemptedStatus ||
                                status.equalsIgnoreCase(Constants.InPlayMatchStatus.ONGOING)) {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.brown_ccbbbb));
                            titleView.setTypeface(faceBold);
                        } else {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.white_998989));
                            titleView.setTypeface(faceRegular);
                        }

                        break;
                    case IN_PLAY_JOINED:
                        if (status.equalsIgnoreCase(Constants.InPlayMatchStatus.ONGOING)) {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.grey6));
                            titleView.setTypeface(faceBold);
                        } else if (status.equalsIgnoreCase(Constants.InPlayMatchStatus.UPCOMING)) {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.white_999999));
                            titleView.setTypeface(faceRegular);
                        } else {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.white_999999));
                            titleView.setTypeface(faceRegular);
                        }
                        break;

                    case IN_PLAY_MATCHES_SCREEN:
                        if (status.equalsIgnoreCase(Constants.InPlayMatchStatus.ONGOING)) {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.grey6));
                            titleView.setTypeface(faceBold);
                        } else if (status.equalsIgnoreCase(Constants.InPlayMatchStatus.UPCOMING)) {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.white_999999));
                            titleView.setTypeface(faceRegular);
                        } else {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.white_999999));
                            titleView.setTypeface(faceRegular);
                        }
                        break;
                }

                parent.addView(titleView, parent.getChildCount(), new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

        }
    }

    public static void addFooterTextNode(LinearLayout parent, String title, int matchSize, String status,
                                         MatchTimelineTypeEnum typeEnum, boolean isPlayed, String startTimeForTimer, int matchAttemptedStatus) {
        if (parent != null) {
            Context context = parent.getContext();

            int matchSizeNew;
            int maxLineWidth;
            /*= (int) context.getResources().getDimension(R.dimen.dim_310); */

            if (matchSize == 2) {
                matchSizeNew = matchSize - 1;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_265);
            } else if (matchSize == 3) {
                matchSizeNew = matchSize - 1;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_265);
            } else if (matchSize == 4) {
                matchSizeNew = matchSize - 1;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_267);
            } else if (matchSize == 5) {
                matchSizeNew = matchSize - 1;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_265);
            } else {
                matchSizeNew = matchSize;
                maxLineWidth = (int) context.getResources().getDimension(R.dimen.dim_270);
            }

            int width = maxLineWidth / matchSizeNew;
            //int width = (int)context.getResources().getDimension(R.dimen.dim_80);

            final TextView titleView = (TextView) View.inflate(context, R.layout.games_status_timeline_title, null);
            if (title != null) {

                titleView.setText(title);

                Typeface faceBold = Typeface.createFromAsset(context.getAssets(), "fonts/lato/Lato-Bold.ttf");
                Typeface faceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/lato/Lato-Regular.ttf");

                switch (typeEnum) {
                    case IN_PLAY_HEADLESS:

                        if (Constants.GameAttemptedStatus.COMPLETELY == matchAttemptedStatus ||
                                Constants.GameAttemptedStatus.PARTIALLY == matchAttemptedStatus ||
                                status.equalsIgnoreCase(Constants.InPlayMatchStatus.ONGOING)) {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.brown_ccbbbb));
                            titleView.setTypeface(faceBold);
                        } else {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.white_998989));
                            titleView.setTypeface(faceRegular);
                        }

                        break;
                    case IN_PLAY_JOINED:
                        if (status.equalsIgnoreCase(Constants.InPlayMatchStatus.ONGOING)) {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.grey6));
                            titleView.setTypeface(faceBold);
                        } else if (status.equalsIgnoreCase(Constants.InPlayMatchStatus.UPCOMING)) {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.white_999999));
                            titleView.setTypeface(faceRegular);
                        } else {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.white_999999));
                            titleView.setTypeface(faceRegular);
                        }
                        break;

                    case IN_PLAY_MATCHES_SCREEN:
                        if (status.equalsIgnoreCase(Constants.InPlayMatchStatus.ONGOING)) {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.grey6));
                            titleView.setTypeface(faceBold);
                        } else if (status.equalsIgnoreCase(Constants.InPlayMatchStatus.UPCOMING)) {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.white_999999));
                            titleView.setTypeface(faceRegular);
                        } else {
                            titleView.setTextColor(ContextCompat.getColor(context, R.color.white_999999));
                            titleView.setTypeface(faceRegular);
                        }
                        break;
                }

                /* Timer or date */
                if (!TextUtils.isEmpty(startTimeForTimer)) {
                    if (DateTimeHelper.isTimerRequired(startTimeForTimer)) {
                        setTimer(startTimeForTimer, titleView);
                    } else {
                        titleView.setText(title);
                    }
                }

                parent.addView(titleView, parent.getChildCount(), new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

        }
    }

    private static void setTimer(final String title, final TextView titleView) {
        CountDownTimer countDownTimer = new CountDownTimer(TimerHelper.getCountDownFutureTime(title), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                titleView.setText(TimerHelper.getTimerFormatFromMillis(millisUntilFinished));
            }

            @Override
            public void onFinish() {
            }
        };
        countDownTimer.start();
    }


}
