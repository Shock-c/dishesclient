package cn.shock.dishesclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SetTable extends AppCompatActivity implements View.OnClickListener{

    int dining_id;
    String responsebody = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_table);

        EditText editText_num = (EditText) findViewById(R.id.table_num);
        Button button_set = (Button) findViewById(R.id.table_sure);

        dining_id = new Integer(editText_num.getText().toString().trim()).intValue();

        button_set.setOnClickListener(this);



    }

    public  void getTables(){

        String weatherUrl = "http://"+getResources().getString(R.string.localhost)+":8080/OrderWeb/AddTable?id="+dining_id;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responsebody = response.body().string();
                Log.i("shockc",responsebody);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i("shockc","网络出错");

            }
        });

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.table_sure :
                getTables();
                while(true){
                    Log.i("shockc","is sleepping ");
                    try {
                        new Thread().sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!responsebody.equals("")&&responsebody != null){
                        Log.i("shockc","rsponsebody is not null");
                        break;
                    }
                }
                if(responsebody.equals("true")){
                    responsebody ="";
                    onBackPressed();
                }else if (responsebody.equals("false")){
                    responsebody = "";
                    Toast.makeText(SetTable.this,"设置错误",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
