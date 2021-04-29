package co.dekhok.railway.model;

public class RadioQuestionModel {
    
                String   question_id;
                String   answer ;
                String   department_id ;
                String   train_no ;
                String   loco_id ;
                String   li_id;
                String   abnormal_id;
                String   remark;
                String   arrival_station;
    int refId;

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getTrain_no() {
        return train_no;
    }

    public void setTrain_no(String train_no) {
        this.train_no = train_no;
    }

    public String getLoco_id() {
        return loco_id;
    }

    public void setLoco_id(String loco_id) {
        this.loco_id = loco_id;
    }

    public String getLi_id() {
        return li_id;
    }

    public void setLi_id(String li_id) {
        this.li_id = li_id;
    }

    public String getAbnormal_id() {
        return abnormal_id;
    }

    public void setAbnormal_id(String abnormal_id) {
        this.abnormal_id = abnormal_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getArrival_station() {
        return arrival_station;
    }

    public void setArrival_station(String arrival_station) {
        this.arrival_station = arrival_station;
    }
}
