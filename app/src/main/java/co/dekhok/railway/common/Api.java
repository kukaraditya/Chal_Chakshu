package co.dekhok.railway.common;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    public static String SeverUrl="http://www.chalchaksu.in/api/";
    public static String sessiondata="5050f4ca981c9fc266f6b1e0b7";

    @POST("locodetail.php")
    @FormUrlEncoded
    Call<String> updateLPForm(@FieldMap Map<String,String> params);

    @POST("alp_detail.php")
    @FormUrlEncoded
    Call<String> updateALPForm(@FieldMap Map<String,String> params);

    @POST("loco-blank.php")
    Call<String> updatePendingData();

}
