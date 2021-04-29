package co.dekhok.railway;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;

import co.dekhok.railway.model.AlpModel;
import co.dekhok.railway.model.ArrivalModel;
import co.dekhok.railway.model.FinalSubmitLocal;
import co.dekhok.railway.model.FormModel;
import co.dekhok.railway.model.RadioQuestionModel;

/**
 * Created by dekho on 03/10/2019.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String PREF_NAME = "railway_session";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "railway";
    private static final String TABLE_Demo = "demo";
    private static final String TABLE_alpquestion = "alpquestion";
    private static final String TABLE_form = "form";
    private static final String TABLE_arrival = "arrival";
    private static final String TABLE_questionradio = "question_radio";
    private static final String TABLE_questionradioalp = "question_radioalp";
    private static final String TABLE_alp = "alptable";
    private static final String TABLE_final = "finaltable";
    private static final String KEY_ID = "id";
    private static final String REF_KEY_ID = "ref_id";

    private static final String locono_typeshed = "locono_typeshed";
    private static final String lpname_hq = "lpname_hq";
    private static final String loco_id = "loco_id";
    private static final String nliname_lp = "nliname_lp";
    private static final String train_no = "train_no";
    private static final String gurardname_hq = "gurardname_hq";
    private static final String type_wagon = "type_wagon";
    private static final String dep_time = "dep_time";
    private static final String dep_station = "dep_station";
    private static final String caliber = "caliber";
    private static final String wagon_load = "wagon_load";
    private static final String bpc_no = "bpc_no";
    private static final String working_cab = "working_cab";
    private static final String loco_id_alp = "loco_id_alp";
    private static final String alpname_hq = "alpname_hq";
    private static final String li_id = "li_id";
    private static final String longitude = "longitude";
    private static final String latitude = "latitude";
    private static final String dep_address = "dep_address";
    private static final String date = "date";
    //////////////////////////////
    private static final String  arrival_time="arrival_time";
    private static final String  arrival_station="arrival_station";
    private static final String  arr_address="arr_address";
    ////////////////
    private static final String KEY_QUESTION_ID = "id";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_ANSWER = "answer";
    /////////////////////
    private static final String   question_id = "question_id";
    private static final String   answer="answer" ;
    private static final String   department_id="department_id" ;
    private static final String   abnormal_id="abnormal_id";
    private static final String   remark="remark";
    ////////////////////


    String ans,qid;
    JSONArray devices = new JSONArray();
    JSONArray devices_new = new JSONArray();
    public DatabaseHandler(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_Demo+" ("+KEY_QUESTION_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_QUESTION+" TEXT, "+KEY_ANSWER+" INTEGER, "+REF_KEY_ID+" INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_alpquestion+" ("+KEY_QUESTION_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_QUESTION+" TEXT, "+KEY_ANSWER+" INTEGER, "+REF_KEY_ID+" INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_form+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +locono_typeshed+ " STRING, " +lpname_hq+" STRING, " +loco_id+ " STRING," +nliname_lp+ " STRING," +train_no+ " STRING," +gurardname_hq+ " STRING," + type_wagon+ " STRING," +dep_time+ " STRING," +dep_station+ " STRING," +caliber+ " STRING," +wagon_load+ " STRING," +bpc_no+ " STRING," +working_cab+ " STRING," + loco_id_alp+" STRING," +alpname_hq+ " STRING," +li_id+ " STRING," +longitude+ " STRING," +latitude+ " STRING," +dep_address+ " STRING," +date+ " STRING )");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_arrival+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +loco_id+ " STRING, " +li_id+ " STRING, "+train_no+ " STRING," +arrival_time+ " STRING," +longitude+ " STRING," +latitude+ " STRING," + arrival_station+ " STRING, " + arr_address + " STRING, " + REF_KEY_ID + " INTEGER ) ");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_questionradio+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +question_id+ " STRING, " +answer+ " STRING, "+department_id+ " STRING," +train_no+ " STRING," +loco_id+ " STRING," +li_id+ " STRING," + abnormal_id+ " STRING, " + remark + " STRING , " + arrival_station + " STRING, " + REF_KEY_ID + " INTEGER ) ");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_questionradioalp+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +question_id+ " STRING, " +answer+ " STRING, "+department_id+ " STRING," +train_no+ " STRING," +loco_id+ " STRING," +li_id+ " STRING," + abnormal_id+ " STRING, " + remark + " STRING , " + arrival_station + " STRING, " + REF_KEY_ID + " INTEGER ) ");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_final+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +question_id+ " STRING, " +answer+ " STRING, "+department_id+ " STRING," +train_no+ " STRING," +loco_id+ " STRING," +li_id+ " STRING," +REF_KEY_ID+ " INTEGET ) ");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_alp+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +locono_typeshed+ " STRING, " +lpname_hq+ " STRING, "+loco_id_alp+ " STRING," +nliname_lp+ " STRING," +train_no+ " STRING," +loco_id+ " STRING," + li_id+ " STRING," + caliber+ " STRING," + REF_KEY_ID+ " INTEGER ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Demo);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_alpquestion);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_form);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_arrival);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_questionradio);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_alp);
        onCreate(db);
    }

    public boolean create(Information information) {
        boolean createSuccessful = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_QUESTION_ID,information.getId());
        values.put(KEY_QUESTION,information.getQuestio());
        values.put(REF_KEY_ID,information.getReferId());

        db.insert(TABLE_Demo, null, values);
        db.close();

        return createSuccessful;
    }

    /* * Creating a Alp Question  */
    public boolean createAlpQuestion(Information information) {
        boolean createSuccessful = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_QUESTION_ID,information.getId());
        values.put(KEY_QUESTION,information.getQuestio());
        values.put(REF_KEY_ID,information.getReferId());

        db.insert(TABLE_alpquestion, null, values);
        db.close();

        return createSuccessful;
    }

    /* * Creating a Form  */

    public int cerateForm(FormModel todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("Train Id",todo.getTrain_no());
        ContentValues values = new ContentValues();
        values.put(locono_typeshed, todo.getLocono_typeshed());
        values.put(lpname_hq, todo.getLpname_hq());
        values.put(loco_id, todo.getLoco_id());
        values.put(nliname_lp, todo.getNliname_lp());
        values.put(train_no, todo.getTrain_no());
        values.put(gurardname_hq, todo.getGurardname_hq());
        values.put(type_wagon, todo.getType_wagon());
        values.put(dep_time, todo.getDep_time());
        values.put(dep_station, todo.getDep_station());
        values.put(caliber, todo.getCaliber());
        values.put(wagon_load, todo.getWagon_load());
        values.put(bpc_no, todo.getBpc_no());
        values.put(working_cab, todo.getWorking_cab());
        values.put(loco_id_alp, todo.getLoco_id_alp());
        values.put(alpname_hq, todo.getAlpname_hq());
        values.put(li_id, todo.getLi_id());
        values.put(longitude, todo.getLongitude());
        values.put(latitude, todo.getLatitude());
        values.put(dep_address, todo.getDep_address());
        values.put(date, todo.getDate());
        // insert row
        int form_id = (int) db.insert(TABLE_form, null, values);
        return form_id;
    }

   /* * Creating a Arrival  */

    public int cerateArrival(ArrivalModel avm) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(loco_id, avm.getLoco_id());
        values.put(li_id,avm.getLi_id());
        values.put(train_no,avm.getTrain_no());
        values.put(arrival_time,avm.getArrival_time());
        values.put(longitude,avm.getLongitude());
        values.put(latitude,avm.getLatitude());
        values.put(arrival_station,avm.getArrival_station());
        values.put(arr_address,avm.getArr_address());
        values.put(REF_KEY_ID,avm.getRefId());

        // insert row
        int arriva_id = (int) db.insert(TABLE_arrival, null, values);
        return arriva_id;
    }

   /* * Creating a QuestionRadio  */
    public int cerateQuestionRadio(RadioQuestionModel rqm) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(question_id,rqm.getQuestion_id());
        values.put(answer,rqm.getAnswer());
        values.put(department_id,rqm.getDepartment_id());
        values.put(loco_id,rqm.getLoco_id());
        values.put(li_id,rqm.getLi_id());
        values.put(train_no,rqm.getTrain_no());
        values.put(abnormal_id,rqm.getAbnormal_id());
        values.put(remark,rqm.getRemark());
        values.put(arrival_station,rqm.getArrival_station());
        values.put(REF_KEY_ID,rqm.getRefId());

        // insert row
        int radioqe_id = (int) db.insert(TABLE_questionradio, null, values);
        return radioqe_id;
    }

   /* * Creating a AlpForm  */
    public int cerateAlpForm(AlpModel rqm) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(locono_typeshed, rqm.getLocono_typeshed());
        values.put(lpname_hq, rqm.getLpname_hq());
        values.put(loco_id_alp, rqm.getLoco_id_alp());
        values.put(nliname_lp, rqm.getNliname_lp());
        values.put(train_no,rqm.getTrain_no());
        values.put(loco_id,rqm.getLoco_id());
        values.put(li_id,rqm.getLi_id());
        values.put(caliber,rqm.getCaliber());
        values.put(REF_KEY_ID,rqm.getRefId());

        // insert row
        int alp_id = (int) db.insert(TABLE_alp, null, values);
        return alp_id;
    }

    /* * Creating a QuestionRadio  */
    public int cerateQuestionRadioAlp(RadioQuestionModel rqm) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(question_id,rqm.getQuestion_id());
        values.put(answer,rqm.getAnswer());
        values.put(department_id,rqm.getDepartment_id());
        values.put(loco_id,rqm.getLoco_id());
        values.put(li_id,rqm.getLi_id());
        values.put(train_no,rqm.getTrain_no());
        values.put(abnormal_id,rqm.getAbnormal_id());
        values.put(remark,rqm.getRemark());
        values.put(arrival_station,rqm.getArrival_station());
        values.put(REF_KEY_ID,rqm.getRefId());

        // insert row
        int radioqe_id = (int) db.insert(TABLE_questionradioalp, null, values);
        return radioqe_id;
    }

    /* * Creating a QuestionRadio  */
    public int cerateFinal(FinalSubmitLocal rqm) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(question_id,rqm.getQuestion_id());
        values.put(answer,rqm.getAnswer());
        values.put(department_id,rqm.getDepartment_id());
        values.put(loco_id,rqm.getLoco_id());
        values.put(li_id,rqm.getLi_id());
        values.put(train_no,rqm.getTrain_no());
        values.put(REF_KEY_ID,rqm.getRefId());

        // insert row
        int radioqe_id = (int) db.insert(TABLE_final, null, values);
        return radioqe_id;
    }


    public void update(String answer_new, String ide) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_Demo + " SET answer=" + answer_new + " WHERE id=" + ide);
       /* Log.e("SqlQuery", "UPDATE " + TABLE_Demo + " SET answer=" + answer_new + " WHERE id=" + ide);*/

    }

    public void updateAlpQuestion(String answer_new, String ide) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_alpquestion + " SET answer=" + answer_new + " WHERE id=" + ide);
       /* Log.e("SqlQuery", "UPDATE " + TABLE_Demo + " SET answer=" + answer_new + " WHERE id=" + ide);*/

    }

    public JSONArray show(int refId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT " + KEY_ANSWER + " FROM " + TABLE_Demo + " WHERE "+ REF_KEY_ID + " = "+refId, null);
        ans = "";
        devices=new JSONArray();
        if (c.moveToFirst()) {
            do {
                ans =  c.getString(0);
                devices.put(ans);
            } while (c.moveToNext());
        }
        return(devices);

    }

    public Cursor showRadioQuestionLp(int refId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_questionradio + " WHERE "+ REF_KEY_ID + " = "+refId,null);
        return c;
    }

    public Cursor showfinalSubmit(int refId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_final + " WHERE "+ REF_KEY_ID + " = "+refId, null);
        return c;
    }

    public Cursor showForm() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_form , null);
       // db.close();
        return c;
    }

    public Cursor showArrival(int refId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_arrival +" WHERE "+REF_KEY_ID+" = "+refId , null);
       // db.close();
        return c;
    }

    public Cursor showFormAlp(int refId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_alp +" WHERE "+REF_KEY_ID+" = "+refId , null);
       // db.close();
        return c;
    }

    public void showFormID(int refId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_form +" WHERE id = "+refId, null);
        if (c.moveToFirst()) {
            do {
                int size = c.getColumnCount();
                for (int i=0; i<size ; i++)
                {
                    String id =  c.getString(i);
                    Log.e(c.getColumnName(i),c.getString(i));
                }
            } while (c.moveToNext());
        }
        c.close();
        db.close();
    }

    public JSONArray showAlpQuestion(int refId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT " + KEY_ANSWER + " FROM " + TABLE_alpquestion + " WHERE "+ REF_KEY_ID + " = "+refId, null);
        ans = "";
        devices=new JSONArray();
        if (c.moveToFirst()) {
            do {
                ans =c.getString(0);
                devices.put(ans);
            } while (c.moveToNext());
        }
      //  c.close();
      //  db.close();
//        Log.e("newanswer", ans);
        return(devices);
    }

    public String fetch(String ide) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT " + KEY_ANSWER + " FROM " + TABLE_Demo + " WHERE id= " + ide, null);

        ans = "";
        if (c.moveToFirst()) {

            do {


                ans=ans+c.getString(0) ;


            } while (c.moveToNext());

        }
       // sessionManager.setPrefs("answer", ans, context);
        Log.e("abcd", "abcd");
        //c.close();
       // db.close();
        return(ans);
    }

    public String fetchAlpQuestion(String ide ) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT " + KEY_ANSWER + " FROM " + TABLE_alpquestion + " WHERE id= " + ide, null);
        ans = "";
        if (c.moveToFirst()) {
            do {
                ans=ans +c.getString(0) ;
            } while (c.moveToNext());
        }
       // sessionManager.setPrefs("answer", ans, context);
        Log.e("abcd", "abcd");
        //c.close();
       // db.close();
        return(ans);
    }

    public JSONArray show_q(int refId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT " + KEY_QUESTION_ID + " FROM " + TABLE_Demo+ " WHERE "+ REF_KEY_ID + " = "+refId, null);
        qid = "";
        devices_new=new JSONArray();
        if (c.moveToFirst()) {

            do {


                qid =c.getString(0);
                devices_new.put(qid);

            } while (c.moveToNext());

        }
        return(devices_new);

    }

    public JSONArray show_q_AlpQuestion(int refId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT " + KEY_QUESTION_ID + " FROM " + TABLE_alpquestion+ " WHERE "+ REF_KEY_ID + " = "+refId, null);
        qid = "";
        devices_new=new JSONArray();
        if (c.moveToFirst()) {
            do {
                qid =c.getString(0);
                devices_new.put(qid);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        //Log.e("newanswer", ans);
        return(devices_new);
    }

    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM " + TABLE_Demo );
    }

    public void deleteAlpQuestion(int refId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM " + TABLE_alpquestion + " WHERE "+ REF_KEY_ID + " = "+refId);
    }
    public void deleteAlpQuestion() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM " + TABLE_alpquestion );
    }

    public  void deleteAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM " + TABLE_form );
        db.execSQL("DELETE  FROM " + TABLE_arrival );
        db.execSQL("DELETE  FROM " + TABLE_Demo );
        db.execSQL("DELETE  FROM " + TABLE_alp );
        db.execSQL("DELETE  FROM " + TABLE_alpquestion );
        db.execSQL("DELETE  FROM " + TABLE_questionradio );
        db.execSQL("DELETE  FROM " + TABLE_questionradioalp );
        db.execSQL("DELETE  FROM " + TABLE_final );
    }

}









