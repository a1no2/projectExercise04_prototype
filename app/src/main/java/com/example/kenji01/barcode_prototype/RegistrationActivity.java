package com.example.kenji01.barcode_prototype;

import android.content.ContentValues;
import android.content.DialogInterface;
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
    private SQLiteDatabase db;
    private DB_helper helper;


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
        //押されたラジオボタンのIDを取得しオブジェクト取得
        int test = radioGroup.getCheckedRadioButtonId();
        Toast.makeText(this,  String.valueOf(test), Toast.LENGTH_SHORT).show();



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
                    helper = new DB_helper(getApplicationContext());
                    db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("data", bookName_editText.getText().toString());
                    values.put("author",author_editText.getText().toString());
                    values.put("code",code_editText.getText().toString());
                    values.put("have",radioGroup.getCheckedRadioButtonId());
                    if(price_editText.getText().toString().equals("")){
                    } else {
                        values.put("price",Integer.parseInt(price_editText.getText().toString()));
                    }
                    if(purchaseDate_editText.getText().toString().equals("")) {
                    } else {
                        values.put("purchase_date",Integer.parseInt(purchaseDate_editText.getText().toString()));
                    }
                    db.insert("book_table", null, values);
                    Toast.makeText(this, "登録完了しました！", Toast.LENGTH_SHORT).show();
                    this.finish();

                }

                break;
        }

    }
}
