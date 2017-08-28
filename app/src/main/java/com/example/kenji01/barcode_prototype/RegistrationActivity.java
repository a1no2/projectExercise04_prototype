package com.example.kenji01.barcode_prototype;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{
    //DB
    private SQLiteDatabase db;
    private DB_helper db_helper;


    //ボタン、テキストフィールド、ラジオボタン
    Button clear_btn,registration_btn;
    EditText bookName_editText,author_editText,
            code_editText,price_editText,purchaseDate_editText;
    RadioGroup radioGroup;
    RadioButton have_radio,went_radio;

    //新しく登録するならcreate　更新なら,そのIDが入る
    String id_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Intent i = getIntent();
        id_ = i.getStringExtra("ID");

        //ボタン、テキストフィールド、ラジオボタンの紐づけ
        clear_btn = (Button)findViewById(R.id.clear_btn);
        clear_btn.setOnClickListener(this);
        registration_btn = (Button)findViewById(R.id.registration_btn);
        registration_btn.setOnClickListener(this);

        bookName_editText = (EditText)findViewById(R.id.bookName_editText);
        author_editText = (EditText)findViewById(R.id.author_editText);
        code_editText = (EditText)findViewById(R.id.code_editText);
        price_editText = (EditText)findViewById(R.id.price_editText);
        purchaseDate_editText = (EditText)findViewById(R.id.purchaseDate_editText);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        have_radio = (RadioButton)findViewById(R.id.have_radio);
        went_radio = (RadioButton)findViewById(R.id.went_radio);


        //DB準備
        db_helper = new DB_helper(getApplicationContext());
        db = db_helper.getWritableDatabase();        //読み書き


        if (id_.equals("create")){

        } else {
//            Curs            String SQL_str =
//                    "select * " +
//                    "from " + db_helper.TABLE_NAME +
//                    " where " + db_helper.BOOK_ID + " = ?;";
//            Log.d("SQL:", SQL_str);or c = db.rawQuery(SQL_str, new String[]{id_});

            Cursor cursor = db.query(
                    DB_helper.TABLE_NAME,
                    new String []{DB_helper.BOOK_NAME, DB_helper.AUTHOR, DB_helper.CODE,
                            String.valueOf(DB_helper.HAVE), String.valueOf(DB_helper.PRICE),
                            String.valueOf(DB_helper.PURCHASE_DATE)},
                    DB_helper.BOOK_ID + " = ?",
                    new String[] {""+String.valueOf(id_)},
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()){
                bookName_editText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DB_helper.BOOK_NAME)));
                author_editText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DB_helper.AUTHOR)));
                code_editText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DB_helper.CODE)));
                price_editText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DB_helper.PRICE)));
                purchaseDate_editText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DB_helper.PURCHASE_DATE)));


                String have = cursor.getString(cursor.getColumnIndexOrThrow(DB_helper.HAVE));

                Toast.makeText(this, have, Toast.LENGTH_LONG).show();
                if (have.equals("1" )){
                    have_radio.setChecked(true);
                } else {
                    went_radio.setChecked(true);
                }

            }
        }


    }

    //ボタンのクリックイベント
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //クリアボタン
            case R.id.clear_btn:
                // 確認ダイアログの生成
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
                alertDlg.setTitle("クリアしてもよろしいですか？");
                alertDlg.setPositiveButton(
                        "クリア",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // OK ボタンクリック処理
                                bookName_editText.setText("");
                                author_editText.setText("");
                                code_editText.setText("");
                                price_editText.setText("");
                                purchaseDate_editText.setText("");
                            }
                        });
                alertDlg.setNegativeButton(
                        "キャンセル",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancel ボタンクリック処理
                            }
                        });

                // 表示
                alertDlg.create().show();
                break;

            //登録ボタン
            case R.id.registration_btn:
                if (bookName_editText.getText().toString().equals("")) {
                    Toast.makeText(this, radioGroup.getCheckedRadioButtonId()+"", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "書籍名が空白です", Toast.LENGTH_SHORT).show();
                    break;
                } else if (radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(this, "欲しいものリストか所持リストを選択してください", Toast.LENGTH_SHORT).show();

                //新規登録
                } else if (id_.equals("create")){
                    db_helper = new DB_helper(getApplicationContext());
                    db = db_helper.getWritableDatabase();

                    //押されたラジオボタンのIDを取得しオブジェクト取得
                    int radioButton_id = radioGroup.getCheckedRadioButtonId();
                    //所持していたら1 欲しいものリスト0
                    if (radioButton_id == 2131492977){
                        radioButton_id = 0;
                    } else if (radioButton_id == 2131492978) {
                        radioButton_id = 1;
                    }


                    String SQL_str = "insert into "
                            + db_helper.TABLE_NAME + " ( "
                                + db_helper.BOOK_NAME + "," + db_helper.AUTHOR + "," + db_helper.CODE + ","
                                + db_helper.HAVE + "," + db_helper.PRICE + "," + db_helper.PURCHASE_DATE + ") "
                            + "values ("
                                + "'" + bookName_editText.getText().toString() + "',"
                                + "'" + author_editText.getText().toString() + "',"
                                + "'" + code_editText.getText().toString() + "',"
                                + "'" + radioButton_id + "',"
                                + "'" + price_editText.getText().toString() + "',"
                                + "'" + purchaseDate_editText.getText().toString() + "'"
                                + " );";

                    try{
                        db.execSQL(SQL_str);
                    }catch (SQLException e) {
                        Toast.makeText(this, "SQLexecSQL\n" + e, Toast.LENGTH_LONG).show();
                        break;
                    }
                    Toast.makeText(this, "登録完了しました！", Toast.LENGTH_SHORT).show();
                    this.finish();

                //更新
                } else {

                    int radioButton_id = radioGroup.getCheckedRadioButtonId();
                    //所持していたら1 欲しいものリスト0
                    if (radioButton_id == 2131492977){
                        radioButton_id = 0;
                    } else if (radioButton_id == 2131492978) {
                        radioButton_id = 1;
                    }
                    ContentValues valuse = new ContentValues();
                    valuse.put(db_helper.BOOK_NAME, bookName_editText.getText().toString());
                    valuse.put(db_helper.AUTHOR, author_editText.getText().toString());
                    valuse.put(db_helper.CODE, code_editText.getText().toString());
                    valuse.put(db_helper.HAVE, radioButton_id);
                    valuse.put(db_helper.PRICE, price_editText.getText().toString());
                    valuse.put(db_helper.PURCHASE_DATE, purchaseDate_editText.getText().toString());

                    String where = db_helper.BOOK_ID + " = " + id_;
                    db.update(
                            db_helper.TABLE_NAME,
                            valuse,
                            where,
                            null
                    );
                    Toast.makeText(this, "登録完了しました！", Toast.LENGTH_SHORT).show();
                    this.finish();

                }

                break;
        }

    }
}
