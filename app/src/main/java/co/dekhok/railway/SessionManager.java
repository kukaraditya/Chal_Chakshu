package co.dekhok.railway;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by dekho on 03/07/2019.
 */

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    static int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "railway_session";

    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_USER_ID = "LOGGED_ID";
    public static final String KEY_USER_NAME = "LOGGED_NAME";
    public static final String KEY_USER_EMAIL = "LOGGED_EMAIL";
    public static final String KEY_USER_PHONE = "LOGGED_USER_PHONE";
    public static final String KEY_USER_GENDER = "LOGGED_USER_GENDER";
    public static final String KEY_USER_AGE = "LOGGED_USER_AGE";
    public static final String KEY_USER_INQUESTION = "INQUESTION";
    public static final String KEY_USER_INQUESTION_ALP = "INQUESTIONALP";
    public static final String KEY_USER_INABNORMALITY = "INABNORMALITY";
    public static final String KEY_USER_INFORM = "INFORM";
    public static final String KEY_USER_ARRIVAL = "ARRIVAL";
    public static final String KEY_USER_QUESTIONNO = "QUESTIONNO";
    public static final String KEY_USER_QUESTIONNOALP = "QUESTIONNOALP";
    public static final String KEY_USER_RESPONSE = "RESPONSE";
    public static final String KEY_USER_RESPONSE_NEW = "RESPONSE_NEW";
    public static final String KEY_USER_RESPONSE_ALP = "RESPONSE_ALP";
    public static final String KEY_USER_TRAIN = "LOGGED_USER_TRAIN";
    public static final String KEY_LP_ID = "LOGGED_LP_ID";
    public static final String KEY_LP_NAME = "LOGGED_LP_NAME";
    public static final String KEY_ALP_NAME = "LOGGED_ALP_NAME";

    public static final String KEY_DEP_ID_AB = "LOGGED_DEP_ID_AB";
    public static final String KEY_LI_ID_AB = "LOGGED_LI_ID_AB";
    public static final String KEY_LOCO_ID_AB = "LOGGED_LOCO_ID_AB";
    public static final String KEY_Q_ID_AB = "LOGGED_Q_ID_AB";
    public static final String KEY_ANS_ID_AB = "LOGGED_ANS_ID_AB";
    public static final String KEY_LAT_ID_AB = "LOGGED_LAT_ID_AB";
    public static final String KEY_LONG_ID_AB = "LOGGED_LONG_ID_AB";
    public static final String KEY_TRAIN_NO_AB = "LOGGED_TRAIN_NO_AB";

    public static final String KEY_TRAIN = "TRAIN";
    public static final String KEY_lOCO_ID = "LOCO_ID";
    public static final String KEY_lOCO_NUMBER = "LOCO_NUMBER";
    public static final String KEY_ALP_ID = "ALP_ID";

    public static final String KEY_SIGNAL = "SIGNAL";
    public static final String KEY_ENGINEERING = "ENGINEERING";
    public static final String KEY_TRAFFIC = "TRAFFIC";
    public static final String KEY_TRD = "TRD";
    public static final String KEY_OTHERS = "OTHERS";
    public static final String KEY_MATTER = "MATTER";
    public static final String KEY_STATION = "STATION";

    public static final String LP_ID = "LPID";
    public static final String LP_NAME = "LPNAME";
    public static final String LOCO_NUMBER = "LOCONO";
    public static final String NLI_NAME = "NLINAME";
    public static final String TRAIN = "TRAINNO";
    public static final String DEP_STATION = "DEPSTATION";
    public static final String GUARD = "GUARDNAME";
    public static final String STOCK = "TYPESTOCK";
    public static final String DEP_TIME = "DEPTIME";
    public static final String CATEGORY = "LPCATEGORY";
    public static final String LOAD = "LPLOAD";
    public static final String BPC_NUMBER = "BPCNUMBER";
    public static final String WORKING_CAB = "CAB";
    public static final String ALP_ID = "ALPID";
    public static final String ALP_NAME = "ALPNAME";

    public static final String looco_id = "loco_id";
    public static final String train = "train";
    public static final String questionno = "questionno";
    public static final String lpp_name = "lp_name";

    public static final String KEY_REF_ID = "refKey";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public SessionManager(DatabaseHandler databaseHandler) {

    }



    public static void setRefKey(int value,Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(KEY_REF_ID, value);
        editor.apply();
    }


    public static int getRefKey(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getInt(KEY_REF_ID,0);
    }

    public static void setPrefs(String key, String value,Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static String getPrefs(String key, Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getString(key, "notfound");
    }
    public void createFormSession(String lp_id,String lp_name, String train_no){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_LP_ID, lp_id);
        editor.putString(KEY_LP_NAME, lp_name);
        editor.putString(KEY_USER_TRAIN, train_no);
        editor.commit();
    }

    public void createFormSessionALP(String lp_id,String alp_name, String train_no){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_LP_ID, lp_id);
        editor.putString(KEY_ALP_NAME, alp_name);
        editor.putString(KEY_USER_TRAIN, train_no);
        editor.commit();
    }

    public void createAbnormalitySession(String dep_id,String li_id, String loco_id,String q_id,String ans_id,String lat_id,String long_id,String train_nmb){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_DEP_ID_AB, dep_id);
        editor.putString(KEY_LI_ID_AB, li_id);
        editor.putString(KEY_LOCO_ID_AB, loco_id);
        editor.putString(KEY_Q_ID_AB, q_id);
        editor.putString(KEY_ANS_ID_AB, ans_id);
        editor.putString(KEY_LAT_ID_AB, lat_id);
        editor.putString(KEY_LONG_ID_AB, long_id);
        editor.putString(KEY_TRAIN_NO_AB, train_nmb);
        editor.commit();


    }

    public HashMap<String, String> getAbnormalityDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_DEP_ID_AB, pref.getString(KEY_DEP_ID_AB, null));
        user.put(KEY_LI_ID_AB, pref.getString(KEY_LI_ID_AB, null));
        user.put(KEY_LOCO_ID_AB, pref.getString(KEY_LOCO_ID_AB, null));
        user.put(KEY_Q_ID_AB, pref.getString(KEY_Q_ID_AB, null));
        user.put(KEY_ANS_ID_AB, pref.getString(KEY_ANS_ID_AB, null));
        user.put(KEY_LAT_ID_AB, pref.getString(KEY_LAT_ID_AB, null));
        user.put(KEY_LONG_ID_AB, pref.getString(KEY_LONG_ID_AB, null));
        user.put(KEY_TRAIN_NO_AB, pref.getString(KEY_TRAIN_NO_AB, null));
        return user;
    }

    public HashMap<String, String> getFormDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_LP_ID, pref.getString(KEY_LP_ID, null));
        user.put(KEY_LP_NAME, pref.getString(KEY_LP_NAME, null));
        user.put(KEY_USER_TRAIN, pref.getString(KEY_USER_TRAIN, null));
        user.put(KEY_USER_QUESTIONNO, pref.getString(KEY_USER_QUESTIONNO, null));
        return user;
    }

    public HashMap<String, String> getFormDetailsALP(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_LP_ID, pref.getString(KEY_LP_ID, null));
        user.put(KEY_ALP_NAME, pref.getString(KEY_ALP_NAME, null));
        user.put(KEY_USER_TRAIN, pref.getString(KEY_USER_TRAIN, null));
        user.put(KEY_USER_QUESTIONNOALP, pref.getString(KEY_USER_QUESTIONNOALP, null));
        return user;
    }

    public void createUserLoginSession(String id,String name, String mobile, String email){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_PHONE, mobile);
        editor.putString(KEY_USER_EMAIL, email);
        editor.commit();
    }

    public void createLocoSession(String train_no,String loco_id,String loco_no,String alp_id,String alp_name){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_TRAIN, train_no);
        editor.putString(KEY_lOCO_ID, loco_id);
        editor.putString(KEY_lOCO_NUMBER, loco_no);
        editor.putString(KEY_ALP_ID, alp_id);
        editor.putString(KEY_ALP_NAME, alp_name);
        editor.commit();
    }

    public void createArrivalSession(String train_no,String loco_id,String questionNo,String lp_name){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(train, train_no);
        editor.putString(looco_id, loco_id);
        editor.putString(lpp_name, lp_name);
        editor.putString(questionno, questionNo);
        editor.commit();
    }

    public HashMap<String, String> getArrivalSession(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(train, pref.getString(train, null));
        user.put(looco_id, pref.getString(looco_id, null));
        user.put(questionno, pref.getString(questionno, null));
        user.put(lpp_name, pref.getString(lpp_name, null));

        return user;
    }

    public void createFormSession(String lp_id,String lp_name,String loco_nmbr,String nli_name,String train,String dep_station,
                                  String guard,String stock,String dep_time,String category,String load,String bpc,String cab,
                                  String alp_id,String alp_name){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(LP_ID, lp_id);
        editor.putString(LP_NAME, lp_name);
        editor.putString(LOCO_NUMBER, loco_nmbr);
        editor.putString(NLI_NAME, nli_name);
        editor.putString(TRAIN, train);
        editor.putString(DEP_STATION, dep_station);
        editor.putString(GUARD, guard);
        editor.putString(STOCK, stock);
        editor.putString(DEP_TIME, dep_time);
        editor.putString(CATEGORY, category);
        editor.putString(LOAD, load);
        editor.putString(BPC_NUMBER, bpc);
        editor.putString(WORKING_CAB, cab);
        editor.putString(ALP_ID, alp_id);
        editor.putString(ALP_NAME, alp_name);
        editor.commit();
    }

    public HashMap<String, String> getFormSession(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(LP_ID, pref.getString(LP_ID, null));
        user.put(LP_NAME, pref.getString(LP_NAME, null));
        user.put(LOCO_NUMBER, pref.getString(LOCO_NUMBER, null));
        user.put(NLI_NAME, pref.getString(NLI_NAME, null));
        user.put(TRAIN, pref.getString(TRAIN, null));
        user.put(DEP_STATION, pref.getString(DEP_STATION, null));
        user.put(GUARD, pref.getString(GUARD, null));
        user.put(STOCK, pref.getString(STOCK, null));
        user.put(DEP_TIME, pref.getString(DEP_TIME, null));
        user.put(CATEGORY, pref.getString(CATEGORY, null));
        user.put(LOAD, pref.getString(LOAD, null));
        user.put(BPC_NUMBER, pref.getString(BPC_NUMBER, null));
        user.put(WORKING_CAB, pref.getString(WORKING_CAB, null));
        user.put(ALP_ID, pref.getString(ALP_ID, null));
        user.put(ALP_NAME, pref.getString(ALP_NAME, null));
        return user;
    }

    public void AbnormalityData(String engineering,String traffic,String signal,String other,String TRD, String matter_counselling, String arr_stn){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_ENGINEERING, engineering);
        Log.e("engivalye",engineering+traffic+signal+other+TRD+matter_counselling+arr_stn);
        editor.putString(KEY_TRAFFIC, traffic);
        editor.putString(KEY_SIGNAL, signal);
        editor.putString(KEY_OTHERS, other);
        editor.putString(KEY_TRD, TRD);
        editor.putString(KEY_MATTER, matter_counselling);
        editor.putString(KEY_STATION, arr_stn);
        editor.commit();
    }

    public HashMap<String, String> getAbnormalityData(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ENGINEERING, pref.getString(KEY_ENGINEERING, null));
        user.put(KEY_TRAFFIC, pref.getString(KEY_TRAFFIC, null));
        user.put(KEY_SIGNAL, pref.getString(KEY_SIGNAL, null));
        user.put(KEY_OTHERS, pref.getString(KEY_OTHERS, null));
        user.put(KEY_TRD, pref.getString(KEY_TRD, null));
        user.put(KEY_MATTER, pref.getString(KEY_MATTER, null));
        user.put(KEY_STATION, pref.getString(KEY_STATION, null));
        Log.e("uservalue",user+"");
        return user;
    }



    public HashMap<String, String> getDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_TRAIN, pref.getString(KEY_TRAIN, null));
        user.put(KEY_lOCO_ID, pref.getString(KEY_lOCO_ID, null));
        user.put(KEY_lOCO_NUMBER, pref.getString(KEY_lOCO_NUMBER, null));
        user.put(KEY_ALP_ID, pref.getString(KEY_ALP_ID, null));
        user.put(KEY_ALP_NAME, pref.getString(KEY_ALP_NAME, null));
        return user;
    }

    public void logoutLoco(){
        editor.remove(KEY_TRAIN);
        editor.remove(KEY_lOCO_ID);
        editor.remove(KEY_lOCO_NUMBER);
        editor.remove(KEY_ALP_ID);
        editor.remove(KEY_ALP_NAME);
        editor.commit();

    }

    public void clearRefId() {
        editor.remove(KEY_REF_ID);
        editor.commit();
    }
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, null));
        user.put(KEY_USER_PHONE, pref.getString(KEY_USER_PHONE, null));
        user.put(KEY_USER_EMAIL, pref.getString(KEY_USER_EMAIL, null));
        return user;
    }
    public HashMap<String, String> getGobalResponse(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USER_RESPONSE, pref.getString(KEY_USER_RESPONSE, null));
        return user;
    }

    public HashMap<String, String> getGobalResponse_new(){
        HashMap<String, String> user_new = new HashMap<String, String>();
        user_new.put(KEY_USER_RESPONSE_NEW, pref.getString(KEY_USER_RESPONSE_NEW, null));
        return user_new;
    }
    public HashMap<String,String> getGlobalResponseAlp(){
        HashMap<String,String> user_alp = new HashMap<String, String>();
        user_alp.put(KEY_USER_RESPONSE_ALP,pref.getString(KEY_USER_RESPONSE_ALP,null));
        return user_alp;
    }
    public void logoutUser(){
        // Clearing all user data from Shared Preferences
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USER_NAME);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_USER_PHONE);
        editor.remove(IS_USER_LOGIN);
        editor.remove(KEY_USER_INQUESTION);

        editor.commit();
    }

    public void logoutForm(){
        // Clearing all user data from Shared Preferences
        editor.remove(LP_ID);
        editor.remove(LP_NAME);
        editor.remove(LOCO_NUMBER);
        editor.remove(NLI_NAME);
        editor.remove(TRAIN);
        editor.remove(DEP_STATION);
        editor.remove(GUARD);
        editor.remove(STOCK);
        editor.remove(DEP_TIME);
        editor.remove(CATEGORY);
        editor.remove(LOAD);
        editor.remove(BPC_NUMBER);
        editor.remove(WORKING_CAB);
        editor.remove(ALP_ID);
        editor.remove(ALP_NAME);
        editor.remove(KEY_USER_INFORM);
        editor.commit();
    }

    public void logoutQuestion(){
        editor.remove(KEY_USER_INQUESTION);
       // editor.remove(KEY_USER_INABNORMALITY);
        editor.commit();
    }

    public void logoutQuestionAbnormality(){
        editor.remove(KEY_USER_INABNORMALITY);
        editor.commit();
    }

    public void logoutArrival(){
        editor.remove(KEY_USER_ARRIVAL);
        editor.commit();
    }




    public void logoutAbnormality(){
        editor.remove(KEY_USER_INQUESTION_ALP);
        editor.commit();
    }

    public void setInQuestion(String questionno,String response) {
        editor.putBoolean(KEY_USER_INQUESTION,true);
        editor.putString(KEY_USER_QUESTIONNO,questionno);
        Log.e("liid","before 1");
        editor.putString(KEY_USER_RESPONSE,response);
        Log.e("liid","before 2"+response);
         editor.commit();
        Log.e("liid","commit");
    }

    public void setInQuestionAlp(String questionno,String response) {
        editor.putBoolean(KEY_USER_INQUESTION_ALP,true);
        editor.putString(KEY_USER_QUESTIONNOALP,questionno);
        Log.e("liid","before 1");
        editor.putString(KEY_USER_RESPONSE_ALP,response);
        Log.e("liid","before 2"+response);
        editor.commit();
        Log.e("liid","commit");
    }

    public void setInAbnormality() {
        editor.putBoolean(KEY_USER_INABNORMALITY,true);
        editor.commit();
    }

    public boolean getInQuestion() {
        return pref.getBoolean(KEY_USER_INQUESTION, false);
    }

    public boolean getInQuestionAlp() {
        return pref.getBoolean(KEY_USER_INQUESTION_ALP, false);
    }

    public boolean getInAbnormality() {
        return pref.getBoolean(KEY_USER_INABNORMALITY, false);
    }

    public boolean getInForm() {
        return pref.getBoolean(KEY_USER_INFORM, false);
    }

    public boolean getInArrival() {
        return pref.getBoolean(KEY_USER_ARRIVAL, false);
    }

    public void setInForm() {
        editor.putBoolean(KEY_USER_INFORM,true);
        editor.commit();
    }

    public void setInArrival() {
        editor.putBoolean(KEY_USER_ARRIVAL,true);
        editor.commit();
    }


    public boolean isUserLoggedIn()
    {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
    public boolean checkLogin(){
        if(!this.isUserLoggedIn()){
            return true;
        } return false;
    }
    public boolean checkQuestion(){
        if(this.getInQuestion()){
            return true;
        } return false;
    }
}
