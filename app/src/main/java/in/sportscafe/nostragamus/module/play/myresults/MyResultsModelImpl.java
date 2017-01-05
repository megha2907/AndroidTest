package in.sportscafe.nostragamus.module.play.myresults;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.Tournament;
import in.sportscafe.nostragamus.module.feed.dto.Feed;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresults.dto.ReplayPowerupResponse;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
import in.sportscafe.nostragamus.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsModelImpl implements MyResultsModel, MyResultsAdapter.OnMyResultsActionListener {

    private static final int PAGINATION_START_AT = 5;

    private static final int LIMIT = 5;

    private  int matchId;

    private boolean isLoading = false;

    private boolean hasMoreItems = true;

    private MyResultsAdapter mResultAdapter;

    private Match match;

    private OnMyResultsModelListener mResultsModelListener;

    private MyResultsModelImpl(OnMyResultsModelListener listener) {
        this.mResultsModelListener = listener;
    }

    public static MyResultsModel newInstance(OnMyResultsModelListener listener) {
        return new MyResultsModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        
        if(null != bundle.getSerializable(Constants.BundleKeys.MATCH_LIST))
        {
            match = (Match) bundle.getSerializable(Constants.BundleKeys.MATCH_LIST);
            matchId = match.getId();
        }
        else if (null != bundle.getString(Constants.BundleKeys.MATCH_ID))
        {
            String match_id = bundle.getString(Constants.BundleKeys.MATCH_ID);
            matchId= Integer.parseInt(match_id);
        }
        else
        {
            mResultsModelListener.onFailedMyResults(Constants.Alerts.RESULTS_INFO_ERROR);
            mResultsModelListener.gotoResultsTimeline();
        }

        getUserInfoFromServer();
    }

    @Override
    public void getMyResultsData(Context context) {
        loadMyResults(0);
    }

    private void loadMyResults(int offset) {
        if(checkInternet()) {
            Log.i("call","callMyResultsApi");
            callMyResultsApi(offset);
        }
    }

    private boolean checkInternet() {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            return true;
        }
        mResultsModelListener.onNoInternet();
        return false;
    }

    private void callMyResultsApi(final int offset) {
        isLoading = true;
        MyWebService.getInstance().getMyResultsRequest(matchId).enqueue(
                new NostragamusCallBack<MyResultsResponse>() {
                    @Override
                    public void onResponse(Call<MyResultsResponse> call, Response<MyResultsResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            Context context = mResultsModelListener.getContext();
                            if (null != context) {
                                List<Match> myResultList = response.body().getMyResults();

                                String responseJson = MyWebService.getInstance().getJsonStringFromObject(myResultList);
                                Log.d("MyResults Response", responseJson);

                                if (offset == 0) {
                                    destroyAdapter();
                                    mResultsModelListener.onSuccessMyResults(createAdapter(context));
                                    mResultsModelListener.onsetMatchDetails(myResultList.get(0));

                                    loadAdapterData(getCategorizedList(myResultList));


                                    if (myResultList.isEmpty()) {
                                        mResultsModelListener.onEmpty();
                                    }
                                } else {
                                    addMoreInAdapter(getCategorizedList(myResultList));
                                }

                                if(myResultList.size() != LIMIT) {
                                    hasMoreItems = false;
                                }
                            }
                        } else {
                            if (offset == 0) {
                                mResultsModelListener.onFailedMyResults(response.message());
                            }
                            if(response.code() == 404) {
                                hasMoreItems = false;
                            }
                        }
                        isLoading = false;
                    }
                }
        );
    }

    private MyResultsAdapter createAdapter(Context context) {
        return mResultAdapter = new MyResultsAdapter(context);
    }

    private void destroyAdapter() {
        if (null != mResultAdapter) {
            mResultAdapter.destroy();
            mResultAdapter = null;
        }
    }

    private void loadAdapterData(List<Feed> feedList) {
        mResultAdapter.clear();
        addMoreInAdapter(feedList);
    }

    private void addMoreInAdapter(List<Feed> feedList) {
        int count = mResultAdapter.getItemCount();
        for (Feed feed : feedList) {
            mResultAdapter.add(feed, count++);
        }
    }

    @Override
    public void checkPagination(int firstVisibleItemPosition, int childCount, int itemCount) {
        if (itemCount > 0 && !isLoading && hasMoreItems) {
            int lastVisibleItem = firstVisibleItemPosition + childCount;
            if (lastVisibleItem >= (itemCount - PAGINATION_START_AT)) {
                Log.d("Pagination", "firstVisibleItem : " + firstVisibleItemPosition
                        + ", " + "visibleItemCount : " + childCount
                        + ", " + "totalItemCount : " + itemCount);

                loadMyResults(itemCount);
            }
        }
    }

    @Override
    public void callReplayPowerupApplied() {
        if(Nostragamus.getInstance().hasNetworkConnection()) {

            callReplayPowerupAppliedApi("match_replay",matchId);

        } else {
            mResultsModelListener.onNoInternet();
        }
    }

    @Override
    public void showFlipQuestion() {
        mResultAdapter.showFlipOptnforQuestion();
        mResultAdapter.notifyDataSetChanged();
    }

    @Override
    public String getMatchResult() {
        return match.getCorrectCount() + "/" + match.getMatchQuestionCount();
    }

    @Override
    public int getMatchPoints() {
        return match.getMatchPoints();
    }

    @Override
    public String getMatchName() {
        return match.getTournamentName();
    }

    @Override
    public void uploadScreenShot(File file) {
        if(checkInternet()) {
            callUploadScreenShotApi(file, "sportscafetest/nostragamus/", UUID.randomUUID().toString()+file.getName());
        }
    }

    private void callUploadScreenShotApi(File file, String filepath, String filename) {
        MyWebService.getInstance().getUploadPhotoRequest(file,filepath,filename)
                .enqueue(new NostragamusCallBack<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                super.onResponse(call, response);

                if (response.isSuccessful()) {
                    getSharableLink(response.body().getResult());
                } else {
                    mResultsModelListener.onScreenShotFailed();
                }
            }

        });

    }

    private void getSharableLink(String screenShotUrl) {
        Context context = mResultsModelListener.getContext();
        BranchUniversalObject buo = new BranchUniversalObject()
                .setTitle("My Result")
                .setContentDescription(String.format(
                        context.getString(R.string.fb_share_result_text),
                        getMatchResult(),
                        getMatchName(),
                        getMatchPoints()
                ))
                .setContentImageUrl(screenShotUrl)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata(Constants.BundleKeys.USER_REFERRAL_ID, NostragamusDataHandler.getInstance().getUserId());


        LinkProperties linkProperties = new LinkProperties()
                .addTag("myResult")
                .setFeature("myResult")
                .addControlParameter("$android_deeplink_path", "myResult/share/");

        buo.generateShortUrl(mResultsModelListener.getContext(), linkProperties,
                new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if(null == error) {
                            mResultsModelListener.onScreenShotUploaded(url);
                        } else {
                            mResultsModelListener.onScreenShotFailed();
                            ExceptionTracker.track(error.getMessage());
                        }
                    }
                });
    }

    private void callReplayPowerupAppliedApi(String powerupId, Integer matchId) {

        MyWebService.getInstance().getReplayPowerup(powerupId,matchId).enqueue(new NostragamusCallBack<ReplayPowerupResponse>() {
            @Override
            public void onResponse(Call<ReplayPowerupResponse> call, Response<ReplayPowerupResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful()) {
                    if (response.body().getResponse().equals(null) || response.body().getResponse().equalsIgnoreCase("failure")){
                        mResultsModelListener.onFailedReplayPowerupResponse();
                    }
                    else {
                        mResultsModelListener.onSuccessReplayPowerupResponse(match);
                    }

                } else {
                    mResultsModelListener.onFailedReplayPowerupResponse();
                }
            }
        });

    }

    private List<Feed> getCategorizedList(List<Match> matchList) {
        Map<String, Tournament> tourMap = new HashMap<>();
        Map<String, Feed> feedMap = new HashMap<>();
        List<Feed> feedList = new ArrayList<>();

        String date;
        Feed feed;
        int tourId;
        Tournament tour;
        for (Match match : matchList) {
            date = TimeUtils.getFormattedDateString(
                    match.getStartTime(),
                    Constants.DateFormats.DD_MM_YYYY,
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                    Constants.DateFormats.GMT
            );

            if (feedMap.containsKey(date)) {
                feed = feedMap.get(date);
            } else {
                feed = new Feed(TimeUtils.getMillisecondsFromDateString(
                        date,
                        Constants.DateFormats.DD_MM_YYYY,
                        Constants.DateFormats.GMT
                ));
                feedMap.put(date, feed);
                feedList.add(feed);
            }

            tourId = match.getTournamentId();

            if (tourMap.containsKey(date + tourId)) {
                tour = tourMap.get(date + tourId);
            } else {
                tour = new Tournament(tourId, match.getTournamentName());
                tourMap.put(date + tourId, tour);
                feed.addTournament(tour);
            }

            tour.addMatches(match);
        }

        Collections.sort(feedList, new Comparator<Feed>() {
            @Override
            public int compare(Feed lhs, Feed rhs) {
                return (int) (lhs.getDate() - rhs.getDate());
            }
        });

        return feedList;
    }



    private void getUserInfoFromServer() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getUserInfoRequest(NostragamusDataHandler.getInstance().getUserId()).enqueue(
                    new NostragamusCallBack<UserInfoResponse>() {
                        @Override
                        public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                            if (response.isSuccessful()) {
                                super.onResponse(call, response);
                                UserInfo updatedUserInfo = response.body().getUserInfo();

                                if (null != updatedUserInfo) {

                                    NostragamusDataHandler.getInstance().setUserInfo(updatedUserInfo);
                                    if (null != updatedUserInfo) {
                                        NostragamusDataHandler.getInstance().setNumberof2xPowerups(updatedUserInfo.getPowerUps().get("2x"));
                                        NostragamusDataHandler.getInstance().setNumberofNonegsPowerups(updatedUserInfo.getPowerUps().get("no_negs"));
                                        NostragamusDataHandler.getInstance().setNumberofAudiencePollPowerups(updatedUserInfo.getPowerUps().get("player_poll"));
                                        NostragamusDataHandler.getInstance().setNumberofReplayPowerups(updatedUserInfo.getPowerUps().get("match_replay"));
                                        NostragamusDataHandler.getInstance().setNumberofFlipPowerups(updatedUserInfo.getPowerUps().get("answer_flip"));
                                    }
                                    NostragamusDataHandler.getInstance().setNumberofBadges(updatedUserInfo.getBadges().size());
                                    NostragamusDataHandler.getInstance().setNumberofGroups(updatedUserInfo.getNumberofgroups());
                                }
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void onClickLeaderBoard(int position) {

    }

    public interface OnMyResultsModelListener {

        void onSuccessMyResults(MyResultsAdapter myResultsAdapter);

        void onFailedMyResults(String message);

        void onNoInternet();

        void onEmpty();

        Context getContext();

        void gotoResultsTimeline();

        void onFailedReplayPowerupResponse();

        void onSuccessReplayPowerupResponse(Match match);

        void onsetMatchDetails(Match match);

        void onScreenShotUploaded(String url);

        void onScreenShotFailed();
    }
}