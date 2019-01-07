package cn.shock.dishesclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class MenuActivity extends AppCompatActivity {

    int dining_id;
    String responsebody = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        dining_id = bundle.getInt("postId");

        writeview();


    }

    public void writeview(){
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
    public void jsonarray(){
        try {
            List<String> list = new ArrayList<String>();
            List<String> list2 = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(responsebody);
            responsebody = "";
            list.add("菜名");
            list2.add("数量");
            for(int i=0;i<jsonArray.length();i++ ){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String str = jsonObject.getString("name");
                String str2 = jsonObject.getInt("number")+"份";
                list.add(str);
                list2.add(str2);
            }
            Log.i("shockc",list.toString());

            listview((String[])list.toArray(new String[(list.size())]),(String[] )list2.toArray(new String[(list2.size())]));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //列表方法
    public void listview(String[] str,String[] str2){
        ListView listview = (ListView) findViewById(R.id.listview);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                str);
        listview.setAdapter(adapter);

        ListView listview2 = (ListView) findViewById(R.id.listview1);

        ArrayAdapter adapter2 = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                str2);
        listview2.setAdapter(adapter2);

    }

    public  void getTables(){

        String weatherUrl = "http://"+getResources().getString(R.string.localhost)+":8080/OrderWeb/GetMenu?id="+dining_id;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responsebody = response.body().string();
                Log.i("shockc responsebody",responsebody);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i("shockc","网络出错");

            }
        });

    }

}
