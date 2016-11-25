package in.sportscafe.nostragamus.module.offline;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.AbstractDataHandler;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.prediction.dto.Answer;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.webservice.MyWebService;

/**
 * Created by Jeeva on 23/5/16.
 */
public class PredictionDataHandler extends AbstractDataHandler {

    private static PredictionDataHandler sPredictionDataHandler = new PredictionDataHandler();

    private SharedPreferences mNewsPreferences;

    private PredictionDataHandler() {
    }

    public static PredictionDataHandler getInstance() {
        return sPredictionDataHandler;
    }

    public void init(Context context) {
        mNewsPreferences = context.getSharedPreferences("in.sportscafe.nostragamus.prediction", Context.MODE_PRIVATE);
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return mNewsPreferences;
    }

    public void savePrediction(Answer answer) {
        setSharedStringData(answer.getMatchId() + "_" + answer.getQuestionId(),
                MyWebService.getInstance().getJsonStringFromObject(answer));
    }

    public Answer getPrediction(Question question) {
        String answer = getSharedStringData(question.getMatchId() + "_" + question.getQuestionId());
        if(null == answer || answer.isEmpty()) {
            return null;
        }

        return MyWebService.getInstance().getObjectFromJson(answer, Answer.class);
    }

    public void saveMyResultMatchIdList(List<String> matchIdList) {
        setSharedStringData(SharedKeys.MY_RESULT_MATCH_IDS,
                MyWebService.getInstance().getJsonStringFromObject(matchIdList));
    }

    public List<String> getMyResultMatchIdList() {
        String matchIdList = getSharedStringData(SharedKeys.MY_RESULT_MATCH_IDS);
        if(null == matchIdList || matchIdList.isEmpty()) {
            return new ArrayList<>();
        }

        return MyWebService.getInstance().getObjectFromJson(matchIdList,
                new TypeReference<List<String>>() {});
    }

    public void saveMyResult(Match myResult) {
        String matchId = myResult.getId().toString();

        List<String> myResultMatchIdList = getMyResultMatchIdList();
        if(myResultMatchIdList.contains(matchId)) {
            myResultMatchIdList.remove(matchId);
        }
        myResultMatchIdList.add(0, matchId);

        saveMyResultMatchIdList(myResultMatchIdList);

        setSharedStringData(matchId, MyWebService.getInstance().getJsonStringFromObject(myResult));
    }

    public Match getMyResult(String matchId) {
        String myResult = getSharedStringData(matchId);
        if(null == myResult || myResult.isEmpty()) {
            return null;
        }

        return MyWebService.getInstance().getObjectFromJson(myResult, Match.class);
    }

    public List<Match> getMyResultList() {
        List<Match> myResultList = new ArrayList<>();

        for (String matchId : getMyResultMatchIdList()) {
            myResultList.add(getMyResult(matchId));
        }

        return myResultList;
    }
}