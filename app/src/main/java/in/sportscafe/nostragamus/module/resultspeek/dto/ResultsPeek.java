package in.sportscafe.nostragamus.module.resultspeek.dto;

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
