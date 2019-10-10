package com.sematec.weather;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CreateQuery="CREATE TABLE SearchCity ("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "city TEXT,"+
                "sequence INTEGER)";
        db.execSQL(CreateQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertCity(String city)
    {
        boolean result=true;
        String insertQuery="INSERT INTO SearchCity(city,sequence) VALUES ("+
                "'"+city+"',"+
                1+")";
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            db.execSQL(insertQuery);
            db.close();

        }catch (Exception e)
        {
            e.printStackTrace();
            result=false;
        }
        return  result;
    }

    public ArrayList<String> GetCityList(){
        ArrayList lst=new ArrayList<String>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT city FROM SearchCity";
            String city="";
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                city="";
                city=cursor.getString(0);
                Log.d("DBtest",city);
                lst.add(city);
            }
            db.close();
        }catch(Exception e){
            Log.d("DBtest", e.getMessage());


        }


        return lst;
    }

    public boolean isExistCity(String name)
    {
        boolean result=false;
        SQLiteDatabase db= this.getReadableDatabase();
        String selectQuery="SELECT * FROM SearchCity WHERE city='"+name+"'";
        Cursor cursor=db.rawQuery(selectQuery,null);
        if (cursor.getCount()>0){
            result=true;
        }
        db.close();

        return result;
    }

    public boolean updateCity(String name) {
        boolean result=true;
        String updateQuery = "UPDATE SearchCity SET " +
         "sequence = sequence+1" +
         " WHERE city='" + name + "'" ;

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(updateQuery);
            db.close();

        }catch(Exception e){
            e.printStackTrace();
            result=false;

        }
        return  result;


    }

    public boolean ClearCityHistory()
    {
        boolean result=true;
        String query="DELETE FROM SearchCity";
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            db.execSQL(query);
            db.close();

        }catch (Exception e)
        {
            e.printStackTrace();
            result=false;
        }

        return  result;
    }


    public int getSequence(String name)
    {
        int result=0;
        SQLiteDatabase db= this.getReadableDatabase();
        String selectQuery="SELECT sequence FROM SearchCity WHERE city='"+name+"'";
        Cursor cursor=db.rawQuery(selectQuery,null);
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        db.close();

        return result;
    }

}
