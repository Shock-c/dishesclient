package cn.shock.dishesclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class Tablelist extends AppCompatActivity implements View.OnClickListener{

    private String [] dishes;
    private String responsebody ="";
    boolean setflag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablelist);
        Button back = (Button) findViewById(R.id.back_button);
        Button Add = (Button) findViewById(R.id.button_add);

        Add.setText("设置");

        back.setOnClickListener(this);
        Add.setOnClickListener(this);

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
        jsonarray();

    }

    //列表点击事件响应
    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Toast 快顯功能 第三個參數 Toast.LENGTH_SHORT 2秒  LENGTH_LONG 5秒
            Intent intent = new Intent(Tablelist.this, MenuActivity.class);
            Bundle bundle = new Bundle();
            int dishes_id = SpiltUtil.getid(dishes[position]);
            if(dishes_id>=0){
                bundle.putInt("postId",dishes_id );
                intent.putExtras(bundle);
                startActivity(intent);
            }else {
                Toast.makeText(Tablelist.this,"网络错误", Toast.LENGTH_SHORT).show();
            }

        }
    };

    //列表方法
    public void listview(String[] str){
        ListView listview =  findViewById(R.id.listview_table);
        dishes = str;

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                str);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(onClickListView);
    }

    public void jsonarray(){
        try {
            List<String> list = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(responsebody);
            responsebody="";
            if(jsonArray.length()<2){
                Toast.makeText(Tablelist.this,"请设置餐桌数量",Toast.LENGTH_SHORT).show();
                setflag = true;
                return;
            }
            for(int i=0;i<jsonArray.length()-1;i++ ){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String statu = "空闲";
                if (jsonObject.getInt("bill")==1){
                    statu = "有客人";
                }
                String str = statu+" "+jsonObject.getInt("id");
                list.add(str);
            }
            Log.i("shockc",list.toString());

            listview((String [])list.toArray(new String[(list.size())]));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public  void getTables(){

        String weatherUrl = "http://"+getResources().getString(R.string.localhost)+":8080/OrderWeb/GetTable";
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
    protected void onResume() {
        super.onResume();
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
        jsonarray();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_button :
                onBackPressed();
                break;
            case R.id.button_add :
                if(setflag){
                    setflag = true;
                    Intent intent =new Intent(Tablelist.this,SetTable.class);
                    startActivity(intent);

                }

                break;
        }
    }
}
