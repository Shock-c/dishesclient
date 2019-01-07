package cn.shock.dishesclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Setdishes extends AppCompatActivity implements View.OnClickListener{

    String responsebody = "";
    String responsedel = "";
    String responsetext = "";
    int dishes_id;
    Button button_sure;
    Button button_del;
    EditText editText_name;
    EditText editText_price;
    EditText editText_instock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setdishes);

        button_sure = (Button) findViewById(R.id.dishes_sure);
        button_del = (Button) findViewById(R.id.dishes_delect);

        editText_name = (EditText) findViewById(R.id.dishes_name);
        editText_price = (EditText) findViewById(R.id.dishes_price);
        editText_instock = (EditText) findViewById(R.id.dishes_instock);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        dishes_id = bundle.getInt("postId");
        Log.i("shockc","dishesid =  "+dishes_id);
        getdishes();
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
        jsonarray();

        button_sure.setOnClickListener(this);
        button_del.setOnClickListener(this);


    }

    public  void getdishes(){

        String weatherUrl = "http://"+getResources().getString(R.string.localhost)+":8080/OrderWeb/SettingDishes";
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

    public void jsonarray(){
        try {
            List<String> list = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(responsebody);
            for(int i=0;i<jsonArray.length();i++ ){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getInt("id")==dishes_id){
                    Log.i("shockc","change id ");
                    editText_name.setText(jsonObject.getString("name"));
                    editText_price.setText(""+jsonObject.getInt("price"));
                    editText_instock.setText(""+jsonObject.getInt("instock"));
                    //线程刷新
                    editText_name.invalidate();
                    editText_price.invalidate();
                    editText_instock.invalidate();
                    break;
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void deldishes(int id){

        String weatherUrl = "http://"+getResources().getString(R.string.localhost)+":8080/OrderWeb/DelectDishes?id="+id;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responsedel = response.body().string();

            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i("shockc","网络出错");
                responsedel = "false";
            }
        });

    }

    public  void updatadishes(String name,int price , int instock){

        String weatherUrl = "http://"+getResources().getString(R.string.localhost)+":8080/OrderWeb/UpdataDishes?id="+dishes_id+"&name="+name+"&price="+price+"&instock="+instock;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responsetext = response.body().string();
                Log.i("shockc","updatadishes "+responsetext);

            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i("shockc","网络出错");
                responsetext = "false";
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dishes_sure :
                Log.i("shockc","OnClick dishes_sure");
                String name = editText_name.getText().toString();
                int price = new Integer(editText_price.getText().toString()).intValue();
                int instock = new Integer(editText_instock.getText().toString()).intValue();
                Log.i("shockc","OnClick dishes_sure"+name+price+instock);
                updatadishes(name,price,instock);
                while (true){
                    String flag = responsetext.trim();
                    try {
                        new Thread().sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(flag.equals("true")){
                        Toast.makeText(Setdishes.this,"修改成功",Toast.LENGTH_SHORT).show();
                        responsetext = "";
                        break;
                    }
                    else if(!flag.equals("")){
                        Log.i("shockc","不为空"+flag);

                        Toast.makeText(Setdishes.this,"修改失败",Toast.LENGTH_SHORT).show();
                        responsetext = "";
                        break;
                    }
                }
                break;
            case R.id.dishes_delect :
                Log.i("shockc","onclick  del");
                deldishes(dishes_id);
                while (true){
                    String flag = responsedel.trim();
                    try {
                        new Thread().sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(flag.equals("true")){
                        onBackPressed();
                        responsedel="";
                        break;
                    }
                    else if(!flag.equals("")){
                        Log.i("shockc","del不为空"+flag);
                        Toast.makeText(Setdishes.this,"删除失败",Toast.LENGTH_SHORT).show();
                        responsedel="";
                        break;
                    }
                }
                break;
        }
    }
}

