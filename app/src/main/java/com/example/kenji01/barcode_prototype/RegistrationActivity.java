package com.example.kenji01.barcode_prototype;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{
    //DB
    private SQLiteDatabase mydb;
    private DB_helper db_helper;


    //ボタン、テキストフィールド、ラジオボタン
    Button clear_btn,registration_btn;
    EditText bookName_editText,author_editText,
            code_editText,price_editText,purchaseDate_editText;
    RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


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

        //DB準備
//        helper = new DB_helper(getApplicationContext());
//        mydb = helper.getWritableDatabase();        //読み書き



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
                    Toast.makeText(this, "書籍名が空白です", Toast.LENGTH_SHORT).show();
                    break;
                } else if (radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(this, "欲しいものリストか所持リストを選択してください", Toast.LENGTH_SHORT).show();
                } else {
                    db_helper = new DB_helper(getApplicationContext());
                    mydb = db_helper.getWritableDatabase();

                    //押されたラジオボタンのIDを取得しオブジェクト取得
                    int radioButton_id = radioGroup.getCheckedRadioButtonId();
                    //所持リストのラジオボタンIDだったら
                    if (radioButton_id == 2131492978){
                        radioButton_id = 1;
                    } else {
                        radioButton_id = -1;
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
                        mydb.execSQL(SQL_str);
                    }catch (SQLException e) {
                        Toast.makeText(this, "SQLexecSQL\n" + e, Toast.LENGTH_LONG).show();
                        break;
                    }
                    Toast.makeText(this, "登録完了しました！", Toast.LENGTH_SHORT).show();
                    this.finish();

                }

                break;
        }

    }
}
