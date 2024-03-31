package com.manager.password.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.annotation.Nullable;

import com.manager.password.entity.PasswordInfo;
import com.manager.password.security.PasswordSecurity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "passwordManager";
    private static final String TABLE_PASSWORD = "password";
    public static final String WEBSITE = "website";
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String IV = "iv";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PASSWORD_TABLE = "CREATE TABLE " + TABLE_PASSWORD + "("
                + "_id" + " INTEGER PRIMARY KEY," + WEBSITE + " TEXT,"
                + PASSWORD + " BLOB,"+IV+" BLOB,"  +USERNAME+" TEXT "+ ")";
        db.execSQL(CREATE_PASSWORD_TABLE);
        Log.d("DB","Table crated");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORD);

        // Create tables again
        onCreate(db);
        Log.d("DB","Table upgraded.");
    }

    public void addRecord(PasswordInfo passwordInfo){
      SQLiteDatabase sqLiteDatabase =  getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WEBSITE,passwordInfo.getWebsiteName());

            try {
                byte[] iv = PasswordSecurity.getIVSecureRandom(KeyProperties.KEY_ALGORITHM_AES);
                values.put(PASSWORD,PasswordSecurity.encryptMessage(passwordInfo.getPassword(),iv));
                values.put(IV,iv);
                values.put(USERNAME,passwordInfo.getUsername());
            } catch (NoSuchProviderException | InvalidAlgorithmParameterException |
                     UnrecoverableEntryException | CertificateException | KeyStoreException |
                     IOException | NoSuchAlgorithmException | NoSuchPaddingException |
                     IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }

        long insertedRecordCount = sqLiteDatabase.insert(TABLE_PASSWORD,null,values);
       if(insertedRecordCount>0){
           Log.d("DB"," "+insertedRecordCount+" Records created");
       }else {
           Log.d("DB","Unable to create Records");
       }
    }
    public List<PasswordInfo> getAll(){
        return getAll(null);
    }
    public List<PasswordInfo> getAll(String searchText){
        String selectQuery = null;
        if(searchText!=null){
             selectQuery = "SELECT  * FROM " + TABLE_PASSWORD+" WHERE "+WEBSITE+" LIKE"+" '%"+ searchText+ "%'";
        }else {
            selectQuery = "SELECT  * FROM " + TABLE_PASSWORD;
        }
        SQLiteDatabase sqLiteDatabase =  getWritableDatabase();
        List<PasswordInfo> passwordInfos = new ArrayList<>();
        Log.d("DB","Getting records from DB..");
       try(Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null)) {
           if (cursor.moveToFirst()) {
               Log.d("DB","Found records in DB..");
               do {
                   PasswordInfo passwordInfo = new PasswordInfo(cursor.getInt(0), cursor.getString(1), cursor.getBlob(2),cursor.getBlob(3), cursor.getString(4));
                   passwordInfos.add(passwordInfo);
               } while (cursor.moveToNext());
           }
           Log.d("DB","Found "+passwordInfos.size()+" records in DB");
       }
       return passwordInfos;
    }

    public void deleteRecord(int id){
        String deleteQuery = "DELETE FROM "+TABLE_PASSWORD+ " WHERE _id="+id;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(deleteQuery);
        Log.d("DB","Record deleted successfully "+id);
    }

    public List<PasswordInfo> search(String searchText){
       return getAll(searchText);
    }
}
