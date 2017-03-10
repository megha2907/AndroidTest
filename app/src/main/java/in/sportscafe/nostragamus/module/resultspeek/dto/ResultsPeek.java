package in.sportscafe.nostragamus.module.resultspeek.dto;

import java.util.List;

import in.sportscafe.nostragamus.module.play.prediction.dto.Question;

/**
 * Created by deepanshi on 2/16/17.
 */

public class ResultsPeek {

    Question playerOneQuestions;

    Question playerTwoQuestions;

    public ResultsPeek() {
    }

    public ResultsPeek(Question playerOneQuestions) {
        this.playerOneQuestions = playerOneQuestions;
    }

    public Question getPlayerOneQuestions() {
        return playerOneQuestions;
    }

    public void setPlayerOneQuestions(Question playerOneQuestions) {
        this.playerOneQuestions = playerOneQuestions;
    }

    public Question getPlayerTwoQuestions() {
        return playerTwoQuestions;
    }

    public void setPlayerTwoQuestions(Question playerTwoQuestions) {
        this.playerTwoQuestions = playerTwoQuestions;
    }

}
