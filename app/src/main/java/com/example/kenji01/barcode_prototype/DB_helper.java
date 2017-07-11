package com.example.kenji01.barcode_prototype;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kenji01 on 2017/07/10.
 */

public class DB_helper extends SQLiteOpenHelper{
    //DBの名前　テーブル名　バージョン
    private static final String DB_NAME = "client_DB";
    private static final String TABLE_NAME = "book_table";
    private static final int DB_VERSION = 1;

    //カラム
    private static String BOOK_ID = "book_id";
    private static String BOOK_NAME = "book_name";
    private static String AUTHOR = "author";
    private static String CODE = "code";
    private static String HAVE = "have";
    private static String PRICE = "price";
    private static String PURCHASE_DATE = "purchase_date";



    public DB_helper(Context content){
        super(content,DB_NAME,null,DB_VERSION);
    }


    //テーブル作成
    @Override
    public void onCreate(SQLiteDatabase db) {
        //sql文実行
        db.execSQL("create table " + TABLE_NAME + "("
                + BOOK_ID           +" integer primary key autoincrement, "
                + BOOK_NAME         +" text, "
                + AUTHOR            +" text, "
                + CODE              +" text, "
                + HAVE              +" integer, "
                + PRICE             +" integer, "
                + PURCHASE_DATE     +" numeric"
        + ");");

    }

    //DBのバージョンが変わった時の処理
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
