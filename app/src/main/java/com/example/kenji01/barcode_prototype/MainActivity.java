package com.example.kenji01.barcode_prototype;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private DB_helper helper;

    //リストビューの準備
    private ArrayList<String> arr;
    private ArrayAdapter<String> adapter;
    private ListView list;

    //データベースの参照位置を保持
    private Cursor c;


    //activityがスタートしたら実行
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = (TextView)findViewById(R.id.result_text);
        list = (ListView)findViewById(R.id.list);

        //インスタンス生成して読み書き可能で取得
        helper = new DB_helper(getApplicationContext());
        db = helper.getWritableDatabase();

        //ListViewにItemをセット
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
//        list.setAdapter(adapter);
        setAdapter();

        //ListView クリックイベント
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), position + "番目のアイテムがクリックされました", Toast.LENGTH_LONG).show();
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
        startActivity(i);
    }


    public void onResume(){
        super.onResume();
        setAdapter();
    }
    public void setAdapter(){
        arr = new ArrayList<>();
        c = db.query(
          DB_helper.TABLE_NAME,
          new String[]{DB_helper.BOOK_NAME,DB_helper.BOOK_ID},
          null,null,null,null,null
        );

        while (c.moveToNext()){
            arr.add(c.getString(c.getColumnIndexOrThrow(DB_helper.BOOK_NAME)));
            log.d("DB",String.valueOf(c.getColumnIndexOrThrow(DB_helper.BOOK_NAME)));

        }

        adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                arr
        );

        list.setAdapter(adapter);
    }


}
