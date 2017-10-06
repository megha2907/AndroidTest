package in.sportscafe.nostragamus.module.contest.dataProvider;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestQueueRequest;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestQueueResponse;
import in.sportscafe.nostragamus.module.contest.dto.VerifyJoinContestRequest;
import in.sportscafe.nostragamus.module.contest.dto.VerifyJoinContestResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 25/09/17.
 */

public class JoinContestApiImpl {

    private static final String TAG = JoinContestApiImpl.class.getSimpleName();

    private JoinContestApiListener mListener;
    private int mVerifyRetryCounter = 1;

    public JoinContestApiImpl(JoinContestApiListener listener) {
        this.mListener = listener;
    }

    public void joinContestQueue(int contestId, int challengeId, String challengeName,
                                 boolean shouldSendPseudoRoomId, int pseudoRoomId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {

            final JoinContestQueueRequest request = new JoinContestQueueRequest();
            request.setChallengeId(challengeId);
            request.setChallengeName(challengeName);
            request.setContestId(contestId);

            /* Should send pseudoRoomId / roomId ONLY in headless flow of inPlay
             * else should pass 0 */
            if (shouldSendPseudoRoomId) {
                request.setPseudoRoomId(pseudoRoomId);
            }

            MyWebService.getInstance().joinContestQueue(request).enqueue(new ApiCallBack<JoinContestQueueResponse>() {
                @Override
                public void onResponse(Call<JoinContestQueueResponse> call, Response<JoinContestQueueResponse> response) {
                    super.onResponse(call, response);
                    if (response.isSuccessful() && response.body() != null) {
                        JoinContestQueueResponse joinContestQueueResponse = (JoinContestQueueResponse) response.body();

                        if (!TextUtils.isEmpty(joinContestQueueResponse.getError())) {
                            if (mListener != null) {
                                mListener.onServerReturnedError(joinContestQueueResponse.getError());
                            }
                        } else {
                            Log.d(TAG, "OrderId :" + joinContestQueueResponse.getOrderId());
                            verifyJoinContest(joinContestQueueResponse.getOrderId());
                        }
                    } else {
                        Log.d(TAG, "Response null");
                        if (mListener != null) {
                            mListener.onFailure(Constants.DataStatus.FROM_SERVER_API_FAILED);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JoinContestQueueResponse> call, Throwable t) {
                    super.onFailure(call, t);
                    Log.d(TAG, "Response failure : " + t.getMessage());
                    if (mListener != null) {
                        mListener.onFailure(Constants.DataStatus.FROM_SERVER_API_FAILED);
                    }
                }
            });
        } else {
            if (mListener != null) {
                mListener.onFailure(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    public void verifyJoinContest(final String orderId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {

            if (!TextUtils.isEmpty(orderId)) {
                final VerifyJoinContestRequest request = new VerifyJoinContestRequest();
                request.setOrderId(orderId);

                MyWebService.getInstance().verifyJoinContest(request).enqueue(new ApiCallBack<VerifyJoinContestResponse>() {
                    @Override
                    public void onResponse(Call<VerifyJoinContestResponse> call, Response<VerifyJoinContestResponse> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful() && response.body() != null) {

                            VerifyJoinContestResponse verifyJoinContestResponse = response.body();

                            if (!TextUtils.isEmpty(verifyJoinContestResponse.getError())) {
                                if (mListener != null) {
                                    mListener.onServerReturnedError(verifyJoinContestResponse.getError());
                                }
                            } else if (verifyJoinContestResponse.isTryAgain()) {
                                tryAgain(orderId);

                            } else {
                                if (mListener != null) {
                                    mListener.onSuccessResponse(response.body());
                                }
                            }

                        } else {
                            Log.d(TAG, "Response null");
                            if (mListener != null) {
                                mListener.onFailure(Constants.DataStatus.FROM_SERVER_API_FAILED);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VerifyJoinContestResponse> call, Throwable t) {
                        super.onFailure(call, t);

                        Log.e(TAG, "Verify JoinContest Api failure");
                        if (mListener != null) {
                            mListener.onFailure(Constants.DataStatus.FROM_SERVER_API_FAILED);
                        }
                    }
                });

            } else {
                Log.e(TAG, "OrderId empty!");
                if (mListener != null) {
                    mListener.onFailure(-1);
                }
            }
        } else {
            if (mListener != null) {
                mListener.onFailure(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    private void tryAgain(final String orderId) {
        if (mVerifyRetryCounter <= 15) { // MAx 15 tries
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mVerifyRetryCounter++;
                    verifyJoinContest(orderId);
                }
            }, 4000);   /* 4 seconds to wait before re-trying */
        } else {
            if (mListener != null) {
                mListener.onFailure(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    public interface JoinContestApiListener {
        void onFailure(int dataStatus);
        void onServerReturnedError(String msg);
        void onSuccessResponse(VerifyJoinContestResponse verifyJoinContestResponse);
    }
}
