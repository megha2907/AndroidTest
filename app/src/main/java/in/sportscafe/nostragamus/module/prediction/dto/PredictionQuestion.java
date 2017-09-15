package in.sportscafe.nostragamus.module.prediction.dto;

/**
 * Created by sandip on 15/09/17.
 */

public class PredictionQuestion {

    private int questionId;

    private String questionText;

    private String questionDescription;

    private String option1;

    private String option1ImgUrl;

    private String option2;

    private String option2ImgUrl;

    private String option3;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption1ImgUrl() {
        return option1ImgUrl;
    }

    public void setOption1ImgUrl(String option1ImgUrl) {
        this.option1ImgUrl = option1ImgUrl;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption2ImgUrl() {
        return option2ImgUrl;
    }

    public void setOption2ImgUrl(String option2ImgUrl) {
        this.option2ImgUrl = option2ImgUrl;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }
}
