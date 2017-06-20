package com.example.kenji01.barcode_prototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private String janCode;
    private TextView resultText;
    private EditText keyword_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = (TextView)findViewById(R.id.result_text);
        keyword_editText = (EditText)findViewById(R.id.keyword_editText);
    }


    public void scanBarcode(View v) {
        new IntentIntegrator(this).setBeepEnabled(false).initiateScan();
    }


    public void search(View v){
        String keyword = keyword_editText.getText().toString();
        if (keyword.equals("")){
            Toast.makeText(this, "検索ワードが空だよ！！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, keyword, Toast.LENGTH_LONG).show();
        }
    }



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
}
