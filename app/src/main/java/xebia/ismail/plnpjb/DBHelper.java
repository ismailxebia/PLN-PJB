package xebia.ismail.plnpjb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="MyAppDB";
    public static final String TABLE_USERS="Users";

    public static final String USER_ID ="_ID";
    public static final String USER_LOGIN = "Login";
    public static final String USER_PASSWORD = "Password";
    public static final String USER_EMAIL = "Email";


    public static final String TABLE_NOTES="NOTES";
    public static final String NOTE_ID ="_ID";
    public static final String NOTE_TEXT ="Text";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Sozdanie tablici User
        db.execSQL("Create table " + TABLE_USERS +
                "(" + USER_ID + " integer primary key," +USER_LOGIN + " text," + USER_EMAIL + " text," + USER_PASSWORD + " text)");
        db.execSQL("Create table " + TABLE_NOTES +
                "(" + NOTE_ID + " integer primary key," +NOTE_TEXT + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + TABLE_USERS);
        db.execSQL("Drop table if exists " + TABLE_NOTES);

        onCreate(db);

    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + TABLE_USERS);
        db.execSQL("Drop table if exists " + TABLE_NOTES);

        onCreate(db);

    }
}
