package co.dekhok.railway;

/**
 * Created by dekho on 26/08/2019.
 */

public class Model {

    String qid;
    String status;

    String question;
    String ansr_one,two,three,four;

    public  void setQid(String qid1){
        qid=qid1;
    }
    public  void setStatus(String status1){
        status=status1;
    }

    public void setQuestion(String question1) {question=question1;}
    public void setAnsr_one(String ansr_one1){ansr_one=ansr_one1;}
    public void setTwo(String two1){two=two1;}
    public void setThree(String three1){three=three1;}
    public void setFour(String four1){four=four1;}


    public String getQid(){
        return qid;
    }
    public String getStatus(){
        return status;
    }

    public String getQuestion(){
        return question;
    }
    public String getAnsr_one(){
        return ansr_one;
    }
    public String getTwo(){
        return two;
    }
    public String getThree(){
        return three;
    }
    public String getFour(){
        return four;
    }


}
