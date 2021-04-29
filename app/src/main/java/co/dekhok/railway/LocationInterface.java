package co.dekhok.railway;

public interface LocationInterface {

    public void onLocationUpdated(double latitude, double longitude);
    public void distanceMoreThanLimit();
    public void distanceUpdate(double distance);
}
