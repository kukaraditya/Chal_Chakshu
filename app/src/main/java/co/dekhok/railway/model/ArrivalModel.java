package co.dekhok.railway.model;

public class ArrivalModel {
    
                String  loco_id;
                String  li_id;
                String  train_no;
                String  arrival_time;
                String  longitude;
                String  latitude;
                String  arrival_station;
                String  arr_address;
                int refId;

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
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

    public String getTrain_no() {
        return train_no;
    }

    public void setTrain_no(String train_no) {
        this.train_no = train_no;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getArrival_station() {
        return arrival_station;
    }

    public void setArrival_station(String arrival_station) {
        this.arrival_station = arrival_station;
    }

    public String getArr_address() {
        return arr_address;
    }

    public void setArr_address(String arr_address) {
        this.arr_address = arr_address;
    }
}
