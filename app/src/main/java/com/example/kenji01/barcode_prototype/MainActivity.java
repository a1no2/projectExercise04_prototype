package com.example.kenji01.barcode_prototype;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayDeque;
import java.util.ArrayList;

import static com.loopj.android.http.AsyncHttpClient.log;

public class MainActivity extends AppCompatActivity {
    private String janCode;
    private TextView resultText;

    //DBを使う準備
    private SQLiteDatabase db;
    private DB_helper db_helper;

    //リストビューの準備
    private ArrayList<String> title_arr;
    private ArrayList<String> id_arr;
    private ArrayAdapter<String> adapter;
    private ListView list;

    //データベースの参照位置を保持
    private Cursor c;

    //くそこーど 削除
    public static int remove_id;

    //activityがスタートしたら実行
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = (TextView)findViewById(R.id.result_text);
        list = (ListView)findViewById(R.id.list);

        //インスタンス生成して読み書き可能で取得
        db_helper = new DB_helper(getApplicationContext());
        db = db_helper.getWritableDatabase();

        //ListViewにItemをセット
        setAdapter();

        //ListView クリックイベント
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), position + "番目のアイテムがクリックされました", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), id_arr.get(position) + ":" + title_arr.get(position), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
                i.putExtra("ID",id_arr.get(position));
                startActivity(i);

            }
        });


        //削除 アイテムのロングクリック
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                c.moveToPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("項目の削除");
                builder.setMessage("この項目を削除してよろしいですか？");
//                builder.setCancelable(false);
                remove_id = position;

                // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
                builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.delete(
                                        db_helper.TABLE_NAME,
                                        db_helper.BOOK_ID + " = ?",
                                        new String[] {id_arr.get(remove_id)}
//                              m          new String[] {c.getString(c.getColumnIndex(db_helper.BOOK_ID))}
                                );
                                setAdapter();

                                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();

                            }
                        });
                // アラートダイアログの否定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
                builder.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setCancelable(true);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                return true;
            }
        });


    }

    //バーコード
    public void scanBarcode(View v) {
        new IntentIntegrator(this).setBeepEnabled(false).initiateScan();
    }
    //バーコード読み取ったのを受け取る
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
                janCode = result.getContents();
                resultText.setText(janCode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void RegistrationIntent(View v){
        Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
        i.putExtra("ID","create");
        startActivity(i);
    }


    public void onResume(){
        super.onResume();
        setAdapter();
    }

    //リストの更新
    public void setAdapter(){
        title_arr = new ArrayList<>();
        id_arr = new ArrayList<>();

        c = db.query(
          DB_helper.TABLE_NAME,
          new String[]{DB_helper.BOOK_NAME,DB_helper.BOOK_ID},
          null,null,null,null,DB_helper.BOOK_NAME + ""
        );
        while (c.moveToNext()){
            title_arr.add(c.getString(c.getColumnIndexOrThrow(DB_helper.BOOK_NAME)));
            id_arr.add(c.getString(c.getColumnIndexOrThrow(DB_helper.BOOK_ID)));

        }
        adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                title_arr
        );
        list.setAdapter(adapter);
    }


//    //メニューバー
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

    public void tst(View v) {
        Toast.makeText(this, "未実装", Toast.LENGTH_SHORT).show();
    }

}
