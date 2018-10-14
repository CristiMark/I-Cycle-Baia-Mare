package vyrus.bikegps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;


/**
 * Created by user on 4/8/2016.
 */
public class DataBase extends SQLiteOpenHelper {

  //  public static final String DATABASE_NAME = "1bike_gps.db";
    public static final String DATABASE_NAME = "Server_bike_gps.db";
    public static final String TABLE_DESCRIPTION = "bike_description";
    public static final String TABLE_MARKERS = "bike_marker";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TRASEU = "traseu";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_lONG = "lon";
    public static final String COLUMN_NUME  = "nume";
    public static final String COLUMN_TIMESTAMP= "timestamp";
    public static final String COLUMN_ID_OBIECTIV = "id_obiectiv";
    private HashMap hp;

    Cursor resultSet;

    private SQLiteDatabase myDataBase;


    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, 390);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        long unixTime = System.currentTimeMillis() / 1000L;

        db.execSQL(
                "create table bike_description " +
                        "(id integer primary key AUTOINCREMENT NOT NULL, desc_name text, desc_color text, desc_description text," +
                        " desc_coordonate text,desc_img blob,desc_imgsec blob,owner text)"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS bike_description");
        db.execSQL("DROP TABLE IF EXISTS settings");
        onCreate(db);
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

/*    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.e("THIS", "Opening the database... " + db.getPath() + " version " + db.getVersion());

    }*/

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }


    public boolean insertGPS (String id, String lat, String lon, String traseu, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_LAT, lat);
        contentValues.put(COLUMN_lONG, lon);
        contentValues.put(COLUMN_TRASEU, traseu);
        contentValues.put(COLUMN_TIMESTAMP, timestamp);
        return true;
    }

    public boolean insertDesc (String desc_name, String desc_color, String desc_description, String desc_coordonate , byte[] desc_img, byte[] desc_imgsec, String owner )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // contentValues.put(COLUMN_ID, id);
        contentValues.put("desc_name", desc_name);
        contentValues.put("desc_color", desc_color);
        contentValues.put("desc_description", desc_description);
        contentValues.put("desc_coordonate", desc_coordonate);
        contentValues.put("desc_img", desc_img);
        contentValues.put("desc_imgsec", desc_imgsec);
        contentValues.put("owner", owner);
        db.insert(TABLE_DESCRIPTION, null, contentValues);
        return true;
    }

    public boolean insertSettings (String server, String interv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
       // contentValues.put(COLUMN_SERVER, server);
       // contentValues.put(COLUMN_INTERVAL, interv);
      //  db.insert(TABLE_SETTINGS, null, contentValues);
        return true;
    }

    public Cursor getData(String sqlStr){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( sqlStr, null );
        return res;
    }

    public String getData(String sqlStr, String col){
        String rez;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( sqlStr, null );
        res.moveToFirst();
        rez = res.getString(res.getColumnIndex(col));
        return rez;
    }

    public int numberOfRows(String table){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public void Delete(String sqlStr){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(sqlStr);
    }

    public void Insert(String sqlStr){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sqlStr);
    }





}

