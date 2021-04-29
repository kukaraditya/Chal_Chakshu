package co.dekhok.railway;

/**
 * Created by dekho on 06/10/2019.
 */

public class Information {

    String questio;
    String id;
    String answer;
    int referId;

    public int getReferId() {
        return referId;
    }

    public void setReferId(int referId) {
        this.referId = referId;
    }

    public String getQuestio() {
        return this.questio;
    }

    public void setQuestion(String question) {
        this.questio = question;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
